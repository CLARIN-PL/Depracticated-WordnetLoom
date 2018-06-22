package pl.edu.pwr.wordnetloom.client.plugins.administrator.labelEditor;

import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.splitpane.WebSplitPane;
import pl.edu.pwr.wordnetloom.client.systems.ui.MFrame;

import javax.swing.*;
import java.awt.*;

public class LabelEditorWindow extends MFrame {
    private final static int MAX_WINDOW_WIDTH = 1000;
    private final static int MAX_WINDOW_HEIGHT = 800;
    private final int MIN_WINDOW_WIDTH = 800;
    private final int MIN_WINDOW_HEIGHT = 800;
    private LabelsListPanel labelsListPanel;
    private EditLabelPanel editLabelPanel;

    private LabelEditorWindow(WebFrame parentFrame) {
        // TODO dodać etykietę
        super(parentFrame, "Edytor etykiet", MAX_WINDOW_WIDTH, MAX_WINDOW_HEIGHT);
        initView();
    }

    public static void showModal(WebFrame parentFrame) {
        new LabelEditorWindow(parentFrame).setVisible(true);
    }

    private void initView() {
        setLayout(new BorderLayout());
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));

        labelsListPanel = new LabelsListPanel(labelName -> editLabelPanel.loadLabel(labelName));
        editLabelPanel = new EditLabelPanel();

        WebSplitPane splitPane = new WebSplitPane(JSplitPane.HORIZONTAL_SPLIT, labelsListPanel, editLabelPanel);
        add(splitPane);
    }

}
