package pl.edu.pwr.wordnetloom.client.plugins.administrator.labelEditor;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.security.UserSessionContext;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComponentGroup;
import se.datadosen.component.RiverLayout;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

class LabelsListPanel extends JPanel {

    private JComboBox languageCombo;
    private JTable labelsTable;
    private TableModel model;
    private LabelsListListener listener;

    LabelsListPanel(LabelsListListener listener) {
        this.listener = listener;
        initView();
        loadLabels(UserSessionContext.getInstance().getLanguage());
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
        });
        JButton searchButton = MButton.buildSearchButton().withActionListener(e -> search(searchField.getText().toString()));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(searchField, BorderLayout.CENTER);
        panel.add(searchButton, BorderLayout.EAST);
        return panel;
    }

    private void search(String text) {
        RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {
            @Override
            public boolean include(Entry<?, ?> entry) {
                return entry.getStringValue(0).contains(text);
            }
        };
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        sorter.setRowFilter(filter);
        labelsTable.setRowSorter(sorter);
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
        throw new NotImplementedException();
    }

    private void editLabel() {
        throw new NotImplementedException();
    }

    private void deleteLabel() {
        throw new NotImplementedException();
    }

    private JScrollPane createLabelsTable() {
        labelsTable = new JTable();
        labelsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int row = labelsTable.rowAtPoint(e.getPoint());
                if (e.getClickCount() == 2 && labelsTable.getSelectedRow() != -1) {
                    String value = (String) labelsTable.getValueAt(row, 0);
                    listener.click(value);
                }
            }
        });
        return new JScrollPane(labelsTable);
    }

    private void loadLabels(String language) {
        Map<String, String> labelsMap = RemoteService.localisedStringServiceRemote.findLabelsByLanguage(language);
        String[][] labelsArray = convertToArray(labelsMap);
        // TODO dorobić etykiety
        String[] columnNames = {"Nazwa", "Wartość"};
        model = new DefaultTableModel(labelsArray, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        labelsTable.setModel(model);
    }

    private String[][] convertToArray(Map<String, String> map) {
        final int NAME = 0;
        final int VALUE = 1;
        String[][] resultArray = new String[map.size()][2];
        int row = 0;

        for (Map.Entry<String, String> entry : map.entrySet()) {
            resultArray[row][NAME] = entry.getKey();
            resultArray[row][VALUE] = entry.getValue();
            row++;
        }
        return resultArray;
    }

    public interface LabelsListListener {
        void click(String labelName);
    }
}
