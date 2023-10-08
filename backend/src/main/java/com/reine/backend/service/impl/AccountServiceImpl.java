package com.reine.backend.service.impl;

import com.reine.backend.dao.AccountInfoRepository;
import com.reine.backend.dao.AccountRepository;
import com.reine.backend.entity.dto.Account;
import com.reine.backend.entity.dto.AccountInfo;
import com.reine.backend.entity.vo.request.ConfirmResetVO;
import com.reine.backend.entity.vo.request.EmailRegisterVO;
import com.reine.backend.entity.vo.request.EmailResetVo;
import com.reine.backend.service.AccountService;
import com.reine.backend.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author reine
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final FlowUtils flowUtils;

    private final AccountRepository accountRepository;

    private final AccountInfoRepository accountInfoRepository;

    private final StringRedisTemplate redisTemplate;

    private final RedisStreamUtils streamUtils;

    private final PasswordEncoder encoder;

    private final RedisIdWorker worker;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = this.findAccountByNameOrEmail(username);
        if (account == null) throw new UsernameNotFoundException("用户名或密码错误");
        AccountThreadLocal.put(account);
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    @Override
    public Account findAccountByNameOrEmail(String text) {
        return accountRepository.findAccountByUsernameOrEmail(text, text);
    }

    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        synchronized (ip.intern()) {
            if (!this.verifyLimit(ip)) return "请求频繁，请稍后再试";
            Random random = new Random();
            String code = String.format("%06d", random.nextInt(999999));
            Map<String, String> data = Map.of("type", type, "email", email, "code", code);
            String res = streamUtils.addMap(Constant.REDIS_STREAM, data);// 推送验证信息到消息队列
            log.debug(res);
            redisTemplate.opsForValue().set(getKey(email), code, 3, TimeUnit.MINUTES);
            return null;
        }
    }

    private boolean verifyLimit(String ip) {
        String key = Constant.VERIFY_EMAIL_LIMIT + ip;
        return flowUtils.limitOnceCheck(key, 60);
    }

    @Override
    public String registerEmailAccount(EmailRegisterVO vo) {
        String email = vo.getEmail();
        String username = vo.getUsername();
        String key = getKey(email);
        String cacheCode = redisTemplate.opsForValue().get(key);
        if (cacheCode == null) return "请先获取验证码";
        if (!cacheCode.equals(vo.getCode())) return "验证码输入错误，请重新输入";
        if (this.existsAccountByEmail(email)) return "此邮箱已被注册";
        if (this.existsAccountByUsername(username)) return "此用户名已被注册";
        String password = encoder.encode(vo.getPassword());
        long accountId = worker.nextId("account");
        AccountIdThreadLocal.put(accountId);
        Account account = new Account(accountId, username, password, email, "user", LocalDateTime.now());
        long accountInfoId = worker.nextId("accountInfo");
        account = accountRepository.save(account);
        AccountInfo accountInfo = new AccountInfo(accountInfoId, RandomUtils.getRandomStr(8), "", null);
        accountInfo.setAccount(account);
        accountInfo = accountInfoRepository.save(accountInfo);
        if (account.getId() == null || accountInfo.getId() == null) return "内部错误，请联系管理员";
        redisTemplate.delete(key);
        AccountIdThreadLocal.remove();
        return null;
    }

    @Override
    public String resetConfirm(ConfirmResetVO vo) {
        String email = vo.getEmail();
        String code = redisTemplate.opsForValue().get(getKey(email));
        if (code == null) return "请先获取验证码";
        if (!code.equals(vo.getCode())) return "验证码输入错误，请重新输入";
        return null;
    }

    @Override
    public String resetEmailPassword(EmailResetVo vo) {
        ConfirmResetVO confirmResetVO = new ConfirmResetVO();
        BeanUtils.copyProperties(vo, confirmResetVO);
        String verify = this.resetConfirm(confirmResetVO);
        if (verify != null) return verify;
        String email = vo.getEmail();
        String password = encoder.encode(vo.getPassword());
        int line = accountRepository.updatePasswordByEmail(email, password);
        if (line > 0) {
            redisTemplate.delete(getKey(email));
        }
        return null;
    }

    private static String getKey(String email) {
        return Constant.VERIFY_EMAIL_DATA + email;
    }

    private boolean existsAccountByEmail(String email) {
        return accountRepository.existsAccountByEmail(email);
    }

    private boolean existsAccountByUsername(String username) {
        return accountRepository.existsAccountByUsername(username);
    }
}
