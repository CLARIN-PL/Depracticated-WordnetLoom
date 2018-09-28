package pl.edu.pwr.wordnetloom.client.systems.ui.autocompleteField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;

// TODO fix size of panel
public class AutocompleteTextField<T> extends JPanel {

    private JTextField textField;
    private JComboBox comboBox;

    private Collection<T> items;
    private SuggestionFilter filter;

    public AutocompleteTextField(Collection<T> items, SuggestionFilter filter){
        this.items = items;
        this.filter = filter;
        initComponents();
        initUI();
    }

    private void initComponents(){
        textField = new JTextField();
        comboBox = new JComboBox();

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                SwingUtilities.invokeLater(() -> {
                    filterComboBox(textField.getText());
                    comboBox.setPopupVisible(textField.getText().length() != 0 && comboBox.getModel().getSize()!=0);
                });

            }

            private void filterComboBox(String text){
                Collection<String> list = filter.filter(items, text);
                comboBox.removeAllItems();
                for (String item: list) {
//                    comboBox.addItem(item);
                    comboBox.insertItemAt(item, 0);
                }
                comboBox.setSelectedIndex(-1);
            }
        });

        comboBox.addActionListener(e -> {
            if (comboBox.getSelectedItem() != null){
                String selectedItem = (String) comboBox.getSelectedItem();
                textField.setText(selectedItem);
                comboBox.setPopupVisible(false);
            }
        });

        Dimension size = new Dimension(400, 30);
        Dimension size2 = getPreferredSize();
        Dimension size3 = getSize();
        Dimension size4 = getMinimumSize();
        Dimension size5 = getMaximumSize();
        textField.setPreferredSize(size);
        comboBox.setPreferredSize(size);
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;

        add(textField, constraints);
        add(comboBox, constraints);

        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Dimension size = getPreferredSize();
                textField.setPreferredSize(size);
                comboBox.setPreferredSize(size);
            }
        });
    }


}
