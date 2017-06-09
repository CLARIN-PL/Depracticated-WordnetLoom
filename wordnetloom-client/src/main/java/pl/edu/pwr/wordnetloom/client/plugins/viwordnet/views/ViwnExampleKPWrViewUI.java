package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views.LexicalUnitPropertiesViewUI;
import pl.edu.pwr.wordnetloom.client.systems.ui.IconDialog;
import pl.edu.pwr.wordnetloom.client.utils.GUIUtils;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import se.datadosen.component.RiverLayout;

public class ViwnExampleKPWrViewUI extends AbstractViewUI implements
        MouseListener {

    private JScrollPane scroll;
    private Sense unit;
    private JList examples;
    private DefaultListModel listModel = new DefaultListModel();
    private final ViwnGraphViewUI graphUI;

    public ViwnExampleKPWrViewUI(ViwnGraphViewUI graphUI) {
        this.graphUI = graphUI;
    }

    @Override
    protected void initialize(JPanel content) {
        getContent().setLayout(new RiverLayout());
        examples = new JList(listModel) {

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
        scroll = new JScrollPane(examples);
        getContent().add(scroll, "hfill vfill");
    }

    private void adjustSizeOfListItem(int width) {
        ExampleCellRenderer cellRenderer = new ExampleCellRenderer();
        examples.setCellRenderer(cellRenderer);
    }

    public void setExampleList(final List<String> exampleList, Sense sense) {
        this.unit = sense;
        listModel.clear();
        Runnable run = new Runnable() {
            public void run() {
                for (String item : exampleList) {
                    listModel.addElement(item);
                    adjustSizeOfListItem(scroll.getWidth() - 50);
                }
            }
        };

        try {
            GUIUtils.delegateToEDT(run);
        } catch (InterruptedException e) {
        } catch (InvocationTargetException e) {
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
            lui.init(this.workbench);

            final IconDialog dia = new IconDialog(this.workbench.getFrame(),
                    Labels.UNIT_PROPERTIES, 585, 520);
            JPanel pan = new JPanel();
            lui.initialize(pan);
            lui.refreshData(unit);
            lui.fillKPWrExample(examples.getSelectedValues());
            lui.closeWindow(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dia.dispose();
                }
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

        private JPanel p;
        private JTextArea ta;

        public ExampleCellRenderer() {
            p = new JPanel();
            p.setLayout(new BorderLayout());

            // text
            ta = new JTextArea();

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
