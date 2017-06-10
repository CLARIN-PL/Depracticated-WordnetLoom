package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.frames;

import pl.edu.pwr.wordnetloom.client.systems.ui.IconFrame;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

public class RelationDisplayConfFrame extends IconFrame {

    private static final long serialVersionUID = 1L;

    public RelationDisplayConfFrame(Workbench workbench) {
        super(workbench.getFrame(), "", 515, 590);
    }

    public static void showModal(Workbench workbench) {
        RelationDisplayConfFrame frame = new RelationDisplayConfFrame(workbench);
        frame.setVisible(true);
        frame.showModal();
    }
}
