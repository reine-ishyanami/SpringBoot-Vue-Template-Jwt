package com.reine.backend.dao;

import com.reine.backend.entity.dto.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author reine
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findAccountByUsernameOrEmail(String username, String email);
}
