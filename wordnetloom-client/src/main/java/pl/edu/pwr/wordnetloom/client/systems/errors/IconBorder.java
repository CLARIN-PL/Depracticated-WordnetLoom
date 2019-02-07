package pl.edu.pwr.wordnetloom.client.systems.errors;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

/** Color border with icon. Icon is show on right side of component. */
public class IconBorder extends AbstractBorder{

    private Color borderColor;
    private ImageIcon icon;

    /** Thicknes of border line */
    private final Insets BORDER_INSETS = new Insets(1,1,1,1);

    private final int ICON_WIDTH = 15;
    private final int ICON_HEIGHT = 15;

    public IconBorder(Color borderColor, ImageIcon icon){
        this.borderColor = borderColor;
        this.icon = scaleIcon(icon.getImage(), ICON_WIDTH, ICON_HEIGHT);
    }

    public IconBorder(Color borderColor, Image image){
        this.borderColor = borderColor;
        this.icon = scaleIcon(image, ICON_WIDTH, ICON_HEIGHT);
    }

    private ImageIcon scaleIcon(Image image, int width, int height){
        return new ImageIcon(image.getScaledInstance(width, height,0));
    }

    public void setIconSize(int width, int height){
        icon = scaleIcon(icon.getImage(), width, height);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height){
        drawBorder(g, width, height, BORDER_INSETS);
        drawImage(c, g, x, y, width, BORDER_INSETS);
    }

    private void drawImage(Component c, Graphics g, int x, int y, int width, Insets insets) {
        Graphics2D g2 = (Graphics2D) g;
        int by = (c.getHeight() / 2) - (icon.getIconHeight() / 2);
        int w = Math.max(2, insets.left);
        int bx = x + width - (icon.getIconHeight() + (w*2)) + 2;
        g2.translate(bx, by);
        // x- 5 set right padding 5 px
        g2.drawImage(icon.getImage(), x-5, y, null);
    }

    private void drawBorder(Graphics g, int width, int height, Insets insets) {
        g.setColor(borderColor);
        // draw top line
        g.fillRect(0, 0, width, insets.top);
        // draw left line
        g.fillRect(0, insets.top, insets.left, height - insets.top);
        // draw bottom line
        g.fillRect(insets.left, height - insets.bottom, width - insets.left, insets.bottom);
        // draw right line
        g.fillRect(width- insets.right, 0, insets.right, height-insets.bottom);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        // right restrict textfield area from right side
        return new Insets(2,5,2,icon.getIconWidth() + 10);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}
