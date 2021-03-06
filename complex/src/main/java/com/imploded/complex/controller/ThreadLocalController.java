package com.imploded.complex.controller;

import com.imploded.complex.service.thread.ThreadLocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThreadLocalController {

    @Autowired
    ThreadLocalService threadLocalService;

    @RequestMapping("/thread/local/test")
    public void getArticleList() {
        threadLocalService.test();
    }
}
