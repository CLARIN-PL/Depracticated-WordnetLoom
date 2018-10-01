package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.security.UserSessionContext;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.renderers.LocalisedRenderer;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComponentGroup;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.PermissionHelper;
import pl.edu.pwr.wordnetloom.dictionary.model.Emotion;
import pl.edu.pwr.wordnetloom.dictionary.model.Markedness;
import pl.edu.pwr.wordnetloom.dictionary.model.Valuation;
import pl.edu.pwr.wordnetloom.sense.model.EmotionalAnnotation;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseEmotion;
import pl.edu.pwr.wordnetloom.sense.model.SenseValuation;
import pl.edu.pwr.wordnetloom.user.model.User;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

import static se.datadosen.component.RiverLayout.LINE_BREAK;
import static se.datadosen.component.RiverLayout.TAB_STOP;

public class EmotionsPropertiesPanel extends JPanel {

    // TODO dorobić etykiety
    private static final String LABEL_MARKEDNESS = "Nacechowanie:";
    private static final String LABEL_EXAMPLE = "Przykłady:";
    private static final String LABEL_NEUTRAL = "Neutralny";
    private static final String LABEL_MARKED = "Nacechowany";
    private static final String LABEL_EMOTIONS = "Emocje:";
    private static final String LABEL_VALUATIONS = "Wartościowanie:";
    private static final String LABEL_STATUS = "Status jednostki:";
    private static final String LABEL_OWNER = "Właściciel:";

    private JRadioButton neutralRadio;
    private JRadioButton notMarkedRadio;
    private JLabel ownerLabel;
    private JComboBox markednessCombo;
    private JTextArea example1;
    private JTextArea example2;

    private EmotionsListPanel listPanel;

    private EmotionalAnnotation editedAnnotation;
    private Sense sense;

    private Map<Emotion, JCheckBox> emotionsMap = new HashMap<>();
    private Map<Valuation, JCheckBox> valuatesMap = new HashMap<>();

    private int panelWidth;
    private int panelHeight;

    public EmotionsPropertiesPanel(WebFrame frame, int width, int height) {
        final int WIDTH = width;
        final int HALF_WIDTH = width / 2 - 5;
        final int ELEMENT_HEIGHT = 50;
        final int LIST_PANEL_HEIGHT = 100;
        final int EXAMPLE_PANEL_HEIGHT = 120;

        panelWidth = width;
        panelHeight = height;

        setLayout(new RiverLayout());

        initComponents();
        listPanel = createListPanel(WIDTH, LIST_PANEL_HEIGHT);
        JPanel statusPanel = createStatusPanel(HALF_WIDTH, ELEMENT_HEIGHT);
        JPanel ownerPanel = createOwnerPanel(HALF_WIDTH, ELEMENT_HEIGHT);
        JPanel emotionsPanel = createEmotionsPanel(getAllEmotions(), WIDTH);
        JPanel valuationsPanel = createValuationsPanel(getAllValuations(), WIDTH);
        JPanel markednessPanel = createMarkednessPanel(WIDTH, ELEMENT_HEIGHT);
        JPanel examplesPanel = createExamplesPanel(WIDTH, EXAMPLE_PANEL_HEIGHT);

        add(listPanel);
        add(LINE_BREAK, statusPanel);
        add(TAB_STOP, ownerPanel);
        add(LINE_BREAK, emotionsPanel);
        add(LINE_BREAK, valuationsPanel);
        add(LINE_BREAK, markednessPanel);
        add(LINE_BREAK, examplesPanel);

        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setStatusEnabled(false);
        setEnableEditing(false);
    }

    private void initComponents() {
        final int TEXT_AREA_ROWS = 2;

        neutralRadio = new JRadioButton(LABEL_NEUTRAL);
        neutralRadio.addActionListener(e -> setAnnotationEnabled());
        notMarkedRadio = new JRadioButton(LABEL_MARKED);
        notMarkedRadio.addActionListener(e -> setAnnotationEnabled());
        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(neutralRadio);
        statusGroup.add(notMarkedRadio);

        ownerLabel = new JLabel("");
        markednessCombo = createMarkednessCombo();

        example1 = new JTextArea();
        example1.setRows(TEXT_AREA_ROWS);
        example1.setLineWrap(true);
        example2 = new JTextArea();
        example2.setRows(TEXT_AREA_ROWS);
        example2.setLineWrap(true);
    }

