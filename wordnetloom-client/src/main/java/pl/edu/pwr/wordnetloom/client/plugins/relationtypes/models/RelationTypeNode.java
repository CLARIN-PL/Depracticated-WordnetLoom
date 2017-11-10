package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.models;

import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class RelationTypeNode implements TreeNode {
    private RelationTypeNode parent;
    private RelationType value;
    private List<RelationTypeNode> children;
    private int level;

    public static final int ROOT_LEVEL = 0;
    public static final int TYPE_LEVEL = 1;
    public static final int SUBTYPE_LEVEL = 2;

    public RelationTypeNode(@NotNull RelationType relationType) {
        init(relationType, null);
    }

    public RelationTypeNode(@NotNull RelationType relationType, RelationTypeNode parent) {
        init(relationType, parent);
    }

    private void init(RelationType relationType, RelationTypeNode parent) {
        value = relationType;
        this.parent = parent;
        children = new ArrayList<>();
        level = ROOT_LEVEL;
    }

    public void setValue(RelationType value) {
        this.value = value;
    }

    public RelationType getValue() {
        return value;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setChildren(List<RelationTypeNode> children) {
        if (level != SUBTYPE_LEVEL) {
            this.children = children;
            for (RelationTypeNode child : children) {
                child.setParent(this);
                child.setLevel(level + 1);
            }
        }
    }

    public void addChild(RelationTypeNode child) {
        if (level != SUBTYPE_LEVEL) { // nie można dodać dziecka, jeżeli węzeł jest na poziomie odtypów
            child.setParent(this);
            child.setLevel(level + 1);
            children.add(child);
        }
    }

    public void addChild(RelationType child) {
        if (level != SUBTYPE_LEVEL) {
            RelationTypeNode childNode = new RelationTypeNode(child);
            addChild(childNode);
        }
    }

    public void setParent(RelationTypeNode parent) {
        this.parent = parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public void removeChild(RelationTypeNode child) {
        child.setParent(null);
        children.remove(child);
    }

    public void removeChild(int childIndex) {
        children.remove(childIndex);
    }

    public void swapChildren(int currentIndex, int targetIndex) {
        assert currentIndex >= 0 && currentIndex < children.size() - 1;
        assert targetIndex >= 0 && targetIndex < children.size() - 1;
        Collections.swap(children, currentIndex, targetIndex);
    }

    @Override
    public RelationTypeNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public RelationTypeNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
        return level != SUBTYPE_LEVEL;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public Enumeration children() {
        return null;
    }

    public void removeAllChildren() {
        for (RelationTypeNode child : children) {
            child.setParent(null);
        }
        children.clear();
    }

    @Override
    public String toString() {
        if (value != null) {
            String locale = RemoteConnectionProvider.getInstance().getLanguage();
            return "";// value.getName(locale);
        }
        return "";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof RelationTypeNode && value != null) {
            RelationTypeNode otherNode = (RelationTypeNode) other;
            return value.equals(otherNode.getValue());
        } else {
            return false;
        }
    }

    public TreePath getPath() {
        Object[] nodes = new Object[level + 1];
        RelationTypeNode node = this;
        int index = level;
        do {
            nodes[index--] = node;
            node = node.getParent();
        } while (node != null);
        return new TreePath(nodes);
    }
}