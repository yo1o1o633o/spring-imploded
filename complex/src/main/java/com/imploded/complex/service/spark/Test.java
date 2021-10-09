package com.imploded.complex.service.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        s();
    }

    private static void s() {
        // 初始化Spark
        SparkConf conf = new SparkConf().setAppName("appName").setMaster("master");
        JavaSparkContext sc = new JavaSparkContext(conf);
        // 将集合拆分成并行集合
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        JavaRDD<Integer> parallelList = sc.parallelize(nums);
        // 指定分区数
        JavaRDD<Integer> parallelList2 = sc.parallelize(nums, 10);
        // 并行计算
        Integer sum = parallelList.reduce(Integer::sum);
        System.out.println(sum);

        // 支持文件
        JavaRDD<String> file1 = sc.textFile("/file/data.txt");
        // 支持目录
        JavaRDD<String> file2 = sc.textFile("/file");
        // 支持通配符
        JavaRDD<String> file3 = sc.textFile("/file/*.txt");
        // 支持压缩文件
        JavaRDD<String> file4 = sc.textFile("/file/word.gz");
        // 支持指定分区数
        JavaRDD<String> file5 = sc.textFile("/file/data.txt", 10);
        // 支持目录, 返回文件名、内容映射
        JavaPairRDD<String, String> fileMap = sc.wholeTextFiles("/file");

        // 读取文件, lines为指向文件的指针
        JavaRDD<String> lines = sc.textFile("/file/data.txt");
        // 转换结果, 但是并没真正开始计算
        JavaRDD<Integer> lineLength = lines.map(String::length);
        // 真正计算
        Integer lineSum = lineLength.reduce(Integer::sum);
        System.out.println(lineSum);
    }
}
