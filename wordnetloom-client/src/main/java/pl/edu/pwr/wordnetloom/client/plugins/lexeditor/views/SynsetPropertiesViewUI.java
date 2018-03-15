package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.ExampleFrame;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeSynset;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.renderers.ExampleCellRenderer;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextArea;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextField;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;
import pl.edu.pwr.wordnetloom.synset.model.SynsetExample;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Synset properties panel
 *
 * @author Max
 */
public class SynsetPropertiesViewUI extends AbstractViewUI implements ActionListener, CaretListener {

    private MTextArea definitionValue;
    private MTextArea commentValue;

    private JScrollPane scrollPaneExamples;
    private MButton btnNewExample;
    private MButton btnEditExample;
    private MButton btnRemoveExample;
    private WebList examplesList;
    private DefaultListModel examplesModel;

    private final MButton buttonSave = MButton.buildSaveButton()
            .withToolTip(Hints.SAVE_CHANGES_IN_SYNSET)
            .withEnabled(false)
            .withActionListener(this);

    private WebCheckBox abstractValue;

    private Synset lastSynset = null;
    private boolean quiteMode = false;
    private final ViwnGraphViewUI graphUI;

    public SynsetPropertiesViewUI(ViwnGraphViewUI graphUI) {
        this.graphUI = graphUI;
    }

