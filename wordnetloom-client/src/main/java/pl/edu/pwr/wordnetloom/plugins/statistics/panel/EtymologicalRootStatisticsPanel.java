package pl.edu.pwr.wordnetloom.plugins.statistics.panel;

import pl.edu.pwr.wordnetloom.model.CountModel;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

public class EtymologicalRootStatisticsPanel extends JPanel {

    private final int COLUMNS_COUNT = 2;
    private final int NAME_COLUMN = 0;
    private final int COUNT_COLUMN = 1;

    private JTable table;
    private JTextField filterField;
    private CountTableModel model;
    private TableRowSorter<CountTableModel> sorter;

    public EtymologicalRootStatisticsPanel() {
        init();
        table = createTable();
        filterField = createFilterField();
        setupPanel();
        setTableModel(getData());
        setupFilter();
    }

    private void init() {
        setLayout(new RiverLayout());
        setBorder(BorderFactory.createTitledBorder("Etymological roots statistics"));
    }

    private void setupPanel() {
        JLabel filterLabel = new JLabel(Labels.SEARCH_NO_COLON);
        add(filterLabel);
        final String TAB_STOP_HFILL = RiverLayout.TAB_STOP + " " + RiverLayout.HFILL; //od początku kolumny, szerokość rodzica
        final String LINE_BREAK_FULL_FILL = RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL + " " + RiverLayout.VFILL; //od nowej lini, szerokość i wysokość rodzica
        add(TAB_STOP_HFILL, filterField);
        add(LINE_BREAK_FULL_FILL, new JScrollPane(table));
    }

    private JTable createTable() {
        JTable table = new JTable(new Object[][]{}, new String[]{"name", "count"});
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION); //wybieranie całego wiersza, uniemożliwia edycje wartości

        // ustawienie formatu numerów - przyciąganie do prawej wartości w kolumnie count
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(COUNT_COLUMN).setCellRenderer(rightRenderer);

        return table;
    }

    private void setupFilter() {
        sorter = new TableRowSorter<>(model);
        table.setAutoCreateRowSorter(false);
        table.setRowSorter(sorter);
    }

    private JTextField createFilterField() {
        filterField = new JTextField();
        filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filter();
            }
        });
        return filterField;
    }

    private void filter() {
        String text = filterField.getText();
        sorter.setRowFilter(new RowFilter<CountTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends CountTableModel, ? extends Integer> entry) {
                String name = (String) entry.getValue(0);
                return name.toLowerCase().contains(text.toLowerCase());
            }
        });
    }

    private java.util.List<CountModel> getData() {
        return RemoteUtils.lexicalUnitRemote.dbGetEtymologicalRootsCount();
    }

    private void setTableModel(java.util.List<CountModel> list) {
        assert table != null;
        model = new CountTableModel(list);
        table.setModel(model);
    }

    /**
     * Model służący do wypełniania tabel składających sie z dwóch kolumn - nazwy i liczby
     */
    private class CountTableModel extends AbstractTableModel {
        private java.util.List<CountModel> modelsList;

        private CountTableModel(java.util.List<CountModel> list) {
            modelsList = list;
        }

        @Override
        public int getRowCount() {
            return modelsList.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMNS_COUNT;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            CountModel model = modelsList.get(rowIndex);
            switch (columnIndex) {
                case NAME_COLUMN:
                    if (model.getName().length() == 0) {
                        return Labels.NOT_CHOSEN;
                    } else {
                        return model.getName();
                    }
                case COUNT_COLUMN:
                    return model.getCount();
            }
            return null;
        }

        @Override
        public String getColumnName(int columnIndex) {
            // TODO pobrać wartości z etykiet
            switch (columnIndex) {
                case NAME_COLUMN:
                    return "Name";
                case COUNT_COLUMN:
                    return "Count";
            }
            return "";
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }
}
