package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.components;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.tabbedpane.WebTabbedPane;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.MSplitPane;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;

import javax.swing.*;
import java.awt.*;

public class RelationTypePanel extends WebPanel {

    private final RelationTreePanel synsetRelationTreePanel;

    private final RelationTreePanel senseRelationTreePanel;

    private final RelationTypePropertiesPanel propertiesPanel;
    private final RelationTypeTestsPanel testsPanel;

    private int parentWidth;
    private int parentHeight;

    public RelationTypePanel(WebFrame parent) {
        setLayout(new BorderLayout());

        synsetRelationTreePanel = new RelationTreePanel(RelationArgument.SYNSET_RELATION);
        synsetRelationTreePanel.setRelationsTypes(RelationTypeManager.getInstance().getParents(RelationArgument.SYNSET_RELATION));

        senseRelationTreePanel = new RelationTreePanel(RelationArgument.SENSE_RELATION);
        senseRelationTreePanel.setRelationsTypes(RelationTypeManager.getInstance().getParents(RelationArgument.SENSE_RELATION));

        propertiesPanel = new RelationTypePropertiesPanel(parent);
        testsPanel = new RelationTypeTestsPanel();

    }

    public RelationTypePanel withSize(int width, int height) {

        parentHeight = height;
        parentWidth = width;

        return this;
    }

    public RelationTypePanel build() {

        WebTabbedPane tabs = new WebTabbedPane();

        WebPanel tabsWrapper = new WebPanel(tabs);
        tabsWrapper.setMargin(5);

        tabs.add("Synset relation", synsetRelationTreePanel);
        tabs.add("Sense relations ", senseRelationTreePanel);

        WebPanel propertiesWrapper = new WebPanel(propertiesPanel);
        propertiesWrapper.setMargin(10);

        WebPanel testsWrapper = new WebPanel(testsPanel);
        testsWrapper.setMargin(10);

        MSplitPane properties = buildSplitPanel(propertiesWrapper, testsWrapper, (int) (parentWidth * 0.55), JSplitPane.HORIZONTAL_SPLIT);
        MSplitPane main = buildSplitPanel(tabsWrapper, properties, parentHeight / 3, JSplitPane.VERTICAL_SPLIT);

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