    public void save() {
        // saving only selected annotation
        setAnnotation(editedAnnotation);
        RemoteService.senseRemote.save(editedAnnotation);
    }

    private void setAnnotation(EmotionalAnnotation annotation) {
        annotation.setEmotionalCharacteristic(canEditingAnnotation());
        annotation.setEmotions(getAnnotationEmotions(annotation));
        annotation.setValuations(getAnnotationValuations(annotation));
        annotation.setMarkedness((Markedness) markednessCombo.getSelectedItem());
        annotation.setExample1(example1.getText());
        annotation.setExample2(example2.getText());
    }

    private Set<SenseValuation> getAnnotationValuations(EmotionalAnnotation annotation) {
        Set<SenseValuation> valuations = new LinkedHashSet<>();
        valuatesMap.forEach((k, v) -> {
            if (v.isSelected()) {
                SenseValuation senseValuation = new SenseValuation(annotation, k);
                valuations.add(senseValuation);
            }
        });
        return valuations;
    }

    private Set<SenseEmotion> getAnnotationEmotions(EmotionalAnnotation annotation) {
        Set<SenseEmotion> emotions = new LinkedHashSet<>();
        emotionsMap.forEach((k, v) -> {
            if (v.isSelected()) {
                SenseEmotion senseEmotion = new SenseEmotion(annotation, k);
                emotions.add(senseEmotion);
            }
        });
        return emotions;
    }

    private JComboBox createMarkednessCombo() {
        JComboBox comboBox = new JComboBox();
        List<Markedness> markednesses = getAllMarkedness();
        comboBox.addItem(null);
        for (Markedness markedness : markednesses) {
            comboBox.addItem(markedness);
        }
        comboBox.setRenderer(new LocalisedRenderer());
        return comboBox;
    }

    private List<Markedness> getAllMarkedness() {
        return (List<Markedness>) RemoteService.dictionaryServiceRemote.findDictionaryByClass(Markedness.class);
    }

    private void setAnnotationEnabled() {
        boolean enabled = canEditingAnnotation();
        if (!enabled) {
            clearFields();
        } else {
            editedAnnotation.setEmotionalCharacteristic(enabled);
            loadAnnotation(editedAnnotation);
        }
        setEnableEditing(enabled);
    }

    private boolean canEditingAnnotation() {
        return notMarkedRadio.isSelected() && PermissionHelper.hasPermissionToEdit(sense);
    }

    private void setEnableEditing(boolean enabled) {
        emotionsMap.forEach((k, emotionCheckBox) -> emotionCheckBox.setEnabled(enabled));
        valuatesMap.forEach((k, valuationCheckBox) -> valuationCheckBox.setEnabled(enabled));
        markednessCombo.setEnabled(enabled);
        example1.setEnabled(enabled);
        example2.setEnabled(enabled);
    }

    private void clearFields() {
        emotionsMap.forEach((k, emotionCheckBox) -> emotionCheckBox.setSelected(false));
        valuatesMap.forEach((k, valuationCheckBox) -> valuationCheckBox.setSelected(false));
        markednessCombo.setSelectedItem(null);
        example1.setText("");
        example2.setText("");
    }

    private JPanel createStatusPanel(int width, int height) {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(neutralRadio);
        panel.add(notMarkedRadio);

        panel.setPreferredSize(new Dimension(width, height));
        setTitledBorder(LABEL_STATUS, panel);
        return panel;
    }

    private JPanel createOwnerPanel(int width, int height) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(ownerLabel);

