package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextArea;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import pl.edu.pwr.wordnetloom.client.systems.managers.SenseExampleManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.autocompleteField.AutocompleteTextField;
import pl.edu.pwr.wordnetloom.client.systems.ui.autocompleteField.StringSuggestionFilter;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.common.model.Example;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class ExamplePanel extends WebPanel {

    private static final long serialVersionUID = -7961855308764934738L;
    private final WebTextArea exampleTextArea;
    private final JComboBox typeComboBox;
    private final WebButton btnSave;
    private final WebButton btnCancel;

    public ExamplePanel() {
        final int WIDTH = 450;
        setPreferredWidth(WIDTH);
        setLayout(new RiverLayout());
        String HFILL_BREAK = RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL;

        JLabel lblNewLabel = new JLabel(Labels.USE_CASE_COLON);
        add(RiverLayout.HFILL,lblNewLabel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(WIDTH, 100));
        add(HFILL_BREAK, scrollPane);

        // TODO dorobić etykietę, być może nazwać to jakoś inaczej
        JLabel exampleLabel = new JLabel("Typ");
        add(HFILL_BREAK, exampleLabel);

        exampleTextArea = new WebTextArea();
        exampleTextArea.setLineWrap(true);
        scrollPane.setViewportView(exampleTextArea);

        typeComboBox = createTypeCombobox();
        add(HFILL_BREAK, typeComboBox);

        Box horizontalBox = Box.createHorizontalBox();
        add(RiverLayout.LINE_BREAK + " " + RiverLayout.RIGHT, horizontalBox);

        btnSave = new WebButton(Labels.ADD);
        btnSave.setHorizontalAlignment(SwingConstants.RIGHT);
        horizontalBox.add(btnSave);

        Component horizontalStrut = Box.createHorizontalStrut(10);
        horizontalBox.add(horizontalStrut);

        btnCancel = new WebButton(Labels.CANCEL);
        btnCancel.setHorizontalAlignment(SwingConstants.RIGHT);
        horizontalBox.add(btnCancel);

//        fillTypeFiled();

    }

    private JComboBox createTypeCombobox() {
        JComboBox typeComboBox = new JComboBox();
        Set<String> exampleTypes = SenseExampleManager.getInstance().getTypes();
        for(String type : exampleTypes){
            typeComboBox.insertItemAt(type, 0);
        }
        AutoCompleteDecorator.decorate(typeComboBox);
        return typeComboBox;
    }

    public void setExample(Example example) {
        exampleTextArea.setText(example.getExample());
        typeComboBox.setSelectedItem(example.getType());

    }

    public JTextArea getExampleTextArea() {
        return exampleTextArea;
    }

    public JComboBox getTypeComboBox() {
        return typeComboBox;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }
}
