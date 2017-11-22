package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.models;

import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RelationTreeModel implements TreeModel {

    private final EventListenerList listeners = new EventListenerList();
    private final Map<Integer, RelationType> relations = new HashMap<>();
    private final Object root = Labels.RELATIONS;


    public RelationTreeModel(final Collection<RelationType> relations) {
        super();
        setRelationTypes(relations);
    }

    public void setRelationTypes(final Collection<RelationType> relations) {
        this.relations.clear();
        AtomicInteger index = new AtomicInteger();
        relations.forEach(r -> this.relations.put(index.getAndIncrement(), r));

        for (TreeModelListener listener : listeners.getListeners(TreeModelListener.class)) {
            listener.treeStructureChanged(null);
        }
    }

    public RelationTreeModel() {
        super();
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return relations.get(index);
    }

    private int countChildren(Object parent) {
        return (int) relations.entrySet()
                .stream()
                .filter(o -> o.getValue().getParent().getId().equals(((RelationType) parent).getId()))
                .count();
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent == root) {
            return relations.size();
        }
        return countChildren(parent);
    }

    @Override
    public boolean isLeaf(Object node) {
        if (node == root) {
            return relations.size() == 0;
        }
        return countChildren(node) == 0;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {

        RelationType childRelation = (RelationType) child;
        RelationType parentRelation = (RelationType) parent;

        return relations
                .entrySet()
                .stream()
                .filter(e -> e.getValue().getParent().getId().equals(parentRelation.getId()))
                .filter(e -> e.getValue().getId().equals(childRelation.getId()))
                .map(Map.Entry::getKey)
                .findFirst().orElse(-1);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(TreeModelListener.class, l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(TreeModelListener.class, l);
    }
}

/*

    private void moveRel_(TreePath path, int offset) {
        RelationType parent = null;
        RelationType child = null;

        child = (RelationType) path.getLastPathComponent();
        int p_len = path.getPathCount();
        if (path.getPathCount() > 2) {
            parent = (RelationType) path.getPath()[p_len - 2];
        }

        if (parent == null) {
            int i = 0;
            while (i < relationTable.length && !relationTable[i].equals(child)) {
                i++;
            }

//			PairUtils<RelationType, RelationType> result = RemoteUtils.relationTypeRemote.dbSwitchOrder(relationTable[i], relationTable[i+offset]);
//			relationTable[i] = result.first;
//			relationTable[i+offset] = result.second;
            RelationType curr = relationTable[i];
            relationTable[i] = relationTable[i + offset];
            relationTable[i + offset] = curr;
        } else {
            RelationType[] children = new RelationType[RemoteUtils.relationTypeRemote.dbGetChildren(parent, LexiconManager.getInstance().getLexicons()).size()];
            children = (RelationType[]) RemoteUtils.relationTypeRemote.dbGetChildren(parent, LexiconManager.getInstance().getLexicons()).toArray(new RelationType[]{});

            int i = 0;
            while (i < children.length && !children[i].equals(child)) {
                i++;
            }

//			PairUtils<RelationType, RelationType> result = RemoteUtils.relationTypeRemote.dbSwitchOrder(children[i], children[i+offset]);
//			children[i] = result.first;
//			children[i+offset] = result.second;
            RelationType curr = children[i];
            children[i] = children[i + offset];
            children[i + offset] = curr;

            for (int j = 0; j < children.length; j++) {
                RelationType rt = children[j];
                rt.setParent(parent);
            }
        }

        for (TreeModelListener listener : listeners.getListeners(TreeModelListener.class)) {
            TreePath p = new TreePath(root);
            TreeModelEvent e = new TreeModelEvent(this, p);
            listener.treeStructureChanged(e);
        }
    }

    public void moveDown(TreePath path) {
        moveRel_(path, 1);
    }

    public void moveUp(TreePath path) {
        moveRel_(path, -1);
    }
*/