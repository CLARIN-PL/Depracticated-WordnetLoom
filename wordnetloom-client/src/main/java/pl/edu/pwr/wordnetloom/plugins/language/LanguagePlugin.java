package pl.edu.pwr.wordnetloom.plugins.language;

import pl.edu.pwr.wordnetloom.workbench.interfaces.Plugin;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;

public class LanguagePlugin implements Plugin {

	@Override
	public void install(Workbench workbench) {
		workbench.installService(new LanguageService(workbench));
	}

}
