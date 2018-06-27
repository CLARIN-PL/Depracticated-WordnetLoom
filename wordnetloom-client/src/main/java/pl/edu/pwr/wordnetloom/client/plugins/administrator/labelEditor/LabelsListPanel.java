package pl.edu.pwr.wordnetloom.client.plugins.administrator.labelEditor;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.security.UserSessionContext;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComponentGroup;
import pl.edu.pwr.wordnetloom.localisation.model.ApplicationLabel;
import se.datadosen.component.RiverLayout;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class LabelsListPanel extends JPanel {

    private JComboBox languageCombo;
    private JTable labelsTable;
    private LabelModel model;
    private LabelsListListener listener;
    private int editingLabelIndex;

    LabelsListPanel(LabelsListListener listener) {
        this.listener = listener;
        initView();
        loadLabels(UserSessionContext.getInstance().getLanguage());
    }

    public void refreshLabel(String key) {
        ApplicationLabel label = RemoteService.localisedStringServiceRemote.find(key, (String) languageCombo.getSelectedItem());
        model.setElementAt(label, editingLabelIndex);
    }

    private void initView() {
        setLayout(new RiverLayout());

        JPanel languagePanel = createLanguagePanel();
        JScrollPane labelsScrollTable = createLabelsTable();
        JPanel buttonPanel = createButtonPanel();
        JPanel searchPanel = createFilterPanel();

        final String TABLE_CONSTRAINT = RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL + " " + RiverLayout.VFILL;
        add(RiverLayout.HFILL, languagePanel);
        add(RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL, searchPanel);
        add(TABLE_CONSTRAINT, labelsScrollTable);
        add(RiverLayout.LINE_BREAK, buttonPanel);
    }

    private JPanel createFilterPanel() {
        JTextField searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

            private void search(String text) {
                labelsTable.setRowSorter(model.getFilter(text));
            }
        });
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(searchField, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLanguagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        languageCombo = new JComboBox();
        getLanguages().forEach(languageCombo::addItem);
        languageCombo.addActionListener(e -> loadLabels((String) languageCombo.getSelectedItem()));

        JButton button = MButton.buildAddButton().withActionListener(e -> {
            // TODO wyświetlenie okienka do tworzenia nowego języka
            throw new NotImplementedException();
        });

        panel.add(languageCombo, BorderLayout.CENTER);
        panel.add(button, BorderLayout.EAST);

        return panel;
    }

    private ArrayList<String> getLanguages() {
        Language[] languages = Language.values();
        return Arrays.stream(Language.values())
                .map(Language::getAbbreviation)
                .collect(Collectors.toCollection(() -> new ArrayList<>(languages.length)));
    }

    private JPanel createButtonPanel() {
        // TODO dodać etykiety
        JButton addButton = MButton.buildAddButton()
                .withToolTip("Dodaj etykietę")
                .withActionListener(e -> addLabel());
        JButton editButton = MButton.buildEditButton()
                .withToolTip("Edytuj etykietę")
                .withActionListener(e -> editLabel());
        JButton deleteButton = MButton.buildDeleteButton()
                .withToolTip("Usuń etykietę")
                .withActionListener(e -> deleteLabel());
        MComponentGroup panel = new MComponentGroup(addButton, editButton, deleteButton);
        panel.withHorizontalLayout();
        return panel;
    }

    private void addLabel() {
        editingLabelIndex = -1;
        ApplicationLabel applicationLabel = new ApplicationLabel();
        applicationLabel.setLanguage((String) languageCombo.getSelectedItem());
        listener.onEdit(applicationLabel);
    }

    private void editLabel() {
        int rowIndex = labelsTable.getSelectedRow();
        editLabel(rowIndex);
    }

    private void deleteLabel() {
        // TODO dorobić etykiety
        int answer = DialogBox.showYesNo("Czy na pewno usunąć zaznaczone etykiety?");
        if (answer == DialogBox.YES) {
            int selectedRow = labelsTable.getSelectedRow();
            ApplicationLabel labelToRemove = model.getElementAt(selectedRow);
            model.remove(selectedRow);
            RemoteService.localisedStringServiceRemote.remove(labelToRemove);
            listener.onDelete();
        }
    }

    private JScrollPane createLabelsTable() {
        labelsTable = new JTable();
        labelsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int row = labelsTable.rowAtPoint(e.getPoint());
                if (e.getClickCount() == 2 && labelsTable.getSelectedRow() != -1) {
                    editLabel(row);
                }
            }
        });
        return new JScrollPane(labelsTable);
    }

    private void editLabel(int row) {
        editingLabelIndex = row;
        listener.onEdit(model.getElementAt(row));
    }

    private void loadLabels(String language) {
        List<ApplicationLabel> labels = RemoteService.localisedStringServiceRemote.findLabelsByLanguage(language);
        // TODO dorobić etykiety
        List<String> columns = Arrays.asList("Nazwa", "Wartość");
        model = new LabelModel(labels, columns);
        labelsTable.setModel(model);
    }


    public interface LabelsListListener {
        void onEdit(ApplicationLabel applicationLabel);

        void onDelete();
    }

    private class LabelModel extends AbstractTableModel {
        private final int COLUMN_COUNT = 2;
        private final int NAME_COLUMN = 0;
        private final int VALUE_COLUMN = 1;

        private List<String> columns;
        private List<ApplicationLabel> items;
        private String filter;

        LabelModel(List<ApplicationLabel> labels, List<String> columns) {
            this.columns = columns;
            this.items = labels;
        }

        public void remove(int rowIndex) {
            int index = labelsTable.convertRowIndexToModel(rowIndex);
            items.remove(index);
            this.fireTableRowsDeleted(index, index);
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
            switch (columnIndex) {
                case NAME_COLUMN:
                    return items.get(rowIndex).getKey();
                case VALUE_COLUMN:
                    return items.get(rowIndex).getValue();
                default:
                    throw new IllegalArgumentException();
            }
        }

        ApplicationLabel getElementAt(int rowIndex) {
            int index = labelsTable.convertRowIndexToModel(rowIndex);
            return items.get(index);
        }

        void setElementAt(ApplicationLabel label, int rowIndex) {
            if (rowIndex >= 0) {
                items.set(rowIndex, label);
                if (isItemFiltered(label)) {
                    this.fireTableRowsUpdated(rowIndex, rowIndex);
                }
            } else {
                items.add(label);
                if (isItemFiltered(label)) {
                    this.fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
                }
            }
        }

        private boolean isItemFiltered(ApplicationLabel label) {
            return label.getKey().contains(filter);
        }

        TableRowSorter<TableModel> getFilter(String text) {
            filter = text;
            RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {
                @Override
                public boolean include(Entry<?, ?> entry) {
                    return entry.getStringValue(0).toLowerCase().contains(text.toLowerCase())
                            || entry.getStringValue(1).toLowerCase().contains(text.toLowerCase());
                }
            };
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
            sorter.setRowFilter(filter);
            return sorter;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columns.get(columnIndex);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}
