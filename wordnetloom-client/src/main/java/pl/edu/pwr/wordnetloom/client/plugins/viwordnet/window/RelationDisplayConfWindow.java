package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window;

import pl.edu.pwr.wordnetloom.client.systems.ui.IconFrame;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

public class RelationDisplayConfWindow extends IconFrame {

    private static final long serialVersionUID = 1L;

    public RelationDisplayConfWindow(Workbench workbench) {
        super(workbench.getFrame(), "", 515, 590);
    }

    public static void showModal(Workbench workbench) {
        RelationDisplayConfWindow frame = new RelationDisplayConfWindow(workbench);
        frame.setVisible(true);
        frame.showModal();
    }
}
