package com.ercart.kata;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MorseCodeDecoder {

    private static final Map<String, String> MorseCode = new HashMap<>();

    public static void main(String[] args) {
//        System.out.println(decodeBits("0001100110011001100000011000000111111001100111111001111110000000000000011001111110011111100111111000000110011001111110000001111110011001100000011"));
        System.out.println(decodeBits("11"));
    }


    private enum Pattern {
        DASH("-", 3), DOT(".", 1), WORD_BREAK("   ", 7), LETTER_BREAK(" ", 3);

        Pattern(String replacer, int bitCodeLength) {
            this.replacer = replacer;
            this.bitCodeLength = bitCodeLength;
        }

        String replacer;
        int bitCodeLength;

    }

    private enum CountMode {
        ZERO, ONE
    }

    public static String decodeBits(String bitCode) {

        StringBuilder result = new StringBuilder();
        int[] bits = bitCode.chars().map((int value) -> value - '0').toArray();
        int endSequence = bits.length - 1;
        while (bits[endSequence] != 1) {
            endSequence--;
        }

        int startSequence = 0;
        while (bits[startSequence] != 1) {
            startSequence++;
        }

        int capacity = getCapacity(bits, endSequence, startSequence);

        CountMode countMode = CountMode.ONE;
        int countOfZeros = 0;
        int countOfOnes = 0;

        for (int i = startSequence; i <= endSequence; i++) {
            switch (countMode) {
                case ONE: {
                    if (bits[i] == 1) {
                        countOfOnes++;
                    } else {
                        if (countOfOnes % (Pattern.DASH.bitCodeLength * capacity) == 0) {
                            result.append(Pattern.DASH.replacer);
                        } else {
                            result.append(Pattern.DOT.replacer);
                        }

                        countMode = CountMode.ZERO;
                        countOfZeros = 1;
                    }
                    break;
                }
                case ZERO: {
                    if (bits[i] == 0) {
                        countOfZeros++;
                    } else {
                        if (countOfZeros % (Pattern.WORD_BREAK.bitCodeLength * capacity) == 0) {
                            result.append(Pattern.WORD_BREAK.replacer);
                        } else if (countOfZeros % (Pattern.LETTER_BREAK.bitCodeLength * capacity) == 0) {
                            result.append(Pattern.LETTER_BREAK.replacer);
                        }
                        countMode = CountMode.ONE;
                        countOfOnes = 1;
                    }
                }
            }
        }

        if (countOfOnes != 0) {
            if (countOfOnes % (Pattern.DASH.bitCodeLength * capacity) == 0) {
                result.append(Pattern.DASH.replacer);
            } else {
                result.append(Pattern.DOT.replacer);
            }
        }

        return result.toString();
    }

    private static int getCapacity(int[] bits, int endSequence, int startSequence) {
        int countOfZeros = 0;
        int countOfOnes = 0;
        int minZeros = 0;
        int maxOnes = 0;
        int capacity;

        for (int i = startSequence; i <= endSequence; i++) {
            if (bits[i] == 0) {
                countOfZeros++;
                if (maxOnes == 0 || countOfOnes > maxOnes) {
                    maxOnes = countOfOnes;
                }
                countOfOnes = 0;

            } else {
                countOfOnes++;
                if (minZeros == 0 || (countOfZeros != 0 && countOfZeros < minZeros)) {
                    minZeros = countOfZeros;
                }
                countOfZeros = 0;
            }
        }
        if (maxOnes == 0 && countOfOnes != 0) {
            maxOnes = countOfOnes;
        }


        if (maxOnes != 0 && (minZeros == 0 || maxOnes == minZeros)) {
            capacity = maxOnes;
        } else {

            if (minZeros >= Pattern.WORD_BREAK.bitCodeLength && minZeros % Pattern.WORD_BREAK.bitCodeLength == 0) {
                capacity = minZeros / Pattern.WORD_BREAK.bitCodeLength;
            } else if (minZeros >= Pattern.LETTER_BREAK.bitCodeLength && minZeros % Pattern.LETTER_BREAK.bitCodeLength == 0) {
                capacity = minZeros / Pattern.LETTER_BREAK.bitCodeLength;
            } else {
                capacity = minZeros;
            }
        }
        return capacity;
    }


    public static String decodeMorse(String data) {
        return Stream.of(data.trim().split("   ")).map((String word) ->
                Stream.of(word.split(" ")).map((String letter) -> MorseCode.get(letter)).collect(Collectors.joining(""))
        ).collect(Collectors.joining(" "));
    }
}