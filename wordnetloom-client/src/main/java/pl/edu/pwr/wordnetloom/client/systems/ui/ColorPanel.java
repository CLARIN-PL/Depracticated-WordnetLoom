package pl.edu.pwr.wordnetloom.client.systems.ui;

import pl.edu.pwr.wordnetloom.client.utils.Labels;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class ColorPanel extends JPanel {
    final int PREVIEW_SIZE = 20;

    private JTextField colorField;
    private JButton colorButton;
    private JPanel colorPreview;
    private Color currentColor;

    public ColorPanel() {
        initComponents();
        initView();
    }

    private void initComponents() {
        colorField = new JTextField();
        colorField.setEditable(false);
        colorField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                trySetColor(colorField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                trySetColor(colorField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                trySetColor(colorField.getText());
            }
        });

        colorPreview = new JPanel();
        colorPreview.setPreferredSize(new Dimension(PREVIEW_SIZE, PREVIEW_SIZE));

        colorButton = new MButton();
        colorButton.addActionListener(e-> {
            Color col = showColorPicker();
            if(col != null){
                setColor(col);
            }
        });
    }

    private void trySetColor(String text){
        if(text.isEmpty()){
            return;
        }
        Color color = Color.decode(text);
        if(color != null){
            setColor(color);
        }
    }

    public void setEditable(boolean editable) {
        colorField.setEditable(editable);
    }

    private void initView() {
        setLayout(new BorderLayout());

        add(colorPreview, BorderLayout.WEST);
        add(colorField, BorderLayout.CENTER);
        add(colorButton, BorderLayout.EAST);
    }

    public JTextField getColorField() {
        return colorField;
    }

    public Color getColor() {
        return currentColor;
    }

    private Color showColorPicker() {
        return JColorChooser.showDialog(null, Labels.COLOR_COLON, currentColor);
    }

    private void setColor(String color) {
        setColor(Color.decode(color));
    }

    private void setColor(Color color){
        currentColor = color;
        colorPreview.setBackground(color);
        String colorText = getHex(color);
        if(!colorField.getText().equals(colorText)) {
            colorField.setText(colorText);
        }
    }

    private String getHex(Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2);
    }
}