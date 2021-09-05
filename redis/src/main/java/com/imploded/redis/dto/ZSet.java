package com.imploded.redis.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ZSet {
    private Map dict;

    private ZSkipList zsl;
}
