package com.ercart.kata;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author dkyryk
 */
public class WeirdPrimeGen {

    private static final int FIRST_SEQUENCE_ELEMENT = 7;

    public static long countOnes(long size) {
        PrimeSequence primeSequence = new PrimeSequence();
        return primeSequence.countOnes((int) size);
    }

    public static long maxPn(long size) {
        PrimeSequence primeSequence = new PrimeSequence();
        return primeSequence.maxPn((int) size);
    }

    public static int anOverAverage(long size) {
        PrimeSequence primeSequence = new PrimeSequence();
        return primeSequence.anOverAverage((int) size);
    }

    private static class PrimeSequence {

        public long countOnes(int size) {

            long onesCount = 1;
            int prevSource = FIRST_SEQUENCE_ELEMENT;
            int index = 2;
            while (index <= size) {
                int source = prevSource + findGcd(index, prevSource);
                int dif = source - prevSource;
                if (dif == 1) {
                    onesCount++;
                }
                prevSource = source;
                index++;
            }

            return onesCount;
        }

        private int findGcd(int a, int b) {
            BigInteger b1 = BigInteger.valueOf(a);
            BigInteger b2 = BigInteger.valueOf(b);
            return b1.gcd(b2).intValue();
        }


        public long maxPn(long size) {
            Set<Integer> uniquePrime = new HashSet<>();
            int prevSource = FIRST_SEQUENCE_ELEMENT;
            int index = 2;
            while (uniquePrime.size() != size) {
                int source = prevSource + findGcd(index, prevSource);
                int dif = source - prevSource;
                if (dif != 1) {
                    uniquePrime.add(dif);
                }
                prevSource = source;
                index++;
            }
            return uniquePrime.stream().mapToInt(Integer::intValue).max().orElse(0);
        }


        public int anOverAverage(int size) {
            int prevSource = FIRST_SEQUENCE_ELEMENT;
            List<Integer> anOver = new ArrayList<>();

            int index = 2;
            while (anOver.size() != size) {
                int source = prevSource + findGcd(index, prevSource);
                int dif = source - prevSource;
                if (dif != 1) {
                    anOver.add(source / index);
                }
                prevSource = source;
                index++;
            }
            return (int) anOver.stream().mapToInt(Integer::intValue).average().orElse(0);
        }
    }

}

