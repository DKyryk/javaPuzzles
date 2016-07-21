package com.ercart.kata;

import java.math.BigInteger;

/**
 * @author dkyryk
 */
public class Fibonacci {

    public static BigInteger fib(BigInteger n) {
        if (BigInteger.ZERO.equals(n)) {
            return BigInteger.ZERO;
        } else if (BigInteger.ONE.equals(n)) {
            return BigInteger.ONE;
        } else {

            Fibonacci fibonacci = new Fibonacci();
            if (n.compareTo(BigInteger.ZERO) < 0) {
                if (n.negate().mod(BigInteger.valueOf(2)).equals(BigInteger.ONE)) {
                    return fibonacci.calculateMatrices(n.negate());
                } else {
                    return fibonacci.calculateMatrices(n.negate()).negate();
                }
            } else {
                return fibonacci.calculateMatrices(n);
            }
        }
    }

    private BigInteger calculateLinear(BigInteger n) {

        long a = 0;
        long b = 1;
        long result = a + b;
        for (int index = 2; index <= n.intValue(); index++) {
            result = a + b;
            a = b;
            b = result;
        }
        return BigInteger.valueOf(result);
    }


    public BigInteger calculateMatrices(BigInteger n) {
        BigInteger[][] result = {{BigInteger.ONE, BigInteger.ZERO}, {BigInteger.ZERO, BigInteger.ONE}};
        BigInteger[][] transformMatrix = new BigInteger[][] {{BigInteger.ONE, BigInteger.ONE},{BigInteger.ONE, BigInteger.ZERO}};

        BigInteger counter = n;
        while (counter.compareTo(BigInteger.ZERO) > 0) {
            if (counter.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE)) {
                result = multiplyMatrices(result, transformMatrix);
            }
            counter = counter.divide(BigInteger.valueOf(2));
            transformMatrix = multiplyMatrices(transformMatrix, transformMatrix);
        }

        return result[1][0];
    }

    private BigInteger[][] multiplyMatrices(BigInteger[][] a, BigInteger[][] b) {
        int aRows = a.length;
        int aColumns = a[0].length;
        int bRows = b.length;
        int bColumns = b[0].length;
        BigInteger[][] result = new BigInteger[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                result[i][j] = BigInteger.ZERO;
                for (int k = 0; k < aColumns; k++) {
                    result[i][j] = result[i][j].add(a[i][k].multiply(b[k][j]));
                }
            }
        }

        return result;
    }

}
