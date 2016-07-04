package com.ercart.hackerrank.steadyGen;

import java.util.Scanner;

public class SteadyGenCalculation {
    private static final String DEFAULT_GEN_ITEMS = "ATCG";

    public static void main(String args[]) {
        SteadyGenCalculation calculation = new SteadyGenCalculation();
        calculation.resolve();
    }

    private void resolve() {
        Scanner scanner = new Scanner(System.in);
        GenDescription genDescription = new GenDescriptionWithMaxSteadyPart(DEFAULT_GEN_ITEMS, scanner.nextLine());
        System.out.println(genDescription.calculateChangesToFormSteadyGen());
        scanner.close();
    }

}
