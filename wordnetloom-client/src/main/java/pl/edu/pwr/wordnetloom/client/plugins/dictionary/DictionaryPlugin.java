package pl.edu.pwr.wordnetloom.client.plugins.dictionary;

import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Plugin;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

public class DictionaryPlugin implements Plugin {

    @Override
    public void install(Workbench workbench) {
        workbench.installService(new DictionaryService(workbench));
    }

}
