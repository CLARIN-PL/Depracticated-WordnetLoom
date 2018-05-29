package pl.edu.pwr.wordnetloom.client.systems.ui;

import com.alee.extended.layout.TableLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupView {

    public static Component createGroupView(Map<String, Component> components,
                                            Dimension d,
                                            float labelRatio, float componentRatio) {

        List<Component> preview = new ArrayList<>();

        WebPanel groupPanel = new WebPanel() {
            @Override
            public void setEnabled(boolean enabled) {
                for (Component previewComponent : preview) {
                    SwingUtils.setEnabledRecursively(previewComponent, enabled);
                }
                super.setEnabled(enabled);
            }
        };

        groupPanel.putClientProperty(SwingUtils.HANDLES_ENABLE_STATE, true);
        groupPanel.setOpaque(true);
        if(d != null) groupPanel.setPreferredSize(d);
        int rowsAmount = components.size() > 1 ? components.size() * 2 - 1 : 1;

        double[] rows = new double[6 + rowsAmount];
        rows[0] = TableLayout.FILL;
        rows[1] = 0;
        rows[2] = TableLayout.PREFERRED;

        for (int i = 3; i < rows.length - 3; i++) {
            rows[i] = TableLayout.PREFERRED;
        }

        rows[rows.length - 3] = TableLayout.PREFERRED;
        rows[rows.length - 2] = 0;
        rows[rows.length - 1] = TableLayout.FILL;

        double[] columns =
                {labelRatio, TableLayout.PREFERRED, TableLayout.PREFERRED, componentRatio,TableLayout.PREFERRED, TableLayout.FILL};

        TableLayout groupLayout = new TableLayout(new double[][]{columns, rows});
        groupLayout.setHGap(4);
        groupLayout.setVGap(4);
        groupPanel.setLayout(groupLayout);

        groupPanel.add(createVerticalSeparator(), "2,0,2," + (rows.length - 1));
        groupPanel.add(createHorizontalSeparator(), "0,2," + (columns.length - 1) + ",2");
        groupPanel.add(createHorizontalSeparator(),
                "0," + (rows.length - 3) + "," + (columns.length - 1) + "," + (rows.length - 3)
        );

        int[] row = {3};

        components.forEach((label, cmp) -> {
            groupPanel.add(createDescription(label), "1," + row[0]);
            groupPanel.add(cmp, "3," + row[0]);
            if (row[0] > 3) {
                groupPanel.add(createHorizontalSeparator(),
                        "0," + (row[0] - 1) + "," + (columns.length - 1) + "," + (row[0] - 1), 0);
            }
            row[0] += 2;
        });

        WebScrollPane panel = new WebScrollPane(groupPanel);
        panel.setDrawBorder(false);
        return panel ;
    }

    private static WebSeparator createHorizontalSeparator() {
        WebSeparator separator = new WebSeparator(WebSeparator.HORIZONTAL);
        separator.setDrawSideLines(false);
        return separator;
    }

    private static WebSeparator createVerticalSeparator() {
        WebSeparator separator = new WebSeparator(WebSeparator.VERTICAL);
        separator.setDrawSideLines(false);
        return separator;
    }

    private static Component createDescription(String title) {
        Color foreground = Color.BLACK;

        WebLabel titleLabel = new WebLabel(title, JLabel.TRAILING);
        titleLabel.setDrawShade(true);
        titleLabel.setForeground(foreground);
        if (foreground.equals(Color.WHITE)) {
            titleLabel.setShadeColor(Color.BLACK);
        }
       return titleLabel;
    }
}
