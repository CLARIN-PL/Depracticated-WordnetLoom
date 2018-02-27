package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.events;

import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

public class ShowRelationTestsEvent {

    private final RelationType relationType;

    public ShowRelationTestsEvent(RelationType relationType) {
        this.relationType = relationType;
    }

    public RelationType getRelationType() {
        return relationType;
    }
}
