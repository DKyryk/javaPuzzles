package com.ercart.codegame;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;
/**
 * @author dkyryk
 */
public class DefibrillatorFinder {
    private static final int EARTH_RADIUS = 6371;

    public static void main(String args[]) {
        DefibrillatorFinder solution = new DefibrillatorFinder();
        solution.resolve();
    }


    public void resolve() {
        Scanner in = new Scanner(System.in);
        List<Defibrillator> defibrillators = new ArrayList<>();
        double userLongitude = convertDegreesToRadian(in.nextLine());
        double userLatitude = convertDegreesToRadian(in.nextLine());
        int defibrillatorsNumber = in.nextInt();
        in.nextLine();
        IntStream.rangeClosed(1, defibrillatorsNumber).forEach((int i) -> {
            Defibrillator defibrillator = new Defibrillator(in.nextLine().split(";"));
            if (defibrillator.isValid) {
                defibrillators.add(defibrillator);
            }
        });

        Defibrillator nearest = findNearest(defibrillators, userLongitude, userLatitude);
        if (nearest != null) {
            System.out.println(nearest.name);
        }

    }

    private Defibrillator findNearest(List<Defibrillator> defibrillators, double userLongitude, double userLatitude) {
        defibrillators.stream().forEach((Defibrillator d) -> d.calculateDistance(userLatitude, userLongitude));
        return defibrillators.stream()
                .min((Defibrillator d1, Defibrillator d2) -> Double.compare(d1.distance, d2.distance))
                .orElse(null);

    }

    private double convertDegreesToRadian(String value) {
        return (Double.valueOf(value.replace(",", ".")) * Math.PI) / 180;
    }

    class Defibrillator {

        int id;
        String name;
        String address;
        String phoneNumber;
        double longitude;
        double latitude;

        private double distance;

        private boolean isValid;

        public Defibrillator(String[] data) {
            if (data.length == 6) {
                this.id = Integer.parseInt(data[0]);
                this.name = data[1];
                this.address = data[2];
                this.phoneNumber = data[3];
                this.longitude = convertDegreesToRadian(data[4]);
                this.latitude = convertDegreesToRadian(data[5]);
                this.isValid = true;
            } else {
                this.isValid = false;
            }
        }

        public void calculateDistance(double targetLatitude, double targetLongitude) {
            double x = (this.longitude - targetLongitude) * Math.cos((this.longitude + targetLongitude) / 2);
            double y = (this.latitude - targetLatitude);
            this.distance = Math.sqrt((Math.pow(x, 2)) + (Math.pow(y, 2))) * EARTH_RADIUS;
        }

    }


}
