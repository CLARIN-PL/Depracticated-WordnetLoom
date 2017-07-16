package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import pl.edu.pwr.wordnetloom.client.systems.managers.PosManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

public class PosesFrame extends DialogWindow implements ActionListener {

    private static final long serialVersionUID = 1L;

    // tablica dostepnych czesci mowy
    private static final PartOfSpeech[] posesTab = PosManager.getInstance().getAllPOSes().toArray(new PartOfSpeech[]{});
    private final ButtonExt buttonOk, buttonCancel;
    private final JCheckBox checkPoses[] = new JCheckBox[posesTab.length];
    private Collection<PartOfSpeech> poses;

    /**
     * konstruktor
     *
     * @param owner - srodowisko
     */
    private PosesFrame(JFrame owner) {
        super(owner, Labels.PARTS_OF_SPEECH, 190, 430);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        buttonOk = new ButtonExt(Labels.OK, this, KeyEvent.VK_O);
        buttonOk.setEnabled(true);
        buttonCancel = new ButtonExt(Labels.CANCEL, this, KeyEvent.VK_A);
        buttonCancel.setEnabled(true);

        // utworzenie checkboxow
        for (int i = 0; i < checkPoses.length; i++) {
            checkPoses[i] = new JCheckBox(posesTab[i].toString());
            checkPoses[i].setSelected(false);
            this.add("br", checkPoses[i]);
        }

        // dodanie do zawartości okna
        this.add("br center", buttonOk);
        this.add("", buttonCancel);
    }

    /**
     * wyświetlenie okienka
     *
     * @param owner - srodowisko
     * @param oldPoses - aktualne czesci mowy
     * @return czesci mowy
     */
    static public String showModal(JFrame owner, String oldPoses) {
        PosesFrame frame = new PosesFrame(owner);
        frame.poses = new ArrayList<>();

        // dekodowanie starych posow
        String[] splitted = oldPoses.split("\\,");
        for (String pos : splitted) {
            PartOfSpeech p = PosManager.getInstance().decode(pos);
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
