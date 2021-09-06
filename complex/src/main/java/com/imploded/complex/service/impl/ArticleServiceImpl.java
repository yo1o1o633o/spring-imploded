package com.imploded.complex.service.impl;

import com.imploded.complex.entity.Article;
import com.imploded.complex.repository.ArticleRepository;
import com.imploded.complex.service.ArticleService;
import com.imploded.complex.service.kafka.CustomPartitioner;
import com.imploded.complex.service.kafka.CustomProducerInterceptor;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
