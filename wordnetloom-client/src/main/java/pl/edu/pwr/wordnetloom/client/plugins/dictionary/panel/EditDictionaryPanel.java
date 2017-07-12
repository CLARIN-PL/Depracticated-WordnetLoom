package pl.edu.pwr.wordnetloom.client.plugins.dictionary.panel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import pl.edu.pwr.wordnetloom.client.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.model.wordnet.Dictionary;
import pl.edu.pwr.wordnetloom.model.wordnet.LanguageVariantDictionary;
import pl.edu.pwr.wordnetloom.model.wordnet.StatusDictionary;

public class EditDictionaryPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JTextField valueTextField;
    private JTextField desctextField;
    private JList list;
    private DefaultListModel listModel;
    private JButton btnNewButton;
    private JButton btnDelete;
    private JComboBox dictionaries;

    public EditDictionaryPanel() {
        setLayout(new FormLayout(
                new ColumnSpec[]{ColumnSpec.decode("max(7dlu;default)"), FormSpecs.RELATED_GAP_COLSPEC,
                    ColumnSpec.decode("160px"), FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("145px:grow"),
                    FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("86px"), FormSpecs.RELATED_GAP_COLSPEC,
                    ColumnSpec.decode("113px"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
                    FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,},
                new RowSpec[]{FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("51px"), FormSpecs.RELATED_GAP_ROWSPEC,
                    FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
                    FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
                    FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,}));

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Dictionary Type", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(panel, "3, 2, 7, 1, fill, fill");
        panel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("320px"),},
                new RowSpec[]{RowSpec.decode("24px"),}));

        dictionaries = new JComboBox();
        for (String name : RemoteUtils.dictionaryRemote.findAllDictionaryNames()) {
            dictionaries.addItem(name);
        }
        panel.add(dictionaries, "1, 1, fill, center");
        dictionaries.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (dictionaries.getSelectedItem().toString()) {
                    case "StatusDictionary":
                        listModel.removeAllElements();
                        for (StatusDictionary d : RemoteUtils.dictionaryRemote.findAllStatusDictionary()) {
                            listModel.addElement(d);
                        }
                        break;
                    case "LanguageVariantDictionary":
                        listModel.removeAllElements();
                        for (LanguageVariantDictionary d : RemoteUtils.dictionaryRemote.findAllLanguageVariantDictionary()) {
                            listModel.addElement(d);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportBorder(
                new TitledBorder(null, "Values", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(scrollPane, "3, 4, 7, 1, fill, fill");

        listModel = new DefaultListModel();

        list = new JList(listModel);
        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                Dictionary dictionary = (Dictionary) listModel.getElementAt(list.getSelectedIndex());
                valueTextField.setText(dictionary.getName());
                desctextField.setText(dictionary.getDescription());
            }
        });

        scrollPane.setViewportView(list);

        JLabel lblNewLabel = new JLabel("Value:");
        add(lblNewLabel, "3, 6");

        JLabel lblNewLabel_1 = new JLabel("Description:");
        add(lblNewLabel_1, "5, 6");

        valueTextField = new JTextField();
        add(valueTextField, "3, 8, fill, fill");
        valueTextField.setColumns(10);

        desctextField = new JTextField();
        add(desctextField, "5, 8, fill, fill");
        desctextField.setColumns(10);

        btnNewButton = new JButton("Add");
        btnNewButton.addActionListener(this);
        add(btnNewButton, "7, 8");

        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(this);
        add(btnDelete, "9, 8");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btnNewButton) {
            switch (dictionaries.getSelectedItem().toString()) {

                case "StatusDictionary":
                    if (valueTextField.getText() != null && !"".equals(valueTextField.getText())) {
                        StatusDictionary item = new StatusDictionary(valueTextField.getText(),
                                desctextField.getText());
                        RemoteUtils.dictionaryRemote.saveOrUpdate(item);
                        listModel.addElement(item);
                        clear();
                    }
                    break;
                case "LanguageVariantDictionary":
                    if (valueTextField.getText() != null && !"".equals(valueTextField.getText())) {
                        LanguageVariantDictionary item = new LanguageVariantDictionary(valueTextField.getText(),
                                desctextField.getText());
                        RemoteUtils.dictionaryRemote.saveOrUpdate(item);
                        listModel.addElement(item);
                        clear();
                    }
                    break;
                default:
                    break;
            }
        } else if (event.getSource() == btnDelete) {
            Dictionary d = (Dictionary) listModel.getElementAt(list.getSelectedIndex());
            if (null != d) {
                listModel.removeElement(d);
                RemoteUtils.dictionaryRemote.remove(d);
            }
        }
    }

    private void clear() {
        valueTextField.setText("");
        desctextField.setText("");
    }

}
