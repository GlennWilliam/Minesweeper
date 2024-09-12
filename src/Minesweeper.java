import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class Minesweeper {
    int tileSize = 70;
    int numRows = 8;
    int numColumns = numRows;
    int boardWidth = numColumns * tileSize;
    int boardHeight = numRows * tileSize;

    JFrame frame = new JFrame("Minesweeper");

    Minesweeper(){
        frame.setVisible(true);
    }
}
