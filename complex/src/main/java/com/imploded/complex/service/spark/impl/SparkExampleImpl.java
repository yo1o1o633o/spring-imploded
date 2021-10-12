package com.imploded.complex.service.spark.impl;

import com.imploded.complex.service.spark.SparkExample;
import com.imploded.complex.service.spark.Tuple2Comparator;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shuai.yang
 */
@Service
public class SparkExampleImpl implements SparkExample, Serializable {

    private Pattern p = Pattern.compile("[\u4e00-\u9fa5]");

    @Autowired
    transient JavaSparkContext sc;

    /**
     * 统计字数
     */
    @Override
    public void wordCount() {
        try {
            JavaRDD<String> txtFile = sc.textFile("file:/F:/GitHub/spring-imploded-a/complex/target/classes/1.txt");
            JavaRDD<String> map = txtFile.flatMap(s -> Arrays.asList(s.split("")).iterator());
            // 过滤非汉字
            JavaPairRDD<String, Integer> pair = map.filter(e -> {
                Matcher m = p.matcher(e);
                return m.find();
            }).mapToPair(s -> new Tuple2<>(s, 1));
            // 统计每个汉字的数量
            JavaPairRDD<String, Integer> reduce = pair.reduceByKey(Integer::sum);
            wordCountByTop(reduce);
            wordCountByTake(reduce);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void wordCountByTake(JavaPairRDD<String, Integer> reduce) {
        // 交换Key和Value
        JavaPairRDD<Integer, String> reduceDesc = reduce.mapToPair(e -> new Tuple2<>(e._2, e._1));
        // 倒序排序
        JavaPairRDD<Integer, String> notSort = reduceDesc.sortByKey(false);
        // 换回原来的Key, Value格式
        JavaPairRDD<String, Integer> reduceAsc = notSort.mapToPair(e -> new Tuple2<>(e._2, e._1));
        // 使用take取前10
        List<Tuple2<String, Integer>> take = reduceAsc.take(10);
        System.out.println(take);
    }

    private void wordCountByTop(JavaPairRDD<String, Integer> reduce) {
        // 交换Key和Value
        JavaPairRDD<Integer, String> reduceDesc = reduce.mapToPair(e -> new Tuple2<>(e._2, e._1));
        // 使用Top取前10, 直接使用比较器进行排序
        List<Tuple2<Integer, String>> top = reduceDesc.top(10, new Tuple2Comparator());
        System.out.println(top);
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
