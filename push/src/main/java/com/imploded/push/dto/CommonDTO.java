package com.imploded.push.dto;

import lombok.Data;

/**
 * @author: jiajun.liu
 * @Date: 2020/7/2 10:23
 */
@Data
public class CommonDTO {
    private MessageType messageType;
    private Class<?> dtoClass;
}
