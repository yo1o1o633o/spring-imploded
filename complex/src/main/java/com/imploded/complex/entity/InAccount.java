package com.imploded.complex.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "in_account")
public class InAccount {
    @Id
    private Integer id;
}
