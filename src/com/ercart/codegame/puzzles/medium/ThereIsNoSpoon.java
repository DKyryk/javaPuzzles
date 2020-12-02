package com.ercart.codegame.puzzles.medium;

import java.util.Scanner;

/**
 * @author dkyryk
 */
public class ThereIsNoSpoon {

    private static final int ABSENT_MARKER = -1;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt(); // the number of cells on the X axis
        int height = in.nextInt(); // the number of cells on the Y axis
        if (in.hasNextLine()) {
            in.nextLine();
        }

        int[][] grid = new int[height][width];

        for (int i = 0; i < height; i++) {
            String line = in.nextLine(); // width characters, each either 0 or .
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == '0') {
                    grid[i][j] = 1;
                } else {
                    grid[i][j] = 0;
                }
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[i][j] == 1) {
                    StringBuilder sb = new StringBuilder(j + " " + i);
                    int rightX = ABSENT_MARKER;
                    for (int k = j + 1; k < width; k++) {
                        if (grid[i][k] == 1) {
                            rightX = k;
                            break;
                        }
                    }
                    if (rightX != ABSENT_MARKER) {
                        sb.append(" ")
                                .append(rightX)
                                .append(" ")
                                .append(i);
                    }
                    else {
                        sb.append(" -1 -1");
                    }
                    int bottomY = ABSENT_MARKER;
                    for (int k = i + 1; k < height; k++) {
                        if (grid[k][j] == 1) {
                            bottomY = k;
                            break;
                        }
                    }
                    if (bottomY != ABSENT_MARKER) {
                        sb.append(" ")
                                .append(j)
                                .append(" ")
                                .append(bottomY);
                    }
                    else {
                        sb.append(" -1 -1");
                    }

                    System.out.println(sb);
                }
            }
        }

    }

}
