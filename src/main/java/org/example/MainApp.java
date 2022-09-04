package org.example;

public class MainApp {
    public static void main(String[] args) throws InterruptedException {
        Board board = new Board(20, 30, 40, true);

        do {
            board.lifeCycle();
            Thread.sleep(1_000);
        } while (!board.periodicConfiguration && !board.stableConfiguration && !board.allDead);
    }
}
