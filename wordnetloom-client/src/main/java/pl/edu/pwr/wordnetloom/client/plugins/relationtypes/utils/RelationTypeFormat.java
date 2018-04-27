package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.utils;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

public class RelationTypeFormat {

    public static String getText(RelationType relationType)
    {
        String text;
        String relationName = LocalisationManager.getInstance().getLocalisedString(relationType.getName());
        RelationType parentRelation;

        parentRelation = RemoteService.relationTypeRemote.findParent(relationType.getId());

        if(parentRelation != null) {
            String parentRelationName = LocalisationManager.getInstance().getLocalisedString(parentRelation.getName());
            text = parentRelationName;
            text+="("+relationName+")";
        } else {
            text = relationName;
        }
        return text;
    }
}
