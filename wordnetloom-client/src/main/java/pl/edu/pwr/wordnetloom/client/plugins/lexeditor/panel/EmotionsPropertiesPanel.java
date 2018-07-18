package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComponentGroup;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.dictionary.model.Emotion;
import pl.edu.pwr.wordnetloom.dictionary.model.Valuation;
import pl.edu.pwr.wordnetloom.sense.model.EmotionalAnnotation;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.UnitEmotion;
import pl.edu.pwr.wordnetloom.sense.model.UnitValuation;
import pl.edu.pwr.wordnetloom.user.model.User;
import se.datadosen.component.RiverLayout;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static se.datadosen.component.RiverLayout.*;

public class EmotionsPropertiesPanel extends JPanel {

    // TODO dorobić etykiety
    private static final String LABEL_NACECHOWANIE = "Nacechowanie:";
    private static final String LABEL_EXAMPLE = "Przykłady:";
    private static final String LABEL_NEUTRAL = "Neutralny";
    private static final String LABEL_MARKED = "Nacechowany";
    private static final String LABEL_EMOTIONS = "Emocje:";
    private static final String LABEL_VALUATIONS = "Wartościowanie:";
    private static final String LABEL_STATUS = "Status jednostki:";
    private static final String LABEL_OWNER = "Właściciel:";

    private List<JCheckBox> emotionBox = new ArrayList<>();
    private List<JCheckBox> valutionsBox = new ArrayList<>();

    private JButton saveButton;
    private JRadioButton neutralRadio;
    private JRadioButton notMarkedRadio;
    private JLabel ownerLabel;
    private JComboBox markednesCombo;
    private JTextArea example1;
    private JTextArea example2;

    EmotionsListPanel listPanel;

    private EmotionalAnnotation editedAnnotation;

    private Map<Long, JCheckBox> emotionsMap = new HashMap<>();
    private Map<Long, JCheckBox> valuatesMap = new HashMap<>();

    private void initComponents(){
        final int TEXT_AREA_ROWS = 2;

        neutralRadio = new JRadioButton(LABEL_NEUTRAL);
        neutralRadio.addActionListener(e->setEnableEditing());
        notMarkedRadio = new JRadioButton(LABEL_MARKED);
        notMarkedRadio.addActionListener(e->setEnableEditing());
        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(neutralRadio);
        statusGroup.add(notMarkedRadio);

        ownerLabel = new JLabel("");
        markednesCombo = new JComboBox();
        example1 = new JTextArea(); // TOOD dodać caretListener
        example1.setRows(TEXT_AREA_ROWS);
        example2 = new JTextArea();
        example2.setRows(TEXT_AREA_ROWS);

        setEnableEditing(false);
    }

    private void setEnableEditing() {
        boolean enabled = notMarkedRadio.isSelected();
        setEnableEditing(enabled);
        if(!enabled){
            clearFields();
        } else {
            loadAnnotation(editedAnnotation);
        }
    }

    private void setEnableEditing(boolean enabled) {
        emotionsMap.forEach((k, emotionCheckBox) -> emotionCheckBox.setEnabled(enabled));
        valuatesMap.forEach((k, valuationCheckBox) -> valuationCheckBox.setEnabled(enabled));
        markednesCombo.setEnabled(enabled);
        example1.setEnabled(enabled);
        example2.setEnabled(enabled);
    }

    private void clearFields() {
        emotionsMap.forEach((k, emotionCheckBox) -> emotionCheckBox.setSelected(false));
        valuatesMap.forEach((k, valuationCheckBox)->valuationCheckBox.setSelected(false));
        // TODO zrobić czyszczenie pola z wartościowaniem
        example1.setText("");
        example2.setText("");
    }

    private JPanel createStatusPanel() {
        final int WIDTH = 275;
        final int HEIGHT = 55;

        JPanel panel = new JPanel(new GridLayout(0,2));
        panel.add(neutralRadio);
        panel.add(notMarkedRadio);

        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setTitledBorder(LABEL_STATUS, panel);
        return panel;
    }

