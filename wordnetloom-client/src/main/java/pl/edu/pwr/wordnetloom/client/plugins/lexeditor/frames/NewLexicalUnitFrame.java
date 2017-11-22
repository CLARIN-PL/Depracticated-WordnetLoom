package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames;

import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.LexicalUnitPropertiesPanel;
import pl.edu.pwr.wordnetloom.client.systems.enums.RegisterTypes;
import pl.edu.pwr.wordnetloom.client.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.DomainMComboBox;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.word.model.Word;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * okienko do wprowadzania parametrow dla nowych jednostek
 *
 * @author Max
 */
public class NewLexicalUnitFrame extends DialogWindow implements ActionListener {

    private final LexicalUnitPropertiesPanel editPanel;

    private static Domain lastPickDomain = null;
    private static PartOfSpeech lastPickPos = null;

    private static final long serialVersionUID = 1L;
    private boolean wasAddClicked = false;

    /**
     * konstruktor
     *
     * @param workbench - srodowisko
     */
    private NewLexicalUnitFrame(Workbench workbench, WebFrame frame) {
        super(frame, Labels.UNIT_PARAMS, 625, 500);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);
        editPanel = new LexicalUnitPropertiesPanel(frame);
        editPanel.getBtnSave().addActionListener(this);
        editPanel.getBtnCancel().addActionListener(this);
        add(editPanel, "hfill");
        pack();
    }

    public Sense saveAndReturnNewSense() {
        Sense newUnit = new Sense();

        newUnit.setWord(new Word(editPanel.getLemma().getText()));
        newUnit.setLexicon(editPanel.getLexicon().getEntity());

        PartOfSpeech pos = editPanel.getPartOfSpeech().getEntity();
        newUnit.setPartOfSpeech(pos);

        Domain domain = editPanel.getDomain().getEntity();
        newUnit.setDomain(domain);

//        int variant = RemoteUtils.lexicalUnitRemote.dbGetNextVariant(editPanel.getLemma().getText(), pos);
//        newUnit.setSenseNumber(variant);
//
//        newUnit = LexicalDA.saveUnitRefreshID(newUnit);
//
//        if (editPanel.getDefinition().getText() != null && !editPanel.getDefinition().getText().isEmpty()) {
//            RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(newUnit, Sense.DEFINITION, editPanel.getDefinition().getText());
//        }
//        if (editPanel.getComment().getText() != null && !editPanel.getComment().getText().isEmpty()) {
//            RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(newUnit, Sense.COMMENT, editPanel.getComment().getText());
//        }
//        if (editPanel.getRegister().getSelectedItem().toString() != null) {
//            RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(newUnit, Sense.REGISTER, editPanel.getRegister().getSelectedItem().toString());
//        }
//        String examples = trasfotmExamplesToString(editPanel.getExamplesModel().toArray());
//        if (examples != null && !examples.isEmpty()) {
//            RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(newUnit, Sense.USE_CASES, examples);
//        }
//        if (editPanel.getLink().getText() != null && !editPanel.getLink().getText().isEmpty()) {
//            RemoteUtils.dynamicAttributesRemote.saveOrUpdateSenseAttribute(newUnit, Sense.LINK, editPanel.getLink().getText());
//        }
//        if (editPanel.getComment().getText() != null && !editPanel.getComment().getText().isEmpty()) {
//            RemoteUtils.trackerRemote.insertedLexicalUnit(newUnit, editPanel.getComment().getText(), PanelWorkbench.getOwnerFromConfigManager());
//        }
        return newUnit;
    }

    private String trasfotmExamplesToString(final Object[] examples) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < examples.length; i++) {
            sb.append(examples[i]);
            sb.append("|");
        }
        return sb.toString();
    }

    static public Sense showModal(Workbench workbench, PartOfSpeech newPos) {
        return showModal(workbench, workbench.getFrame(), null, newPos, DomainManager.getInstance().getById(0));
    }

    static public Sense showModal(Workbench workbench, String word, PartOfSpeech newPos) {
        return showModal(workbench, workbench.getFrame(), word, newPos, DomainManager.getInstance().getById(0));
    }

    /**
     * wyswietlenie okienka dialogowego
     *
     * @param workbench - srodowisko
     * @param frame
     * @param word
     * @param newPos    - pos dla jednostki, null gdy mozna go wybrac
     * @param domain
     * @return nowa jednostka lub null gdy anulowano
     */
    static public Sense showModal(Workbench workbench, WebFrame frame,
                                  String word, PartOfSpeech newPos, Domain domain) {

        NewLexicalUnitFrame modalFrame = new NewLexicalUnitFrame(workbench, frame);
        modalFrame.editPanel.refreshData();

        modalFrame.editPanel.getLexicon().setSelectedIndex(1);
        if (word != null) {
            modalFrame.editPanel.getLemma().setText(word);
            modalFrame.editPanel.getLemma().setEditable(false);
        } else {
            modalFrame.editPanel.getLemma().setText("");
        }
        if (newPos != null) {
            modalFrame.editPanel.getPartOfSpeech().setSelectedItem(newPos);
        } else if (lastPickPos != null) {
            modalFrame.editPanel.getPartOfSpeech().setSelectedItem(
                    new CustomDescription<>(lastPickPos.toString(), lastPickPos));
        }

        if (lastPickDomain != null) {
            modalFrame.editPanel.getDomain().setSelectedItem(
                    new CustomDescription<>(DomainMComboBox.nameWithoutPrefix(lastPickDomain.toString()), lastPickDomain));
        }

        modalFrame.editPanel.getRegister().setSelectedItem(RegisterTypes.OG);

        modalFrame.setVisible(true);
        Sense newUnit = null;
        if (modalFrame.wasAddClicked) {
            newUnit = modalFrame.saveAndReturnNewSense();
        }

        modalFrame.dispose();
        modalFrame = null;
        return newUnit;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == editPanel.getBtnSave()) {

            String testLemma = editPanel.getLemma().getText();

            List<Sense> units = LexicalDA.getFullLexicalUnits(testLemma, LexiconManager.getInstance().getUserChosenLexiconsIds());

            if (validateSelections()) {
                if (checkUnitExists(testLemma, units)) {
                    wasAddClicked = true;
                    setVisible(false);
                }
            }

            lastPickDomain = editPanel.getDomain().getEntity();
            lastPickPos = editPanel.getPartOfSpeech().getEntity();

        } else if (event.getSource() == editPanel.getBtnCancel()) {
            setVisible(false);
        }
    }

    private boolean checkUnitExists(String testLemma, List<Sense> units) {
        if (units != null && units.size() > 0) {

            Domain testDomain = editPanel.getDomain().getEntity();
            PartOfSpeech testPos = editPanel.getPartOfSpeech().getEntity();
            String testComments = editPanel.getComment().getText();

//            for (Sense unit : units) {
//                String comment = Common.getSenseAttribute(unit, Sense.COMMENT);
//                // check domain exists
//                if (testLemma.equals(unit.getLemma().getWord())
//                        && unit.getDomain().getId().equals(testDomain.getId())
//                        && unit.getPartOfSpeech().equals(testPos)
//                        && testComments.equals(comment)) {
//                    this.setAlwaysOnTop(false);
//                    DialogBox.showError(Messages.FAILURE_UNIT_EXISTS);
//                    this.setAlwaysOnTop(true);
//                    return false;
//                }
//            }
        }
        return true;
    }

    private boolean validateSelections() {
        if (editPanel.getLemma().getText() == null || "".equals(editPanel.getLemma().getText())) {
            DialogBox.showError(Messages.SELECT_LEMMA);
            return false;
        }
        if (editPanel.getLexicon().getEntity() == null) {
            DialogBox.showError(Messages.SELECT_LEXICON);
            return false;
        }
        if (editPanel.getPartOfSpeech().getEntity() == null) {
            DialogBox.showError(Messages.SELECT_POS);
            return false;
        }
        if (editPanel.getDomain().getEntity() == null) {
            DialogBox.showError(Messages.SELECT_DOMAIN);
            return false;
        }
        return true;
    }
}
