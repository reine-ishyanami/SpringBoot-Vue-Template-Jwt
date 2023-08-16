package com.reine.backend.controller;

import com.reine.backend.entity.RestBean;
import com.reine.backend.entity.vo.request.ConfirmResetVO;
import com.reine.backend.entity.vo.request.EmailRegisterVO;
import com.reine.backend.entity.vo.request.EmailResetVo;
import com.reine.backend.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

/**
 * @author reine
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "验证相关接口")
@RequestMapping("/api/auth")
@Validated
public class AuthorizeController {

    private final AccountService service;

    @Operation(summary = "请求验证码")
    @GetMapping("/ask-code")
    @Parameters({
            @Parameter(name = "email", description = "邮件地址", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "type", description = "验证码类型", required = true, in = ParameterIn.QUERY)
    })
    public RestBean<Void> askVerifyCode(
            @RequestParam @Email String email,
            @RequestParam @Pattern(regexp = "(register|reset)") String type,
            HttpServletRequest request
    ) {
        return this.messageHandler(() -> service.registerEmailVerifyCode(type, email, request.getRemoteAddr()));
    }

    @Operation(summary = "注册用户")
    @PostMapping("/register")
    public RestBean<Void> register(@RequestBody @Valid EmailRegisterVO vo) {
        return this.messageHandler(() -> service.registerEmailAccount(vo));
    }

    @Operation(summary = "验证重置密码验证码")
    @PostMapping("/reset-confirm")
    public RestBean<Void> resetConfirm(@RequestBody @Valid ConfirmResetVO vo) {
        return this.messageHandler(() -> service.resetConfirm(vo));
    }

    @Operation(summary = "重置密码")
    @PostMapping("/reset-password")
    public RestBean<Void> resetPassword(@RequestBody @Valid EmailResetVo vo) {
        return this.messageHandler(() -> service.resetEmailPassword(vo));
    }

    private RestBean<Void> messageHandler(Supplier<String> action) {
        String msg = action.get();
        return msg == null ? RestBean.success() : RestBean.failure(400, msg);
    }

}
