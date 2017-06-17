package net.lanesurface.minesweeper;

import java.util.Scanner;

public class Game {
    private static final int WIDTH = 9,
                             HEIGHT = 9;
    private static Tile[][] board = new Tile[HEIGHT][WIDTH];
    
    private static final int NUM_BOMBS = 10; // Easy mode. 
    private static int score = 0;
    
    private static final String GAME_INSTRUCTIONS =
            "GAME INSTRUCTIONS:\n\n" +
            "Each command is composed of three characters:\n" +
            "$ The first character should be a digit representing the column number to modify.\n" +
            "$ The second character should be a letter representing the row to modify.\n" +
            "$ The third character should be a letter stating whether you want to (r)eveal the tile or (f)lag it.\n\n" +
            "For example, \"3Af\" flags the tile in the 3rd column and 1st row.\n\n";
    
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        // Initialize our board and generate positions for out bombs. Note that
        // this game doesn't wait until the player chooses their first position
        // to generate the bombs, so they could die on the first click!
        for (int row = 0; row < HEIGHT; row++)
            for (int column = 0; column < WIDTH; column++)
                board[row][column] = new Tile();
        
        for (int i = 0; i < NUM_BOMBS; i++) {
            // Generate random positions for our bombs. I don't account for
            // duplicate positions, so yeah....
            int row = (int)(Math.random()*WIDTH);
            int col = (int)(Math.random()*HEIGHT);
            
            board[row][col].enableBomb();
        }
        
        // Cacalate (ca-ca-late) the values for each tile.
        calculateAdjacentBombs();

        System.out.println(GAME_INSTRUCTIONS);
        
        // Main game loop. 
        while (true) {
            // Some trickery allows the column number to display properly.
            System.out.print(" ");
            for (int i = 0; i < WIDTH; i++)
                System.out.print(" " + (i+1));
            System.out.println();
            
            for (int row = 0; row < HEIGHT; row++) {
                char rLetter = (char)('A' + row);
                System.out.print(rLetter + " ");
                
                for (int column = 0; column < WIDTH; column++) {
                    Tile tile = board[row][column];
                    if (tile.revealed)
                        System.out.print(tile.getMarker() + " ");
                    else if (tile.wasFlagged()) 
                        System.out.print("P ");
                    else System.out.print("* ");
                }
                System.out.println();
            }
            
            // Get the next position to flag or reveal.
            System.out.println(">> ");
            String command = input.next();
            
            boolean reveal = command.charAt(2) == 'r';
            
            // Doesn't account for malformed input. Perhaps a bad thing, but I
            // don't care enough to check.
            int x = Integer.parseInt(command.substring(0, 1))-1,
                y = (int)(command.charAt(1)-'A');
            Tile tile = board[y][x];
            if (reveal) {
                if (tile.containsEnabledBomb()) {
                    gameOver();
                    break;
                }
                
                // Show the number of bombs in the vicinity. If there are zero,
                // show a blank space, and reveal all other connecting tiles
                // with zero bombs recursively.
                if (tile.getBombsInVicinity() != 0) tile.revealed = true;
                else revealTiles(x, y);
            }
            else tile.toggleFlag();
        }
    }
    private static void calculateAdjacentBombs() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int column = 0; column < WIDTH; column++) {
                int count = 0;
                for (int i = column-1; i <= column+1; i++) {
                    for (int j = row-1; j <= row+1; j++) {
                        if (i < 0 || i > (WIDTH-1) || j < 0 || j > (HEIGHT-1))
                            continue;
                        if (board[j][i].containsEnabledBomb())
                            count++;
                    }
                }
                board[row][column].setBombsInVicinity(count);
            }
        }
    }
    private static void revealTiles(int x, int y) {
        Tile tile = board[y][x];
        if(tile.revealed == true || tile.getBombsInVicinity() != 0)
            return;
        
        tile.revealed = true;
        
        for (int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++) {
                if (i < 0 || i > (WIDTH-1) || j < 0 || j > (HEIGHT-1))
                    continue;
                
                revealTiles(i, j);
            }
        }
    }
    private static void gameOver() {
        System.out.println("\nSorry, you lost the game.\n" +
                           "Your final score was: " + score);
        
        // Print the board with all spaces revealed.
        for (int row = 0; row < HEIGHT; row++) {
            for (int column = 0; column < WIDTH; column++) {
                Tile tile = board[row][column];
                
                if (tile.containsEnabledBomb())
                    System.out.print("# ");
                else System.out.print(tile.getMarker() + " ");
            }
            System.out.println();
        }
    }
}
