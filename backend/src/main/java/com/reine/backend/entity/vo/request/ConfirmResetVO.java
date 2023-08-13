package com.reine.backend.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author reine
 */
@Data
public class ConfirmResetVO {

    @Email
    private String email;

    @Length(max = 6, min = 6)
    private String code;
}
