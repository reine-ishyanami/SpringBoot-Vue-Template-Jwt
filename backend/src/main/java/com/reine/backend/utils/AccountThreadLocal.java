package com.reine.backend.utils;

import com.reine.backend.entity.dto.Account;

/**
 * @author reine
 */
public class AccountThreadLocal {

    private AccountThreadLocal() {
    }

    private static final ThreadLocal<Account> ACCOUNT_LOCAL = new ThreadLocal<>();

    public static void put(Account account) {
        ACCOUNT_LOCAL.set(account);
    }

    public static Account get() {
        return ACCOUNT_LOCAL.get();
    }

    public static void remove() {
        ACCOUNT_LOCAL.remove();
    }
}
