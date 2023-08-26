package com.reine.backend.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reine.backend.entity.BaseData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * @author reine
 */
@Data
@Entity
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Table(name = "db_account")
@EntityListeners(AuditingEntityListener.class)
public class Account implements BaseData {

    @Column(name = "id")
    @Id
    private final Long id;

    @Column(name = "username")
    private final String username;

    @Column(name = "password")
    private final String password;

    @Column(name = "email")
    private final String email;

    @Column(name = "role")
    private final String role;

    @Column(name = "register_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime registerTime;

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

}
