package pl.edu.pwr.wordnetloom.client.plugins.administrator.dictionaryEditor;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComponentGroup;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import se.datadosen.component.RiverLayout;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static se.datadosen.component.RiverLayout.*;

public class DictionaryListPanel extends JPanel {

    private final String DICTIONARY_MODEL_PACKET = "pl.edu.pwr.wordnetloom.dictionary.model.";

    public interface DictionaryListListener{
        void onEdit(Dictionary dictionary);
    }

    private JComboBox dictionaryType;
    private JComboBox languageCombo;
    private JTable dictionaryTable;
    private DictionaryTableModel tableModel;
    private JTextComponent searchTextField;

    DictionaryListListener listener;

    public DictionaryListPanel(int width, DictionaryListListener listener) {
        this.listener = listener;
        initView(width);
    }

    private void initView(int width) {
        setLayout(new RiverLayout());

        JPanel comboPanel = new JPanel(new GridLayout(1,2));
        languageCombo = createLanguageComponent();
        dictionaryType = createDictionaryTypeComponent();
        comboPanel.add(dictionaryType);
        comboPanel.add(languageCombo);
        dictionaryTable = createDictionaryTable();
        searchTextField = createSearchField();
        JPanel buttonsPanel = createButtonsPanel();

        add(HFILL,comboPanel);
        add(LINE_BREAK + " " + HFILL, searchTextField);
        add(LINE_BREAK + " " + HFILL + " " + VFILL, dictionaryTable);
        add(LINE_BREAK + " " + HFILL, buttonsPanel);

        setPreferredSize(new Dimension(width, getHeight()));
    }

    private JComboBox createLanguageComponent() {
        JComboBox comboBox = new JComboBox();
        // TODO utworzyć słuchacza
        comboBox.addActionListener(new LoadListener());
        for(Language language : Language.values()){
            comboBox.addItem(language.getAbbreviation());
        }
        return comboBox;
    }

    private JComboBox createDictionaryTypeComponent(){
        JComboBox comboBox = new JComboBox();
        comboBox.addActionListener(new LoadListener());
        List<String> dictionaryNames = RemoteService.dictionaryServiceRemote.findAllDictionaryNames();
        for(String s : dictionaryNames){
            comboBox.addItem(s);
        }
        return comboBox;
    }

