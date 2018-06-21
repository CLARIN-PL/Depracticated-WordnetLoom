package pl.edu.pwr.wordnetloom.client.systems.errors;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

public class ColorBorder extends AbstractBorder {

    private final Insets BORDER_INSETS = new Insets(2,2,2,2);

    private Color color;

    public ColorBorder(Color color){
        this.color = color;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height){
        g.setColor(color);
        // TODO opisać to
        g.fillRect(0, 0, width, BORDER_INSETS.top);
        g.fillRect(0, BORDER_INSETS.top, BORDER_INSETS.left, height - BORDER_INSETS.top);
        g.fillRect(BORDER_INSETS.left, height - BORDER_INSETS.bottom, width - BORDER_INSETS.left, BORDER_INSETS.bottom);
        g.fillRect(width- BORDER_INSETS.right, 0, BORDER_INSETS.right, height-BORDER_INSETS.bottom);
    }


    @Override
    public Insets getBorderInsets(Component c){
        return BORDER_INSETS;
    }

    @Override
    public boolean isBorderOpaque(){
        return true;
    }
}
