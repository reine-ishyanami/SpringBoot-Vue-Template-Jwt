package com.reine.backend.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author reine
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountIdThreadLocal {

    private static final ThreadLocal<Long> ACCOUNT_ID_LOCAL = new ThreadLocal<>();

    public static void put(Long id) {
        ACCOUNT_ID_LOCAL.set(id);
    }

    public static Long get() {
        return ACCOUNT_ID_LOCAL.get();
    }

    public static void remove() {
        ACCOUNT_ID_LOCAL.remove();
    }
}
