package pl.edu.pwr.wordnetloom.client.systems.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JFrame;
import se.datadosen.component.RiverLayout;

public class DialogWindow extends JDialog {

    private static final long serialVersionUID = 1L;
    static private int BOTTOM_MARGIN = 40;

    public DialogWindow(JFrame baseFrame, String title, int x, int y, int width, int height) {
        super(baseFrame, title, true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        if (x + width > screenSize.width) {
            x = screenSize.width - width;
        }
        if (y + height + BOTTOM_MARGIN > screenSize.height) {
            y = screenSize.height - height - BOTTOM_MARGIN;
        }
        
        initializeComponents(x, y, width, height);
    }

    public DialogWindow(JFrame baseFrame, String title, int width, int height) {
        super(baseFrame, title, true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        initializeComponents(calculateCenterPosition(screenSize.width, width), calculateCenterPosition(screenSize.height, height), width, height);
    }

    /**
     * Fullscreen window with margin 25,25,25,25
     * @param frame  - parent frame
     * @param title  - window title
     */
    public DialogWindow(JFrame frame, String title) {
        super(frame, title, true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int width = screenSize.width - 50;
        int height = screenSize.height - 80;
        
        initializeComponents( calculateCenterPosition(screenSize.width, width), calculateCenterPosition(screenSize.height, height), width, height);
    }
    
    private void initializeComponents(int x, int y, int width, int height){
        this.setBounds(x, y, width, height);
        this.setLayout(new RiverLayout());
    }
    
    private int calculateCenterPosition(int a, int b){
        return (a - b) / 2;
    }
}
