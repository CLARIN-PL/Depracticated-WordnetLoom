package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window;


import com.alee.laf.WebLookAndFeel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.tabbedpane.WebTabbedPane;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.components.RelationTypePanel;
import pl.edu.pwr.wordnetloom.client.systems.ui.MFrame;
import pl.edu.pwr.wordnetloom.client.utils.Labels;

import java.awt.*;
import java.util.ArrayList;

public class RelationsEditorWindow extends MFrame {

    private final static int MIN_WINDOW_WIDTH = 200;
    private final static int MIN_WINDOW_HEIGHT = 400;
    private final static int MAX_WINDOW_WIDTH = 550;
    private final static int MAX_WINDOW_HEIGHT = 700;

    private WebTabbedPane tabs;
    private RelationTypePanel synsetRelationPanel;
    private RelationTypePanel senseRelationPanel;

    public static void showModal(WebFrame parenFrame) {
        RelationsEditorWindow frame = new RelationsEditorWindow(parenFrame);
        frame.setLocationRelativeTo(parenFrame);
        frame.setVisible(true);
        frame.showModal();
    }

    private RelationsEditorWindow(WebFrame frame) {
        super(frame, Labels.EDIT_RELATION_TYPES, MAX_WINDOW_WIDTH, MAX_WINDOW_HEIGHT);
        init();
    }

    private void init() {
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));

        tabs = new WebTabbedPane();
        add(tabs, BorderLayout.CENTER);

        synsetRelationPanel = new RelationTypePanel(new ArrayList<>())
                .withSize(MAX_WINDOW_WIDTH, MAX_WINDOW_HEIGHT)
                .build();

        senseRelationPanel = new RelationTypePanel(new ArrayList<>())
                .withSize(MAX_WINDOW_WIDTH, MAX_WINDOW_HEIGHT)
                .build();

        tabs.add("Synset relation", synsetRelationPanel);
        tabs.add("Lexical units relations ", senseRelationPanel);


    }

    //For tests
    public static void main(String[] args) {
        WebLookAndFeel.install();
        IconFontSwing.register(FontAwesome.getIconFont());
        RelationsEditorWindow.showModal(null);
    }
}