    private JPanel createOwnerPanel() {
        final int WIDTH = 275;
        final int HEIGHT = 55;

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(ownerLabel);

        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setTitledBorder(LABEL_OWNER, panel);
        return panel;
    }

    private JPanel createMarkednessPanel() {
        final int WIDTH = 560;
        final int HEIGHT = 60;

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(markednesCombo);

        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setTitledBorder(LABEL_MARKED, panel);
        return panel;
    }

    private JPanel createExamplesPanel() {
        final int WIDTH = 560;
        final int HEIGHT = 100;
        // TODO być może będzie trzeba zmienic układ
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JScrollPane(example1));
        panel.add(new JScrollPane(example2));

        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setTitledBorder(LABEL_EXAMPLE, panel);
        return panel;
    }

    private EmotionsListPanel createListPanel() {
        final int WIDTH = 560;
        final int HEIGHT = 100;
        EmotionsListPanel panel = new EmotionsListPanel();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // TODO dorobić etykietę
        setTitledBorder("Adnotacje", panel);
        return panel;
    }



    private void setTitledBorder(final String title, JPanel panel){
        panel.setBorder(new TitledBorder(title));
    }

    // TODO zamiast przycisku można przesłać zdarzenie
    public EmotionsPropertiesPanel(WebFrame frame){
        // TODO ustawianie odpowiedniego rozmiaru

        setLayout(new RiverLayout());

        initComponents();
        listPanel = createListPanel();
        JPanel statusPanel = createStatusPanel();
        JPanel ownerPanel = createOwnerPanel();
        JPanel emotionsPanel = createEmotionsPanel(getEmotions());
        JPanel valuationsPanel = createValuationsPanel(getValuations());
        JPanel markednessPanel = createMarkednessPanel();
        JPanel examplesPanel = createExamplesPanel();

        add(listPanel);
        add(LINE_BREAK, statusPanel);
        add(TAB_STOP, ownerPanel);
        add(LINE_BREAK, emotionsPanel);
        add(LINE_BREAK, valuationsPanel);
        add(LINE_BREAK, markednessPanel);
        add(LINE_BREAK, examplesPanel);

    }

    public void load(Sense sense) {
        listPanel.loadAnnotations(sense.getId());
    }

    private JPanel createEmotionsPanel(List<Emotion> emotions) {
        final int WIDTH = 560;
        final int HEIGHT = 100;
        final int COLUMNS  = 3;
        // TODO zmienić ten układ na coś bardziej dynamicznego
        JPanel panel = new JPanel(new GridLayout(0, COLUMNS, 0 ,3));

        for(Emotion emotion : emotions) {
            String emotionName = LocalisationManager.getInstance().getLocalisedString(emotion.getName());
            JCheckBox checkBox = new JCheckBox(emotionName);
            panel.add(checkBox);
            emotionsMap.put(emotion.getId(), checkBox);
        }

        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        setTitledBorder(LABEL_EMOTIONS, panel);
        return panel;
    }

    private JPanel createValuationsPanel(List<Valuation> valuations) {
        final int WIDTH = 560;
        final int HEIGHT = 120;
        final int COLUMNS = 3;

        JPanel panel = new JPanel(new GridLayout(0, COLUMNS));

        for(Valuation valuation : valuations){
            String valuationName = LocalisationManager.getInstance().getLocalisedString(valuation.getName());
            JCheckBox checkBox = new JCheckBox(valuationName);
            panel.add(checkBox);
            valuatesMap.put(valuation.getId(), checkBox);
        }

        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setTitledBorder(LABEL_VALUATIONS, panel);
        return panel;
    }

    public void loadAnnotation(EmotionalAnnotation annotation) {
        // TODO zrobić ładowanie anotacji
        this.editedAnnotation = annotation;
        setStatus(annotation);
        setOwner(annotation);
        setEmotions(annotation);
        setValuations(annotation);
        // TODO ładowanie nacechowania
        setEnableEditing(true);
    }

    // TODO być może będzie trzeba
    private void setStatus(EmotionalAnnotation annotation){
        if(annotation.hasEmotionalCharacteristic()) {
            notMarkedRadio.setSelected(true);
        } else {
            neutralRadio.setSelected(true);
        }
    }

    private void setOwner(EmotionalAnnotation annotation) {
        User owner = annotation.getOwner();
        String ownerName = owner.getFullname();
        ownerLabel.setText(ownerName);
    }

    private void setEmotions(EmotionalAnnotation annotation) {
        // clear emotions checkBoxes
        emotionsMap.forEach((k, emotionsCheckBox) -> emotionsCheckBox.setSelected(false));
        for(UnitEmotion emotion : annotation.getEmotions()){
            emotionsMap.get(emotion.getEmotion()).setSelected(true);
        }
    }

    private void setValuations(EmotionalAnnotation annotation) {
        valuatesMap.forEach((k, valuationCheckBox) -> valuationCheckBox.setSelected(false));
        for(UnitValuation valuation : annotation.getValuations()){
            valuatesMap.get(valuation.getValuation()).setSelected(true);
        }
    }


    private List<Emotion> getEmotions() {
        return (List<Emotion>) RemoteService.dictionaryServiceRemote.findDictionaryByClass(Emotion.class);
    }

    private List<Valuation> getValuations(){
        return (List<Valuation>) RemoteService.dictionaryServiceRemote.findDictionaryByClass(Valuation.class);
    }

    private class EmotionsListPanel extends JPanel {

        private JList emotionsList;
        private DefaultListModel listModel;


        public EmotionsListPanel() {
            setLayout(new BorderLayout());
            initComponents();
            JPanel buttonsPanel = createButtonsPanel();
            add(emotionsList, BorderLayout.CENTER);
            add(buttonsPanel, BorderLayout.EAST);

        }

        private void initComponents(){
            emotionsList = new JList();
            emotionsList.setCellRenderer(new AnnotationRenderer());
            emotionsList.addListSelectionListener(e -> loadSelectedAnnotation());
            listModel = new DefaultListModel();
            emotionsList.setModel(listModel);
        }

        private void loadSelectedAnnotation(){
            EmotionalAnnotation annotation = (EmotionalAnnotation) emotionsList.getSelectedValue();
            EmotionsPropertiesPanel.this.loadAnnotation(annotation);
        }

        private List<EmotionalAnnotation> findEmotionalAnnotation(Long senseId){
            return RemoteService.senseRemote.getEmotionalAnnotations(senseId);
        }

        public void loadAnnotations(Long senseId) {
            listModel.clear();
            if(senseId == null){
                return;
            }
            List<EmotionalAnnotation> annotationList = findEmotionalAnnotation(senseId);
            for(EmotionalAnnotation annotation : annotationList){
                listModel.addElement(annotation);
            }
        }

        private JPanel createButtonsPanel(){
            JButton addButton = MButton.buildAddButton()
                    .withToolTip(Labels.ADD)
                    .withActionListener(e->addEmotion());
            // TODO dorobić etykietę
            JButton removeButton = MButton.buildRemoveButton()
                    .withToolTip("Usuń")
                    .withActionListener(e->removeEmotion());

            MComponentGroup panel = new MComponentGroup(addButton, removeButton);
            panel.withVerticalLayout();
            return panel;
        }

        private void addEmotion(){
            throw new NotImplementedException();
        }

        private void removeEmotion(){
            throw new NotImplementedException();
        }
    }

    private class AnnotationRenderer implements ListCellRenderer<EmotionalAnnotation> {

        private JLabel label = new JLabel();

        @Override
        public Component getListCellRendererComponent(JList<? extends EmotionalAnnotation> list, EmotionalAnnotation value, int index, boolean isSelected, boolean cellHasFocus) {
            // TODO dorobić etykietę
            label.setText("Adnotacja " + (index + 1));
            return label;
        }
    }
}
