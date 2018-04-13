package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.models;

import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RelationTypeNode extends DefaultMutableTreeNode {

    private RelationType relationType;

    public RelationTypeNode(Object userObject) {
        super(userObject);
        if (userObject instanceof RelationType) {
            relationType = (RelationType) userObject;
        }
    }

    @Override
    public String toString() {
        if (userObject instanceof RelationType) {
            RelationType r = (RelationType) userObject;
            return LocalisationManager.getInstance().getLocalisedString(r.getName());
        }
        return super.toString();
    }

    public RelationType getRelationType(){
        return relationType;
    }
}
