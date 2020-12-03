package com.ercart.codegame.puzzles.hard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

/**
 * @author dkyryk
 */
public class Surface {

    private static Map<Point, Lake> LAKES_PER_POINT = new HashMap<>();
    private static int LAKE_ID = 1;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt();
        int height = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }

        int[][] surface = new int[height][width];
        for (int y = 0; y < height; y++) {
            String row = in.nextLine();
            for (int x = 0; x < row.length(); x++) {
                if (row.charAt(x) == '#') {
                    surface[y][x] = 0;
                } else {
                    surface[y][x] = 1;
                    analyzePoint(surface, x, y);
                }
            }
        }
        int pointsToTest = in.nextInt();

        List<Point> points = new ArrayList<>();
        for (int i = 0; i < pointsToTest; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            points.add(new Point(x, y));
        }
        for (Point point : points) {

            int result = Optional.ofNullable(LAKES_PER_POINT.get(point))
                    .map(Lake::size)
                    .orElse(0);

            System.out.println(result);
        }
    }

    private static void analyzePoint(int[][] surface, int x, int y) {
        Point current = new Point(x, y);
        Lake up = getAdjacentUp(surface, x, y);
        Lake left = getAdjacentLeft(surface, x, y);
        if (up == null && left == null) {
            Lake lake = new Lake(current);
            LAKES_PER_POINT.put(current, lake);
        } else if (up == null) {
            combineWithLake(current, left);
        } else if (left == null) {
            combineWithLake(current, up);
        } else {
            if (up.equals(left)) {
                combineWithLake(current, up);
            } else {
                combineLakes(current, up, left);
            }
        }
    }

    private static void combineLakes(Point current, Lake up, Lake left) {
        if (up.size() > left.size()) {
            addToBiggest(current, up, left);
        } else {
            addToBiggest(current, left, up);
        }
    }

    private static void addToBiggest(Point current, Lake aggregator, Lake add) {
        aggregator.getPoints().add(current);
        LAKES_PER_POINT.put(current, aggregator);
        aggregator.getPoints().addAll(add.getPoints());
        for (Point point : add.getPoints()) {
            LAKES_PER_POINT.put(point, aggregator);
        }
    }

    private static void combineWithLake(Point current, Lake lake) {
        lake.getPoints().add(current);
        LAKES_PER_POINT.put(current, lake);
    }

    private static Lake getAdjacentUp(int[][] surface, int x, int y) {
        if (y != 0) {
            if (surface[y - 1][x] == 1) {
                return LAKES_PER_POINT.get(new Point(x, y - 1));
            }
        }
        return null;
    }

    private static Lake getAdjacentLeft(int[][] surface, int x, int y) {
        if (x != 0) {
            if (surface[y][x - 1] == 1) {
                return LAKES_PER_POINT.get(new Point(x - 1, y));
            }
        }
        return null;
    }

    private static class Point {
        int x;
        int y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x &&
                    y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private static class Lake implements Comparable<Lake> {
        private final Set<Point> points = new HashSet<>();
        private final int id;

        private Lake(Point point) {
            this.points.add(point);
            this.id = LAKE_ID;
            LAKE_ID++;
        }

        int size() {
            return points.size();
        }

        private Set<Point> getPoints() {
            return points;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Lake lake = (Lake) o;
            return id == lake.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public int compareTo(Lake o) {
            return Integer.compare(size(), o.size());
        }
    }
}
