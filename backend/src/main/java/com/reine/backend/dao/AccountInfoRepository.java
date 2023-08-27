package com.reine.backend.dao;

import com.reine.backend.entity.dto.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author reine
 */
@Repository
public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long> {
}
