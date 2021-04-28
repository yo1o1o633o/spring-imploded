package com.s.imploded.entity.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "msg_test")
public class Test {
    @Id
    @Field(value = "_id")
    private String id;

    @Field(value = "phone_id")
    private Integer phoneId;
}
