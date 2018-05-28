package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators;

import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

public class SenseFormat {

    private static final String SENSE_FORMAT = "%s %s* (%s)";

    public static String getText(Sense sense) {
        if(sense.getWord() == null){
            return "";
        }
        String word = sense.getWord().getWord();
        String variant = String.valueOf(sense.getVariant());
        String domain = LocalisationManager.getInstance().getLocalisedString(sense.getDomain().getName());

        return String.format(SENSE_FORMAT, word, variant, domain);
    }

    public static String getText(DataEntry dataEntry) {
        String word = dataEntry.getName();
        String variant = dataEntry.getVariant();
        String domain = LocalisationManager.getInstance().getLocalisedString(dataEntry.getDomain());

        return String.format(SENSE_FORMAT, word, variant, domain);
    }

    public static String getTextWithLexicon(Sense sense) {
        if(sense.getLexicon() == null){
            return "";
        }
        String lexicon = sense.getLexicon().getIdentifier();
        return getText(sense) + " " + lexicon;
    }
}
