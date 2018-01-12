package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames;

import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RelationTypeFrame extends DialogWindow implements ActionListener, KeyListener {

    protected static final long serialVersionUID = 1L;

    protected MComboBox relationType;
    protected MComboBox relationSubType;
    protected MButton buttonChoose, buttonCancel;
    protected MTextArea description;
    protected JList testsLit;
    protected static MComboBox parentItem;
    protected MComboBox middleItem;
    protected static MComboBox childItem;
    protected RelationType fixedRelationType;
    protected RelationType chosenType = null;
    protected List<RelationType> mainRelations = null;
    protected Collection<RelationType> subRelations = null;
    protected static PartOfSpeech pos;

    private class PartOfSpeechRenderer implements ListCellRenderer {

        protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if(value != null){
                RelationType relationType = (RelationType)value;
                String text = LocalisationManager.getInstance().getLocalisedString(relationType.getName());
                renderer.setText(text);
            }
            return renderer;
        }
    }

    /**
     * konstruktor
     *
     * @param frame             - srodowisko
     * @param type              - typ relacji
     * @param pos               - czesc mowy
     * @param fixedRelationType - typ relacji ustawiony na sztywno
     * @param parentUnits       - jednostki podzedne
     * @param middleUnits       - jednostki pośrednie
     * @param childUnits        - jednostki nadrzedne
     */
    private RelationTypeFrame(WebFrame frame,
                              String type,
                              PartOfSpeech pos,
                              RelationType fixedRelationType,
                              RelationType suggestedRelationType,
                              Sense suggestedUnit,
                              Collection<Sense> parentUnits,
                              Collection<Sense> middleUnits,
                              Collection<Sense> childUnits) {
        super(frame, Labels.RELATION_PARAMS, 650, 500);
        RelationTypeFrame.pos = pos;
        this.fixedRelationType = fixedRelationType;

        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //this.setAlwaysOnTop(true);

        // element nadrzedny
        parentItem = new MComboBox();
        for (Sense parent : parentUnits) {
            parentItem.addItem(parent.getWord().getWord());
        }

        // element podrzedny
        childItem = new MComboBox();
        for (Sense child : childUnits) {
            childItem.addItem(child.getWord().getWord());
        }
        //Wybranie zaproponowanej jednostki
        if (suggestedUnit != null) {

            childItem.setSelectedItem(suggestedUnit.getWord().getWord());
            parentItem.setSelectedItem(suggestedUnit.getWord().getWord());
        }

        // element posredni
        middleItem = new MComboBox();
        if (middleUnits != null && !middleUnits.isEmpty()) {
            for (Sense middle : middleUnits) {
                middleItem.addItem(middle.getWord());
            }
        } else {
            middleItem.setEnabled(false);
        }

        // opis relacji
        description = new MTextArea("");
        description.setRows(6);
        description.setEditable(false);

        // lista testow
        testsLit = new JList();

        // podtyp relacji
        relationSubType = new MComboBox();
        relationSubType.addKeyListener(this);
        relationSubType.setEnabled(false);
        relationSubType.setRenderer(new PartOfSpeechRenderer());

        // typ relacji
        relationType = new MComboBox();
        relationType.addKeyListener(this);
        relationType.setRenderer(new PartOfSpeechRenderer());

        // wyswietlenie relacji
        mainRelations = new ArrayList<>();
        mainRelations = RelationTypeManager.getInstance().getParents(RelationArgument.SYNSET_RELATION);
        for(RelationType relType : mainRelations) {
            relationType.addItem(relType);
        }
//        Collection<IRelationType> readRelations = LexicalDA.getHighestRelations(type, pos);
//        for (IRelationType relType : readRelations) {
//            relType = LexicalDA.getEagerRelationTypeByID(relType);
//            if (fixedRelationType == null
//                    || relType.getId().longValue() == fixedRelationType.getId().longValue()
//                    || (fixedRelationType.getParent() != null
//                    && relType.getId().longValue() == fixedRelationType.getParent().getId())) {
//                relationType.addItem(RelationTypeManager.getFullNameFor(relType.getId()));
//                mainRelations.add(relType);
//            }
//        }

        // przycisk wybierz
        buttonChoose = MButton.buildSelectButton()
                .withMnemonic(KeyEvent.VK_ENTER)
                .withKeyListener(this)
                .withActionListener(this);

        buttonCancel = MButton.buildCancelButton()
                .withMnemonic(KeyEvent.VK_A)
                .withActionListener(this)
                .withKeyListener(this);

        relationSubType.addActionListener(this);
        relationType.addActionListener(this);
        // czy sa jakieś relacje
//        if (mainRelations.size() > 0) {
//            //Ustawienie na sugestię, jeśli istnieje
//            if (suggestedRelationType != null) {
//                if (suggestedRelationType.getParent() == null) { //TODO
//                    relationType.setSelectedItem(RelationTypeManager.getInstance().getFullName(suggestedRelationType.getId(), RelationArgument.SENSE_RELATION));
//                } else {
//                    relationType.setSelectedItem(RelationTypeManager.getInstance().getFullName(suggestedRelationType.getParent().getId(), RelationArgument.SENSE_RELATION));
//                    relationSubType.setSelectedItem(RelationTypeManager.getInstance().getFullName(suggestedRelationType.getId(), RelationArgument.SENSE_RELATION));
//                }
//            } else {
//                relationType.setSelectedIndex(0);
//            }
//            buttonChoose.setEnabled(true);
//        } else {
//            buttonChoose.setEnabled(false);
//        }

        // dopisanie zdarzen
        parentItem.addKeyListener(this);
        parentItem.addActionListener(this);
        middleItem.addKeyListener(this);
        middleItem.addActionListener(this);
        childItem.addKeyListener(this);
        childItem.addActionListener(this);

        // dodanie elemetow UI do okna
        add("", new MLabel(Labels.RELATION_TYPE_COLON, 't', relationType));
        add("tab hfill", relationType);
        add("br", new MLabel(Labels.RELATION_SUBTYPE_COLON, 'y', relationType));
        add("tab hfill", relationSubType);
        add("br", new MLabel(Labels.RELATION_DESC_COLON, '\0', description));
        add("br hfill", new JScrollPane(description));

        add("br", new MLabel(Labels.SOURCE_UNIT_COLON, 'r', parentItem));
        add("tab hfill", parentItem);

        if (middleItem.isEnabled()) {
            add("br", new MLabel(Labels.INTERMEDIATE_UNIT_COLON, 'p', parentItem));
            add("tab hfill", middleItem);
        }
        add("br", new MLabel(Labels.TARGET_UNIT_COLON, 'd', childItem));
        add("tab hfill", childItem);
        add("br", new MLabel(Labels.TESTS_COLON, '\0', testsLit));
        add("br hfill vfill", new JScrollPane(testsLit));
        add("br center", buttonChoose);
        add("", buttonCancel);
    }

    /**
     * @param frame             parent component
     * @param type              relation type
     * @param pos               part of speech
     * @param fixedRelationType fixed relation type
     * @author amusial Constructor for derived classes
     */
    protected RelationTypeFrame(WebFrame frame,
                                String type,
                                PartOfSpeech pos,
                                RelationType fixedRelationType) {
        super(frame, Labels.RELATION_PARAMS, 650, 500);
        RelationTypeFrame.pos = pos;
        this.fixedRelationType = fixedRelationType;

        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * wyswietlenie okienka dialogowego
     *
     * @param workbench   - srodowisko
     * @param type        - typ relacji
     * @param pos         - czesc mowy dla ktorej ma byc brana relacja
     * @param parentUnits - jednostki nadrzedne
     * @param childUnits  - jednostki podrzedne
     * @return typ relacji albo null
     */
    public static RelationType showModal(Workbench workbench,
                                         String type,
                                         PartOfSpeech pos,
                                         Collection<Sense> parentUnits,
                                         Collection<Sense> childUnits) {
        return showModal(workbench.getFrame(), type, pos, null, null, null, parentUnits, null, childUnits);
    }

    /**
     * @param workbench
     * @param type
     * @param pos
     * @param fixedRelationType
     * @param parentUnits
     * @param middleUnits
     * @param childUnits
     * @return typ relacji albo null
     */
    public static RelationType showModal(Workbench workbench,
                                         String type,
                                         PartOfSpeech pos,
                                         RelationType fixedRelationType,
                                         Collection<Sense> parentUnits,
                                         Collection<Sense> middleUnits,
                                         Collection<Sense> childUnits) {
        return showModal(workbench.getFrame(), type, pos, fixedRelationType, null, null, parentUnits, middleUnits, childUnits);
    }

    /**
     * konwersja jednostki na liste
     *
     * @param unit - jednostka
     * @return lista zawierajaca jednostek
     */
    public static Collection<Sense> unitToList(Sense unit) {
        Collection<Sense> list = new ArrayList<>();
        list.add(unit);
        return list;
    }

    /**
     * wyswietlenie okienka dialogowego
     *
     * @param frame                 - srodowisko
     * @param type                  - typ relacji
     * @param pos                   - czesc mowy dla ktorej ma byc brana relacja
     * @param relationType          - typ relacji jesli jest ustawiona na sztywno
     * @param suggestedRelationType - typ sugerowanej relacji, o ile istnieje
     * @param suggestedUnit
     * @param parentUnits           - jednostki nadrzedne
     * @param middleUnits           - jednostki posrednie
     * @param childUnits            - jednostki podrzedne
     * @return typ relacji albo null
     */
    public static RelationType showModal(WebFrame frame,
                                         String type,
                                         PartOfSpeech pos,
                                         RelationType relationType,
                                         RelationType suggestedRelationType,
                                         Sense suggestedUnit,
                                         Collection<Sense> parentUnits,
                                         Collection<Sense> middleUnits,
                                         Collection<Sense> childUnits) {
        RelationTypeFrame framew = new RelationTypeFrame(frame, type, pos, relationType, suggestedRelationType, suggestedUnit, parentUnits, middleUnits, childUnits);
        framew.setVisible(true);
        return framew.chosenType;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                event.consume();
                setVisible(false);
                break;
        }
    }

    /**
     * odczytanie zaznaczonej relacji
     *
     * @return zaznaczona relacja
     */
    protected RelationType getSelectedRelation() {
//        if (subRelations != null && subRelations.size() > 0) {
//            // jest pod typ
//            int index = relationSubType.getSelectedIndex();
//            for (RelationType type : subRelations) {
//                if (index-- == 0) {
//                    return type;
//                }
//            }
//        } else {
//            // brak podtypu
//            int index = relationType.getSelectedIndex();
//            for (RelationType type : mainRelations) {
//                if (index-- == 0) {
//                    return type;
//                }
//            }
//        }
//        return null;

        if(relationSubType.isEnabled()) { // relacja ma podtypy
           return (RelationType)relationSubType.getSelectedItem();
        } else {
            return (RelationType)relationType.getSelectedItem();
        }
    }

    /**
     * wczytanie testow dla podanej relacji
     *
     * @param type - typ relacji
     */
    protected void loadTests(RelationType type) {
        if (middleItem.getItemCount() == 0) {
            int a = parentItem.getSelectedIndex();
            int b = childItem.getSelectedIndex();

            if (a < 0 || b < 0) {
                return;
            }

//            List<String> tests = LexicalDA.getTests(type,
//                    (parentItem.getItemAt(parentItem.getSelectedIndex())).toString(),
//                    (childItem.getItemAt(childItem.getSelectedIndex())).toString(),
//                    pos);
//            testsLit.setListData(tests.toArray(new String[]{}));
//        } else {
//            List<String> tests = LexicalDA.getTests(type,
//                    (parentItem.getItemAt(parentItem.getSelectedIndex())).toString(),
//                    middleItem.getItemAt(middleItem.getSelectedIndex()).toString(),
//                    pos);
//            tests.addAll(LexicalDA.getTests(type,
//                    (parentItem.getItemAt(parentItem.getSelectedIndex())).toString(),
//                    (childItem.getItemAt(childItem.getSelectedIndex())).toString(),
//                    pos));
//
//            testsLit.setListData(tests.toArray(new String[]{}));
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if(event.getSource() == buttonChoose) {
            chosenType = getSelectedRelation();
            this.setVisible(false);
        } else if(event.getSource() == buttonCancel) {
            this.setVisible(false);
        } else if(event.getSource() == relationType) {
            relationSubType.removeAllItems();
            description.setText("");
            testsLit.setListData(new String[]{});

            RelationType parentRelation = (RelationType)relationType.getSelectedItem();
            List<RelationType> relationsSubType = RelationTypeManager.getInstance().getChildren(parentRelation.getId(), RelationArgument.SYNSET_RELATION);
            if(!relationsSubType.isEmpty()) {
                for(RelationType relType : relationsSubType){
                    relationSubType.addItem(relType);
                }
                relationSubType.setEnabled(true);
            } else {
                relationSubType.setEnabled(false);
            }
        }
//        } else if (event.getSource() == relationType) {
//            relationSubType.removeAllItems();
//            description.setText("");
//            testsLit.setListData(new String[]{});
//
//            int index = relationType.getSelectedIndex();
//            for (IRelationType type : mainRelations) {
//
//                if (index-- == 0) {
//
//                    subRelations = new ArrayList<>();
//                    Collection<IRelationType> readRelations = LexicalDA.getChildren(type);
//
//                    for (IRelationType relType : readRelations) {
//                        if (fixedRelationType == null || fixedRelationType.getId().longValue() == relType.getId().longValue()) {
//                            relationSubType.addItem(RelationTypeManager.getFullNameFor(relType.getId()));
//                            subRelations.add(relType);
//                        }
//                    }
//                    if (subRelations.size() > 0) {
//                        relationSubType.setSelectedIndex(0);
//                    } else {
//                        loadTests(type);
//                    }
//                    description.setText(type.getDescription().getText());
//                    break;
//                }
//            }
//            relationSubType.setEnabled(subRelations != null && subRelations.size() > 0);
//
//        } else if (event.getSource() == relationSubType || event.getSource() == parentItem || event.getSource() == childItem || event.getSource() == middleItem) {
//
//            testsLit.setListData(new String[]{});
//            IRelationType relation = getSelectedRelation();
//            if (relation != null) {
//                loadTests(relation);
//            }
//        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }
}
