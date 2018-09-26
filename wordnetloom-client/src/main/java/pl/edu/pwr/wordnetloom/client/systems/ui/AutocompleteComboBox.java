package pl.edu.pwr.wordnetloom.client.systems.ui;


import org.jdesktop.swingx.autocomplete.AutoCompleteComboBoxEditor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.util.*;
import java.util.List;

public class AutocompleteComboBox<T> extends JComboBox{

    private Searchable searchable;
    private DefaultComboBoxModel<T> model;

    public AutocompleteComboBox(@NotNull Searchable searchable) {
        super();
        this.searchable = searchable;
        model = new DefaultComboBoxModel<>();
        setModel(model);
        setEditable(true);
        Component component = getEditor().getEditorComponent();
        if (component instanceof JTextComponent) {
            final JTextComponent textComponent = (JTextComponent)component;
            textComponent.getDocument().addDocumentListener(new DocumentListener() {
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

                private ItemListener[] listeners;
                private ActionListener[] actionListeners;

                private void update() {
                    SwingUtilities.invokeLater(() -> {
                        String text = textComponent.getText();
                        List<T> founds = new ArrayList<T>(searchable.search(text));
                        removeListeners();
                        model.removeAllElements();
                        for(T item : founds){
                            model.addElement(item);
                        }

//                        setSelectedIndex(-1);
//                        textComponent.setText(text);
//                        setPopupVisible(true);
                        textComponent.setCaretPosition(text.length());
//                        addListeners();
                    });
                }

                private void removeListeners() {
                    listeners = AutocompleteComboBox.this.getItemListeners();
                    actionListeners = AutocompleteComboBox.this.getActionListeners();
                    for (ItemListener listener : listeners){
                        AutocompleteComboBox.this.removeItemListener(listener);
                    }

                    for (ActionListener listener : actionListeners){
                        AutocompleteComboBox.this.removeActionListener(listener);
                    }
                }

                private void addListeners() {
                    for (ItemListener listener : listeners){
                        AutocompleteComboBox.this.addItemListener(listener);
                    }

                    for(ActionListener listener : actionListeners){
                        AutocompleteComboBox.this.addActionListener(listener);
                    }
                }
            });

            textComponent.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (!textComponent.getText().isEmpty()) {
                        setPopupVisible(true);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    setPopupVisible(false);
                }
            });
        }
    }
}


