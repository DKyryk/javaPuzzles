package com.ercart.codegame;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author dkyryk
 */
public class Aneo {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int maxSpeed = in.nextInt();

        int lightCount = in.nextInt();
        List<Light> lights = new ArrayList<>();
        for (int i = 0; i < lightCount; i++) {
            int distance = in.nextInt();
            int duration = in.nextInt();
            lights.add(new Light(distance, duration));
        }

        for (int speed = maxSpeed; speed > 0; speed--) {
            if (isAllLightsGreenAtSpeed(speed, lights)) {
                System.out.println(speed);
                return;
            }
        }
    }

    private static boolean isAllLightsGreenAtSpeed(int speed, List<Light> lights) {

        int speedInMetersPerHour = speed * 1000;
        for (Light light : lights) {
            BigDecimal intervalAtArrival = BigDecimal.valueOf(light.distance)
                    .setScale(4, BigDecimal.ROUND_UP)
                    .multiply(BigDecimal.valueOf(3600))
                    .divide(BigDecimal.valueOf(speedInMetersPerHour), BigDecimal.ROUND_UP)
                    .divide(BigDecimal.valueOf(light.duration), BigDecimal.ROUND_UP);
            BigInteger interval = intervalAtArrival.toBigInteger();
            if (interval.intValue() % 2 == 1) {
                return false;
            }
        }
        return true;
    }

    private static class Light {
        int distance;
        int duration;

        private Light(int distance, int duration) {
            this.distance = distance;
            this.duration = duration;
        }
    }

}
