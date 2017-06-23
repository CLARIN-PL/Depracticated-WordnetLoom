/*
    Copyright (C) 2011 Łukasz Jastrzębski, Paweł Koczan, Michał Marcińczuk,
                       Bartosz Broda, Maciej Piasecki, Adam Musiał,
                       Radosław Ramocki, Michał Stanek
    Part of the WordnetLoom

    This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 3 of the License, or (at your option)
any later version.

    This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE.

    See the LICENSE and COPYING files for more details.
 */
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.edu.pwr.wordnetloom.client.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractView;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.corpusexample.model.CorpusExample;
import pl.edu.pwr.wordnetloom.word.model.Word;

public class ViwnExamplesView extends AbstractView {

    public ViwnExamplesView(Workbench workbench, String title) {
        super(workbench, title, new ViwnExamplesViewUI());
    }

    public void load_examples(String lemma) {
        List<CorpusExample> examples = RemoteUtils.testRemote.getCorpusExamplesFor(new Word(lemma));

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
