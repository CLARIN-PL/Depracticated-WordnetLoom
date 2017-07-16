package pl.edu.pwr.wordnetloom.client.systems.ui;

import java.awt.Font;
import javax.swing.JLabel;

/**
 * label bez pogrubienia
 *
 * @author Max
 *
 */
public class LabelPlain extends JLabel {

    private static final long serialVersionUID = 1L;

    /**
     * konstruktor
     *
     * @param caption - edytkieta do wyswietlenia
     */
    public LabelPlain(String caption) {
        super(caption);
        Font newFont = this.getFont().deriveFont(Font.PLAIN);
        this.setFont(newFont);
    }
}
