package com.reine.backend.controller;

import com.reine.backend.entity.ApiResponse;
import com.reine.backend.entity.vo.request.EmailRegisterVO;
import com.reine.backend.service.AccountService;
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
@RequestMapping("/api/auth")
public class AuthorizeController {

    private final AccountService service;

    @GetMapping("/ask-code")
    public ApiResponse<Void> askVerifyCode(
            @RequestParam @Email String email,
            @RequestParam @Pattern(regexp = "(register|reset)") String type,
            HttpServletRequest request
    ) {
        return this.messageHandler(() -> service.registerEmailVerifyCode(type, email, request.getRemoteAddr()));
    }


    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody @Valid EmailRegisterVO vo) {
        return this.messageHandler(() -> service.registerEmailAccount(vo));
    }

    private ApiResponse<Void> messageHandler(Supplier<String> action) {
        String msg = action.get();
        return msg == null ? ApiResponse.success() : ApiResponse.failure(400, msg);
    }

}
