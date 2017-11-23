package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window;


import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.components.RelationTypePanel;
import pl.edu.pwr.wordnetloom.client.systems.ui.MFrame;
import pl.edu.pwr.wordnetloom.client.utils.Labels;

import java.awt.*;

public class RelationsEditorWindow extends MFrame {

    public final static int MIN_WINDOW_WIDTH = 850;
    public final static int MIN_WINDOW_HEIGHT = 680;
    public final static int MAX_WINDOW_WIDTH = 850;
    public final static int MAX_WINDOW_HEIGHT = 680;

    private RelationTypePanel relationTypePanel;

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


        relationTypePanel = new RelationTypePanel()
                .withSize(MAX_WINDOW_WIDTH, MAX_WINDOW_HEIGHT)
                .build();

        add(relationTypePanel, BorderLayout.CENTER);
    }

}