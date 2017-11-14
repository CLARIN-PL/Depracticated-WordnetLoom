package pl.edu.pwr.wordnetloom.client.systems.ui;

import com.alee.laf.rootpane.WebFrame;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

public class MFrame extends WebFrame implements Serializable {

    private static final String FILE_MAIN_ICON = "icons/wordnet.gif";
    static private int BOTTOM_MARGIN = 40;

    WebFrame baseFrame;

    /**
     * konstruktor
     */
    public MFrame() {
        setIconImage(new ImageIcon(MFrame.class.getClassLoader().getResource(FILE_MAIN_ICON)).getImage());
    }

    /**
     * konstruktor
     *
     * @param baseFrame
     * @param title     - tytul okna
     * @param width     - szerokosc okna
     * @param height    - wysokosc okna
     */
    public MFrame(WebFrame baseFrame, String title, int width, int height) {
        this(title, width, height);
        this.baseFrame = baseFrame;
    }

    /**
     * konstruktor
     *
     * @param title  - tytul okna
     * @param x      - polozenie x
     * @param y      - polozenie y
     * @param width  - szerokosc okna
     * @param height - wysokosc okna
     */
    public MFrame(String title, int x, int y, int width, int height) {
        setIconImage(new ImageIcon(MFrame.class.getClassLoader().getResource(FILE_MAIN_ICON)).getImage());
        // odczytanie rozmiarow ekranu

        Dimension screenSize = new Dimension(
                (int) getGraphicsConfiguration().getBounds().getWidth(),
                (int) getGraphicsConfiguration().getBounds().getHeight()
        );

        // koreka polożenia
        int offsetX = getGraphicsConfiguration().getBounds().x;
        int offsetY = getGraphicsConfiguration().getBounds().y;

        // koreka polożenia
        if (x + width > screenSize.width) {
            x = offsetX + screenSize.width - width;
        }
        if (y + height + BOTTOM_MARGIN > screenSize.height) {
            y = offsetY + screenSize.height - height - BOTTOM_MARGIN;
        }

        // ustawienie parametrów okna
        setBounds(x, y, width, height);
        setLayout(new RiverLayout());
        setTitle(title);
    }

    /**
     * konstruktor
     *
     * @param width  - szerokosc okna
     * @param height - wysokosc okna
     */
    public MFrame(int width, int height) {
        setIconImage(new ImageIcon(FILE_MAIN_ICON).getImage());
        // odczytanie rozmiarow ekranu
        Dimension screenSize = new Dimension(
                (int) getGraphicsConfiguration().getBounds().getWidth(),
                (int) getGraphicsConfiguration().getBounds().getHeight()
        );

        // koreka polożenia
        int x = getGraphicsConfiguration().getBounds().x + (screenSize.width - width) / 2;
        int y = (screenSize.height - height - BOTTOM_MARGIN) / 2;

        // ustawienie parametrów okna
        setBounds(x, y, width, height);
        setLayout(new RiverLayout());
    }

    /**
     * konstruktor
     *
     * @param title  - tytul okna
     * @param width  - szerokosc okna
     * @param height - wysokosc okna
     */
    public MFrame(String title, int width, int height) {
        this(width, height);
        setTitle(title);
    }

    /**
     * konstruktor - pelny ekran z marginesem 25,25,25,25
     *
     * @param baseFrame - okno bazowe
     * @param title     - tytul okna
     */
    public MFrame(WebFrame baseFrame, String title) {
        this.baseFrame = baseFrame;

        // odczytanie rozmiarow ekranu
        Dimension screenSize = new Dimension(
                (int) getGraphicsConfiguration().getBounds().getWidth(),
                (int) getGraphicsConfiguration().getBounds().getHeight()
        );

        // koreka polożenia
        int width = screenSize.width - 50;
        int height = screenSize.height - 80;
        int x = getGraphicsConfiguration().getBounds().x + (screenSize.width - width) / 2;
        int y = (screenSize.height - height - BOTTOM_MARGIN) / 2;

        // ustawienie parametrów okna
        setBounds(x, y, width, height);
        setLayout(new RiverLayout());
        setTitle(title);
    }


    @Override
    public void setLocation(int x, int y) {
        Dimension screenSize = new Dimension(
                (int) getGraphicsConfiguration().getBounds().getWidth(),
                (int) getGraphicsConfiguration().getBounds().getHeight()
        );

        // koreka polożenia
        int offsetX = getGraphicsConfiguration().getBounds().x;
        int offsetY = getGraphicsConfiguration().getBounds().y;

        // koreka polożenia
        if (x + getWidth() > screenSize.width) {
            x = offsetX + screenSize.width - getWidth();
        }
        if (y + getHeight() + BOTTOM_MARGIN > screenSize.height) {
            y = offsetY + screenSize.height - getHeight() - BOTTOM_MARGIN;
        }

        // ustawienie parametrów okna
        super.setLocation(x, y);
    }

    /**
     * Wyswietlenie okienka jako modalne
     */
    public void showModal() {
        final WebFrame frame = this;

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                baseFrame.setEnabled(false);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                baseFrame.setEnabled(true);
                frame.removeWindowListener(this);
                baseFrame.toFront();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                if (frame.isShowing()) {
                    frame.toFront();
                } else {
                    baseFrame.removeWindowListener(this);
                }
            }
        });

        frame.setVisible(true);
    }

}
