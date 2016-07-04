package com.ercart.hackerrank.steadyGen;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author dkyryk
 */
public class GenDescriptionWithMaxSteadyPart implements GenDescription {

    private final String genItems;
    private final String getLine;
    private Map<Integer, GenDifference> excessElements = new HashMap<>();
    private final int[] genElements;

    public GenDescriptionWithMaxSteadyPart(String items, String text) {
        this.genItems = items;
        this.getLine = text;
        this.genElements = text.chars().toArray();
    }

    private void init() {
        Map<Integer, GenDifference> genItemsDiff = new HashMap<>();
        int steadyGenRequirement = getLine.length() / genItems.length();
        genItems.chars().forEach((int item) -> genItemsDiff.put(item, new GenDifference(-steadyGenRequirement)));
        getLine.chars().forEach((int letter) -> {
            if (genItemsDiff.containsKey(letter)) {
                genItemsDiff.get((letter)).increaseAppliedDifference();
            }
        });
        genItemsDiff.entrySet().stream()
                .filter((Map.Entry<Integer, GenDifference> entry) -> entry.getValue().getAppliedDifference() > 0)
                .forEach((Map.Entry<Integer, GenDifference> entry) ->
                        excessElements.put(entry.getKey(), new GenDifference(entry.getValue().getAppliedDifference())));
    }

    @Override
    public int calculateChangesToFormSteadyGen() {
        init();
        if (excessElements.isEmpty()) {
            return 0;
        }

        Set<Integer> removePartSizes = new HashSet<>();
        int index = 0;
        int start = 0;

        while (index < genElements.length) {
            do {
                Integer element = genElements[index];
                if (excessElements.containsKey(element)) {
                    excessElements.get(element).decreaseAppliedDifference();
                }
                index++;
            } while (!isGenLinePartRemovesExcess() && index < genElements.length);

            if (index >= genElements.length) {
                break;
            }

            do {
                Integer element = genElements[start];
                if (excessElements.containsKey(element)) {
                    excessElements.get(element).increaseAppliedDifference();
                }
                start++;
            } while (isGenLinePartRemovesExcess() && start < index);
            removePartSizes.add(index - start + 1);

        }

        return removePartSizes.stream().mapToInt(Integer::intValue).min().orElse(-1);
    }

    private boolean isGenLinePartRemovesExcess() {
        return excessElements.values().stream()
                .filter((GenDifference genDif) -> genDif.getAppliedDifference() > 0).count() == 0;
    }

}
