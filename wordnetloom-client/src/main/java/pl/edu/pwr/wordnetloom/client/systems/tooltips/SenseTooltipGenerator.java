package pl.edu.pwr.wordnetloom.client.systems.tooltips;

import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;

import java.util.List;

public class SenseTooltipGenerator implements ToolTipGeneratorInterface {

    private ToolTipBuilder builder;

    public SenseTooltipGenerator() {
        builder = new ToolTipBuilder();
    }

    @Override
    public String getToolTipText(Object object) {
        if (!hasEnabledTooltips()) {
            return null;
        }
        Sense sense = (Sense) object;
        Sense fetchedSense = RemoteService.senseRemote.fetchSense(sense.getId());
        // we must get relations from database, because collections are lazy
        List<SenseRelation> relations = RemoteService.senseRelationRemote.findRelations(sense, null, true, false);
        return getSenseTooltipText(fetchedSense, relations);
    }

    private String getSenseTooltipText(Sense sense, List<SenseRelation> relations) {
        builder.clear();
        builder.addLexicon(sense.getLexicon())
                .addDomain(sense.getDomain())
                .addPartOfSpeech(sense.getPartOfSpeech());
        SenseAttributes attributes = sense.getSenseAttributes();
        if (attributes != null) {
            builder.addRegister(attributes.getRegister())
                    .addDefinition(attributes.getDefinition());
        }
        builder.addExamples(sense.getExamples());
        if (relations != null && !relations.isEmpty()) {
            builder.addSenseRelations(relations);
        }
        return builder.buildText();
    }

    @Override
    public boolean hasEnabledTooltips() {
        return RemoteConnectionProvider.getInstance().getUser().getSettings().getShowToolTips();
    }
}
