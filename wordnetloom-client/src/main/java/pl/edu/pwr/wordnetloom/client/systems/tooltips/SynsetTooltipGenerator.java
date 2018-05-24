package pl.edu.pwr.wordnetloom.client.systems.tooltips;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.security.UserSessionContext;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

import java.util.List;

public class SynsetTooltipGenerator implements ToolTipGeneratorInterface {

    private ToolTipBuilder builder;

    public SynsetTooltipGenerator() {
        builder = new ToolTipBuilder();
    }

    @Override
    public String getToolTipText(Object object) {
        if (!hasEnabledTooltips()) {
            return null;
        }
        Synset synset = (Synset) object;
        Synset fetchedSynset = RemoteService.synsetRemote.fetchSynset(synset.getId());
        SynsetAttributes attributes = RemoteService.synsetRemote.fetchSynsetAttributes(synset.getId());
        //TODO sprawdzić, czy wystarczy pobrać tylko relację, gdzie synset jest relacja
        List<SynsetRelation> synsetRelations = RemoteService.synsetRelationRemote.findRelationsWhereSynsetIsParent(synset, LexiconManager.getInstance().getUserChosenLexiconsIds(), NodeDirection.values());

        return getSenseToolTipText(fetchedSynset, attributes, synsetRelations);
    }

    private String getSenseToolTipText(Synset synset, SynsetAttributes attributes, List<SynsetRelation> relations) {
        builder.clear();

        if (attributes != null) {
            builder.addDefinition(attributes.getDefinition())
                    .addArtificial(synset.getAbstract());
        } else {
            builder.addArtificial(false);
        }
        builder.addStatus(synset.getStatus());

        Sense headSense = synset.getSenses().get(0);
        builder.addDomain(headSense.getDomain());
        if (attributes != null) {
            builder.addOwner(attributes.getOwner())
                    .addSynsetComment(attributes.getComment());
        }
        SenseAttributes senseAttributes = RemoteService.senseRemote.fetchSenseAttribute(headSense.getId());
        if (senseAttributes != null) {
            builder.addSenseComment(senseAttributes.getComment());
        }
        if (relations != null && !relations.isEmpty()) {
            builder.addSynsetRelations(relations);
        }

        return builder.buildText();
    }

    @Override
    public boolean hasEnabledTooltips() {
        return UserSessionContext.getInstance().getUserSettings().getShowToolTips();
    }
}
