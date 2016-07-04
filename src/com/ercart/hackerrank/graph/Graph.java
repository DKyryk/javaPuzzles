package com.ercart.hackerrank.graph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author dkyryk
 */
public class Graph {
    private int source;
    private final List<Edge> edges;
    private Set<Integer> settledNodes;
    private Set<Integer> unSettledNodes;
    private Map<Integer, Integer> paths;
    private Map<Integer, Integer> distance;


    public Graph(List<Edge> edges) {
        this.edges = edges;
    }

    public void buildPaths(int source) {
        this.source = source;
        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        distance = new HashMap<>();
        paths = new HashMap<>();
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Integer node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    public List<Edge> findShortestPath(int endNode) {
        List<Edge> shortestPath = new ArrayList<>();
        int index = endNode;
        while (paths.containsKey(index)) {
            int previousNode = paths.get(index);
            shortestPath.add(new Edge(previousNode, index));
            index = previousNode;
        }

        return shortestPath;
    }

    private void findMinimalDistances(Integer node) {

        edges.stream().filter((Edge edge) ->
                (edge.getStartNode() == node && !isSettled(edge.getEndNode())
                        && (getShortestDistance(edge.getEndNode()) > getShortestDistance(node) + 1)))
                .forEach((Edge neighbor) -> {
                    distance.put(neighbor.getEndNode(), getShortestDistance(node) + 1);
                    paths.put(neighbor.getEndNode(), node);
                    unSettledNodes.add(neighbor.getEndNode());
                });

    }


    private Integer getMinimum(Set<Integer> nodes) {
        Integer minimum = null;
        for (Integer vertex : nodes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Integer vertex) {
        return settledNodes.contains(vertex);
    }

    private int getShortestDistance(Integer destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

}
