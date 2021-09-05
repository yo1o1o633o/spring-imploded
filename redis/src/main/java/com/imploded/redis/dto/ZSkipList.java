package com.imploded.redis.dto;

import lombok.Data;

@Data
public class ZSkipList {

    private Integer level;

    private Long length;

    private ZSkipListNode header;

    private ZSkipListNode tail;
}
