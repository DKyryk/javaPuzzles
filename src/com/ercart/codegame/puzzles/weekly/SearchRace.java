package com.ercart.codegame.puzzles.weekly;

import java.util.Scanner;

/**
 * @author dkyryk
 */
public class SearchRace {

    private static final float COEF = 0.85F;

    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);
        int checkpoints = in.nextInt(); // Count of checkpoints to read

        Point[] targets = new Point[checkpoints];
        for (int i = 0; i < checkpoints; i++) {
            int x = in.nextInt(); // Position X
            int y = in.nextInt(); // Position Y
            targets[i] = new Point(x, y);
        }

        // game loop
        while (true) {
            int targetIndex = in.nextInt(); // Index of the checkpoint to lookup in the checkpoints input, initially 0
            Point target = targets[targetIndex];
            int x = in.nextInt(); // Position X
            int y = in.nextInt(); // Position Y
            Point car = new Point(x, y);

            int vx = in.nextInt(); // horizontal speed. Positive is right
            int vy = in.nextInt(); // vertical speed. Positive is downwards
            int angle = in.nextInt(); // facing angle of this car
            Point currentSpeedNextPoint = new Point(car.x + vx, car.y + vy);

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            int thrust = getThrust(car, target, currentSpeedNextPoint, angle);
            System.out.println(target.x + " " + target.y + " " + thrust);
        }
    }

    private static int getThrust(Point car, Point target, Point currentSpeedNextPoint, int angle) {

        float targetAngle = car.getAngle(target);
        float angleDiff = Math.abs(targetAngle - angle);
        float targetDistance = car.getDistance(target);
        float distanceCurrentSpeed = car.getDistance(currentSpeedNextPoint);

        if (angleDiff > 110) {
            return 0;
        }
        if (angleDiff > 60) {
            return 10;
        }
        if (angleDiff > 20) {
            if (targetDistance < speed(distanceCurrentSpeed, 40)) {
                return 20;
            }
            return 40;
        }

        float steps = targetDistance / (speed(distanceCurrentSpeed, 200));
        if (steps < 3) {
            return 20;
        }
        return 200;
    }

    private static float speed(float current, int adj) {
        return (current + adj) * COEF;
    }

    private static class Point {
        int x;
        int y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private float getAngle(Point target) {
            float angle = (float) Math.toDegrees(Math.atan2(target.y - y, target.x - x));
            if (angle < 0) {
                angle += 365;
            }
            return angle;
        }

        private float getDistance(Point target) {
            return (float) Math.sqrt((target.x - x) * (target.x - x) + (target.y - y) * (target.y - y));
        }
    }
}
