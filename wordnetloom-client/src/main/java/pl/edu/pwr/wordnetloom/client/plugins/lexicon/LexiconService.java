package pl.edu.pwr.wordnetloom.client.plugins.lexicon;

import com.alee.laf.menu.WebMenu;
import pl.edu.pwr.wordnetloom.client.plugins.lexicon.window.LexiconsWindow;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.MMenuItem;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ServiceManager;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class LexiconService extends AbstractService {

    private final JMenuItem lexiconItem = new MMenuItem(Labels.LEXICON);

    public LexiconService(final Workbench workbench) {
        super(workbench);

        lexiconItem.addActionListener((ActionEvent e) -> {
            showLexiconWindow();
            ViWordNetService s = ServiceManager.getViWordNetService(workbench);
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
        WebMenu help = workbench.getMenu(Labels.SETTINGS);
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
        LexiconsWindow window = new LexiconsWindow(workbench.getFrame(), LexiconManager.getInstance().getUserChosenLexiconsIds());
        List<Long> lexicons = window.showModal();
        //LexiconManager.getInstance().setLexicons(lexicons);
    }
}
