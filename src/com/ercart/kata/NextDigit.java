package com.ercart.kata;

import java.util.stream.IntStream;

/**
 * @author dkyryk
 */
public class NextDigit {

    private final static long NO_NEXT_VALUE_RESULT = -1;

    private final long sourceValue;

    public NextDigit(long sourceValue) {
        this.sourceValue = sourceValue;
    }

    public long nextBigger() {
        return nextNumber(Rule.BIGGER);
    }

    public long nextSmaller() {
        return nextNumber(Rule.SMALLER);
    }


    private long nextNumber(Rule rule) {

        if (sourceValue < 10) {
            return NO_NEXT_VALUE_RESULT;
        }


        byte[] digits = splitToDigits(sourceValue);

        for (int groupSize = 2; groupSize <= digits.length; groupSize++) {

            int changeDigit = rule.emptyChangeValue;
            int changeDigitIndex = -1;
            int groupLeadingDigitIndex = digits.length - groupSize;
            byte groupLeadingDigit = digits[groupLeadingDigitIndex];
            for (int possibleChangeDigitIndex = digits.length - 1; possibleChangeDigitIndex > groupLeadingDigitIndex; possibleChangeDigitIndex--) {

                boolean isChangeFound = false;
                switch (rule) {
                    case BIGGER: {
                        isChangeFound = digits[possibleChangeDigitIndex] > groupLeadingDigit
                                && digits[possibleChangeDigitIndex] < changeDigit;
                        break;
                    }
                    case SMALLER: {
                        isChangeFound = digits[possibleChangeDigitIndex] < groupLeadingDigit
                                && digits[possibleChangeDigitIndex] > changeDigit;
                        break;
                    }


                }
                if (isChangeFound) {
                    changeDigit = digits[possibleChangeDigitIndex];
                    changeDigitIndex = possibleChangeDigitIndex;
                }
            }

            if (changeDigit != rule.emptyChangeValue && !(changeDigit == 0 && groupLeadingDigitIndex == 0)) {
                byte buffer = digits[changeDigitIndex];
                digits[changeDigitIndex] = digits[groupLeadingDigitIndex];
                digits[digits.length - groupSize] = buffer;
                int[] sortedPart = IntStream.rangeClosed(groupLeadingDigitIndex + 1, digits.length - 1)
                        .map(i -> digits[i]).sorted().toArray();

                for (int i = 0; i < sortedPart.length; i++) {
                    switch (rule) {
                        case BIGGER: {
                            digits[digits.length - sortedPart.length + i] = (byte) sortedPart[i];
                            break;
                        }
                        case SMALLER: {
                            digits[digits.length - 1 - i] = (byte) sortedPart[i];
                            break;
                        }
                    }
                }

                return combineToNumber(digits);

            }
        }

        return NO_NEXT_VALUE_RESULT;
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

    private enum Rule {
        BIGGER(10), SMALLER(-1);

        private int emptyChangeValue;

        Rule(int emptyChangeValue) {
            this.emptyChangeValue = emptyChangeValue;
        }
    }
}
