package pl.edu.pwr.wordnetloom.plugins.dictionary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import pl.edu.pwr.wordnetloom.plugins.dictionary.frame.DictionaryFrame;
import pl.edu.pwr.wordnetloom.systems.ui.MenuItemExt;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractService;

public class DictionaryService extends AbstractService{

	private JMenuItem dicItem = new MenuItemExt("Dictionary");

	public  DictionaryService(final Workbench workbench) {
		super(workbench);
		dicItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showLexiconWindow();
			}
		});
	}

	@Override
	public void installViews() {}

	@Override
	public void installMenuItems() {
		JMenu help = workbench.getMenu(Labels.OTHER);
		if ( help==null) return;
		 help.addSeparator();
		 help.add(dicItem);
	}

	@Override
	public boolean onClose() {
		return true;
	}

	@Override
	public void onStart() {}

	private void showLexiconWindow() {
		DictionaryFrame window = new DictionaryFrame(workbench.getFrame(), "Dictionary");
		window.showModal();
	}
}
