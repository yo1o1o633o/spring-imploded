package com.imploded.complex.controller;

import com.imploded.complex.service.spark.SparkExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shuai.yang
 */
@RestController
public class SparkController {
    @Autowired
    SparkExample sparkExample;

    @RequestMapping("/word/count")
    public void wordCount() {
        sparkExample.wordCount();
    }

    @RequestMapping("/calculate/pi")
    public void calculatePi() {
        sparkExample.calculatePi();
    }
}
