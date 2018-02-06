package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.panel.WebPanel;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeSynset;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextArea;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextField;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * klasa opisujacy wyglada okienka z wlasciwoscami synsetu
 *
 * @author Max
 */
public class SynsetPropertiesViewUI extends AbstractViewUI implements ActionListener, CaretListener {

    private MTextArea definitionValue;
    private MTextArea commentValue;

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

        definitionValue = new MTextArea(Labels.VALUE_UNKNOWN);
        definitionValue.addCaretListener(this);
        definitionValue.setRows(3);

        commentValue = new MTextArea(Labels.VALUE_UNKNOWN);
        commentValue.addCaretListener(this);
        commentValue.setRows(3);

        abstractValue = new WebCheckBox(Labels.ARTIFICIAL);
        abstractValue.setSelected(false);
        abstractValue.addActionListener(this);

        content.add("vtop", new JLabel(Labels.DEFINITION_COLON));
        content.add("tab hfill", new JScrollPane(definitionValue));
        content.add("br vtop", new JLabel(Labels.COMMENT_COLON));
        content.add("tab hfill", new JScrollPane(commentValue));
        content.add("br", abstractValue);
        content.add("br center", buttonSave);

        // ustawienie akywnosci
        commentValue.setEnabled(false);
        definitionValue.setEnabled(false);
        abstractValue.setEnabled(false);
    }

    @Override
    public JComponent getRootComponent() {
        return definitionValue;
    }

    /**
     * odswiezenie informacji o synsecie
     *
     * @param synset - synset
     */
    public void refreshData(Synset synset) {

        definitionValue .setText(formatValue(null));
        commentValue.setText(formatValue(null));
        abstractValue.setSelected(false);

        if (synset != null) {

            SynsetAttributes sa = RemoteService.synsetRemote.fetchSynsetAttributes(synset.getId());

            if(sa.getDefinition() != null) {
                definitionValue.setText(synset.getSynsetAttributes().getDefinition());
            }

            if(sa.getComment() != null ) {
                commentValue.setText(formatValue(synset.getSynsetAttributes().getComment()));
            }

            abstractValue.setSelected(sa.getIsAbstract());
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
            String definition = definitionValue.getText();
            String comment = commentValue.getText();
            boolean isAbstract = abstractValue.isSelected();

            if (!LexicalDA.updateSynset(lastSynset, definition, comment, isAbstract)) {
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
