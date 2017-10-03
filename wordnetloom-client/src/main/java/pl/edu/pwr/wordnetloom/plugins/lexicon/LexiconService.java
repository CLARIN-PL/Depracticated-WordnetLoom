package pl.edu.pwr.wordnetloom.plugins.lexicon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import pl.edu.pwr.wordnetloom.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.systems.enums.RelationTypes;
import pl.edu.pwr.wordnetloom.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.systems.managers.PosManager;
import pl.edu.pwr.wordnetloom.systems.ui.MenuItemExt;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.plugins.lexicon.frames.LexiconsFrame;

public class LexiconService extends AbstractService {

	private JMenuItem lexiconItem = new MenuItemExt(Labels.LEXICON);

	public LexiconService(final Workbench workbench) {
		super(workbench);
		lexiconItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showLexiconWindow();
				ViWordNetService s = (ViWordNetService) workbench
						.getService("pl.edu.pwr.wordnetloom.plugins.viwordnet.ViWordNetService");
				s.getLexicalUnitsView().refreshLexicons();
				s.getSynsetView().refreshLexicons();
				s.clearAllViews();
				s.reloadCurrentListSelection();
			}
		});
	}

	@Override
	public void installViews() {}

	@Override
	public void installMenuItems() {
		JMenu help = workbench.getMenu(Labels.HELP);
		if ( help==null) return;
		 help.addSeparator();
		 help.add(lexiconItem);
	}

	@Override
	public boolean onClose() {
		return true;
	}

	@Override
	public void onStart() {}

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
