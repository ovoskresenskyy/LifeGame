package org.example;

public class Cells {
    private char status;

    Cells(boolean alive){
        if (alive) {
            makeLiveNow();
        } else {
            makeDeadNow();
        }
    }

    char getStatus() { return status; }

    void makeLiveNow(){ status = 'X'; }

    void makeDeadNow(){ status = '.'; }

    void willAwakeNextCycle(){ status = 'a'; }

    void willDieNextCycle(){ status = 'd'; }

    boolean isAliveNow(boolean countNextCycle){ return countNextCycle ? status == 'X' || isDieNextCycle() : status == 'X'; }

    boolean isDeadNow(){ return status == '.'; }

    boolean isAwakeNextCycle(){ return status == 'a'; }

    boolean isDieNextCycle(){ return status == 'd'; }

}
