package pl.edu.pwr.wordnetloom.client.plugins.owner;

import pl.edu.pwr.wordnetloom.client.plugins.owner.data.SessionData;
import pl.edu.pwr.wordnetloom.client.plugins.owner.frames.OwnerFrame;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * serwis zajmujący sie obsluga danych uzytkownika
 *
 * @author Max
 */
public class OwnerService extends AbstractService {

    private static final String PARAM_OWNER = "Owner";
    private static final String PARAM_PROJECT = "Project";

    /**
     * konstruktor serwisu
     *
     * @param workbench - srodowisko uruchomieniowe
     */
    public OwnerService(Workbench workbench) {
        super(workbench);
    }

    @Override
    public void onStart() {

        if (workbench.getParam(PARAM_OWNER) == null || workbench.getParam(PARAM_PROJECT) == null) {
            // brak danych o właścicielu, wyświetlenie okienka do wprowadzenia danych
            SessionData data = OwnerFrame.showModal(workbench);
            if (data.owner == null) {
                System.exit(0); // nie będziemy działać dalej
            } else {
                workbench.setParam(PARAM_OWNER, data.owner);
                workbench.updateOwner();
            }
        }
    }

    @Override
    public void installMenuItems() {
    }

    @Override
    public boolean onClose() {
        return true;
    }

    @Override
    public void installViews() {
    }

}
