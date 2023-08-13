package com.reine.backend.controller;

import com.reine.backend.entity.ApiResponse;
import com.reine.backend.entity.vo.request.EmailRegisterVO;
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
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

/**
 * @author reine
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "验证相关接口")
@RequestMapping("/api/auth")
public class AuthorizeController {

    private final AccountService service;

    @Operation(summary = "请求验证码")
    @GetMapping("/ask-code")
    @Parameters({
            @Parameter(name = "email", description = "邮件地址", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "type", description = "验证码类型", required = true, in = ParameterIn.QUERY)
    })
    public ApiResponse<Void> askVerifyCode(
            @RequestParam @Email String email,
            @RequestParam @Pattern(regexp = "(register|reset)") String type,
            HttpServletRequest request
    ) {
        return this.messageHandler(() -> service.registerEmailVerifyCode(type, email, request.getRemoteAddr()));
    }


    @Operation(summary = "注册用户")
    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody @Valid EmailRegisterVO vo) {
        return this.messageHandler(() -> service.registerEmailAccount(vo));
    }

    private ApiResponse<Void> messageHandler(Supplier<String> action) {
        String msg = action.get();
        return msg == null ? ApiResponse.success() : ApiResponse.failure(400, msg);
    }

}
