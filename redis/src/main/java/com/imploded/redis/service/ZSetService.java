package com.imploded.redis.service;

import com.imploded.redis.dto.ZSkipList;
import com.imploded.redis.dto.ZSkipListLevel;
import com.imploded.redis.dto.ZSkipListNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ZSetService {
    private static final Integer ZSKIPLIST_MAXLEVEL = 32;

    public ZSkipListNode createNode(Integer level, Double score, Object obj) {
        ZSkipListNode zn = new ZSkipListNode();
        zn.setObj(obj);
        zn.setScore(score);
        zn.setLevel(new ArrayList<>(level));
        return zn;
    }

    public ZSkipList create() {
        int i;
        ZSkipList zsl = new ZSkipList();

        zsl.setLevel(1);
        zsl.setLength(0L);
        zsl.setHeader(createNode(ZSKIPLIST_MAXLEVEL, 0.0D, null));
        for (i = 0; i < 10; i++) {
            List<ZSkipListLevel> levelList = zsl.getHeader().getLevel();
            levelList.get(i).setForward(null);
            levelList.get(i).setSpan(0);
        }
        zsl.getHeader().setBackward(null);
        zsl.setTail(null);
        return zsl;
    }
}
