package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.models;

import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.swing.tree.DefaultMutableTreeNode;

public class RelationTypeNode extends DefaultMutableTreeNode {

    public RelationTypeNode(Object userObject) {
        super(userObject);
    }

    @Override
    public String toString() {
        if (userObject instanceof RelationType) {
            RelationType r = (RelationType) userObject;
            return LocalisationManager.getInstance().getLocalisedString(r.getName());
        }
        return super.toString();
    }
}
