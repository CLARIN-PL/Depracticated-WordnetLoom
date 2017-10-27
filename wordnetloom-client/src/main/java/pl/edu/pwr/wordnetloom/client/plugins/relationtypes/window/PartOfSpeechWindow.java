package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window;

import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

public class PartOfSpeechWindow extends DialogWindow implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final PartOfSpeech[] posesTab = PartOfSpeechManager.getInstance().getAll().toArray(new PartOfSpeech[]{});
    private final MButton buttonOk, buttonCancel;
    private final JCheckBox checkPoses[] = new JCheckBox[posesTab.length];
    private Collection<PartOfSpeech> poses;

    /**
     * konstruktor
     *
     * @param owner - srodowisko
     */
    private PartOfSpeechWindow(JFrame owner) {
        super(owner, Labels.PARTS_OF_SPEECH, 190, 430);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        buttonOk = MButton.buildOkButton()
                .withEnabled(true);

        buttonCancel = MButton.buildCancelButton(this)
                .withEnabled(true);

        // utworzenie checkboxow
        for (int i = 0; i < checkPoses.length; i++) {
            checkPoses[i] = new JCheckBox(posesTab[i].toString());
            checkPoses[i].setSelected(false);
            add("br", checkPoses[i]);
        }

        // dodanie do zawartości okna
        add("br center", buttonOk);
        add("", buttonCancel);
    }

    /**
     * wyświetlenie okienka
     *
     * @param owner    - srodowisko
     * @param oldPoses - aktualne czesci mowy
     * @return czesci mowy
     */
    static public String showModal(JFrame owner, String oldPoses) {
        PartOfSpeechWindow frame = new PartOfSpeechWindow(owner);
        frame.poses = new ArrayList<>();

        // dekodowanie starych posow
        String[] splitted = oldPoses.split("\\,");
        for (String pos : splitted) {
            PartOfSpeech p = PartOfSpeechManager.getInstance().decode(pos);
            frame.checkPoses[p.getId().intValue()].setSelected(true);
            frame.poses.add(p);
        }
        frame.setVisible(true);
        frame.dispose();

        // zapisanie wyniku w formie tekstu
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (PartOfSpeech pos : frame.poses) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            sb.append(pos);
        }
        return sb.toString();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        if (arg0.getSource() == buttonCancel) {
            setVisible(false);

        } else if (arg0.getSource() == buttonOk) {
            poses.clear();
            for (int i = 0; i < checkPoses.length; i++) {
                if (checkPoses[i].isSelected()) {
                    poses.add(posesTab[i]);
                }
            }
            setVisible(false);
        }
    }

}
