package com.imploded.complex.service.mysql.impl;

import com.imploded.complex.entity.Article;
import com.imploded.complex.repository.ArticleRepository;
import com.imploded.complex.service.mysql.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleRepository articleRepository;

    @Override
    public void test() {
        List<Article> articleList = articleRepository.findAllById(23);
        System.out.println(articleList);
    }
}
