package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.components;

import com.alee.extended.colorchooser.ColorChooserFieldType;
import com.alee.extended.colorchooser.WebColorChooserField;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebFormattedTextField;
import com.google.common.eventbus.Subscribe;
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
import java.util.HashSet;
import java.util.Set;

public class RelationTypePropertiesPanel extends WebPanel implements Loggable {

    private static final String LINE_BREAK = RiverLayout.LINE_BREAK;
    private static final String LEFT_BREAK = RiverLayout.LINE_BREAK + " " + RiverLayout.LEFT;
    private static final String LINE_FILL = RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL;
    private static final String TAB_FILL = RiverLayout.TAB_STOP + " " + RiverLayout.HFILL;
    private static final String RIGHT = RiverLayout.RIGHT;

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

        add(LINE_BREAK + " " + RIGHT, nameLabel);
        add(TAB_FILL, relationName);
        add(LINE_BREAK, displayLabel);
        add(TAB_FILL, relationDisplay);
        add(LINE_BREAK, shortcutLabel);
        add(TAB_FILL, relationShortcut);
        add(LINE_BREAK, descriptionLabel);
        add(TAB_FILL, descriptionScrollWrapper);
        add(LINE_BREAK, lexiconLabel);
        add(TAB_FILL, lexicon);
        add(RIGHT, lexiconBtn);
        add(LINE_BREAK, multilingualLabel);
        add(TAB_FILL, multilingual);
        add(LINE_BREAK, posLabel);
        add(TAB_FILL, allowedPartsOfSpeech);
        add(RIGHT, showAllowedPartsOfSpeechBtn);
        add(LINE_BREAK, reverseLabel);
        add(TAB_FILL, reverseRelation);
        add(RIGHT, reverseRelationBtn);
        add(LINE_BREAK, colorLabel);
        add(TAB_FILL, colorChooser);
        add(LINE_BREAK, directionLabel);
        add(TAB_FILL, relationDirection);
        add(RiverLayout.LINE_BREAK + " " + RiverLayout.CENTER, btnSave);
    }

    @Subscribe
    public void onShowRelationType(ShowRelationTypeEvent event) {
        currentRelation = RemoteService.relationTypeRemote.findByIdWithDependencies(event.getRelationType().getId());
        Application.eventBus.post(new ShowRelationTestsEvent(currentRelation));
        bind(currentRelation);
    }

    private void bind(RelationType rt) {
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

    private MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            logger().error("Formatter color parse error:", exc);
        }
        return formatter;
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

    private final MComboBox<NodeDirection> relationDirection = new MComboBox<>(NodeDirection.values())
            .withSize(new Dimension(150, 25));

    private final WebFormattedTextField colorRelation = new WebFormattedTextField(createFormatter("#HHHHHH"));

    private final MLabel lexiconLabel = new MLabel(Labels.LEXICON_COLON)
            .withMnemonic('l')
            .withAlignment(SwingConstants.RIGHT)
            .withSize(new Dimension(130, 15))
            .withLabelFor(lexicon);

    private final MLabel nameLabel = new MLabel(Labels.RELATION_NAME_COLON)
            .withMnemonic('n')
            .withAlignment(SwingConstants.RIGHT)
            .withSize(new Dimension(130, 15))
            .withLabelFor(relationName);

    private final MLabel displayLabel = new MLabel(Labels.DISPALY_FORM_COLON)
            .withMnemonic('f')
            .withAlignment(SwingConstants.RIGHT)
            .withSize(new Dimension(130, 15))
            .withLabelFor(relationDisplay);

    private final MLabel shortcutLabel = new MLabel(Labels.SHORTCUT_COLON)
            .withMnemonic('s')
            .withAlignment(SwingConstants.RIGHT)
            .withSize(new Dimension(130, 15))
            .withLabelFor(relationShortcut);

    private final MLabel descriptionLabel = new MLabel(Labels.DESCRIPTION_COLON)
            .withMnemonic('o')
            .withAlignment(SwingConstants.RIGHT)
            .withSize(new Dimension(130, 15))
            .withLabelFor(relationDescription);

    private final MLabel posLabel = new MLabel(Labels.PARTS_OF_SPEECH_COLON)
            .withMnemonic('c')
            .withAlignment(SwingConstants.RIGHT)
            .withSize(new Dimension(130, 15))
            .withLabelFor(allowedPartsOfSpeech);

    private final MLabel reverseLabel = new MLabel(Labels.REVERSE_RELATION)
            .withMnemonic('\0')
            .withAlignment(SwingConstants.RIGHT)
            .withSize(new Dimension(130, 15))
            .withLabelFor(reverseRelation);

    private final MLabel colorLabel = new MLabel(Labels.COLOR_COLON)
            .withMnemonic('q')
            .withAlignment(SwingConstants.RIGHT)
            .withSize(new Dimension(130, 15))
            .withLabelFor(colorRelation);

    private final MLabel directionLabel = new MLabel()
            .withMnemonic('d')
            .withAlignment(SwingConstants.RIGHT)
            .withSize(new Dimension(130, 15))
            .withCaption(Labels.DIRECTION_COLON)
            .withLabelFor(relationDirection);

    private final MLabel multilingualLabel = new MLabel(Labels.MULTILINGUAL)
            .withMnemonic('m')
            .withAlignment(SwingConstants.RIGHT)
            .withSize(new Dimension(130, 15))
            .withLabelFor(multilingual);

    private final WebColorChooserField colorChooser = new WebColorChooserField();

}
