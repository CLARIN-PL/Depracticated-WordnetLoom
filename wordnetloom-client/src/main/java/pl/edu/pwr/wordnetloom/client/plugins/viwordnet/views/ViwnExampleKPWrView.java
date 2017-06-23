package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views;

import java.util.ArrayList;
import java.util.List;
import pl.edu.pwr.wordnetloom.client.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractView;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.corpusexample.model.CorpusExample;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.word.model.Word;

public class ViwnExampleKPWrView extends AbstractView {

    public static final String KPWR_TAG = "[KPWr]";
    private final ViwnExampleKPWrViewUI ui = (ViwnExampleKPWrViewUI) getUI();

    public ViwnExampleKPWrView(Workbench workbench, String title, ViwnGraphViewUI graphUI) {
        super(workbench, title, new ViwnExampleKPWrViewUI(graphUI));
    }

    public void load_examples(Sense unit) {
        List<CorpusExample> examples = RemoteUtils.testRemote.getCorpusExamplesFor(unit.getLemma());

        List<String> exampleList = new ArrayList<>();
        for (CorpusExample ex : examples) {
            String text = ex.getText();
            if (text.contains(KPWR_TAG)) {
                exampleList.add(text);
            }
        }
        ui.setExampleList(exampleList, unit);
    }

    public void load_examples(String lemma) {
        List<CorpusExample> examples = RemoteUtils.testRemote.getCorpusExamplesFor(new Word(lemma));

        StringBuilder b = new StringBuilder();
        List<String> exampleList = new ArrayList<>();
        for (CorpusExample ex : examples) {
            exampleList.add(ex.getText());
        }
        ui.setExampleList(exampleList, null);
    }
}
