package com.ercart.codegame;

import java.util.Scanner;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

/**
 * @author dkyryk
 */
public class CodersStrikeBack {
    private static boolean BOOST_USED = false;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            int x = in.nextInt();
            int y = in.nextInt();
            int nextCheckpointX = in.nextInt(); // x position of the next check point
            int nextCheckpointY = in.nextInt(); // y position of the next check point
            int distance = in.nextInt(); // distance to the next checkpoint
            int angle = in.nextInt(); // angle between your pod orientation and the direction of the next checkpoint
            int opponentX = in.nextInt();
            int opponentY = in.nextInt();

            if (useBoost(distance, angle)) {
                System.out.println(nextCheckpointX + " " + nextCheckpointY + " BOOST");
            } else {
                String command = IntStream.of(nextCheckpointX, nextCheckpointY, calculateThrust(distance, angle))
                        .mapToObj(String::valueOf)
                        .collect(joining(" "));
                System.out.println(command);
            }

        }
    }

    private static boolean useBoost(int distance, int angle) {
        if (!BOOST_USED && distance > 500 && between(angle, -5, 5)) {
            BOOST_USED = true;
            return true;
        } else {
            return false;
        }
    }

    private static int calculateThrust(int distance, int angle) {
        if (angle > 90 || angle < -90) {
            return 0;
        }
        if (between(distance, 30, 50)) {
            return 50;
        }
        if (between(distance, 10, 30)) {
            return 20;
        }
        if (distance < 10) {
            return 5;
        }

        if (between(angle, -90, -45) || between(angle, 45, 90)) {
            return 50;
        }

        return 100;
    }

    private static boolean between(int check, int min, int max) {
        return check > min && check <= max;
    }
}
