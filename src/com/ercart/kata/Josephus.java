package com.ercart.kata;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author dkyryk
 */
public class Josephus {

    public static <T> List<T> josephusPermutation(final List<T> items, final int step) {
        List<T> result = new ArrayList<T>();
        List<T> source = new LinkedList<T>(items);
        int index = 0;
        if (source.isEmpty()) {
            return result;
        }
        do {
            index = index + step - 1;
            if (index > source.size() - 1) {
                index = index % source.size();
            }
            result.add(source.get(index));
            source.remove(index);

        } while (!source.isEmpty());


        return result;
    }
}
