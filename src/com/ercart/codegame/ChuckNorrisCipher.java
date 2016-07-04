package com.ercart.codegame;


import java.util.stream.Collectors;

/**
 * @author dkyryk
 */
public class ChuckNorrisCipher {

    private String source;

    public ChuckNorrisCipher(String source) {
        this.source = source;
    }

    public String encode() {

        String binData = source.chars()
                .mapToObj((int value) -> String.format("%7s", Integer.toBinaryString(value) ).replace(' ', '0'))
                .collect(Collectors.joining());
        int[] symbols = binData.chars().toArray();
        StringBuilder result = new StringBuilder();
        int count = 1;
        Symbol countSymbol = detectSymbol(symbols[0]);

        for (int i = 1; i < symbols.length; i++) {
            Symbol symbol = detectSymbol(symbols[i]);
            if (symbol == countSymbol) {
                count++;
            } else {
                addGroupWithSeparator(countSymbol, count, result);
                count = 1;
                countSymbol = symbol;
            }
        }
        addGroup(countSymbol, count, result);

        return result.toString();
    }

    private void addGroupWithSeparator(Symbol countSymbol, int count, StringBuilder result) {
        addGroup(countSymbol, count, result);
        result.append(" ");
    }

    private void addGroup(Symbol countSymbol, int count, StringBuilder result) {
        result.append(countSymbol.pattern).append(" ");
        for (int i = 0; i < count; i++) {
            result.append("0");
        }
    }

    private Symbol detectSymbol(int value) {
        if (value == '1') {
            return Symbol.ONE;
        } else if (value == '0') {
            return Symbol.ZERO;
        }
        return Symbol.NOT_RESOLVED;
    }

    private enum Symbol {
        ONE("0"), ZERO("00"), NOT_RESOLVED("");
        private String pattern;

        Symbol(String pattern) {
            this.pattern = pattern;
        }
    }

}