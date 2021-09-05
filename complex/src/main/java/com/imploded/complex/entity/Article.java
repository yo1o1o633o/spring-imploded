package com.imploded.complex.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "blog_article")
public class Article {

    @Id
    private Integer id;
}
