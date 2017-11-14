package pl.edu.pwr.wordnetloom.client.systems.ui;

import com.alee.laf.panel.WebPanel;
import pl.edu.pwr.wordnetloom.client.systems.misc.NodeDrawer;

import java.awt.*;

public class TreePanel extends WebPanel {

    private static final long serialVersionUID = 1L;
    private NodeDrawer parent = null;
    private boolean sizeSet = false;
    private boolean down = true;
    private Object focusTag = null;

    /**
     * konstruktor
     *
     * @param down - jest true to pionowe drzewo, jesli false to poziome
     */
    public TreePanel(boolean down) {
        setBackground(Color.WHITE);
        this.down = down;
    }

    /**
     * obiekt podeswietlany
     *
     * @param object - obiekt
     */
    public void setFocusTag(Object object) {
        focusTag = object;
    }

    /**
     * ustawienie kierunku drzewa
     *
     * @param down - jest true to pionowe drzewo, jesli false to poziome
     */
    public void setDirection(boolean down) {
        this.down = down;
        sizeSet = false;
        repaint();
    }

    /**
     * ustawienie danych do narysowania
     *
     * @param parent - ojciec
     */
    public void setNode(NodeDrawer parent) {
        this.parent = parent;
        sizeSet = false;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (parent != null) {
            g.setFont(g.getFont().deriveFont(9f));
            if (down) {
                parent.trialDrawY(g);
                parent.drawY(g, 10, 10, focusTag);
            } else {
                parent.trialDrawX(g);
                parent.drawX(g, 10, 10, focusTag);
            }

            if (!sizeSet) {
                sizeSet = true;
                Dimension size = new Dimension(parent.getBound().width + 20, parent.getBound().height + 20);
                setPreferredSize(size);
                setSize(size);
            }
        }
    }
}
