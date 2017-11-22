package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.components;

import com.alee.laf.panel.WebPanel;
import pl.edu.pwr.wordnetloom.client.systems.ui.MSplitPane;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RelationTypePanel extends WebPanel {

    private final RelationTreePanel treePanel;
    private final RelationTypePropertiesPanel propertiesPanel;
    private final RelationTypeTestsPanel testsPanel;

    private int parentWidth;
    private int parentHeight;

    public RelationTypePanel(List<RelationType> relations) {
        setLayout(new BorderLayout());

        treePanel = new RelationTreePanel();
        propertiesPanel = new RelationTypePropertiesPanel();
        testsPanel = new RelationTypeTestsPanel();

        setRelationTypes(relations);
    }

    public void setRelationTypes(List<RelationType> relations) {
        treePanel.setRelationsTypes(relations);
    }

    public RelationTypePanel withSize(int width, int height) {

        parentHeight = height;
        parentWidth = width;

        return this;
    }

    public RelationTypePanel build() {


        WebPanel propertiesWrapper = new WebPanel(propertiesPanel);
        propertiesWrapper.setMargin(10);

        WebPanel testsWrapper = new WebPanel(testsPanel);
        testsWrapper.setMargin(10);

        MSplitPane properties = buildSplitPanel(propertiesWrapper, testsWrapper, (int) (parentWidth * 0.45), JSplitPane.HORIZONTAL_SPLIT);
        MSplitPane main = buildSplitPanel(treePanel, properties, parentHeight / 3, JSplitPane.VERTICAL_SPLIT);

        add(main, BorderLayout.CENTER);

        return this;
    }

    private MSplitPane buildSplitPanel(JComponent first, JComponent second, int dividerPosition, int splitType) {
        MSplitPane mainSplit = new MSplitPane(splitType, first, second).withExpandable(false);
        mainSplit.setStartDividerLocation(dividerPosition);
        mainSplit.setResizeWeight(1.0f);
        return mainSplit;
    }

}
