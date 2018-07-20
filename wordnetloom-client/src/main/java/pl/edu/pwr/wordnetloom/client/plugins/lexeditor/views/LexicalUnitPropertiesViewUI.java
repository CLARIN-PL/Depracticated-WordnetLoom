package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import com.alee.laf.panel.WebPanel;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.events.SearchUnitsEvent;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.EmotionsPropertiesPanel;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.LexicalUnitPropertiesPanel;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events.UpdateGraphEvent;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events.UpdateSynsetUnitsEvent;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * klasa opisujacy wyglada okienka z własciwoścami danej jednostki leksykalnej
 */
public class LexicalUnitPropertiesViewUI extends AbstractViewUI implements Loggable {

    private final ViwnGraphViewUI graphUI;
    private LexicalUnitPropertiesPanel editPanel;

    private WebPanel content;

    public LexicalUnitPropertiesViewUI(ViwnGraphViewUI graphUI) {
        this.graphUI = graphUI;
    }

    @Override
    public void initialize(final WebPanel content) {
        this.content = content;
        editPanel = new LexicalUnitPropertiesPanel(graphUI.getWorkbench().getFrame());
        content.add(editPanel);
    }

    public void fillKPWrExample(Object[] examples) {
        if (examples == null) {
            return;
        }
        for (int i = 0; i < examples.length; i++) {
//            editPanel.getExamplesModel().addElement(examples[i]);
        }
//        editPanel.getBtnSave().setEnabled(editPanel.isPermissionToEdit());
    }

    public void refreshData(Sense unit) {
//        editPanel.setSense(unit);
    }

    public void closeWindow(ActionListener close) {

//        editPanel.getBtnCancel().addActionListener(close);
    }

    public void saveChangesInUnit() {
        if (validateSelections()) {
            try {
                SenseAttributes savedAttributes = saveSenseAttributes();
                refreshData(savedAttributes.getSense());
                sendUpdateUnitEvents(savedAttributes, savedAttributes.getSense());
                setVariantInEditPanel(savedAttributes);
            } catch (Exception e) {
                logger().error("Number format", e);
                DialogBox.showError(Messages.ERROR_WRONG_NUMBER_FORMAT);
            }
        }
    }

    private void setVariantInEditPanel(SenseAttributes savedAttributes) {
//        editPanel.getVariant().setText("" + savedAttributes.getSense().getVariant());
    }

    private SenseAttributes saveSenseAttributes() {
//        Sense sense = editPanel.updateAndGetSense();
//        SenseAttributes attributes = editPanel.getSenseAttributes(sense.getId());
//        attributes.setSense(sense);
//        return RemoteService.senseRemote.save(attributes);
        return null;
    }

    /**
     * When unit is saved, we update:
     * - graph
     * - units in synset
     * - search results units
     *
     * @param savedAttributes
     * @param savedSense
     */
    private void sendUpdateUnitEvents(SenseAttributes savedAttributes, Sense savedSense) {
        Application.eventBus.post(new UpdateGraphEvent(savedAttributes.getSense()));
        Synset synset = RemoteService.synsetRemote.findSynsetBySense(savedSense, LexiconManager.getInstance().getUserChosenLexiconsIds());
        Application.eventBus.post(new UpdateSynsetUnitsEvent(synset));
        Application.eventBus.post(new SearchUnitsEvent());
    }

    @Override
    public JComponent getRootComponent() {
//        if (editPanel == null) {
//            return editPanel.getLemma();
//        }
//        return editPanel.getLemma();
        return null;
    }

    private boolean validateSelections() {
//        if (editPanel.getLemma().getText() == null || "".equals(editPanel.getLemma().getText())) {
//            DialogBox.showError(Messages.SELECT_LEMMA);
//            return false;
//        }
//        if (editPanel.getLexicon().getEntity() == null) {
//            DialogBox.showError(Messages.SELECT_LEXICON);
//            return false;
//        }
//        if (editPanel.getPartOfSpeech().getEntity() == null) {
//            DialogBox.showError(Messages.SELECT_POS);
//            return false;
//        }
//        if (editPanel.getDomain().getEntity() == null) {
//            DialogBox.showError(Messages.SELECT_DOMAIN);
//            return false;
//        }
//        return true;
        return false;
    }
}
