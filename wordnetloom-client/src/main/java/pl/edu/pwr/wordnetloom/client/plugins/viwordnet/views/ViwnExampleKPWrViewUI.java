package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views;

import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views.LexicalUnitPropertiesViewUI;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.utils.GUIUtils;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnExampleKPWrView.KPWR_TAG;

public class ViwnExampleKPWrViewUI extends AbstractViewUI implements
        MouseListener {

    private WebScrollPane scroll;
    private Sense unit;
    private WebList examples;
    private final DefaultListModel listModel = new DefaultListModel();
    private final ViwnGraphViewUI graphUI;

    public ViwnExampleKPWrViewUI(ViwnGraphViewUI graphUI) {
        this.graphUI = graphUI;
    }

    @Override
    protected void initialize(WebPanel content) {
        getContent().setLayout(new RiverLayout());
        examples = new WebList(listModel) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }
        };
        examples.setCellRenderer(new ExampleCellRenderer());

        ComponentListener l = new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                examples.setFixedCellHeight(10);
                examples.setFixedCellHeight(-1);
            }

        };

        examples.addComponentListener(l);
        examples.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        examples.addMouseListener(this);
        scroll = new WebScrollPane(examples);
        getContent().add(scroll, "hfill vfill");
    }

    private void adjustSizeOfListItem(int width) {
        ExampleCellRenderer cellRenderer = new ExampleCellRenderer();
        examples.setCellRenderer(cellRenderer);
    }

    public void setExampleList(final List<String> exampleList, Sense sense) {
        unit = sense;
        listModel.clear();
        Runnable run;
        run = () -> {
            exampleList.stream().map((item) -> {
                listModel.addElement(item.replace(KPWR_TAG, ""));
                return item;
            }).forEachOrdered((_item) -> {
                adjustSizeOfListItem(scroll.getWidth() - 50);
            });
        };

        try {
            GUIUtils.delegateToEDT(run);
        } catch (InterruptedException | InvocationTargetException e) {
        }
    }

    @Override
    public JComponent getRootComponent() {
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON3) {

            LexicalUnitPropertiesViewUI lui = new LexicalUnitPropertiesViewUI(graphUI);
            lui.init(workbench);

            final DialogWindow dia = new DialogWindow(workbench.getFrame(),
                    Labels.UNIT_PROPERTIES, 585, 520);

            WebPanel pan = new WebPanel();
            lui.initialize(pan);
            lui.refreshData(unit);
            lui.fillKPWrExample(examples.getSelectedValues());
            lui.closeWindow((ActionEvent e1) -> {
                dia.dispose();
            });
            dia.setLocationRelativeTo(workbench.getFrame());
            dia.setContentPane(pan);
            dia.pack();
            dia.setVisible(true);

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public class ExampleCellRenderer implements ListCellRenderer {

        private final WebPanel p;
        private final WebTextArea ta;

        public ExampleCellRenderer() {
            p = new WebPanel();
            p.setLayout(new BorderLayout());

            // text
            ta = new WebTextArea();

            ta.setLineWrap(true);
            ta.setWrapStyleWord(true);

            p.add(ta, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(final JList list,
                                                      final Object value, final int index, final boolean isSelected,
                                                      final boolean hasFocus) {

            ta.setText((String) value);
            if (isSelected) {
                ta.setBackground(new Color(135, 206, 250));
            } else if (index % 2 == 0) {
                ta.setBackground(Color.LIGHT_GRAY);
            } else {
                ta.setBackground(Color.gray);
            }
            int width = list.getWidth();
            if (width > 0) {
                ta.setSize(width, Short.MAX_VALUE);
            }
            return p;

        }
    }
}
