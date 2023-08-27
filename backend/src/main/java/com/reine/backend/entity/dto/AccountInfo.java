package com.reine.backend.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author reine
 */
@Data
@Entity
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Table(name = "db_account_info")
@EntityListeners(AuditingEntityListener.class)
public class AccountInfo {
    @Column(name = "id")
    @Id
    private final Long id;

    @Column(name = "nickname")
    private final String nickname;

    @Column(name = "avatar")
    private final String avatar;

    @Column(name = "birthday")
    private final LocalDate birthday;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @LastModifiedDate
    private LocalDateTime updateTime;

    @Column(name = "create_user")
    @CreatedBy
    private Long createUser;

    @Column(name = "update_user")
    @LastModifiedBy
    private Long updateUser;

    @OneToOne
    @JoinColumn(
            name = "account_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_account_id", value = ConstraintMode.CONSTRAINT)
    )
    private Account account;
}
