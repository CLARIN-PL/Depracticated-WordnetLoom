package pl.edu.pwr.wordnetloom.plugins.dictionary;

import pl.edu.pwr.wordnetloom.workbench.interfaces.Plugin;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;

public class DictionaryPlugin implements Plugin {

	@Override
	public void install(Workbench workbench) {
		workbench.installService(new DictionaryService(workbench));
	}

}
