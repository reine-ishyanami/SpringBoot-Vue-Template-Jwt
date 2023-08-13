package com.reine.backend.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author reine
 */
@Data
public class EmailResetVo {

    @Email
    private String email;

    @Length(max = 6, min = 6)
    private String code;

    @Length(min = 6, max = 20)
    private String password;
}
