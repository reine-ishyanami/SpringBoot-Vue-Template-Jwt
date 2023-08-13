package com.reine.backend.dao;

import com.reine.backend.entity.dto.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author reine
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findAccountByUsernameOrEmail(String username, String email);

    boolean existsAccountByEmail(String email);

    boolean existsAccountByUsername(String username);

    @Transactional
    @Modifying
    @Query("update Account set password = ?2 where email = ?1")
    int updatePasswordByEmail(String email, String password);
}
