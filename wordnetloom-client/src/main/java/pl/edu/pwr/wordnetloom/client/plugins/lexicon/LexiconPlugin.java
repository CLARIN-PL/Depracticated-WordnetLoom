package pl.edu.pwr.wordnetloom.client.plugins.lexicon;

import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Plugin;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

public class LexiconPlugin implements Plugin {

    @Override
    public void install(Workbench workbench) {
        workbench.installService(new LexiconService(workbench));
    }
}
