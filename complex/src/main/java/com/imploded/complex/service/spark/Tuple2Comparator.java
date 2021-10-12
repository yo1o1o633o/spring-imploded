package com.imploded.complex.service.spark;

import scala.Serializable;
import scala.Tuple2;

import java.util.Comparator;

/**
 * @author shuai.yang
 */
public class Tuple2Comparator implements Serializable, Comparator<Tuple2<Integer, String>> {

    @Override
    public int compare(Tuple2<Integer, String> t1, Tuple2<Integer, String> t2) {
        if (t1 != null && t2 != null) {
            return t1._1 - t2._1;
        }
        return 0;
    }
}