package com.ercart.kata;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dkyryk
 */
public class RomanEncoder {

    private Dictionary dictionary;

    public RomanEncoder() {
        dictionary = new Dictionary();
    }

    public String encode(int year) {
        int[] values = String.valueOf(year).chars().map((int i) -> i - '0').toArray();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            int position = values.length - i;
            result.append(dictionary.decodeValue(position, values[i]));
        }

        return result.toString();
    }

    private class Dictionary {

        Map<Integer, Map<Integer, String>> symbolsByPositionValue;

        public Dictionary() {
            symbolsByPositionValue = new HashMap<>();
            symbolsByPositionValue.put(1, new HashMap<>());
            symbolsByPositionValue.get(1).put(1, "I");
            symbolsByPositionValue.get(1).put(2, "II");
            symbolsByPositionValue.get(1).put(3, "III");
            symbolsByPositionValue.get(1).put(4, "IV");
            symbolsByPositionValue.get(1).put(5, "V");
            symbolsByPositionValue.get(1).put(6, "VI");
            symbolsByPositionValue.get(1).put(7, "VII");
            symbolsByPositionValue.get(1).put(8, "VIII");
            symbolsByPositionValue.get(1).put(9, "IX");

            symbolsByPositionValue.put(2, new HashMap<>());
            symbolsByPositionValue.get(2).put(1, "X");
            symbolsByPositionValue.get(2).put(2, "XX");
            symbolsByPositionValue.get(2).put(3, "XXX");
            symbolsByPositionValue.get(2).put(4, "XL");
            symbolsByPositionValue.get(2).put(5, "L");
            symbolsByPositionValue.get(2).put(6, "LX");
            symbolsByPositionValue.get(2).put(7, "LXX");
            symbolsByPositionValue.get(2).put(8, "LXXX");
            symbolsByPositionValue.get(2).put(9, "XC");

            symbolsByPositionValue.put(3, new HashMap<>());
            symbolsByPositionValue.get(3).put(1, "C");
            symbolsByPositionValue.get(3).put(2, "CC");
            symbolsByPositionValue.get(3).put(3, "CCC");
            symbolsByPositionValue.get(3).put(4, "CD");
            symbolsByPositionValue.get(3).put(5, "D");
            symbolsByPositionValue.get(3).put(6, "DC");
            symbolsByPositionValue.get(3).put(7, "DCC");
            symbolsByPositionValue.get(3).put(8, "DCCC");
            symbolsByPositionValue.get(3).put(9, "CM");

            symbolsByPositionValue.put(4, new HashMap<>());
            symbolsByPositionValue.get(4).put(1, "M");
            symbolsByPositionValue.get(4).put(2, "MM");
            symbolsByPositionValue.get(4).put(3, "MMM");
            symbolsByPositionValue.get(4).put(4, "MMMM");
            symbolsByPositionValue.get(4).put(5, "MMMMM");
            symbolsByPositionValue.get(4).put(6, "MMMMMM");
            symbolsByPositionValue.get(4).put(7, "MMMMMMM");
            symbolsByPositionValue.get(4).put(8, "MMMMMMMM");
            symbolsByPositionValue.get(4).put(9, "MMMMMMMMM");
        }

        public String decodeValue(int position, int value) {
            if (symbolsByPositionValue.containsKey(position)) {
                String result = symbolsByPositionValue.get(position).get(value);
                return (result != null) ? result : "";
            }

            return "";
        }
    }
}

