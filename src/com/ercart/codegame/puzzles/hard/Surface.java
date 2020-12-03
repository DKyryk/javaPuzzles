package com.ercart.codegame.puzzles.hard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

/**
 * @author dkyryk
 */
public class Surface {

    private static Lake[][] LAKES;
    private static int LAKE_ID = 1;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt();
        int height = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }

        LAKES = new Lake[height][width];
        for (int y = 0; y < height; y++) {
            String row = in.nextLine();
            for (int x = 0; x < row.length(); x++) {
                if (row.charAt(x) == 'O') {
                    analyzePoint(x, y);
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

            int result = Optional.ofNullable(LAKES[point.y][point.x])
                    .map(Lake::size)
                    .orElse(0);

            System.out.println(result);
        }
    }

    private static void analyzePoint(int x, int y) {
        Point current = new Point(x, y);
        Lake up = getAdjacentUp(x, y);
        Lake left = getAdjacentLeft(x, y);
        if (up == null && left == null) {
            Lake lake = new Lake(current);
            LAKES[current.y][current.x] = lake;
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
        LAKES[current.y][current.x] = aggregator;
        aggregator.getPoints().addAll(add.getPoints());
        for (Point point : add.getPoints()) {
            LAKES[point.y][point.x] = aggregator;
        }
    }

    private static void combineWithLake(Point current, Lake lake) {
        lake.getPoints().add(current);
        LAKES[current.y][current.x] = lake;
    }

    private static Lake getAdjacentUp(int x, int y) {
        if (y != 0) {
            return LAKES[y - 1][x];
        }
        return null;
    }

    private static Lake getAdjacentLeft(int x, int y) {
        if (x != 0) {
            return LAKES[y][x - 1];
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