    private JTextComponent createSearchField(){
        JTextComponent textField = new JTextField();
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(searchTextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(searchTextField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(searchTextField.getText());
            }

            private void search(String text){
                dictionaryTable.setRowSorter(tableModel.getSorter(text));
            }
        });
        return textField;
    }

    private JTable createDictionaryTable(){
        JTable table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int row = table.rowAtPoint(e.getPoint());
                if(e.getClickCount() == 2 && table.getSelectedRow() != -1){
                    editDictionary(row);
                }
            }
        });
        return table;
    }

    private JPanel createButtonsPanel() {
        JButton addButton = MButton.buildAddButton()
                .withToolTip(Labels.ADD)
                .withActionListener(e -> {
                    try {
                        addDictionary();
                    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e1) {
                        e1.printStackTrace();
                    }
                });
        JButton editButton = MButton.buildEditButton()
                .withToolTip(Labels.EDIT)
                .withActionListener(e -> editDictionary());
        JButton deleteButton = MButton.buildDeleteButton()
                .withToolTip(Labels.DELETE)
                .withActionListener(e -> removeDictionary());

        MComponentGroup panel = new MComponentGroup(addButton, editButton, deleteButton);
        panel.withHorizontalLayout();
        return panel;
    }

    public void loadDictionary() throws ClassNotFoundException {
        // TODO odczytanie rodzaju słownika
        if(dictionaryType != null && languageCombo != null){
            List<? extends Dictionary> dictionaries = RemoteService.dictionaryServiceRemote.findDictionaryByClass((String) dictionaryType.getSelectedItem());
            List<String> columns = Arrays.asList("Nazwa", "Słownik");
            String language = (String) languageCombo.getSelectedItem();
            tableModel = new DictionaryTableModel(dictionaries, columns, dictionaryTable, language);
            dictionaryTable.setModel(tableModel);
        }
    }

    private void addDictionary() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Dictionary dictionary = createNewDictionary((String) dictionaryType.getSelectedItem());
        listener.onEdit(dictionary);
    }

    private Dictionary createNewDictionary(String type) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return (Dictionary) Class.forName(DICTIONARY_MODEL_PACKET + type).newInstance();
    }

    private void editDictionary(int row){
        Dictionary dictionary = tableModel.getElementAt(row);
        listener.onEdit(dictionary);
    }

    private void editDictionary(){
        int selectedIndex = dictionaryTable.getSelectedRow();
        editDictionary(selectedIndex);
    }

    private void removeDictionary(){
        int selectedRow = dictionaryTable.getSelectedRow();
        Dictionary dictionary = tableModel.getElementAt(selectedRow);
        String dictionaryName = LocalisationManager.getInstance().getLocalisedString(dictionary.getName());
        // TODO dorobić etykietę
        int answer = DialogBox.showYesNo("Czy na pewno usunać słownik " + dictionaryName);
        if(answer == DialogBox.YES){
            tableModel.removeElement(selectedRow);
            removeDictionaryFromDatabase(dictionary);
            removeDictionaryFromLocalisationManager(dictionary);
            listener.onEdit(null);
        }
    }

    private void removeDictionaryFromDatabase(Dictionary dictionary){
        RemoteService.dictionaryServiceRemote.remove(dictionary);
    }

    private void removeDictionaryFromLocalisationManager(Dictionary dictionary) {
        LocalisationManager.getInstance().removeLocalisationString(dictionary.getName());
    }

    private class LoadListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                loadDictionary();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    private class DictionaryTableModel extends AbstractTableModel{

        private final int COLUMN_COUNT = 2;
        private final int NAME_COLUMN = 0;
        private final int DESCRIPTION_COLUMN = 1;

        private List<String> columns;
        private List<? extends Dictionary> items;
        private JTable table;
        private String language;

        private DictionaryTableModel(List<? extends Dictionary> items, List<String> columns, JTable table, String language) {
            this.items = items;
            this.columns = columns;
            this.table = table;
            this.language = language;
        }

        @Override
        public String getColumnName(int columnIndex){
            return columns.get(columnIndex);
        }

        @Override
        public int getRowCount() {
            return items.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex){
                case NAME_COLUMN:
                    Long nameID = items.get(rowIndex).getName();
                    return getLocalisedString(nameID);
                case DESCRIPTION_COLUMN:
                    Long descriptionID = items.get(rowIndex).getDescription();
                    return getLocalisedString(descriptionID);
                default:
                    throw new IllegalArgumentException();
            }
        }

        public void removeElement(int row) {
            items.remove(row);
            this.fireTableRowsDeleted(row, row);
        }

        private String getLocalisedString(Long id) {
//            return LocalisationManager.getInstance().getLocalisedString(id);
            if(id == null){
                return "";
            }
            LocalisedKey key = new LocalisedKey(id, language);
            return RemoteService.localisedStringServiceRemote.findStringsByKey(key).getValue();
        }

        Dictionary getElementAt(int rowIndex){
            int index = table.convertRowIndexToModel(rowIndex);
            return items.get(index);
        }

        TableRowSorter<TableModel> getSorter(String text){
            RowFilter<Object, Object> rowFilter = new RowFilter<Object, Object>() {
                @Override
                public boolean include(Entry<?, ?> entry) {
                    return entry.getStringValue(NAME_COLUMN).toLowerCase().contains(text.toLowerCase());
                }
            };
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(this);
            sorter.setRowFilter(rowFilter);
            return sorter;
        }

        // TODO dorobić metodę dodającą element i ustawiającą element
    }
}
