package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window;


import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.tabbedpane.WebTabbedPane;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.components.RelationTypePanel;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.MFrame;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;

import java.awt.*;

public class RelationsEditorWindow extends MFrame {

    private final static int MIN_WINDOW_WIDTH = 850;
    private final static int MIN_WINDOW_HEIGHT = 680;
    private final static int MAX_WINDOW_WIDTH = 850;
    private final static int MAX_WINDOW_HEIGHT = 680;

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

        WebPanel wrapper = new WebPanel(tabs);
        wrapper.setMargin(5);

        add(wrapper, BorderLayout.CENTER);

        synsetRelationPanel = new RelationTypePanel(RelationTypeManager.getInstance().getParents(RelationArgument.SYNSET_RELATION))
                .withSize(MAX_WINDOW_WIDTH, MAX_WINDOW_HEIGHT)
                .build();

        senseRelationPanel = new RelationTypePanel(RelationTypeManager.getInstance().getParents(RelationArgument.SENSE_RELATION))
                .withSize(MAX_WINDOW_WIDTH, MAX_WINDOW_HEIGHT)
                .build();

        tabs.add("Synset relation", synsetRelationPanel);
        tabs.add("Lexical units relations ", senseRelationPanel);


    }

}