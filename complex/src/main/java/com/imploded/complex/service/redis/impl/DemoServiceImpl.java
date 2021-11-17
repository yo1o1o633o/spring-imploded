package com.imploded.complex.service.redis.impl;

import com.imploded.complex.service.redis.DemoService;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {

    @Override
    public void test() {
        System.out.println("成功!");
    }
}
