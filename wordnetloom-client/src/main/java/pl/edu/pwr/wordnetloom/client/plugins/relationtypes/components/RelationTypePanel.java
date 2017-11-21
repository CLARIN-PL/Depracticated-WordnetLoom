package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.components;

import com.alee.laf.panel.WebPanel;
import pl.edu.pwr.wordnetloom.client.systems.ui.MSplitPane;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RelationTypePanel extends WebPanel {

    private RelationTreePanel treePanel;
    private RelationTypePropertiesPanel propertiesPanel;
    private RelationTypeTestsPanel testsPanel;

    private List<RelationType> relations;

    private int parentWidth;
    private int parentHeight;

    public RelationTypePanel(List<RelationType> relations) {
        setLayout(new BorderLayout());
        this.relations = relations;
    }

    public RelationTypePanel withSize(int width, int height) {

        parentHeight = height;
        parentWidth = width;

        return this;
    }


    public RelationTypePanel build() {
        treePanel = new RelationTreePanel();
        propertiesPanel = new RelationTypePropertiesPanel();
        testsPanel = new RelationTypeTestsPanel();
        MSplitPane properties = buildSplitPanel(propertiesPanel, testsPanel, parentHeight / 4);
        MSplitPane main = buildSplitPanel(treePanel, properties, parentHeight / 2);
        add(main, BorderLayout.CENTER);
        return this;
    }

    private MSplitPane buildSplitPanel(JComponent top, JComponent bottom, int dividerPosition) {
        MSplitPane mainSplit = new MSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);
        mainSplit.setStartDividerLocation(dividerPosition);
        mainSplit.setResizeWeight(1.0f);
        return mainSplit;
    }

}
