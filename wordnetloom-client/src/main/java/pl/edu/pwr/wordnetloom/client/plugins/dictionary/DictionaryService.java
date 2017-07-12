package pl.edu.pwr.wordnetloom.client.plugins.dictionary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import pl.edu.pwr.wordnetloom.client.plugins.dictionary.frame.DictionaryFrame;
import pl.edu.pwr.wordnetloom.client.systems.ui.MenuItemExt;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

public class DictionaryService extends AbstractService {

    private JMenuItem dicItem = new MenuItemExt("Dictionary");

    public DictionaryService(final Workbench workbench) {
        super(workbench);
        dicItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLexiconWindow();
            }
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
        help.addSeparator();
        help.add(dicItem);
    }

    @Override
    public boolean onClose() {
        return true;
    }

    @Override
    public void onStart() {
    }

    private void showLexiconWindow() {
        DictionaryFrame window = new DictionaryFrame(workbench.getFrame(), "Dictionary");
        window.showModal();
    }
}
