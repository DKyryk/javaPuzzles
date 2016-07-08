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
            return fibonacci.calculateMatrices(n);
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
        long[][] result = {{1, 0}, {0, 1}};
        long[][] transformMatrix = new long[][] {{1,1},{1,0}};

        long counter = n.longValue();
        while (counter > 0) {
            if (counter % 2 == 1) {
                result = multiplyMatrices(result, transformMatrix);
            }
            counter = counter / 2;
            transformMatrix = multiplyMatrices(transformMatrix, transformMatrix);
        }

        return BigInteger.valueOf(result[1][0]);
    }

    private long[][] multiplyMatrices(long[][] a, long[][] b) {
        int aRows = a.length;
        int aColumns = a[0].length;
        int bRows = b.length;
        int bColumns = b[0].length;
        long[][] result = new long[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                for (int k = 0; k < aColumns; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        return result;
    }

}
