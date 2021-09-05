package com.imploded.complex.repository;

import com.imploded.complex.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findAllById(Integer id);
}
