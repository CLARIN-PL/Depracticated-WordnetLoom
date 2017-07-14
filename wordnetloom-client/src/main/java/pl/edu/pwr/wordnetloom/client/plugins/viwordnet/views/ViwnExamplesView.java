package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractView;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.corpusexample.model.CorpusExample;

public class ViwnExamplesView extends AbstractView {

    public ViwnExamplesView(Workbench workbench, String title) {
        super(workbench, title, new ViwnExamplesViewUI());
    }

    public void load_examples(String lemma) {
        List<CorpusExample> examples = new ArrayList<>();// RemoteUtils.testRemote.getCorpusExamplesFor(new Word(lemma));

        ViwnExamplesViewUI ui = (ViwnExamplesViewUI) getUI();
        StringBuilder b = new StringBuilder();

        b.append("<html>");
        String[] colors = new String[]{"#F5F5F5", "#DCDCDC"};
        int count = 0;
        for (CorpusExample ex : examples) {
            String text = ex.getText();
            if (!text.contains(ViwnExampleKPWrView.KPWR_TAG)) {
                Pattern p = Pattern.compile("(.*)_(.*)_(.*)");
                Matcher m = p.matcher(text);
                b.append("<p style=\"background-color:")
                        .append(colors[count % 2])
                        .append(";margin-top: 0px;margin-bottom: 5px;\">");
                if (m.matches()) {
                    b.append(m.group(1));
                    b.append("<b>");
                    b.append(m.group(2));
                    b.append("</b>");
                    b.append(m.group(3));
                } else {
                    b.append(text);
                    b.append("</p>");
                    count++;
                }
            }
        }
        b.append("</html>");

        ui.make_tree(b.toString());
    }
}
