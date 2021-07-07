package com.imploded.push.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author fandy.lin
 */
@Data
public class PushClickDTO {
    private String type;
    private String url;
    private String activity;
    private String activity_uri;
    private Map<String,Object> custom;
}
