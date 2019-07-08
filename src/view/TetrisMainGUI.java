package view;

import java.awt.EventQueue;

/**
 * Starting point for the GUI interface. 
 * 
 * @author davidshannon
 * @version 1
 */
public final class TetrisMainGUI {
    
    /**
     * Initial delay for the timer (1000 ms).
     */
    private static final int MY_INITIAL_DELAY = 1000;

    /** Private Constructor to prohibit class instantiation. */
    private TetrisMainGUI() { }

    /**
     * Starting point for program.
     * 
     * @param theArgs Command line arguments.  
     */
    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //setLookAndFeel();
                new TetrisGUI(MY_INITIAL_DELAY).start();                                 
            }
        });

    }
}
