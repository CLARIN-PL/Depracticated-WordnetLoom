package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.google.common.eventbus.Subscribe;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.ExampleFrame;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events.UpdateSynsetPropertiesEvent;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeSynset;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.security.UserSessionContext;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.renderers.ExampleCellRenderer;
import pl.edu.pwr.wordnetloom.client.systems.renderers.LocalisedRenderer;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextArea;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextField;
import pl.edu.pwr.wordnetloom.client.utils.*;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.common.model.Example;
import pl.edu.pwr.wordnetloom.dictionary.model.Status;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;
import pl.edu.pwr.wordnetloom.synset.model.SynsetExample;
import pl.edu.pwr.wordnetloom.user.model.Role;
import se.datadosen.component.RiverLayout;
import sun.security.acl.PermissionImpl;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Synset properties panel
 *
 * @author Max
 */
public class SynsetPropertiesViewUI extends AbstractViewUI implements ActionListener, CaretListener {

    private MTextArea definitionValue;
    private MTextArea commentValue;
    private JComboBox statusComboBox;

    private WebScrollPane scrollPaneExamples;
    private MButton btnNewExample;
    private MButton btnEditExample;
    private MButton btnRemoveExample;
    private WebList examplesList;
    private DefaultListModel examplesModel;

    private ChangeListener changeListener;

    private final MButton buttonSave = MButton.buildSaveButton()
            .withToolTip(Hints.SAVE_CHANGES_IN_SYNSET)
            .withEnabled(false)
            .withActionListener(this);

    private WebCheckBox abstractValue;

    private Synset lastSynset = null;
    private boolean quiteMode = false;
    private final ViwnGraphViewUI graphUI;

    private boolean permissionToEdit = false;

    public SynsetPropertiesViewUI(ViwnGraphViewUI graphUI) {
        this.graphUI = graphUI;
        Application.eventBus.register(this);
    }

