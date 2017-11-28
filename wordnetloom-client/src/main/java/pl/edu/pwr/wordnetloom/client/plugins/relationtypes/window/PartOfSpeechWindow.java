package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window;

import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButtonPanel;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PartOfSpeechWindow extends DialogWindow implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final MButton btnOk = MButton.buildOkButton()
            .withEnabled(true);

    private final MButton btnCancel = MButton.buildCancelButton(this)
            .withEnabled(true);

    private final Map<Long, WebCheckBox> checkboxes = new HashMap<>();

    private PartOfSpeechWindow(WebFrame parent) {
        super(parent, Labels.PARTS_OF_SPEECH, 190, 430);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        buildCheckBoxesForAvailablePartsOfSpeech();
        addPartOfSpeech();
        addButtonsPanel();
        
    }

    private void buildCheckBoxesForAvailablePartsOfSpeech() {
        PartOfSpeechManager
                .getInstance()
                .getAll()
                .forEach(p -> checkboxes.put(p.getId(), new WebCheckBox(LocalisationManager
                        .getInstance()
                        .getLocalisedString(p.getName()))));
    }

    private void setPartsOfSpeech(Set<PartOfSpeech> list) {
        list.forEach(p -> checkboxes.get(p.getId()).setSelected(true));
    }

    private void addPartOfSpeech() {
        checkboxes.forEach((k, v) -> add("br", v));
    }

    public void addButtonsPanel() {

        MButtonPanel buttonPanel = new MButtonPanel(btnOk, btnCancel)
                .withHorizontalLayout();
        add("br center", buttonPanel);
    }

    private Set<PartOfSpeech> getSelected() {

        Set<PartOfSpeech> partOfSpeechSet = new HashSet<>();
        checkboxes.forEach((k, v) -> {
            if (v.isSelected())
                partOfSpeechSet.add(PartOfSpeechManager.getInstance().getById(k));
        });

        return partOfSpeechSet;
    }

    static public Set<PartOfSpeech> showModal(WebFrame parent, Set<PartOfSpeech> pos) {

        PartOfSpeechWindow frame = new PartOfSpeechWindow(parent);
        frame.setPartsOfSpeech(pos);
        frame.setVisible(true);
        frame.dispose();

        return frame.getSelected();
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == btnCancel) {
            setVisible(false);

        } else if (event.getSource() == btnOk) {
            checkboxes.clear();
            setVisible(false);
        }
    }

}
