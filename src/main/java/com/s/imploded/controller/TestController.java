package com.s.imploded.controller;

import com.s.imploded.service.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shuai.yang
 */
@RestController
public class TestController {
    @Autowired
    MongoService mongoService;

    @RequestMapping("/insert")
    public void in() {
        mongoService.insert();
    }
}
