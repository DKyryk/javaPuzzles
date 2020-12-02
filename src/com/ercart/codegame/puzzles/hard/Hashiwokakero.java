package com.ercart.codegame.puzzles.hard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * @author dkyryk
 */
public class Hashiwokakero {

    /*
      1) Build matrix of nodes
      2) Have total number of nodes per each required links
      3) Have array of links
      4) traverse matrix to get up/down/left/right neighbor info for each node
      5) while processedNodes.size != allnodes.size do calculation (or all nodes is empty)

      - method to try path
      - method to try all guaranteed paths
     */

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt(); // the number of cells on the X axis
        int height = in.nextInt(); // the number of cells on the Y axis
        if (in.hasNextLine()) {
            in.nextLine();
        }

        Map<Integer, List<Node>> nodesByRequiredLinks = new HashMap<>();
        IntStream.rangeClosed(1, 8)
                .forEach(i -> nodesByRequiredLinks.put(i, new ArrayList<>()));

        Cell[][] grid = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            String line = in.nextLine(); // width characters, each either a number or a '.'
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c != '.') {
                    int requiredLinks = c - '0';
                    grid[i][j] = new Node(requiredLinks);
                }
            }
        }

        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");


        // Two coordinates and one integer: a node, one of its neighbors, the number of links connecting them.
        System.out.println("0 0 2 0 1");
    }

    private interface Cell {

    }

    private static class Node implements Cell {
        private Node right;
        private Node left;
        private Node up;
        private Node down;

        private final int requiredLinks;

        private int linksCount;

        private Node(int requiredLinks) {
            this.requiredLinks = requiredLinks;
        }
    }

    private enum Bridge implements Cell {
        VERTICATL, DOUBLE_VERTICAL, HORIZONTAL, DOUBLE_HORIZONTAL
    }

}
