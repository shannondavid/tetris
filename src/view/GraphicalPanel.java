/**
 * David Shannon
 * TCSS 305 -- Assignment 6a, Tetris
 * Spring 2018
 */


package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JPanel;
//import javax.swing.border.Border;

/**
 * Class for the graphical panel. 
 * 
 * @author davidshannon
 * @version 1
 */
public class GraphicalPanel extends JPanel implements PropertyChangeListener {

    //data fields:
    
    /** Generated serial version ID. */
    private static final long serialVersionUID = 6355610856107820841L;
    
    /** Dimension for this JPanel. */
    private static final Dimension MY_PANEL_DIM = new Dimension(200, 400);
    
    /** Dimension for next-piece Panel. */
    private static final Dimension MY_PIECE_DIM = new Dimension(160, 160);
    
    /** Number of lines until the difficulty increases. */
    private static final int LINES_TO_NEXT_LEVEL = 5;
    
    /** Amount score increases whenever timer is called. */
    private static final int SCORE_INCREMENT = 10;
    
    /** Amount score increases when a line is cleared.*/
    private static final int LINE_SCORE_INCREMENT = 100;
    
    /** X-coordinate offset for the text to be displayed in graphical panel. */
    private static final int GRFX_X_OFFST = 35;
    
    /** Pixel offset when calling paint component. */
    private static final int PAINT_OFFST = 55;
    
    /** Y-coordinate offset for the score to be displayed in graphical panel. */
    private static final int SCORE_Y_OFFST = 160;
    
    /** Y-coordinate offset for the level to be displayed in graphical panel. */
    private static final int LEVEL_Y_OFFST = 180;
    
    /** Y-coordinate offset for the lines to be displayed in graphical panel. */
    private static final int LINES_Y_OFFST = 200;
    
    /** Y-coordinate offset for the total lines to be displayed in graphical panel. */
    private static final int T_LINES_Y_OFFST = 220;
    
    /** Y-coordinate offset for the high score to be displayed in graphical panel. */
    private static final int HS_Y_OFFST = 260;
    
    /** X-coordinate offset for the next piece to be displayed in graphical panel. */
    private static final int NP_X_OFFST = 60;
    
    /** Y-coordinate offset for the next piece to be displayed in graphical panel. */
    private static final int NP_Y_OFFST = 45;
    
    /** Support for firing property change events from this class. */
    private final PropertyChangeSupport myPCS = new PropertyChangeSupport(this);
    
    /** JPanel to display current piece. */
    private final JPanel myNextPiecePanel;
    
    /** String representation of next TetrisPiece. */
    private String myNextPiece;
    
    /** The size in pixels of a side of one "square" on the grid. */
    private int mySquareSize;
    
    /** Score of tetris game, to be displayed. */
    private int myScore;
    
    /** Level of tetris game, to be displayed. */
    private int myLevel;
    
    /** Counts number of lines cleared in game; used internally. */
    private int myLinesCleared;
    
    /** int for storing high scores. */
    private int myHighScore;
    
    
    //constructor:
    
    /**
     * Constructor for the graphical Panel.
     * 
     * @param theSquareSize the size of an individual Tetris block.
     */
    public GraphicalPanel(final int theSquareSize) {
        super();
        myNextPiecePanel = new JPanel();
        mySquareSize = theSquareSize;
        
        myScore = 0;
        myLevel = 1;
        myLinesCleared = 0;
        myHighScore = 0;

        setUpPanel();   
    }
    
    /**
     * Method for changing size of square.
     * @param theSize The new size of a single block.
     */
    public void setSquareSize(final int theSize) {
        mySquareSize = theSize;
    }
    
    /**
     * Public method for resetting score, level and lines cleared. 
     */
    public void resetScores() {
        myScore = 0;
        myLevel = 1;
        myLinesCleared = 0;
    }
   
    
    /**
     * Private helper method for setting up panel.
     */
    private void setUpPanel() {
        this.setSize(MY_PANEL_DIM);           //is this redundant?
        this.setPreferredSize(MY_PANEL_DIM);
        myNextPiecePanel.setSize(MY_PIECE_DIM);
        this.add(myNextPiecePanel, BorderLayout.CENTER);
    }
    
