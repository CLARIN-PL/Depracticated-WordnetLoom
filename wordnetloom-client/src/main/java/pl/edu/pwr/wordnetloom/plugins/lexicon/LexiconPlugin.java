package pl.edu.pwr.wordnetloom.plugins.lexicon;

import pl.edu.pwr.wordnetloom.workbench.interfaces.Plugin;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;

public class LexiconPlugin implements Plugin {

	@Override
	public void install(Workbench workbench) {
		workbench.installService(new LexiconService(workbench));
	}

}
