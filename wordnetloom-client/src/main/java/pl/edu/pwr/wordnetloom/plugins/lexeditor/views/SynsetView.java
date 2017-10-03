package pl.edu.pwr.wordnetloom.plugins.lexeditor.views;

import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractView;
import pl.edu.pwr.wordnetloom.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;

public class SynsetView extends AbstractView {

	public SynsetView(Workbench workbench, String title) {
		super(workbench, title, new SynsetViewUI());
	}

	public void addUnitChangeListener(SimpleListenerInterface newListener) {
		getUI().addActionListener(newListener);
	}
	public void refreshLexicons(){
		SynsetViewUI viewUI=(SynsetViewUI)getUI();
		viewUI.refreshLexicons();
	}
}
