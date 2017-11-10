package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextArea;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.forms.util.LayoutStyle;
import pl.edu.pwr.wordnetloom.client.utils.Labels;

import javax.swing.*;
import java.awt.*;

public class ExamplePanel extends WebPanel {

    private static final long serialVersionUID = -7961855308764934738L;
    private final WebTextArea exampleTextArea;
    private final WebButton btnSave;
    private final WebButton btnCancel;

    public ExamplePanel() {
        setLayout(new FormLayout(new ColumnSpec[]{
                ColumnSpec.decode("450px"),},
                new RowSpec[]{
                        RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                        new RowSpec(Sizes.DEFAULT),
                        RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                        RowSpec.decode("fill:max(65dlu;default):grow"),
                        RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                        new RowSpec(Sizes.DEFAULT),}));

        JLabel lblNewLabel = new JLabel(Labels.USE_CASE_COLON);
        add(lblNewLabel, "1, 2, fill, fill");

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, "1, 4, fill, fill");

        exampleTextArea = new WebTextArea();
        exampleTextArea.setLineWrap(true);
        scrollPane.setViewportView(exampleTextArea);

        Box horizontalBox = Box.createHorizontalBox();
        add(horizontalBox, "1, 6, right, default");

        btnSave = new WebButton(Labels.ADD);
        btnSave.setHorizontalAlignment(SwingConstants.RIGHT);
        horizontalBox.add(btnSave);

        Component horizontalStrut = Box.createHorizontalStrut(10);
        horizontalBox.add(horizontalStrut);

        btnCancel = new WebButton(Labels.CANCEL);
        btnCancel.setHorizontalAlignment(SwingConstants.RIGHT);
        horizontalBox.add(btnCancel);

    }

    public JTextArea getExampleTextArea() {
        return exampleTextArea;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }
}
