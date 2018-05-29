package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.components;

import com.alee.extended.colorchooser.ColorChooserFieldType;
import com.alee.extended.colorchooser.WebColorChooserField;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebFormattedTextField;
import com.google.common.eventbus.Subscribe;
import org.jdesktop.swingx.HorizontalLayout;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.lexicon.window.LexiconsWindow;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.events.ShowRelationTestsEvent;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.events.ShowRelationTypeEvent;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window.PartOfSpeechWindow;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window.ReverseRelationWindow;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.util.*;

public class RelationTypePropertiesPanel extends WebPanel implements Loggable {

    private RelationType currentRelation;
    private WebFrame parent;

    public RelationTypePropertiesPanel(WebFrame parent) {
        this.parent = parent;
        Application.eventBus.register(this);
        init();
    }

    private void init() {
        setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Relation properties", TitledBorder.LEADING,
                TitledBorder.TOP, null, new Color(51, 51, 51)));

        WebScrollPane descriptionScrollWrapper = new WebScrollPane(relationDescription);

        setLayout(new RiverLayout());

        colorChooser.setPipetteEnabled(false);
        colorChooser.setFieldType(ColorChooserFieldType.hex);

        lexicon.setFocusable(false);
        allowedPartsOfSpeech.setFocusable(false);
        reverseRelation.setFocusable(false);

        Map<String, Component> components = new LinkedHashMap<>();
        components.put(Labels.RELATION_NAME_COLON, relationName);
        components.put(Labels.SHORTCUT_COLON, relationShortcut);
        components.put(Labels.DISPALY_FORM_COLON, relationDisplay);
        components.put(Labels.DESCRIPTION_COLON, descriptionScrollWrapper);

        components.put(Labels.LEXICON_COLON, new MComponentGroup(lexicon, lexiconBtn)
                .withHorizontalLayout()
                .withFirstElementSize(new Dimension(220, 25)));

        components.put(Labels.MULTILINGUAL, multilingual);

        components.put(Labels.PARTS_OF_SPEECH_COLON, new MComponentGroup(allowedPartsOfSpeech, showAllowedPartsOfSpeechBtn)
                .withHorizontalLayout()
                .withFirstElementSize(new Dimension(220, 25)));

        components.put(Labels.REVERSE_RELATION, new MComponentGroup(reverseRelation,reverseRelationBtn)
                .withHorizontalLayout()
                .withFirstElementSize(new Dimension(220, 25)));

        components.put(Labels.COLOR_COLON, colorChooser);
        components.put(Labels.DIRECTION_COLON, relationDirection);

        add("", GroupView.createGroupView(components, null, 0.1f,0.9f));

        add(RiverLayout.LINE_BREAK + " " + RiverLayout.CENTER, btnSave);
    }

    @Subscribe
    public void onShowRelationType(ShowRelationTypeEvent event) {
        currentRelation = RemoteService.relationTypeRemote.findByIdWithDependencies(event.getRelationType().getId());
        Application.eventBus.post(new ShowRelationTestsEvent(currentRelation));
        bind(currentRelation);
    }

    public void lock(){
        setEnableComponents(false);
    }

    public void unlock(){
        setEnableComponents(true);
    }

    private void setEnableComponents(boolean enable) {
        relationName.setEnabled(enable);
        relationDisplay.setEnabled(enable);
        relationShortcut.setEnabled(enable);
        relationDescription.setEnabled(enable);
        lexicon.setEnabled(enable);
        lexiconBtn.setEnabled(enable);
        multilingual.setEnabled(enable);
        allowedPartsOfSpeech.setEnabled(enable);
        showAllowedPartsOfSpeechBtn.setEnabled(enable);
        reverseRelation.setEnabled(enable);
        reverseRelationBtn.setEnabled(enable);
        colorChooser.setEnabled(enable);
        relationDirection.setEnabled(enable);
    }

    private void bind(RelationType rt) {
        unlock();
        relationName.setText(LocalisationManager.getInstance().getLocalisedString(rt.getName()));
        relationDisplay.setText(LocalisationManager.getInstance().getLocalisedString(rt.getDisplayText()));
        relationShortcut.setText(LocalisationManager.getInstance().getLocalisedString(rt.getShortDisplayText()));
        relationDescription.setText(LocalisationManager.getInstance().getLocalisedString(rt.getDescription()));
        relationDirection.setSelectedItem(rt.getNodePosition());
        colorChooser.setColor(Color.decode(rt.getColor() != null ? rt.getColor() : "#FFFFFF"));
        multilingual.setSelected(rt.getMultilingual());
        reverseRelation.setText(rt.getReverse() != null ? LocalisationManager.getInstance().getLocalisedString(rt.getReverse().getName()) : "");
        lexicon.setText(LexiconManager.getInstance().lexiconNamesToString(rt.getLexicons()));
        allowedPartsOfSpeech.setText(PartOfSpeechManager.getInstance().partsOfSpeechToString(rt.getPartsOfSpeech()));
    }

    private void save(){
        if(currentRelation != null){
            LocalisationManager.getInstance().updateLocalisedString(currentRelation.getName(), relationName.getText());
            LocalisationManager.getInstance().updateLocalisedString(currentRelation.getDisplayText(), relationDisplay.getText());
            LocalisationManager.getInstance().updateLocalisedString(currentRelation.getShortDisplayText(), relationShortcut.getText());
            LocalisationManager.getInstance().updateLocalisedString(currentRelation.getDescription(), relationDescription.getText());

            String hex = "#"+Integer.toHexString(colorChooser.getColor().getRGB()).substring(2);
            currentRelation.setColor(hex.toUpperCase());
            currentRelation.setMultilingual(multilingual.isSelected());
            currentRelation.setNodePosition(relationDirection.getEntity());

            RemoteService.relationTypeRemote.save(currentRelation);
        }
    }

    private void openReverseRelationDialog() {

        Pair<RelationType, Boolean> r = ReverseRelationWindow.showModal(
                parent,
                currentRelation.getReverse(),
                currentRelation.isAutoReverse(),
                RelationTypeManager.getInstance().getParents(currentRelation.getRelationArgument()));

        currentRelation.setReverse(r.getA());
        currentRelation.setAutoReverse(r.getB());
        bind(currentRelation);
    }

    private void openPartOfSpeechDialog() {
        Set<PartOfSpeech> current = currentRelation != null ? currentRelation.getPartsOfSpeech() : new HashSet<>();

        final Set<PartOfSpeech> selected = PartOfSpeechWindow.showModal(parent, current);

        if (currentRelation != null) {
            currentRelation.setPartsOfSpeech(selected);
            allowedPartsOfSpeech.setText(PartOfSpeechManager.getInstance().partsOfSpeechToString(currentRelation.getPartsOfSpeech()));
        }
    }

    private void openLexiconDialog() {
        Set<Lexicon> lexicons = currentRelation != null ? currentRelation.getLexicons() : new HashSet<>();

        final Set<Lexicon> selected = LexiconsWindow.showModal(parent, lexicons);

        if (currentRelation != null) {
            currentRelation.setLexicons(selected);
            lexicon.setText(LexiconManager.getInstance().lexiconNamesToString(currentRelation.getLexicons()));
        }
    }

    private final MButton btnSave = MButton.buildSaveButton()
            .withActionListener(e -> save());

    private final MTextField lexicon = new MTextField("");

    private final MButton lexiconBtn = new MButton()
            .withActionListener(e -> openLexiconDialog())
            .withCaption("...");

    private final MTextField relationName = new MTextField("");

    private final MTextField relationDisplay = new MTextField("");

    private final MTextField relationShortcut = new MTextField("");

    private final MTextArea relationDescription = new MTextArea("")
            .withRows(4);

    private final MTextField allowedPartsOfSpeech = new MTextField("");

    private final WebCheckBox multilingual = new WebCheckBox();

    private final MButton showAllowedPartsOfSpeechBtn = new MButton()
            .withCaption("...")
            .withToolTip(Hints.CHOOSE_POS_FOR_RELATION)
            .withActionListener(e -> openPartOfSpeechDialog());

    private final MTextField reverseRelation = new MTextField(Labels.NO_VALUE);

    private final MButton reverseRelationBtn = new MButton()
            .withCaption("...")
            .withActionListener(e -> openReverseRelationDialog())
            .withToolTip(Hints.CHOOSE_REVERSE_RELATION);

    private final MComboBox<NodeDirection> relationDirection = new MComboBox<>(NodeDirection.values());
    private final WebColorChooserField colorChooser = new WebColorChooserField();

}
