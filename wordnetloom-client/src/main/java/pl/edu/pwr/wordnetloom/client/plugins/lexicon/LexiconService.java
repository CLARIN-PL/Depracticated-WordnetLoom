package pl.edu.pwr.wordnetloom.client.plugins.lexicon;

import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import pl.edu.pwr.wordnetloom.client.plugins.lexicon.frames.LexiconsFrame;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.systems.enums.RelationTypes;
import pl.edu.pwr.wordnetloom.client.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.PosManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.MenuItemExt;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

public class LexiconService extends AbstractService {

    private final JMenuItem lexiconItem = new MenuItemExt(Labels.LEXICON);

    public LexiconService(final Workbench workbench) {
        super(workbench);

        lexiconItem.addActionListener((ActionEvent e) -> {
            showLexiconWindow();
            ViWordNetService s = (ViWordNetService) workbench
                    .getService("pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService");
            s.getLexicalUnitsView().refreshLexicons();
            s.getSynsetView().refreshLexicons();
            s.clearAllViews();
            s.reloadCurrentListSelection();
        });
    }

    @Override
    public void installViews() {
    }

    @Override
    public void installMenuItems() {
        JMenu help = workbench.getMenu(Labels.HELP);
        if (help == null) {
            return;
        }
        help.addSeparator();
        help.add(lexiconItem);
    }

    @Override
    public boolean onClose() {
        return true;
    }

    @Override
    public void onStart() {
    }

    private void showLexiconWindow() {
        LexiconsFrame window = new LexiconsFrame(workbench.getFrame(), LexiconManager.getInstance().getLexicons());
        String lexicons = window.showModal();
        LexiconManager.getInstance().save(lexicons);
        workbench.setParam("Lexicons", lexicons);
        PosManager.getInstance().refresh();
        DomainManager.getInstance().refresh();
        RelationTypes.refresh();
    }
}