    /** Public method for updating score; called when timer fired. */
    public void updateScore() {
        myScore += SCORE_INCREMENT;
        setHighScore(myScore);
        repaint();
    }
    
    /**
     * Public method for setting high score. 
     * 
     * @param theScore the potentially new high score.
     */
    public void setHighScore(final int theScore) {
        if (theScore > myHighScore) {
            myHighScore = theScore;
        }
    }
    
    
    /**
     * Paints the next game piece. Triggered by repaint() method call.
     * 
     * @param theGraphics The graphics context to use for painting.
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        final String[] nPStrings = myNextPiece.split("\n");
        theGraphics.drawString("Next Piece: ", NP_X_OFFST, NP_Y_OFFST);
        for (int x = 0; x < nPStrings.length; x++) {
            for (int y = 0; y < nPStrings.length; y++) {
                final Rectangle2D.Double block =
                                new Rectangle2D.Double((double) y * mySquareSize + PAINT_OFFST,
                                                       (double) x * mySquareSize + PAINT_OFFST,
                                                       (double) mySquareSize,
                                                       (double) mySquareSize);
                if (nPStrings[x].charAt(y) == ' ') {
                    g2d.setPaint(Color.GRAY);
                    g2d.fill(block);
                } else {
                    g2d.setColor(getColor(nPStrings[x].charAt(y)));
                    g2d.fill(block);
                    g2d.setColor(Color.black);
                    g2d.draw(block);
                }
            }
        }
        theGraphics.setColor(Color.black);
        theGraphics.drawString("Score: " + myScore, GRFX_X_OFFST, SCORE_Y_OFFST);
        theGraphics.drawString("Level: " + myLevel, GRFX_X_OFFST, LEVEL_Y_OFFST);
        theGraphics.drawString("Lines to Next Level: " + Math.abs
                        ((myLinesCleared % LINES_TO_NEXT_LEVEL) - LINES_TO_NEXT_LEVEL),
                               GRFX_X_OFFST, LINES_Y_OFFST);
        theGraphics.drawString("Total Lines Cleared: " + myLinesCleared, GRFX_X_OFFST,
                               T_LINES_Y_OFFST);
        theGraphics.drawString("High Score: " + myHighScore, GRFX_X_OFFST, HS_Y_OFFST);
        
        
    }
    
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        final String propertyName = theEvent.getPropertyName();
        
        if ("preview".equals(propertyName)) {
            myNextPiece = (String) theEvent.getNewValue();
            repaint();
        } else if ("lines cleared".equals(propertyName)) { 
            final int num = ((Object[]) theEvent.getNewValue()).length;
            if (num != 0) {
                myLinesCleared += num;
                myScore += LINE_SCORE_INCREMENT * Math.pow(2, num - 1);
                if (myLinesCleared % LINES_TO_NEXT_LEVEL == 0) {
                    myLevel++;
                    myPCS.firePropertyChange("level up", myLevel - 1, myLevel);
                }
            }
        }
    }
    
    /**
     * Private method for retrieving color of each piece. *
     * @return the color of the block
     * @param theChar the block type
     */
    private Color getColor(final char theChar) {
        Color color = Color.WHITE;
        if (theChar == 'I') {
            color = Color.PINK;
        } else if (theChar == 'J') {
            color = Color.BLUE;
        } else if (theChar == 'L') {
            color = Color.ORANGE;
        } else if (theChar == 'O') { 
            color = Color.YELLOW;
        } else if (theChar == 'S') {
            color = Color.GREEN;
        } else if (theChar == 'T') {
            color = Color.CYAN;
        } else if (theChar == 'Z') {
            color = Color.RED;
        }
        
        return color;
        
    }
    
    /**
     * Adds a listener for property change events from this class.
     * 
     * @param theListener a PropertyChangeListener to add.
     */
    public void addPropertyChangeListener(final PropertyChangeListener theListener) {
        myPCS.addPropertyChangeListener(theListener);
    }
    
    /**
     * Removes a listener for property change events from this class.
     * 
     * @param theListener a PropertyChangeListener to remove.
     */
    public void removePropertyChangeListener(final PropertyChangeListener theListener) {
        myPCS.removePropertyChangeListener(theListener);
    }
}
