package pl.edu.pwr.wordnetloom.client.plugins.lexicon;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import pl.edu.pwr.wordnetloom.client.plugins.lexicon.window.LexiconsWindow;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
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
        JMenu help = workbench.getMenu(Labels.SETTINGS);
        if (help == null) {
            return;
        }
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
        LexiconsWindow window = new LexiconsWindow(workbench.getFrame(), LexiconManager.getInstance().getLexicons());
        List<Long> lexicons = window.showModal();
        LexiconManager.getInstance().setLexicons(lexicons);
    }
}
