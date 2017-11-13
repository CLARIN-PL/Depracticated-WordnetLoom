package pl.edu.pwr.wordnetloom.client.systems.ui;

import javax.swing.*;
import java.awt.*;

public class LabelPlain extends JLabel {

    private static final long serialVersionUID = 1L;

    public LabelPlain(String caption) {
        super(caption);
        Font newFont = getFont().deriveFont(Font.PLAIN);
        setFont(newFont);
    }
}
