import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.w3c.dom.Text;


public class Minesweeper {
    private class MineTile extends JButton{
        int r;
        int c;

        public MineTile (int r, int c){
            this.r = r;
            this.c = c;
        }
    }

    int tileSize = 70;
    int numRows = 8;
    int numColumns = numRows;
    int boardWidth = numColumns * tileSize;
    int boardHeight = numRows * tileSize;

    // JFrame is a top-level container used to create a window in a Swing application.
    // JLabel is a component that displays a short string or an image icon.
    // JPanel is a generic lightweight container.
    JFrame frame = new JFrame("Minesweeper"); // Main window of the game
    JLabel textLabel = new JLabel(); // Label to display text 
    JPanel textPanel = new JPanel(); // Panel to hold the text label
    JPanel boardPanel = new JPanel(); // Panel to hold the game board

    MineTile[][] board = new MineTile[numRows][numColumns]; // 2D array to hold the tiles
    ArrayList<MineTile> mineList; // List to hold the mines

    int mineCount = 10;
    Random random = new Random();
    int tilesClicked = 0; 
    boolean gameOver = false;

    Minesweeper(){
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper: " + Integer.toString(mineCount));
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numColumns));
        frame.add(boardPanel);

        for (int r = 0; r < numRows; r++){
            for (int c = 0; c < numColumns; c++){
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial", Font.PLAIN, 45));
                tile.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mousePressed(MouseEvent e){
                        if(gameOver){
                            return;
                        }
                        MineTile tile = (MineTile) e.getSource();
                        if(e.getButton() == MouseEvent.BUTTON1){ // 1: Left Click; 2: Scroll; 3: Right Click
                            if(tile.getText() == ""){
                                if(mineList.contains(tile)){
                                    revealMines();
                                }
                                else{
                                    checkMine(tile.r, tile.c);
                                }
                            }
                    
                        }
                        else if(e.getButton() == MouseEvent.BUTTON3){ // Right Click
                            if(tile.getText() == ""){
                                tile.setText("🚩");
                            }
                            else if(tile.getText() == "🚩"){
                                tile.setText("");
                            }
                        }
                    }
                });

                boardPanel.add(tile);
                
            }
        }

        frame.setVisible(true);

        setMines();
        
    }

    void setMines(){
        mineList = new ArrayList<MineTile>();
        
        int mineLeft = mineCount;
        while(mineLeft > 0){
            int r = random.nextInt(numRows);
            int c = random.nextInt(numColumns);
            MineTile tile = board[r][c];
            if(!mineList.contains(tile)){
                mineList.add(tile);
                mineLeft--;
            }
        }
    }

    void revealMines(){
        for (int i = 0; i < mineList.size(); i++){
            MineTile tile = mineList.get(i);
            tile.setText("💣"); 
        }
        gameOver = true;
        textLabel.setText("Game Over");
    }

    void checkMine(int r, int c){
        if(r < 0 || r >= numRows || c < 0 || c >= numColumns){
            return;
        }
        MineTile tile = board[r][c];
        if(!tile.isEnabled()){
            return;
        }
        tile.setEnabled(false);

        tilesClicked++;
        int minesFound = 0;

        // Top
        minesFound += countMine(r-1, c-1); // Top Left
        minesFound += countMine(r-1, c); // Top middle
        minesFound += countMine(r-1, c+1); // Top Right
        
        // Left and Right
        minesFound += countMine(r, c-1); // Left
        minesFound += countMine(r, c+1); // Right
        
        // Bottom
        minesFound += countMine(r+1, c-1); // Bottom Left
        minesFound += countMine(r+1, c); // Bottom middle
        minesFound += countMine(r+1, c+1); // Bottom Right

        if(minesFound > 0){
            tile.setText(Integer.toString(minesFound));
            System.out.println("Mines found around (" + r + ", " + c + "): " + minesFound);
        }
        else{
            tile.setText("");
            System.out.println("No mines found around (" + r + ", " + c + ")");

            // Check recursively
            checkMine(r-1, c-1); // Top Left
            checkMine(r-1, c); // Top middle
            checkMine(r-1, c+1); // Top Right

            checkMine(r, c-1); // Left
            checkMine(r, c+1); // Right

            checkMine(r+1, c-1); // Bottom Left
            checkMine(r+1, c); // Bottom middle
            checkMine(r+1, c+1); // Bottom Right

        }
        if(tilesClicked == numRows * numColumns - mineList.size()){
            gameOver = true;
            textLabel.setText("You Win!");
        }
    }

    int countMine(int r, int c){
        if(r < 0 || r >= numRows || c < 0 || c >= numColumns){
            return 0;
        }
        if(mineList.contains(board[r][c])){
            return 1;
        }
        return 0;
    }
}
