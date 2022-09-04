package org.example;

import java.util.ArrayList;

public class Board {
    char[][] board;
    final boolean circular;
    boolean periodicConfiguration, stableConfiguration, allDead;

    private ArrayList<ArrayList<Double>> allCycles;

    Board(int row, int column, int populationPercent, boolean circular) {
        periodicConfiguration = stableConfiguration = allDead = false;
        board = new char[row][column];
        allCycles = new ArrayList<>();

        firstGeneration(populationPercent);
        this.circular = circular;
    }

    private void firstGeneration(int populationPercent) {

        ArrayList<Double> alivePoints = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < board[rowIndex].length; columnIndex++) {
                if (Math.random() < (populationPercent / 100.0)) {
                    board[rowIndex][columnIndex] = 'X';
                    alivePoints.add(Double.valueOf(rowIndex + "." + columnIndex));
                } else {
                    board[rowIndex][columnIndex] = '.';
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

        for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < board[rowIndex].length; columnIndex++) {
                aliveNeighbors = board[rowIndex][columnIndex] == 'X' ? -1 : 0; //exclude ourselves

                if (circular) {
                    int[] neighborsRowsIndexArray = {rowIndex == 0 ? board.length - 1 : rowIndex - 1,
                            rowIndex,
                            rowIndex == board.length - 1 ? 0 : rowIndex + 1};

                    int[] neighborsColumnsIndexArray = {columnIndex == 0 ? board[rowIndex].length - 1 : columnIndex - 1,
                            columnIndex,
                            columnIndex == board[rowIndex].length - 1 ? 0 : columnIndex + 1};

                    for (int neighborRow : neighborsRowsIndexArray) {
                        for (int neighborColumn : neighborsColumnsIndexArray) {
                            if (board[neighborRow][neighborColumn] == 'X' || board[neighborRow][neighborColumn] == 'd') {
                                aliveNeighbors++;
                            }
                        }
                    }
                } else {
                    int iFrom, iTo, jFrom, jTo;

                    iFrom = rowIndex == 0 ? 0 : -1;
                    iTo = rowIndex == board.length - 1 ? 1 : 2;

                    jFrom = columnIndex == 0 ? 0 : -1;
                    jTo = columnIndex == board[rowIndex].length - 1 ? 1 : 2;

                    for (int i = iFrom; i < iTo; i++) {
                        for (int j = jFrom; j < jTo; j++) {
                            if (board[rowIndex + i][columnIndex + j] == 'X' || board[rowIndex + i][columnIndex + j] == 'd') {
                                aliveNeighbors++;
                            }
                        }
                    }
                }

                if (board[rowIndex][columnIndex] == '.' && aliveNeighbors == 3) {
                    board[rowIndex][columnIndex] = 'a';
                } else if (board[rowIndex][columnIndex] == 'X' && (aliveNeighbors < 2 || aliveNeighbors > 3)) {
                    board[rowIndex][columnIndex] = 'd';
                }
            }
        }
        updateBoard();
        print();
    }

    private void updateBoard() {
        ArrayList<Double> alivePoints = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < board[rowIndex].length; columnIndex++) {
                if (board[rowIndex][columnIndex] == 'a') {
                    board[rowIndex][columnIndex] = 'X';
                } else if (board[rowIndex][columnIndex] == 'd') {
                    board[rowIndex][columnIndex] = '.';
                }

                if (board[rowIndex][columnIndex] == 'X') {
                    alivePoints.add(Double.valueOf(rowIndex + "." + columnIndex));
                }
            }
        }

        checkConfiguration(alivePoints);
    }

    private void checkConfiguration(ArrayList<Double> alivePoints) {
        if (alivePoints.size() == 0) {
            allDead = true;
            System.out.println("Sad but everyone is dead.");
        } else if (alivePoints.equals(allCycles.get(allCycles.size() - 1))) {
            stableConfiguration = true;
            System.out.println("This configuration is stable.");
        } else {
            for (ArrayList<Double> list : allCycles) {
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
        for (char[] row : board) {
            for (char column : row) {
                System.out.print(column + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
