package com.imploded.complex.controller;

import com.imploded.complex.service.mysql.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {
    @Autowired
    ArticleService articleService;

    @RequestMapping("/article/list")
    public void getArticleList() {
        articleService.test();
    }
}
