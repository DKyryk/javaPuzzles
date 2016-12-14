package com.ercart.kata;

import java.util.stream.IntStream;

/**
 * @author dkyryk
 */
public class NextDigit {


    private final long sourceValue;

    public NextDigit(long sourceValue) {
        this.sourceValue = sourceValue;
    }

    public long nextSmaller() {

        if (sourceValue < 10) {
            return -1;
        }


        byte[] digits = splitToDigits(sourceValue);

        for (int groupSize = 2; groupSize <= digits.length; groupSize++) {

            int minLessValue = -1;
            int minLessValueIndex = -1;
            int groupLeadingDigitIndex = digits.length - groupSize;
            byte groupLeadingDigit = digits[groupLeadingDigitIndex];
            for (int findLessIndex = digits.length - 1; findLessIndex > groupLeadingDigitIndex; findLessIndex--) {
                if (digits[findLessIndex] < groupLeadingDigit && digits[findLessIndex] > minLessValue) {
                    minLessValue = digits[findLessIndex];
                    minLessValueIndex = findLessIndex;
                }
            }

            if (minLessValue != -1 && !(minLessValue == 0 && groupLeadingDigitIndex == 0)) {
                byte buffer = digits[minLessValueIndex];
                digits[minLessValueIndex] = digits[groupLeadingDigitIndex];
                digits[digits.length - groupSize] = buffer;
                int[] sortedPart = IntStream.rangeClosed(groupLeadingDigitIndex + 1, digits.length - 1)
                        .map(i -> digits[i]).sorted().toArray();

                for (int i = 0; i < sortedPart.length; i++) {
                    digits[digits.length - 1 - i] = (byte) sortedPart[i];
                }

                return combineToNumber(digits);

            }
        }

        return -1;
    }

    private byte[] splitToDigits(long n) {
        int cardinality = String.valueOf(n).length();
        byte[] digits = new byte[cardinality];

        int index = cardinality - 1;

        long div = n;
        for (int i = index; i >= 0; i--) {
            digits[i] = (byte) (div % 10);
            div = div / 10;
        }

        return digits;
    }

    private long combineToNumber(byte[] digits) {
        long result = 0;
        for (byte digit : digits) {
            result = result * 10 + digit;
        }

        return result;
    }
}