    @Override
    protected void initialize(WebPanel content) {

        content.setLayout(new RiverLayout());

        definitionValue = new MTextArea("");
        definitionValue.addCaretListener(this);
        definitionValue.setRows(3);

        commentValue = new MTextArea("");
        commentValue.addCaretListener(this);
        commentValue.setRows(3);

        abstractValue = new WebCheckBox(Labels.ARTIFICIAL);
        abstractValue.setSelected(false);
        abstractValue.addActionListener(this);

        scrollPaneExamples = new JScrollPane();

        examplesList = new WebList() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }
        };
        examplesList.setCellRenderer(new ExampleCellRenderer());

        ComponentListener l = new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                examplesList.setFixedCellHeight(10);
                examplesList.setFixedCellHeight(-1);
            }

        };

        examplesList.addComponentListener(l);
        examplesList.setVisibleRowCount(4);

        examplesModel = new DefaultListModel();
        examplesList.setModel(examplesModel);
        examplesList.addListSelectionListener( sl -> {
            if(examplesList.isSelectionEmpty()){
                adjustButtons(false);
            } else {
                adjustButtons(true);
            }

        });

        scrollPaneExamples.setViewportView(examplesList);

        btnNewExample = MButton.buildAddButton();

        btnNewExample.addActionListener((ActionEvent e) -> {
            String example = ExampleFrame.showModal(null, Labels.NEW_EXAMPLE, "", false);
            if (example != null && !"".equals(example)) {
                SynsetExample exp = new SynsetExample();
                exp.setExample(example);
                examplesModel.addElement(exp);
                saveChanges();
                examplesList.updateUI();
            }
        });

        JLabel lblExample = new JLabel(Labels.USE_CASE_COLON);
        lblExample.setVerticalAlignment(SwingConstants.TOP);
        lblExample.setHorizontalAlignment(SwingConstants.RIGHT);

        btnEditExample = MButton.buildEditButton();
        btnEditExample.addActionListener((ActionEvent e) -> {
            int idx = examplesList.getSelectedIndex();
            SynsetExample example = (SynsetExample) examplesModel.get(idx);
            if (idx >= 0) {
                String modified = ExampleFrame.showModal(null,
                        Labels.EDIT_EXAMPLE,
                        example.getExample(), true);
                String old = example.getExample();
                if (modified != null && !old.equals(modified)) {
                    example.setExample(modified);
                    examplesList.updateUI();
                    saveChanges();
                }
            }
        });

        btnRemoveExample = MButton.buildDeleteButton();
        btnRemoveExample.addActionListener((ActionEvent e) -> {
            int idx = examplesList.getSelectedIndex();
            if (idx >= 0) {
                examplesModel.remove(idx);
                saveChanges();
            }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new RiverLayout(0, 0));
        buttonsPanel.add("br", btnNewExample);
        buttonsPanel.add("br", btnEditExample);
        buttonsPanel.add("br", btnRemoveExample);

        content.setMargin(5,1,5,1);
        content.add("vtop", new JLabel(Labels.DEFINITION_COLON));
        content.add("br hfill", new JScrollPane(definitionValue));
        content.add("br vtop", new JLabel(Labels.COMMENT_COLON));
        content.add("br hfill", new JScrollPane(commentValue));
        content.add("br vtop", new JLabel(Labels.EXAMPLES));
        content.add("br hfill",  scrollPaneExamples);
        content.add("", buttonsPanel);
        content.add("br hfill", abstractValue);
        content.add("br center", buttonSave);

        commentValue.setEnabled(false);
        definitionValue.setEnabled(false);
        abstractValue.setEnabled(false);
    }

    @Override
    public JComponent getRootComponent() {
        return definitionValue;
    }

    private void adjustButtons(boolean active){
        btnEditExample.setEnabled(active);
        btnRemoveExample.setEnabled(active);
    }

    public void refreshData(Synset synset) {

        definitionValue .setText(formatValue(null));
        commentValue.setText(formatValue(null));
        abstractValue.setSelected(false);

        if (synset != null) {

            SynsetAttributes sa = RemoteService.synsetRemote.fetchSynsetAttributes(synset.getId());

            if(sa.getDefinition() != null) {
                definitionValue.setText(sa.getDefinition());
            }

            if(sa.getComment() != null ) {
               commentValue.setText(sa.getComment());
            }

            if(sa.getExamples() != null){
                examplesModel.clear();
                sa.getExamples().forEach(e -> examplesModel.addElement(e));
            }

            abstractValue.setSelected(synset.getAbstract());
        }

       lastSynset = synset;
       quiteMode = true;
       commentValue.setEnabled(synset != null);
       definitionValue.setEnabled(synset != null);
       abstractValue.setEnabled(synset != null);
       buttonSave.setEnabled(false);
       quiteMode = false;
    }

    /**
     * formatowanie wartości, tak aby nie bylo nulla
     *
     * @param value - wartość wejsciowa
     * @return wartośc wjesciowa lub "brak danych" gdy był null
     */
    private static String formatValue(String value) {
        return (value == null || value.length() == 0) ? Labels.VALUE_UNKNOWN : value;
    }

    /**
     * została wywołana jakaś akcja
     *
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (quiteMode == true || lastSynset == null) {
            return; // nie ma co aktualizować
        }
        // zmiana zaznaczenia w checkboxie
        if (event.getSource() == abstractValue) {
            buttonSave.setEnabled(true);
            // zapisanie zmian
        } else if (event.getSource() == buttonSave) { // zapisanie zmian
            saveChanges();
        }
    }

    private void saveChanges() {
        String definition = definitionValue.getText();
        String comment = commentValue.getText();
        boolean isAbstract = abstractValue.isSelected();

        Set<SynsetExample> examples = new HashSet<>();

        if(!examplesModel.isEmpty()) {
            for(int i = 0; i < examplesModel.size(); i++)
            {
                examples.add((SynsetExample)examplesModel.getElementAt(i));
            }
        }

        if (!LexicalDA.updateSynset(lastSynset, definition, comment, isAbstract, examples)) {
            refreshData(lastSynset); // nieudana zmiana statusu
            DialogBox.showError(Messages.ERROR_NO_STATUS_CHANGE_BECAUSE_OF_RELATIONS_IN_SYNSETS);
        }

        buttonSave.setEnabled(false);
        if (definitionValue.isEnabled()) { // zwrocenie focusu
            definitionValue.grabFocus();
        } else {
            commentValue.grabFocus();
        }
        // poinformowanie o zmianie parametrow
        listeners.notifyAllListeners(lastSynset);

        ViwnNode node = graphUI.getSelectedNode();
        if (node != null && node instanceof ViwnNodeSynset) {
            ViwnNodeSynset s = (ViwnNodeSynset) node;
            s.setLabel(null);
            graphUI.graphChanged();
        } else {
            // dodano nowy synset, nie istnieje on nigdzie w grafie
            graphUI.graphChanged();
        }
    }

    @Override
    public void caretUpdate(CaretEvent arg0) {
        if (quiteMode == true || lastSynset == null) {
            return; // nie ma co aktualizować
        }
        if (arg0.getSource() instanceof MTextField) {
            MTextField field = (MTextField) arg0.getSource();
            buttonSave.setEnabled(buttonSave.isEnabled() | field.wasTextChanged());
        }
        if (arg0.getSource() instanceof MTextArea) {
            MTextArea field = (MTextArea) arg0.getSource();
            buttonSave.setEnabled(buttonSave.isEnabled() | field.wasTextChanged());
        }
    }
}
