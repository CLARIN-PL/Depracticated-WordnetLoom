package pl.edu.pwr.wordnetloom.client.plugins.administrator.dictionaryEditor;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;
import java.util.List;

public class DictionaryListPanel extends JPanel {

    private JComboBox dictionaryType;
    private JTable dictionaryTable;
    private DictionaryTableModel tableModel;
    private JTextComponent searchTextField;

    public DictionaryListPanel() {
        initView();
    }

    private void initView() {
        // TODO combobox z typem słownika
        // TODO textField do wyszukiwania
        // TODO lista
        // TODO przyciski
        setLayout(new RiverLayout());

        dictionaryType = createDictionaryTypeComponent();
        dictionaryTable = createDictionaryTable();
        searchTextField = createSearchField();

        add(RiverLayout.HFILL,dictionaryType);
        add(RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL, searchTextField);
        add(RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL + " " + RiverLayout.VFILL, dictionaryTable);


    }

    private JComboBox createDictionaryTypeComponent(){
        JComboBox comboBox = new JComboBox();
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
        // TODO dokończyć robienie pola wyszukiwania
        return textField;
    }

    private JTable createDictionaryTable(){
        JTable table = new JTable();
        // TODO dokończyć tabele
        return table;
    }

    private class DictionaryTableModel extends AbstractTableModel{

        private final int COLUMN_COUNT = 2;
        private final int NAME_COLUMN = 0;
        private final int DESCRIPTION_COLUMN = 1;

        private List<String> columns;
        private List<Dictionary> items;
        private String filter;
        private JTable table;

        private DictionaryTableModel(List<Dictionary> items, List<String> columns, JTable table) {
            this.items = items;
            this.columns = columns;
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
