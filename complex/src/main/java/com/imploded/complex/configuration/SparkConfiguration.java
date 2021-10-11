package com.imploded.complex.configuration;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shuai.yang
 */
@Configuration
public class SparkConfiguration {

    @Bean
    public SparkConf sparkConf() {
        return new SparkConf().setAppName("sparkTest").setMaster("local");
    }

    @Bean
    public JavaSparkContext javaSparkContext() {
        return new JavaSparkContext(sparkConf());
    }
}
