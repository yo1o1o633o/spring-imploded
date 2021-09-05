package com.imploded.redis.dto;

import lombok.Data;

@Data
public class ZSkipListLevel {
    private Integer span;

    private ZSkipListNode forward;
}
