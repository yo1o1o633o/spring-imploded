package com.imploded.mysql.entity;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author shuai.yang
 */
@Data
@Entity
@Table(name = "on_user")
public class User {
    /**
     * 自增id
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(name = "session_id")
    private Integer sessionId;
}
