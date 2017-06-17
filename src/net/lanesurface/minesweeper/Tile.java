package net.lanesurface.minesweeper;

public class Tile {
    public boolean revealed;
    
    private boolean bomb;
    private boolean flagged;
    
    private int surroundingBombs;
    private char marker;
    
    public void enableBomb() {
        bomb = true;
    }
    public boolean containsEnabledBomb() {
        return bomb;
    }
    public boolean wasFlagged() {
        return flagged;
    }
    public void toggleFlag() {
        flagged = !flagged;
    }
    public void setBombsInVicinity(int count) {
        if (count == 0) marker = ' ';
        else marker = (char)('0' + count);
        
        surroundingBombs = count;
    }
    public int getBombsInVicinity() {
        return surroundingBombs;
    }
    public char getMarker() {
        return marker;
    }
}
