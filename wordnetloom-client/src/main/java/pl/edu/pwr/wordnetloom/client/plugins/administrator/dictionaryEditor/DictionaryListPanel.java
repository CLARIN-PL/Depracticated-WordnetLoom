package pl.edu.pwr.wordnetloom.client.plugins.administrator.dictionaryEditor;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComponentGroup;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.dictionary.model.Register;
import se.datadosen.component.RiverLayout;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

public class DictionaryListPanel extends JPanel {

    public interface DictionaryListListener{
        void onEdit(Dictionary dictionary);
    }

    private JComboBox dictionaryType;
    private JTable dictionaryTable;
    private DictionaryTableModel tableModel;
    private JTextComponent searchTextField;

    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    DictionaryListListener listener;

    public DictionaryListPanel(DictionaryListListener listener) {
        this.listener = listener;
        initView();
    }

    private void initView() {
        // TODO przyciski
        setLayout(new RiverLayout());

        dictionaryType = createDictionaryTypeComponent();
        dictionaryTable = createDictionaryTable();
        searchTextField = createSearchField();
        JPanel buttonsPanel = createButtonsPanel();

        add(RiverLayout.HFILL,dictionaryType);
        add(RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL, searchTextField);
        add(RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL + " " + RiverLayout.VFILL, dictionaryTable);
        add(RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL, buttonsPanel);
    }

    private JComboBox createDictionaryTypeComponent(){
        JComboBox comboBox = new JComboBox();
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(dictionaryType != null){
                        loadDictionary();
                    }
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
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
                    editDictionary(row); // TODO tutaj może wstawić numer wiersza
                }
            }
        });
        return table;
    }

    private JPanel createButtonsPanel() {
        addButton = MButton.buildAddButton()
                .withToolTip(Labels.ADD)
                .withActionListener(e->addDictionary());
        editButton = MButton.buildEditButton()
                .withToolTip(Labels.EDIT)
                .withActionListener(e->editDictionary());
        deleteButton = MButton.buildDeleteButton()
                .withToolTip(Labels.DELETE)
                .withActionListener(e->removeDictionary());

        MComponentGroup panel = new MComponentGroup(addButton, editButton, deleteButton);
        panel.withHorizontalLayout();
        return panel;
    }

    public void loadDictionary() throws ClassNotFoundException {
        // TODO odczytanie rodzaju słownika
        List<? extends Dictionary> dictionaries = RemoteService.dictionaryServiceRemote.findDictionaryByClass((String) dictionaryType.getSelectedItem());
        List<String> columns = Arrays.asList("Nazwa", "Słownik");
        tableModel = new DictionaryTableModel(dictionaries, columns, dictionaryTable);
        dictionaryTable.setModel(tableModel);
    }

    private void addDictionary(){
        throw new NotImplementedException();
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
        throw new NotImplementedException();
    }

    private class DictionaryTableModel extends AbstractTableModel{

        private final int COLUMN_COUNT = 2;
        private final int NAME_COLUMN = 0;
        private final int DESCRIPTION_COLUMN = 1;

        private List<String> columns;
        private List<? extends Dictionary> items;
        private String filter;
        private JTable table;

        private DictionaryTableModel(List<? extends Dictionary> items, List<String> columns, JTable table) {
            this.items = items;
            this.columns = columns;
            this.table = table;
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
                    return LocalisationManager.getInstance().getLocalisedString(nameID);
                case DESCRIPTION_COLUMN:
                    Long descriptionID = items.get(rowIndex).getDescription();
                    return LocalisationManager.getInstance().getLocalisedString(descriptionID);
                default:
                    throw new IllegalArgumentException();
            }
        }

        Dictionary getElementAt(int rowIndex){
            int index = table.convertRowIndexToModel(rowIndex);
            return items.get(index);
        }

        TableRowSorter<TableModel> getSorter(String text){
            filter = text;
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
