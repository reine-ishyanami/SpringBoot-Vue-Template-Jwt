package com.reine.backend.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @author reine
 */
@Data
public class AuthorizeVO {

    private String username;

    private String nickname;

    private String role;

    private String token;

    private Date expire;
}