    //TODO refactor
    @Override
    protected void initialize(WebPanel content) {

        content.setLayout(new RiverLayout());
        content.setMargin(5, 1, 5, 1);

        definitionValue = new MTextArea("");
        definitionValue.addCaretListener(this);
        definitionValue.setRows(3);

        commentValue = new MTextArea("");
        commentValue.addCaretListener(this);
        commentValue.setRows(3);

        statusComboBox = createStatusComboBox();

        abstractValue = new WebCheckBox(Labels.ARTIFICIAL);
        abstractValue.setSelected(false);
        abstractValue.addActionListener(this);


        examplesList = new WebList() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }
        };
        scrollPaneExamples = new WebScrollPane(examplesList);

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
        examplesList.addListSelectionListener(sl -> {
            if (examplesList.isSelectionEmpty()) {
                adjustButtons(false);
            } else {
                adjustButtons(true);
            }

        });

        btnNewExample = MButton.buildAddButton();

        btnNewExample.addActionListener((ActionEvent e) -> addExample());

        JLabel lblExample = new JLabel(Labels.USE_CASE_COLON);
        lblExample.setVerticalAlignment(SwingConstants.TOP);
        lblExample.setHorizontalAlignment(SwingConstants.RIGHT);

        btnEditExample = MButton.buildEditButton();
        btnEditExample.addActionListener((ActionEvent e) -> editExample());

        btnRemoveExample = MButton.buildDeleteButton();
        btnRemoveExample.addActionListener((ActionEvent e) -> removeExample());

        WebPanel buttonsPanel = new WebPanel();
        buttonsPanel.setLayout(new RiverLayout(0, 0));
        buttonsPanel.add("br", btnNewExample);
        buttonsPanel.add("br", btnEditExample);
        buttonsPanel.add("br", btnRemoveExample);


        WebPanel propertiesPanel = new WebPanel(new RiverLayout(0, 0));

        propertiesPanel.add("vtop", new JLabel(Labels.STATUS_COLON));
        propertiesPanel.add("br hfill", statusComboBox);
        propertiesPanel.add("vtop", new JLabel(Labels.DEFINITION_COLON));
        propertiesPanel.add("br hfill", new JScrollPane(definitionValue));
        propertiesPanel.add("br vtop", new JLabel(Labels.COMMENT_COLON));
        propertiesPanel.add("br hfill", new JScrollPane(commentValue));
        propertiesPanel.add("br vtop", new JLabel(Labels.EXAMPLES));

        WebPanel subPanel = new WebPanel(new RiverLayout(0, 0));

        subPanel.add("hfill", scrollPaneExamples);
        subPanel.add("", buttonsPanel);

        propertiesPanel.add("br hfill", subPanel);
        propertiesPanel.add("br", abstractValue);
        propertiesPanel.add("br center", buttonSave);

        WebScrollPane scroll = new WebScrollPane(propertiesPanel);
        scroll.setDrawBorder(false);
        content.add("hfill vfill", scroll);

        abstractValue.setEnabled(false);

        initChangeListener();
    }

    private void removeExample() {
        int idx = examplesList.getSelectedIndex();
        if (idx >= 0) {
            examplesModel.remove(idx);
            saveChanges();
        }
    }

    private void editExample() {
        int idx = examplesList.getSelectedIndex();
        Example example = (SynsetExample) examplesModel.get(idx);
        if (idx >= 0) {
            Example newExample = ExampleFrame.showModal(null, Labels.EDIT_EXAMPLE, example, true);
            updateExample(example, newExample);
            examplesList.updateUI();
//            String modified = ExampleFrame.showModal(null,
//                    Labels.EDIT_EXAMPLE,
//                    example, true);
//            String old = example.getExample();
//            if (modified != null && !old.equals(modified)) {
//                example.setExample(modified);
//                examplesList.updateUI();
//                saveChanges();
//            }
        }
    }

    private void updateExample(Example oldExample, Example newExample){
        oldExample.setExample(newExample.getExample());
        oldExample.setType(newExample.getType());
    }

    private void addExample() {
        Example example = ExampleFrame.showModal(null, Labels.NEW_EXAMPLE, new SynsetExample(), false);
        examplesModel.addElement(example);
        examplesList.updateUI();
//        if (example != null && !"".equals(example)) {
//            SynsetExample exp = new SynsetExample();
//            exp.setExample(example);
//            examplesModel.addElement(exp);
////                saveChanges();
//            examplesList.updateUI();
//        }
    }

    void setEnableEditing(){
        JComponent[] components = {statusComboBox, definitionValue, commentValue, examplesList, abstractValue, buttonSave};
        if(lastSynset != null) {
            permissionToEdit = PermissionHelper.checkPermissionToEditAndSetComponents(lastSynset, components);
        } else {
            permissionToEdit = PermissionHelper.checkPermissionToEditAndSetComponents(components);
        }
    }

    private void initChangeListener() {
        // TODO dokończyć
        changeListener = new ChangeListener();
        changeListener.addComponents(definitionValue, commentValue, statusComboBox,
                examplesList, abstractValue);
        changeListener.setListener(()-> {
            System.out.println(changeListener.isChanged() && permissionToEdit);
            buttonSave.setEnabled(changeListener.isChanged() && permissionToEdit);

        });
    }

    private JComboBox createStatusComboBox() {
        JComboBox comboBox = new JComboBox();
        comboBox.addItem(null);
        for(Status status : getAllStatuses()){
            comboBox.addItem(status);
        }
        comboBox.setRenderer(new LocalisedRenderer());
        return comboBox;
    }

    private List<Status> getAllStatuses() {
        return (List<Status>) RemoteService.dictionaryServiceRemote.findDictionaryByClass(Status.class);
    }

    @Override
    public JComponent getRootComponent() {
        return definitionValue;
    }

    private void adjustButtons(boolean active) {
        btnEditExample.setEnabled(active);
        btnRemoveExample.setEnabled(active);
    }

    @Subscribe
    public void handleUpdatePropertiesEvent(UpdateSynsetPropertiesEvent event){
        SwingUtilities.invokeLater(()->refreshData(event.getSynset()));
    }

    private void clear() {
        statusComboBox.setSelectedIndex(0);
        definitionValue.clear();
        commentValue.clear();
        examplesModel.clear();
        abstractValue.setSelected(false);
        buttonSave.setEnabled(false);
    }

    // TODO refactor
    public void refreshData(Synset synset) {
        definitionValue.setText(formatValue(null));
        commentValue.setText(formatValue(null));
        abstractValue.setSelected(false);

        if (synset != null) {

            SynsetAttributes sa = RemoteService.synsetRemote.fetchSynsetAttributes(synset.getId());;

            statusComboBox.setSelectedItem(synset.getStatus());
            if (sa.getDefinition() != null) {
                definitionValue.setText(sa.getDefinition());
            }

            if (sa.getComment() != null) {
                commentValue.setText(sa.getComment());
            }

            if (sa.getExamples() != null) {
                examplesModel.clear();
                sa.getExamples().forEach(e -> examplesModel.addElement(e));
            }

            abstractValue.setSelected(synset.getAbstract());
        } else {
            clear();
        }
        lastSynset = synset;
        quiteMode = true;
        abstractValue.setEnabled(canEditSynset(synset));
        buttonSave.setEnabled(false);
        quiteMode = false;

        setEnableEditing();
    }

    private boolean canEditSynset(Synset synset) {
        return permissionToEdit && synset != null;
    }

    /**
     * formatowanie wartości, tak aby nie bylo nulla
     *
     * @param value - wartość wejsciowa
     * @return wartośc wjesciowa lub "brak danych" gdy był null
     */
    private static String formatValue(String value) {
//        return (value == null || value.length() == 0) ? Labels.VALUE_UNKNOWN : value;
        return value == null ? "" : value;
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
            buttonSave.setEnabled(permissionToEdit);
            // zapisanie zmian
        } else if (event.getSource() == buttonSave) { // zapisanie zmian
            saveChanges();
        }
    }

    private void saveChanges() {
        String definition = definitionValue.getText().isEmpty() ? definitionValue.getText() : null;
        String comment = commentValue.getText().isEmpty() ? commentValue.getText() : null;
        boolean isAbstract = abstractValue.isSelected();

//        Set<SynsetExample> examples = new HashSet<>();
        List<SynsetExample> examples = new ArrayList<>();
        if (!examplesModel.isEmpty()) {
            for (int i = 0; i < examplesModel.size(); i++) {
                examples.add((SynsetExample) examplesModel.getElementAt(i));
            }
        }

        if (!updateSynset(lastSynset, definition, comment, isAbstract, examples)) {
            refreshData(lastSynset); // nieudana zmiana statusu
            DialogBox.showError(Messages.ERROR_NO_STATUS_CHANGE_BECAUSE_OF_RELATIONS_IN_SYNSETS);
        }

        buttonSave.setEnabled(false);
        if (definitionValue.isEnabled()) { // zwrocenie focusu
            definitionValue.grabFocus();
        } else {
            commentValue.grabFocus();
        }

        ViwnNode node = graphUI.getSelectedNode();
        if (node != null && node instanceof ViwnNodeSynset) {
            ViwnNodeSynset s = (ViwnNodeSynset) node;
            s.setLabel(null);
        }
        graphUI.graphChanged();
        graphUI.refreshView(lastSynset);
    }

    public boolean updateSynset(Synset synset, String definition, String comment, boolean isAbstract, List<SynsetExample> examples) {
        boolean result = true;
        if (synset != null) {

            if (isAbstract != synset.getAbstract()) {
                synset.setAbstract(isAbstract);
                RemoteService.synsetRemote.save(synset);
            }

            SynsetAttributes sa = RemoteService.synsetRemote.fetchSynsetAttributes(synset.getId());
            sa.setDefinition(definition);
            sa.setComment(comment);
            examples.forEach(e -> e.setSynsetAttributes(sa));
            sa.setExamples(examples);

            RemoteService.synsetRemote.save(sa);
        }
        return result;
    }

    @Override
    public void caretUpdate(CaretEvent arg0) {
        if (quiteMode || lastSynset == null) {
            return;
        }
        if (arg0.getSource() instanceof MTextField) {
            MTextField field = (MTextField) arg0.getSource();
            buttonSave.setEnabled(permissionToEdit && buttonSave.isEnabled() | field.wasTextChanged());
        }
        if (arg0.getSource() instanceof MTextArea) {
            MTextArea field = (MTextArea) arg0.getSource();
            buttonSave.setEnabled(permissionToEdit && buttonSave.isEnabled() | field.wasTextChanged());
        }
    }
}
