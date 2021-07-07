package com.imploded.push.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author yason.li
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedirectAppMsgDTO extends CommonDTO {
    @JsonProperty(value = "appid")
    private Integer appid;

    @JsonProperty(value = "scope")
    private Integer scope;

    @JsonProperty(value = "type")
    private Integer type;

    @JsonProperty(value = "uid")
    private String uid;

    @JsonProperty(value = "uid_sub")
    private String subUid;

    @JsonProperty(value = "firstids")
    private String firstids;

    @JsonProperty(value = "device_tokens")
    private String deviceTokens;

    @JsonProperty(value = "platform")
    private Integer platform;

    @JsonProperty(value = "message")
    private PushMessageDTO message;

    @JsonProperty(value = "to8to")
    private Map<String,Object> to8to;

    @JsonProperty(value = "to8to_custom")
    private Map<String,Object> to8toCustom;

    @JsonProperty(value = "start_time")
    private String startTime;

    @JsonProperty(value = "expire_time")
    private String expireTime;

    @JsonProperty(value = "production_mode")
    private String productionMode;
}
