package com.imploded.redis.dto;

import lombok.Data;

import java.util.List;

@Data
public class ZSkipListNode {
    Object obj;

    private Double score;

    private ZSkipListNode backward;

    private List<ZSkipListLevel> level;
}
