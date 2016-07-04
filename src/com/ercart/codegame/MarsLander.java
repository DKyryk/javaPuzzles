package com.ercart.codegame;


import java.util.Scanner;

/**
 * @author dkyryk
 */
public class MarsLander {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int surfaceSize = in.nextInt(); // the number of points used to draw the surface of Mars.

        int flatStartX = -1;
        int flatEndX = 0;

        int flatY = 0;

        int previousXPosition = in.nextInt(); // X coordinate of a surface point. (0 to 6999)
        int previousYPosition = in.nextInt(); // Y coordinate of a surface point. By linking all the points together in a sequential fashion, you form the surface of Mars.
        for (int i = 1; i < surfaceSize; i++) {
            int landX = in.nextInt(); // X coordinate of a surface point. (0 to 6999)
            int landY = in.nextInt(); // Y coordinate of a surface point. By linking all the points together in a sequential fashion, you form the surface of Mars.
            if (landY == previousYPosition) {
                flatStartX = previousXPosition;
                flatY = landY;
            } else {
                if (flatStartX > 0) {
                    flatEndX = previousXPosition;
                }
            }

            previousYPosition = landY;
            previousXPosition = landX;

        }

        // game loop
        while (true) {
            int positionX = in.nextInt();
            int positionY = in.nextInt();

            if (positionX > flatEndX) {
                // move left
            } else if (positionX < flatStartX) {
                // move right
            }

            int hSpeed = in.nextInt(); // the horizontal speed (in m/s), can be negative.
            int vSpeed = in.nextInt(); // the vertical speed (in m/s), can be negative.
            int fuel = in.nextInt(); // the quantity of remaining fuel in liters.
            int rotate = in.nextInt(); // the rotation angle in degrees (-90 to 90).
            int power = in.nextInt(); // the thrust power (0 to 4).


            int rotateCommand = 0;
            int powerCommand;
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            if (vSpeed <= -40) {
                if (power < 4) {
                    powerCommand = power + 1;
                } else {
                    powerCommand = power;
                }
            } else {
                if (power > 0) {
                    powerCommand = power - 1;
                } else {
                    powerCommand = power;
                }
            }

            // 2 integers: rotate power. rotate is the desired rotation angle (should be 0 for level 1), power is the desired thrust power (0 to 4).
            System.out.println(rotateCommand + " " + powerCommand);
        }
    }
}

