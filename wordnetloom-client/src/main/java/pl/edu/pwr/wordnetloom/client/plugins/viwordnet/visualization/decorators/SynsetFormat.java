package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators;

import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

public class SynsetFormat {

    private final static String HTML_HEADER = "<html><body style=\"font-weight:normal\">";
    private final static String HTML_FOOTER = "</body></html>";
    private final static String DESC_STR = "<div style=\"text-align:left; margin-left:10px; width:250px;\">%s</div>";

    /**
     * Return synset description in html mode or as plain text. If width is positive result is in html format.
     * @param synset
     * @param width width od list
     * @return
     */
    private static String getText(Synset synset, int width) {
        boolean htmlFormat = width > 0;
        StringBuilder nameBuilder = new StringBuilder();
        Sense sense;
        if(htmlFormat) {
            nameBuilder.append("<div style=\"text-align:left; margin-left:10px; width:"+width+"px;\">");
        }
        nameBuilder.append("[");
        for(int i=0; i < synset.getSenses().size(); i++) {
            sense = synset.getSenses().get(i);
            if(htmlFormat && i == 0){
                nameBuilder.append("<font color=\"blue\">");
                nameBuilder.append(SenseFormat.getText(sense));
                nameBuilder.append("</font>");
            } else {
                nameBuilder.append(SenseFormat.getText(sense));
            }
            if(i != synset.getSenses().size() -1) {
                nameBuilder.append(" | ");
            }
        }
        nameBuilder.append("]");
        if(htmlFormat){
            nameBuilder.append("</div>");
            return HTML_HEADER + nameBuilder.toString() + HTML_FOOTER;
        }
        return nameBuilder.toString();
    }

    public static String getText(Synset synset) {
        return getText(synset, -1);
    }

    public static String getHtmlText(Synset synset, int width) {
        return getText(synset, width);
    }
}
