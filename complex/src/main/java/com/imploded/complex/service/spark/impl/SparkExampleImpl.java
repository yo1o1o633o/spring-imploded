package com.imploded.complex.service.spark.impl;

import com.imploded.complex.service.spark.SparkExample;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author shuai.yang
 */
@Service
public class SparkExampleImpl implements SparkExample {

    @Autowired
    JavaSparkContext sc;

    /**
     * 统计字数
     */
    @Override
    public void wordCount() {
        JavaRDD<String> txtFile = sc.textFile("word.txt", 1);
        JavaPairRDD<String, Integer> counts = txtFile.flatMap(s -> Arrays.asList(s.split("")).iterator()).mapToPair(word -> new Tuple2<>(word, 1)).reduceByKey(Integer::sum);
        counts.saveAsTextFile("count.txt");
    }

    /**
     * 计算圆周率
     */
    @Override
    public void calculatePi() {
        int sum = 100;
        List<Integer> s = new ArrayList<>();
        for (int i = 0; i < sum; i++) {
            s.add(i);
        }
        long count = sc.parallelize(s).filter(i -> {
            double x = Math.random();
            double y = Math.random();
            return x * x + y * y < 1;
        }).count();

        System.out.println("PI: " + 4.0 * count / sum);
    }
}
