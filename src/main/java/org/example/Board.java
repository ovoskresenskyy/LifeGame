package org.example;

import java.util.ArrayList;

public class Board {
    Cells[][] board;
    final boolean circular;
    boolean periodicConfiguration, stableConfiguration, allDead;

    private ArrayList<ArrayList<Cells>> allCycles;

    Board(int row, int column, int populationPercent, boolean circular) {
        periodicConfiguration = stableConfiguration = allDead = false;
        board = new Cells[row][column];
        allCycles = new ArrayList<>();

        firstGeneration(populationPercent);
        this.circular = circular;
    }

    private void firstGeneration(int populationPercent) {

        ArrayList<Cells> alivePoints = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < board[rowIndex].length; columnIndex++) {
                if (Math.random() < (populationPercent / 100.0)) {
                    Cells cell = new Cells(true);
                    board[rowIndex][columnIndex] = cell;
                    alivePoints.add(cell);
                } else {
                    board[rowIndex][columnIndex] = new Cells(false);
                }
            }
        }

        allCycles.add(alivePoints);
        print();
    }

    public void lifeCycle() {
/* Description of values
        X = live now
        . = dead now
        a = alive next cycle
        d = dead next cycle
*/

        int aliveNeighbors;
        Cells cell;

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                cell = board[row][column];

                aliveNeighbors = (cell.isAliveNow(false) ? -1 : 0) + countAliveNeighbors(row, column); //exclude ourselves

                if (cell.isDeadNow() && aliveNeighbors == 3) {
                    cell.willAwakeNextCycle();
                } else if (cell.isAliveNow(false) && (aliveNeighbors < 2 || aliveNeighbors > 3)) {
                    cell.willDieNextCycle();
                }
            }
        }
        updateBoard();
        print();
    }

    private int countAliveNeighbors(int row, int column) {
        int aliveNeighbors = 0;

        if (circular) {
            int[] neighborsRowsIndexArray = {row == 0 ? board.length - 1 : row - 1,
                    row,
                    row == board.length - 1 ? 0 : row + 1};
            int[] neighborsColumnsIndexArray = {column == 0 ? board[row].length - 1 : column - 1,
                    column,
                    column == board[row].length - 1 ? 0 : column + 1};

            for (int neighborRow : neighborsRowsIndexArray) {
                for (int neighborColumn : neighborsColumnsIndexArray) {
                    if (board[neighborRow][neighborColumn].isAliveNow(true)) {
                        aliveNeighbors++;
                    }
                }
            }
        } else {
            int iFrom, iTo, jFrom, jTo;

            iFrom = row == 0 ? 0 : -1;
            iTo = row == board.length - 1 ? 1 : 2;

            jFrom = column == 0 ? 0 : -1;
            jTo = column == board[row].length - 1 ? 1 : 2;

            for (int i = iFrom; i < iTo; i++) {
                for (int j = jFrom; j < jTo; j++) {
                    if (board[row + i][column + j].isAliveNow(true)) {
                        aliveNeighbors++;
                    }
                }
            }
        }

        return aliveNeighbors;
    }

    private void updateBoard() {
        ArrayList<Cells> alivePoints = new ArrayList<>();

        for (Cells[] cells : board) {
            for (Cells value : cells) {
                if (value.isAwakeNextCycle()) {
                    value.makeLiveNow();
                } else if (value.isDieNextCycle()) {
                    value.makeDeadNow();
                }

                if (value.isAliveNow(false)) {
                    alivePoints.add(value);
                }
            }
        }

        checkConfiguration(alivePoints);
    }

    private void checkConfiguration(ArrayList<Cells> alivePoints) {
        if (alivePoints.size() == 0) {
            allDead = true;
            System.out.println("Sad but everyone is dead.");
        } else if (alivePoints.equals(allCycles.get(allCycles.size() - 1))) {
            stableConfiguration = true;
            System.out.println("This configuration is stable.");
        } else {
            for (ArrayList<Cells> list : allCycles) {
                if (alivePoints.equals(list)) {
                    periodicConfiguration = true;
                    System.out.println("This configuration is periodical.");
                }
            }
        }

        if (!stableConfiguration && !periodicConfiguration) {
            allCycles.add(alivePoints);
        }
    }

    private void print() {
        for (Cells[] cells : board) {
            for (Cells value : cells) {
                System.out.print(value.getStatus() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
