package com.reine.backend.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @author reine
 */
public record AuthorizeVO(String username, String role, String token, Date expire) {
}
