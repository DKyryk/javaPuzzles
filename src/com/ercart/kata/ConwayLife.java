package com.ercart.kata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dkyryk
 */
public class ConwayLife {

    public static int[][] getGeneration(int[][] cells, int generations) {
        ConwayLife life = new ConwayLife(cells);
        life.progress(generations);
        return life.transformToRawState();
    }

    private final Cell[][] universe;

    private ConwayLife(int[][] cells) {
        universe = new Cell[cells.length + 2][];

        universe[0] = new Cell[cells[0].length + 2];
        for (int j = 0; j < universe[0].length; j++) {
            universe[0][j] = new Cell(CellState.UNKNOWN.stateCode, 0, j);
        }
        universe[universe.length - 1] = new Cell[cells[cells.length - 1].length + 2];
        for (int j = 0; j < universe[universe.length - 1].length; j++) {
            universe[universe.length - 1][j] = new Cell(CellState.UNKNOWN.stateCode, universe.length - 1, j);
        }
        for (int i = 0; i < cells.length; i++) {
            universe[i + 1] = new Cell[cells[i].length + 2];

            universe[i + 1][0] = new Cell(CellState.UNKNOWN.stateCode, i + 1, 0);
            universe[i + 1][universe[i + 1].length - 1] = new Cell(CellState.UNKNOWN.stateCode, i + 1, universe[i + 1].length - 1);
            for (int j = 0; j < cells[i].length; j++) {
                universe[i + 1][j + 1] = new Cell(cells[i][j], i + 1, j + 1);
            }
        }
    }

    private void printState() {
        for (int i = 0; i < universe.length; i++) {
            for (int j = 0; j < universe[i].length; j++) {
                System.out.printf("|%2d|", universe[i][j].getState().stateCode);
            }
            System.out.println();
        }
    }

    private void progress(int generations) {
        for (int i = 0; i < generations; i++) {
            Arrays.stream(universe).flatMap(Arrays::stream)
                    .filter((Cell cell) -> cell.getState() != CellState.UNKNOWN)
                    .forEach((Cell cell) -> cell.calculateEvolution(universe));
            Arrays.stream(universe).flatMap(Arrays::stream)
                    .filter((Cell cell) -> cell.getState() != CellState.UNKNOWN)
                    .forEach(Cell::evolve);
        }
    }

    private int[][] transformToRawState() {
        int[][] rawState = new int[universe.length - 2][];
        for (int i = 0; i < rawState.length; i++) {
            rawState[i] = new int[universe[i + 1].length - 2];
            for (int j = 0; j < rawState[i].length; j++) {
                rawState[i][j] = universe[i + 1][j + 1].getState().stateCode;
            }
        }

        return rawState;
    }

    private class Cell {
        private int row;
        private int column;
        private CellState state;
        private CellEvolution evolution;

        private Cell(int stateCode, int row, int column) {
            this.row = row;
            this.column = column;
            this.state = CellState.resolveFromCode(stateCode);
            this.evolution = CellEvolution.INTACT;
        }

        public void calculateEvolution(Cell[][] world) {
            if (state == CellState.UNKNOWN) {
                return;
            }
            List<Cell> neighbors = new ArrayList<>();
            neighbors.add(world[row - 1][column - 1]);
            neighbors.add(world[row - 1][column]);
            neighbors.add(world[row - 1][column + 1]);
            neighbors.add(world[row][column - 1]);
            neighbors.add(world[row][column + 1]);
            neighbors.add(world[row + 1][column - 1]);
            neighbors.add(world[row + 1][column]);
            neighbors.add(world[row + 1][column + 1]);

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

        public void evolve() {
            switch (evolution) {
                case BORN: {
                    this.state = CellState.ALIVE;
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

        public CellState getState() {
            return state;
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
