package com.ercart.graph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author dkyryk
 */
public class SnakeLadders {
    private static final int START_NODE = 1;
    private static final int END_NODE = 100;
    private static final int MAX_STEP = 6;


    public static void main(String args[]) {
        SnakeLadders solution = new SnakeLadders();
        solution.resolve();
    }

    private void resolve() {
        Scanner scanner = new Scanner(System.in);
        Integer numberOfTests = Integer.valueOf(scanner.nextLine());
        for (int i = 0; i < numberOfTests; i++) {
            Map<Integer, Integer> transitions = new HashMap<>();
            addTransitions(scanner, transitions);
            addTransitions(scanner, transitions);
            System.out.println(findShortestPathSize(transitions));
        }
        scanner.close();
    }

    private void addTransitions(Scanner scanner, Map<Integer, Integer> transitions) {
        int limit = Integer.valueOf(scanner.nextLine());
        for (int j = 0; j < limit; j++) {
            int[] nodes = Stream.of(scanner.nextLine().split(" ")).mapToInt(Integer::valueOf).toArray();
            transitions.put(nodes[0], nodes[1]);
        }
    }


    private int findShortestPathSize(Map<Integer, Integer> transitions) {
        List<Edge> edges = buildEdges(transitions);
        Graph graph = new Graph(edges);
        graph.buildPaths(START_NODE);

        return graph.findShortestPath(END_NODE).size();
    }

    private List<Edge> buildEdges(Map<Integer, Integer> transitions) {
        List<Edge> edges = new ArrayList<>();
        for (int startNode = START_NODE; startNode < END_NODE; startNode++) {
            if (!transitions.containsKey(startNode)) {
                for (int step = 1; step <= MAX_STEP; step++) {
                    int endNode = startNode + step;
                    if (endNode <= END_NODE) {
                        if (transitions.containsKey(endNode)) {
                            endNode = transitions.get(endNode);
                        }
                        edges.add(new Edge(startNode, endNode));
                    }
                }
            }
        }
        return edges;
    }

}
