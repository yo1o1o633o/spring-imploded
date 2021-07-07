package com.imploded.push.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


/**
 * @author fandy.lin
 */
@Data
public class PushMessageDTO {

    private String title;

    private String description;
   private String ticker;
    private String mipush;
   @JsonProperty(value = "mi_activity")
    private String mi_activity;
   private Integer badge;

    private PushClickDTO click;
}
