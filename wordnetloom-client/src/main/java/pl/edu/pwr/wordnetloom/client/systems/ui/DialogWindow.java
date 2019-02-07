package pl.edu.pwr.wordnetloom.client.systems.ui;

import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.rootpane.WebFrame;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class DialogWindow extends WebDialog {

    private static final long serialVersionUID = 1L;
    static private int BOTTOM_MARGIN = 40;

    public DialogWindow(WebFrame baseFrame, String title, int x, int y, int width, int height) {
        super(baseFrame, title, true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        if (x + width > screenSize.width) {
            x = screenSize.width - width;
        }
        if (y + height + BOTTOM_MARGIN > screenSize.height) {
            y = screenSize.height - height - BOTTOM_MARGIN;
        }

        initializeComponents(x, y, width, height);
        initDefaultCloseOnEscape();
    }

    public DialogWindow(WebFrame baseFrame, String title, int width, int height) {
        super(baseFrame, title, true);
        setLocationRelativeTo(baseFrame);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        initializeComponents(calculateCenterPosition(screenSize.width, width), calculateCenterPosition(screenSize.height, height), width, height);
        initDefaultCloseOnEscape();
    }

    private void initDefaultCloseOnEscape(){
        this.getRootPane().registerKeyboardAction(e -> {
           this.dispose();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * Fullscreen window with margin 25,25,25,25
     *
     * @param frame - parent frame
     * @param title - window title
     */
    public DialogWindow(WebFrame frame, String title) {
        super(frame, title, true);
        setLocationRelativeTo(frame);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int width = screenSize.width - 50;
        int height = screenSize.height - 80;

        initializeComponents(calculateCenterPosition(screenSize.width, width), calculateCenterPosition(screenSize.height, height), width, height);
        initDefaultCloseOnEscape();
    }

    private void initializeComponents(int x, int y, int width, int height) {
        setBounds(x, y, width, height);
        setLayout(new RiverLayout());
    }

    private int calculateCenterPosition(int a, int b) {
        return (a - b) / 2;
    }

    public void setInScreenCenter(int width, int height) {

        Dimension screenSize = new Dimension(
                (int) getGraphicsConfiguration().getBounds().getWidth(),
                (int) getGraphicsConfiguration().getBounds().getHeight()
        );

        // set to center of main screen
        int x = getGraphicsConfiguration().getBounds().x + (screenSize.width - width) / 2;
        int y = (screenSize.height - height - BOTTOM_MARGIN) / 2;
        setBounds(x, y, width, height);
    }
}
