package pl.edu.pwr.wordnetloom.client.systems.ui;

import pl.edu.pwr.wordnetloom.sense.model.SenseExample;

import javax.swing.*;
import java.awt.*;

public class ExampleCellRenderer implements ListCellRenderer {

        private final JPanel panel;
        private final JTextArea textArea;

        public ExampleCellRenderer() {
            panel = new JPanel();
            panel.setLayout(new BorderLayout());

            textArea = new JTextArea();

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
            textArea.setText(((SenseExample) value).getExample());
            if (isSelected) {
                textArea.setBackground(new Color(135, 206, 250));
            } else if (index % 2 == 0) {
                textArea.setBackground(Color.LIGHT_GRAY);
            } else {
                textArea.setBackground(Color.gray);
            }
            int width = list.getWidth();
            if (width > 0) {
                textArea.setSize(width, Short.MAX_VALUE);
            }
            return panel;

        }
    }