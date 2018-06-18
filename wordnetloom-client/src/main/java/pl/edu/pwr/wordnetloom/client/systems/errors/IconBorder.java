package pl.edu.pwr.wordnetloom.client.systems.errors;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public class IconBorder extends AbstractBorder{
    private ImageIcon icon;
    private Border originalBorder;

    public IconBorder(ImageIcon icon, Border orginalBorder){
        this.icon = icon;
        this.originalBorder = orginalBorder;
    }


    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height){
//        Insets insets = getBorderInsets(c);
        Insets insets = new Insets(2,2,2,2);
        g.setColor(Color.RED);
        g.fillRect(0, 0, width, insets.top);
        g.fillRect(0, insets.top, insets.left, height - insets.top);
        g.fillRect(insets.left, height - insets.bottom, width - insets.left, insets.bottom);
        g.fillRect(width- insets.right, 0, insets.right, height-insets.bottom);
//        originalBorder.paintBorder(c, g, x, y, width, height);

        int by = (c.getHeight() / 2) - (icon.getIconHeight() / 2);
        int w = Math.max(2, insets.left);
        int bx = x + width - (icon.getIconHeight() + (w * 2)) + 2;
        g.translate(bx, by);
        g.drawImage(icon.getImage(), x, y, icon.getImageObserver());
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return originalBorder.getBorderInsets(c);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}
