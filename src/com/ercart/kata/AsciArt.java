package com.ercart.kata;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author dkyryk
 */
public class AsciArt {
    public static void main(String args[]) {
        AsciArt solution = new AsciArt();
        solution.resolve();
    }

    private void resolve() {
        Scanner scanner = new Scanner(System.in);
        final int letterLength = scanner.nextInt();
        final int letterHeight = scanner.nextInt();
        scanner.nextLine();
        String codeWord = scanner.nextLine().toUpperCase();

        Decoder decoder = new Decoder(letterLength);
        IntStream.rangeClosed(1, letterHeight).forEach((int i) -> decoder.addPatterns(scanner.nextLine()));
        decoder.printDecodedText(codeWord);
        scanner.close();
    }


    private class Decoder {
        private final int[] DICTIONARY = "ABCDEFGHIJKLMNOPQRSTUVWXYZ?".chars().toArray();
        private final int UNKNOWN_SYMBOL = '?';
        private final int letterLength;
        private List<Map<Integer, String>> patternsByLine;


        public Decoder(int letterLength) {
            this.letterLength = letterLength;
            this.patternsByLine = new ArrayList<>();
        }

        public void addPatterns(String line) {
            final Map<Integer, String> pattern = new HashMap<>();
            IntStream.range(0, DICTIONARY.length).forEach((int index) -> {
                int startSymbolPattern = index * letterLength;
                int endSymbolPattern = (index + 1) * letterLength;
                if (endSymbolPattern > line.length()) {
                    endSymbolPattern = line.length();
                }
                pattern.put(DICTIONARY[index], line.substring(startSymbolPattern, endSymbolPattern));
            });

            patternsByLine.add(pattern);
        }

        public void printDecodedText(String text) {

            patternsByLine.stream().forEach((Map<Integer, String> linePatterns) ->
                    System.out.println(text.chars().mapToObj((int symbol) -> {
                        if (linePatterns.containsKey(symbol)) {
                            return linePatterns.get(symbol);
                        } else {
                            return linePatterns.get(UNKNOWN_SYMBOL);
                        }
                    }).collect(Collectors.joining()))
            );

        }
    }
}
