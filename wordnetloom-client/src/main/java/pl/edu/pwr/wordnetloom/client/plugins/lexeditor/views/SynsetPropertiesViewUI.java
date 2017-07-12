package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeSynset;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.ComboBoxPlain;
import pl.edu.pwr.wordnetloom.client.systems.ui.TextAreaPlain;
import pl.edu.pwr.wordnetloom.client.systems.ui.TextFieldPlain;
import pl.edu.pwr.wordnetloom.client.utils.Common;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.model.wordnet.StatusDictionary;
import pl.edu.pwr.wordnetloom.model.wordnet.Synset;
import se.datadosen.component.RiverLayout;

/**
 * klasa opisujacy wyglada okienka z wlasciwoscami synsetu
 *
 * @author Max
 */
public class SynsetPropertiesViewUI extends AbstractViewUI implements ActionListener, CaretListener {

    private static final String SUPER_MODE_VALUE = "1";
    private static final String SUPER_MODE = "SuperMode";

    private TextAreaPlain definitionValue;
    private TextAreaPlain commentValue;
    private TextFieldPlain sumoValue;
    private ComboBoxPlain<StatusDictionary> statusValue;
    private ButtonExt buttonSave;
    private JCheckBox abstractValue;

    private Synset lastSynset = null;
    private boolean quiteMode = false;
    private final ViwnGraphViewUI graphUI;

    private StatusDictionary defaultStatus = RemoteUtils.dictionaryRemote.findDefaultStatusDictionaryValue();

    public SynsetPropertiesViewUI(ViwnGraphViewUI graphUI) {
        this.graphUI = graphUI;
    }

    @Override
    protected void initialize(JPanel content) {

        content.setLayout(new RiverLayout());

        definitionValue = new TextAreaPlain(Labels.VALUE_UNKNOWN);
        definitionValue.addCaretListener(this);
        definitionValue.setRows(3);

        sumoValue = new TextFieldPlain("");
        sumoValue.addCaretListener(this);

        statusValue = new ComboBoxPlain<>();
        for (StatusDictionary s : RemoteUtils.dictionaryRemote.findAllStatusDictionary()) {
            statusValue.addItem(new CustomDescription<>(s.getName(), s));
        }
        statusValue.setSelectedItem(defaultStatus);
        statusValue.addActionListener(this);

        commentValue = new TextAreaPlain(Labels.VALUE_UNKNOWN);
        commentValue.addCaretListener(this);
        commentValue.setRows(3);

        buttonSave = new ButtonExt(Labels.SAVE, this);
        buttonSave.setEnabled(false);
        buttonSave.setToolTipText(Hints.SAVE_CHANGES_IN_SYNSET);

        abstractValue = new JCheckBox(Labels.ARTIFICIAL);
        abstractValue.setSelected(false);
        abstractValue.addActionListener(this);

        content.add("vtop", new JLabel(Labels.DEFINITION_COLON));
        content.add("tab hfill", new JScrollPane(definitionValue));
        content.add("br vtop", new JLabel(Labels.COMMENT_COLON));
        content.add("tab hfill", new JScrollPane(commentValue));
        content.add("br vtop", new JLabel("Sumo:"));
        content.add("tab hfill", sumoValue);
        content.add("br vtop", new JLabel(Labels.STATUS_COLON));
        content.add("tab hfill", statusValue);
        content.add("br", abstractValue);
        content.add("br center", buttonSave);

        // ustawienie akywnosci
        statusValue.setEnabled(false);
        commentValue.setEnabled(false);
        definitionValue.setEnabled(false);
        abstractValue.setEnabled(false);
        sumoValue.setEnabled(false);
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
        if (synset != null) {
            synset = LexicalDA.refresh(synset);
        }
        lastSynset = synset;
        quiteMode = true;

        // ustawienie wartosci elementow
        definitionValue.setText(synset != null ? formatValue(Common.getSynsetAttribute(synset, Synset.DEFINITION)) : formatValue(null));
        commentValue.setText(synset != null ? formatValue(Common.getSynsetAttribute(synset, Synset.COMMENT)) : formatValue(null));
        abstractValue.setSelected(synset != null && Synset.isAbstract(Common.getSynsetAttribute(synset, Synset.ISABSTRACT)));
        StatusDictionary defaultStatus = RemoteUtils.dictionaryRemote.findDefaultStatusDictionaryValue();
        statusValue.setSelectedItem(synset != null ? new CustomDescription<>(synset.getStatus().getName(), synset.getStatus())
                : new CustomDescription<>(defaultStatus.getName(), defaultStatus)); // czy combo jest aktywne

        sumoValue.setText(synset != null ? formatValue(Common.getSynsetAttribute(synset, Synset.SUMO)) : formatValue(null));

        commentValue.setEnabled(synset != null);
        definitionValue.setEnabled(synset != null);
        abstractValue.setEnabled(synset != null);
        statusValue.setEnabled(synset != null);
        sumoValue.setEnabled(synset != null);
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

            // proba zmiany statusu
        } else if (event.getSource() == statusValue) {

            buttonSave.setEnabled(true);
            // zapisanie zmian
        } else if (event.getSource() == buttonSave) { // zapisanie zmian
            String definition = definitionValue.getText();
            StatusDictionary statusIndex = statusValue.getSelectedItem() != null ? statusValue.retriveComboBoxItem() : defaultStatus;
            String comment = commentValue.getText();
            boolean isAbstract = abstractValue.isSelected();
            String sumo = sumoValue.getText();

            if (!LexicalDA.updateSynset(lastSynset, definition, statusIndex, comment, isAbstract, sumo)) {
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
        if (arg0.getSource() instanceof TextFieldPlain) {
            TextFieldPlain field = (TextFieldPlain) arg0.getSource();
            buttonSave.setEnabled(buttonSave.isEnabled() | field.wasTextChanged());
        }
        if (arg0.getSource() instanceof TextAreaPlain) {
            TextAreaPlain field = (TextAreaPlain) arg0.getSource();
            buttonSave.setEnabled(buttonSave.isEnabled() | field.wasTextChanged());
        }
    }
}
