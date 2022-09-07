package org.example;

public class MainApp {
    public static void main(String[] args) throws InterruptedException {
        Board board = new Board(20, 30, 30, false);

        do {
            board.lifeCycle();
            Thread.sleep(50);
        } while (!board.periodicConfiguration && !board.stableConfiguration && !board.allDead);

        System.out.println("End!");
    }
}
