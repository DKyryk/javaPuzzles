package com.ercart.hackerrank.steadyGen;


import java.util.HashMap;
import java.util.Map;

/**
 * @author dkyryk
 */
class GenDescriptionWithShiftingWindow implements GenDescription {

    private final String genItems;
    private final String getLine;
    private Map<Integer, GenDifference> excessElements = new HashMap<>();
    private final int[] genElements;

    public GenDescriptionWithShiftingWindow(String items, String text) {
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

        int changeWindow = excessElements.values().stream()
                .mapToInt((GenDifference genDiff) -> genDiff.getInitialDifference()).sum();
        boolean isRequiredChangeFound = false;

        while (changeWindow <= genElements.length && !isRequiredChangeFound) {
            excessElements.values().stream().forEach(GenDifference::resetAppliedDifference);
            //evaluate first window
            for (int j = 0; j < changeWindow; j++) {
                int element = genElements[j];
                if (excessElements.containsKey(element)) {
                    excessElements.get(element).getAppliedDifference();
                }
            }
            if (isReducedFormSteadyGen()) {
                isRequiredChangeFound = true;
                break;
            }

            int windowShift = calculateWindowShift();
            int index = windowShift;
            while (index + changeWindow < genElements.length) {
                //evaluate diff
                if (windowShift > changeWindow / 2) {
                    excessElements.values().stream().forEach(GenDifference::resetAppliedDifference);
                    for (int j = index; j < index + changeWindow; j++) {
                        int element = genElements[j];
                        if (excessElements.containsKey(element)) {
                            excessElements.get(element).decreaseAppliedDifference();
                        }
                    }
                } else {
                    for (int j = 1; j <= windowShift; j++) {
                        int returnedElement = genElements[index - j];
                        if (excessElements.containsKey(returnedElement)) {
                            excessElements.get(returnedElement).increaseAppliedDifference();
                        }
                        int newElement = genElements[index + changeWindow - j];
                        if (excessElements.containsKey(newElement)) {
                            excessElements.get(newElement).decreaseAppliedDifference();
                        }
                    }
                }
                //check if found
                if (isReducedFormSteadyGen()) {
                    isRequiredChangeFound = true;
                    break;
                } else {
                    //shift window
                    windowShift = calculateWindowShift();
                    index += windowShift;
                }
            }
            if (!isRequiredChangeFound) {
                changeWindow++;
            }
        }

        return isRequiredChangeFound ? changeWindow : -1;
    }

    private boolean isReducedFormSteadyGen() {
        return excessElements.values().stream()
                .filter((GenDifference genDiff) -> genDiff.getAppliedDifference() > 0).count() == 0;
    }

    private int calculateWindowShift() {
        return excessElements.values().stream()
                .filter((GenDifference genDiff) -> genDiff.getAppliedDifference() > 0)
                .mapToInt((GenDifference genDiff) -> genDiff.getAppliedDifference()).sum();
    }
}
