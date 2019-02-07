package pl.edu.pwr.wordnetloom.client.systems.renderers;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextArea;
import pl.edu.pwr.wordnetloom.sense.model.SenseExample;
import pl.edu.pwr.wordnetloom.synset.model.SynsetExample;

import javax.swing.*;
import java.awt.*;

public class ExampleCellRenderer implements ListCellRenderer {

    private final WebPanel panel;
    private final WebTextArea textArea;

    public ExampleCellRenderer() {
        panel = new WebPanel();
        panel.setLayout(new BorderLayout());

        textArea = new WebTextArea();

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        panel.add(textArea, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(final JList list,
                                                  final Object value, final int index, final boolean isSelected,
                                                  final boolean hasFocus) {
        if(value == null)
        {
            return null;
        }
        if(( value instanceof SenseExample)) {
            textArea.setText(((SenseExample) value).getExample());
        }
        if(( value instanceof SynsetExample)) {
            textArea.setText(((SynsetExample) value).getExample());
        }
        if (isSelected) {
            textArea.setBackground(new Color(135, 206, 250));
        } else if (index % 2 == 0) {
            textArea.setBackground(new Color(230, 230, 230));
        } else {
            textArea.setBackground(Color.WHITE);
        }
        int width = list.getWidth();
        if (width > 0) {
            textArea.setSize(width, Short.MAX_VALUE);
        }
        return panel;

    }
}