package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window;

import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.GroupView;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComponentGroup;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import se.datadosen.component.RiverLayout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.stream.Collectors;

public class PartOfSpeechWindow extends DialogWindow implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final MButton btnOk = MButton.buildOkButton()
            .withWidth(65)
            .withActionListener(this)
            .withEnabled(true);

    private final Map<Long, Pair<String,WebCheckBox>> checkboxes = new LinkedHashMap<>();

    private PartOfSpeechWindow(WebFrame parent, Set<PartOfSpeech> pos) {
        super(parent, Labels.PARTS_OF_SPEECH, 300, 200);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        buildCheckBoxesForAvailablePartsOfSpeech();
        setPartsOfSpeech(pos);

        add("hfill vfill", GroupView.createGroupView( checkboxes.entrySet()
                                    .stream()
                                    .collect(Collectors.toMap(e -> e.getValue().getA(), e -> e.getValue().getB())),
                                        null,0.4f,0.4f));
        addButtonsPanel();
    }

    private void buildCheckBoxesForAvailablePartsOfSpeech() {
        PartOfSpeechManager
                .getInstance()
                .getAll()
                .forEach(p -> checkboxes.put(p.getId(), new Pair(LocalisationManager
                        .getInstance()
                        .getLocalisedString(p.getName()),new WebCheckBox())));
    }

    private void setPartsOfSpeech(Set<PartOfSpeech> list) {
        list.forEach(p -> checkboxes.get(p.getId()).getB().setSelected(true));
    }


    public void addButtonsPanel() {

        MComponentGroup buttonPanel = new MComponentGroup(btnOk)
                .withHorizontalLayout();
        add("br center", buttonPanel);
    }

    private Set<PartOfSpeech> getSelected() {

        Set<PartOfSpeech> partOfSpeechSet = new HashSet<>();
        checkboxes.forEach((k, v) -> {
            if (v.getB().isSelected())
                partOfSpeechSet.add(PartOfSpeechManager.getInstance().getById(k));
        });

        return partOfSpeechSet;
    }

    static public Set<PartOfSpeech> showModal(WebFrame parent, Set<PartOfSpeech> pos) {

        PartOfSpeechWindow frame = new PartOfSpeechWindow(parent, pos);
        frame.setVisible(true);

        return frame.getSelected();
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == btnOk) {
            setVisible(false);
            dispose();
        }
    }

}
