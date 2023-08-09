package com.reine.backend.entity.dto;

import com.reine.backend.entity.BaseData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author reine
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "db_account")
public class Account implements BaseData {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    private String role;

    @Column(name = "register_time")
    private Date registerTime;
}