        panel.setPreferredSize(new Dimension(width, height));
        setTitledBorder(LABEL_OWNER, panel);
        return panel;
    }

    private JPanel createMarkednessPanel(int width, int height) {
        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.add(markednessCombo);

        panel.setPreferredSize(new Dimension(width, height));
        setTitledBorder(LABEL_MARKEDNESS, panel);
        return panel;
    }

    private JPanel createExamplesPanel(int width, int height) {
        final int LABEL_WIDTH = 40;
        final int EXAMPLE_WIDTH = width - LABEL_WIDTH;
        final int EXAMPLE_HEIGHT = height / 2 - 20;
        final String EXAMPLE1 = "#1:";
        final String EXAMPLE2 = "#2:";

        JPanel panel = new JPanel(new RiverLayout());
        panel.setPreferredSize(new Dimension(width, height));

        JScrollPane example1Scroll = new JScrollPane(example1);
        JScrollPane example2Scroll = new JScrollPane(example2);
        example1Scroll.setPreferredSize(new Dimension(EXAMPLE_WIDTH, EXAMPLE_HEIGHT));
        example2Scroll.setPreferredSize(new Dimension(EXAMPLE_WIDTH, EXAMPLE_HEIGHT));

        JLabel example1Label = new JLabel(EXAMPLE1);
        example1Label.setSize(LABEL_WIDTH, example1Label.getHeight());
        JLabel example2Label = new JLabel(EXAMPLE2);
        example2Label.setSize(LABEL_WIDTH, example2Label.getHeight());

        panel.add(example1Label);
        panel.add(RiverLayout.HFILL, example1Scroll);
        panel.add(RiverLayout.LINE_BREAK, example2Label);
        panel.add(RiverLayout.HFILL, example2Scroll);

        setTitledBorder(LABEL_EXAMPLE, panel);
        return panel;
    }

    private EmotionsListPanel createListPanel(int width, int height) {
        EmotionsListPanel panel = new EmotionsListPanel();
        panel.setPreferredSize(new Dimension(width, height));
        // TODO dorobić etykietę
        setTitledBorder("Anotacje", panel);
        return panel;
    }

    private void setTitledBorder(final String title, JPanel panel) {
        panel.setBorder(new TitledBorder(title));
    }

    public void load(Sense sense) {
        if (sense != null) {
            listPanel.loadAnnotations(sense);
        } else {
            listPanel.loadAnnotations(null);
        }
        this.sense = sense;
    }

    private JPanel createEmotionsPanel(List<Emotion> emotions, int width) {
        final int COLUMNS = 3;
        JPanel panel = new JPanel(new GridLayout(0, COLUMNS, 0, 3));

        for (Emotion emotion : emotions) {
            String emotionName = LocalisationManager.getInstance().getLocalisedString(emotion.getName());
            JCheckBox checkBox = new JCheckBox(emotionName);
            panel.add(checkBox);
            emotionsMap.put(emotion, checkBox);
        }

        int height = calculateHeight(emotions.size(), COLUMNS);
        panel.setPreferredSize(new Dimension(width, height));

        setTitledBorder(LABEL_EMOTIONS, panel);
        return panel;
    }

    private int calculateHeight(int elementsSize, int columnsCount) {
        final int ROW_HEIGHT = 30;
        int rows = elementsSize / columnsCount;
        if (elementsSize % columnsCount != 0) {
            rows++;
        }
        return rows * ROW_HEIGHT;
    }

    private JPanel createValuationsPanel(List<Valuation> valuations, int width) {
        final int COLUMNS = 3;

        JPanel panel = new JPanel(new GridLayout(0, COLUMNS));

        for (Valuation valuation : valuations) {
            String valuationName = LocalisationManager.getInstance().getLocalisedString(valuation.getName());
            JCheckBox checkBox = new JCheckBox(valuationName);
            panel.add(checkBox);
            valuatesMap.put(valuation, checkBox);
        }

        int height = calculateHeight(valuations.size(), COLUMNS);
        panel.setPreferredSize(new Dimension(width, height));
        setTitledBorder(LABEL_VALUATIONS, panel);
        return panel;
    }

    private void loadAnnotation(EmotionalAnnotation annotation) {
        if (annotation != null) {
            this.editedAnnotation = annotation;
            setStatus(annotation);
            setOwner(annotation);
            setEmotions(annotation);
            setValuations(annotation);
            setMarkedness(annotation);
            setEnableEditing(canEditingAnnotation());
            setStatusEnabled(true);
        } else {
            setStatusEnabled(false);
        }
    }

    private void setStatusEnabled(boolean enabled) {
        boolean hasPermissionToEdit;
        if(sense != null){
            hasPermissionToEdit = PermissionHelper.checkPermissionToEditAndSetComponents(sense, neutralRadio, notMarkedRadio);
        } else {
            hasPermissionToEdit = PermissionHelper.checkPermissionToEditAndSetComponents(neutralRadio, notMarkedRadio);
        }
        if (hasPermissionToEdit) {
            neutralRadio.setEnabled(enabled);
            notMarkedRadio.setEnabled(enabled);
        }
    }



    private void setMarkedness(EmotionalAnnotation annotation) {
        markednessCombo.setSelectedItem(annotation.getMarkedness());
    }

    private void setStatus(EmotionalAnnotation annotation) {
        if (annotation.hasEmotionalCharacteristic()) {
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
        for (SenseEmotion emotion : annotation.getEmotions()) {
            emotionsMap.get(emotion.getEmotion()).setSelected(true);
        }
    }

    private void setValuations(EmotionalAnnotation annotation) {
        valuatesMap.forEach((k, valuationCheckBox) -> valuationCheckBox.setSelected(false));
        for (SenseValuation valuation : annotation.getValuations()) {
            valuatesMap.get(valuation.getValuation()).setSelected(true);
        }
    }

    private List<Emotion> getAllEmotions() {
        return (List<Emotion>) RemoteService.dictionaryServiceRemote.findDictionaryByClass(Emotion.class);
    }

    private List<Valuation> getAllValuations() {
        return (List<Valuation>) RemoteService.dictionaryServiceRemote.findDictionaryByClass(Valuation.class);
    }

    private class EmotionsListPanel extends JPanel {

        private JList annotationsList;
        private DefaultListModel listModel;
        private List<EmotionalAnnotation> annotations;

        private JButton addButton;
        private JButton removeButton;

        EmotionsListPanel() {
            setLayout(new BorderLayout());
            initComponents();
            JPanel buttonsPanel = createButtonsPanel();
            add(new JScrollPane(annotationsList), BorderLayout.CENTER);
            add(buttonsPanel, BorderLayout.EAST);
        }

        private void initComponents() {
            annotationsList = new JList();
            annotationsList.setCellRenderer(new AnnotationRenderer());
            annotationsList.addListSelectionListener(e -> loadSelectedAnnotation());
            listModel = new DefaultListModel();
            annotationsList.setModel(listModel);
        }

        private void loadSelectedAnnotation() {
            EmotionalAnnotation annotation = (EmotionalAnnotation) annotationsList.getSelectedValue();
            EmotionsPropertiesPanel.this.loadAnnotation(annotation);
        }

        private List<EmotionalAnnotation> findEmotionalAnnotation(Long senseId) {
            return RemoteService.senseRemote.getEmotionalAnnotations(senseId);
        }

        void loadAnnotations(Sense sense) {
            listModel.clear();
            if (sense == null) {
                return;
            }
            annotations = findEmotionalAnnotation(sense.getId());
            for (EmotionalAnnotation annotation : annotations) {
                listModel.addElement(annotation);
            }
            if (!annotations.isEmpty()) {
                annotationsList.setSelectedIndex(0);
            }

            setEnableEditing(sense);
        }

        private void setEnableEditing(Sense sense){
            JComponent[] components = {addButton, removeButton};
            PermissionHelper.checkPermissionToEditAndSetComponents(sense, components);
        }

        private JPanel createButtonsPanel() {

            addButton = MButton.buildAddButton()
                    .withToolTip(Labels.ADD)
                    .withActionListener(e -> addEmotion());
            removeButton = MButton.buildRemoveButton()
                    .withToolTip(Labels.DELETE)
                    .withActionListener(e -> removeEmotion());

            MComponentGroup panel = new MComponentGroup(addButton, removeButton);
            panel.withVerticalLayout();
            return panel;
        }

        private void addEmotion() {
            // creating new annotation. Annotation must have unit and owner
            EmotionalAnnotation annotation = new EmotionalAnnotation();
            annotation.setSense(sense);
            User currentUser = UserSessionContext.getInstance().getUser();
            annotation.setOwner(currentUser);
            // annotation must be saved at this point, because saving later cause an error
            annotation = RemoteService.senseRemote.save(annotation);
            // adding saved annotation to the list and selecting it
            listModel.addElement(annotation);
            annotationsList.setSelectedIndex(listModel.getSize() - 1);
        }

        private void removeEmotion() {
            // TODO dorobić etykietę
            final String MESSAGE = "Czy na pewno usunąć anotacje";
            if (DialogBox.showYesNo(MESSAGE) == DialogBox.YES) {
                int selectedIndex = annotationsList.getSelectedIndex();
                EmotionalAnnotation selectedAnnotation = (EmotionalAnnotation) listModel.getElementAt(selectedIndex);
                listModel.remove(selectedIndex);

                RemoteService.senseRemote.delete(selectedAnnotation);
            }
        }
    }

    private class AnnotationRenderer implements ListCellRenderer<EmotionalAnnotation> {

        private JLabel label = new JLabel();

        @Override
        public Component getListCellRendererComponent(JList<? extends EmotionalAnnotation> list, EmotionalAnnotation value, int index, boolean isSelected, boolean cellHasFocus) {
            label.setText(value.getOwner().getFullname());
            return label;
        }
    }
}
