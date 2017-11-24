package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.events;

import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

public class ShowRelationTypeEvent {

    private final RelationType relationType;

    private final RelationArgument argument;

    public ShowRelationTypeEvent(RelationType relationType, RelationArgument argument) {
        this.relationType = relationType;
        this.argument = argument;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public RelationArgument getArgument() {
        return argument;
    }
}
