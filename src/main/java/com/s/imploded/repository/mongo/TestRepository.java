package com.s.imploded.repository.mongo;

import com.s.imploded.entity.mongo.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author shuai.yang
 */
public interface TestRepository extends MongoRepository<Test, String> {
}
