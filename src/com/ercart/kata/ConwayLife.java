package com.ercart.kata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dkyryk
 */
public class ConwayLife {

    public static int[][] getGeneration(int[][] cells, int generations) {
        ConwayLife life = new ConwayLife(cells);
        life.progress(generations);
        return life.transformToRawState();
    }

    private final Map<Coordinates, Cell> world;

    public ConwayLife(int[][] cells) {

        world = new ConcurrentHashMap<>();

        for (int j = 0; j < cells[0].length + 2; j++) {
            addNewCellToWorld(CellState.DEAD, 0, j);
            addNewCellToWorld(CellState.DEAD, cells.length + 1, j);
        }
        for (int i = 0; i < cells.length; i++) {
            addNewCellToWorld(CellState.DEAD, i + 1, 0);
            addNewCellToWorld(CellState.DEAD, i + 1, cells[i].length + 1);
            for (int j = 0; j < cells[i].length; j++) {
                CellState state = CellState.resolveFromCode(cells[i][j]);
                addNewCellToWorld(state, i + 1, j + 1);
            }
        }
    }

    private void addNewCellToWorld(CellState state, int y, int x) {
        Coordinates coordinates = new Coordinates(x, y);
        if (!world.containsKey(coordinates)) {
            Cell cell = new Cell(state, x, y);
            world.put(coordinates, cell);
        }
    }

    private void printState() {
        int[][] rawState = transformToRawState();
        for (int i = 0; i < rawState.length; i++) {
            for (int j = 0; j < rawState[i].length; j++) {
                System.out.printf("|%2d|", rawState[i][j]);
            }
            System.out.println();
        }
    }

    private void progress(int generations) {
        for (int i = 0; i < generations; i++) {
            world.values().stream().forEach(Cell::calculateEvolution);
            world.values().stream().forEach(Cell::evolve);
        }
    }

    private int[][] transformToRawState() {

        int minX = world.keySet().stream()
                .filter(coordinates -> world.get(coordinates).getState() == CellState.ALIVE)
                .mapToInt(coord -> coord.x).min().orElse(0);
        int maxX = world.keySet().stream()
                .filter(coordinates -> world.get(coordinates).getState() == CellState.ALIVE)
                .mapToInt(coord -> coord.x).max().orElse(0);
        int minY = world.keySet().stream()
                .filter(coordinates -> world.get(coordinates).getState() == CellState.ALIVE)
                .mapToInt(coord -> coord.y).min().orElse(0);
        int maxY = world.keySet().stream()
                .filter(coordinates -> world.get(coordinates).getState() == CellState.ALIVE)
                .mapToInt(coord -> coord.y).max().orElse(0);

        int width = maxX - minX + 1;
        int height = maxY - minY + 1;

        int[][] rawState = new int[height][];
        for (int i = 0, y = minY; i < height; i++, y++) {
            rawState[i] = new int[width];
            for (int j = 0, x = minX; j < width; j++, x++) {
                Coordinates coordinates = new Coordinates(x, y);
                if (world.containsKey(coordinates)) {
                    rawState[i][j] = world.get(coordinates).getState().stateCode;
                } else {
                    rawState[i][j] = 0;
                }
            }
        }

        return rawState;
    }

    private class Cell {
        private int x;
        private int y;
        private CellState state;
        private CellEvolution evolution;

        private Cell(CellState state, int x, int y) {
            this.y = y;
            this.x = x;
            this.state = state;
            this.evolution = CellEvolution.INTACT;
        }

        public void calculateEvolution() {
            if (state == CellState.UNKNOWN) {
                return;
            }
            List<Cell> neighbors = new ArrayList<>();
            addExistingNeighbor(neighbors, x - 1, y - 1);
            addExistingNeighbor(neighbors, x - 1, y);
            addExistingNeighbor(neighbors, x - 1, y + 1);
            addExistingNeighbor(neighbors, x + 1, y - 1);
            addExistingNeighbor(neighbors, x + 1, y);
            addExistingNeighbor(neighbors, x + 1, y + 1);
            addExistingNeighbor(neighbors, x, y - 1);
            addExistingNeighbor(neighbors, x, y + 1);

            long aliveNeighborsCount = neighbors.stream().filter((Cell cell) -> cell.getState() == CellState.ALIVE).count();
            switch (state) {
                case ALIVE: {
                    if (aliveNeighborsCount < 2) {
                        evolution = CellEvolution.DIE;
                    } else if (aliveNeighborsCount > 3) {
                        evolution = CellEvolution.DIE;
                    } else {
                        evolution = CellEvolution.INTACT;
                    }
                    break;
                }
                case DEAD: {
                    if (aliveNeighborsCount == 3) {
                        evolution = CellEvolution.BORN;
                    } else {
                        evolution = CellEvolution.INTACT;
                    }
                    break;
                }
            }
        }
        private void addExistingNeighbor(List<Cell> neighbors, int x, int y) {
            Coordinates coordinates = new Coordinates(x, y);
            if (world.containsKey(coordinates)) {
                neighbors.add(world.get(coordinates));
            }
        }

        public void evolve() {
            switch (evolution) {
                case BORN: {
                    this.state = CellState.ALIVE;
                    createMissingDeadNeighbors();
                    break;
                }
                case DIE: {
                    this.state = CellState.DEAD;
                    break;
                }
                case INTACT: {
                    break;
                }
            }
        }

        private void createMissingDeadNeighbors() {
            addNewCellToWorld(CellState.DEAD, y - 1, x - 1);
            addNewCellToWorld(CellState.DEAD, y - 1, x);
            addNewCellToWorld(CellState.DEAD, y - 1, x + 1);
            addNewCellToWorld(CellState.DEAD, y + 1, x - 1);
            addNewCellToWorld(CellState.DEAD, y + 1, x);
            addNewCellToWorld(CellState.DEAD, y + 1, x + 1);
            addNewCellToWorld(CellState.DEAD, y, x - 1);
            addNewCellToWorld(CellState.DEAD, y, x + 1);
        }

        public CellState getState() {
            return state;
        }
    }

    private class Coordinates {
        private final int x;
        private final int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Coordinates that = (Coordinates) o;

            return x == that.x && y == that.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    private enum CellState {
        ALIVE(1), DEAD(0), UNKNOWN(-1);

        int stateCode;

        CellState(int stateCode) {
            this.stateCode = stateCode;
        }

        private static CellState resolveFromCode(int code) {
            for (CellState cellState : CellState.values()) {
                if (cellState.stateCode == code) {
                    return cellState;
                }
            }
            return UNKNOWN;
        }
    }

    private enum CellEvolution {
        DIE, BORN, INTACT
    }

}
