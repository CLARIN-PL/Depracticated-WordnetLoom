package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.models;

import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.List;

public class RelationTreeModel extends DefaultTreeModel {


//    private List<RelationTypeNode> relationTypeNodes;

    public RelationTreeModel() {
        super(new RelationTypeNode(null));
//        relationTypeNodes = new ArrayList<>();
    }

    public void setRelationTypeNodes(List<RelationType> relationTypes) {
        RelationTypeNode rootNode = ((RelationTypeNode) root);
        rootNode.removeAllChildren();
        for (RelationType relationType : relationTypes) {
            rootNode.addChild(new RelationTypeNode(relationType));
        }
//        ((RelationTypeNode)root).setChildren(relationTypeNodes);
//        relationTypeNodes.clear();
//        RelationTypeNode node;
//        for(IRelationType relationType: relationTypes){
//            node = new RelationTypeNode(relationType);
//            relationTypeNodes.add(node);
//        }
    }

    /** Dodaje węzeł do korzenia */
//    public void addNode(RelationTypeNode node){
//        relationTypeNodes.add(node);
//    }

    /**
     * Usuwa węzeł z korzenia
     */
//    public void removeNode(RelationTypeNode node){
//        relationTypeNodes.remove(node);
//    }
    public void swapNode(RelationTypeNode node, int currentPosition) {
        RelationTypeNode parent = node.getParent();
        int nodeIndex = parent.getIndex(node);
        parent.swapChildren(nodeIndex, currentPosition);
    }

    public void addNode(RelationType relationType) {
        ((RelationTypeNode) root).addChild(new RelationTypeNode(relationType));
    }

    //TODO mozna zmienic nazwę
    public void moveNodeUp(RelationTypeNode node) {
        RelationTypeNode parent = node.getParent();
        if (parent != null) {
            int nodeIndex = parent.getIndex(node);
            if (nodeIndex != 0) {
                parent.swapChildren(nodeIndex, nodeIndex - 1);
            }
        }
    }

    public void moveNodeDown(RelationTypeNode node) {
        RelationTypeNode parent = node.getParent();
        if (parent != null) {
            int nodeIndex = parent.getIndex(node);
            if (nodeIndex != parent.getChildCount() - 1) {
                parent.swapChildren(nodeIndex, nodeIndex + 1);
            }
        }
    }

    public void addChildren(int parentIndex, List<RelationType> children) {
        assert parentIndex >= 0 && parentIndex < root.getChildCount() - 1;
        //relationTypeNodes.get(parentIndex).setChildren(children);
//        List<RelationTypeNode> childNodes = new ArrayList<>();
//        for(IRelationType type : children){
//            childNodes.add(new RelationTypeNode(type));
//        }
//        relationTypeNodes.get(parentIndex).setChildren(childNodes);
        RelationTypeNode parent = (RelationTypeNode) root.getChildAt(parentIndex);
        for (RelationType child : children) {
            parent.addChild(new RelationTypeNode(child));
        }
    }

    public void addChild(RelationTypeNode parent, RelationType child) {
        RelationTypeNode childNode = new RelationTypeNode(child);
        parent.addChild(childNode);
    }

    /*public void removeAllChildren(int parentIndex) {
        assert parentIndex >= 0 && parentIndex < relationTypeNodes.size() -1;
//        getChildren(relationTypeNodes.get(parentIndex)).clear();
        relationTypeNodes.get(parentIndex).removeAllChildren();
    }

    public void removeChild(int parentIndex, int childIndex){
        assert parentIndex >= 0 && parentIndex < relationTypeNodes.size() -1;
//        getChildren(relationTypeNodes.get(parentIndex)).remove(childIndex);
        relationTypeNodes.get(parentIndex).removeChild(childIndex);
    }*/

    public void removeChild(RelationTypeNode parent, RelationTypeNode child) {
        parent.removeChild(child);
    }

    public void removeChild(RelationTypeNode child) {
        if (child.getParent() != null) {
            child.getParent().removeChild(child);
        }
    }

    /**
     * Usuwa wszystkie węzły z wyjątkiem korzenia
     */
    public void clear() {
        RelationTypeNode treeNode;
        RelationTypeNode rootNode = (RelationTypeNode) root;
        for (int node = 0; node < root.getChildCount(); node++) {
            rootNode.getChildAt(node).removeAllChildren();
            rootNode.removeChild(node);
        }
    }

    public TreePath getPathToNode(RelationTypeNode relationTypeNode) {
        Object[] objects = new Object[relationTypeNode.getLevel() + 1];
        objects[0] = (TreeNode) root;
        RelationTypeNode node = relationTypeNode;
        int counter = node.getLevel();
        while (counter >= 1) {
            objects[counter] = node;
            node = node.getParent();
            counter--;
        }
        return new TreePath(objects);
    }

    @Override
    public RelationTypeNode getRoot() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return (RelationTypeNode) root;
    }

    @Override
    public RelationTypeNode getChild(Object parent, int index) {
        return ((RelationTypeNode) parent).getChildAt(index);
    }

//    private List<RelationTypeNode> getChildren(Object parent)
//    {
//        return ((RelationTypeNode)parent).getChildren();
//    }

    @Override
    public int getChildCount(Object parent) {
        return ((RelationTypeNode) parent).getChildCount();
    }

    @Override
    public boolean isLeaf(Object node) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return ((RelationTypeNode) node).isLeaf();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return ((RelationTypeNode) parent).getIndex((RelationTypeNode) child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        super.addTreeModelListener(l);
        listenerList.add(TreeModelListener.class, l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        super.addTreeModelListener(l);
        listenerList.remove(TreeModelListener.class, l);
    }


//    private class RelationTypeNode {
//        private IRelationType value;
//        private List<IRelationType> children;
//
//        public RelationTypeNode(){
//            children = new ArrayList<>();
//        }
//
//        public RelationTypeNode(IRelationType relationType){
//            value = relationType;
//            children = new ArrayList<>();
//        }
//
//        public IRelationType getValue() { return value;}
//        public List<IRelationType> getChildren() {return children;}
//
//        public void setValue(IRelationType value) { this.value = value;}
//        public void setChildren(List<IRelationType> children) { this.children = children;}
//
//        public void addChild(IRelationType relationType) { children.add(relationType);}
//        public void removeChild(IRelationType relationType) {children.remove(relationType);}
//        public void removeChild(int index) {children.remove(index);}
//
//        @Override
//        public String toString(){
//            if(value != null){
//                String locale = RemoteConnectionProvider.getInstance().getLanguage();
//                return value.getName(locale);
//            }
//            return null;
//        }
//    }


//    private EventListenerList listeners = new EventListenerList();
//    private IRelationType[] relationTable = new IRelationType[0];
//    private Object root = Labels.RELATIONS;
//
//    /**
//     * konstruktor
//     *
//     * @param relations - relacje do przechowania
//     */
//    public RelationTreeModel(Collection<IRelationType> relations) {
//        super();
//        setData(relations);
//    }
//
//    /**
//     * konstruktor
//     *
//     */
//    public RelationTreeModel() {
//        super();
//    }
//
//    /*
//     *  (non-Javadoc)
//     * @see javax.swing.tree.TreeModel#getRoot()
//     */
//    public Object getRoot() {
//        return root;
//    }
//
//    /*
//	 *  (non-Javadoc)
//	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
//     */
//    public Object getChild(Object obj, int index) {
//        if (obj == root) {
//            return relationTable[index];
//        }
//        IRelationType relType = (IRelationType) obj;
//        Collection<IRelationType> children = RemoteUtils.relationTypeRemote.dbGetChildren(relType, LexiconManager.getInstance().getLexicons());
//        for (IRelationType child : children) {
//            if (index-- == 0) {
//                return child;
//            }
//        }
//        return null;
//    }
//
//    /*
//	 *  (non-Javadoc)
//	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
//     */
//    public int getChildCount(Object obj) {
//        if (obj == root) {
//            return relationTable.length;
//        }
//        IRelationType relType = (IRelationType) obj;
//        return RemoteUtils.relationTypeRemote.dbGetChildren(relType, LexiconManager.getInstance().getLexicons()).size();
//    }
//
//    @Override
//    public boolean isLeaf(Object obj) {
//        if (obj == root) {
//            return relationTable.length == 0;
//        }
//        IRelationType relType = (IRelationType) obj;
//        return RemoteUtils.relationTypeRemote.dbGetChildren(relType, LexiconManager.getInstance().getLexicons()).isEmpty();
//    }
//
//    @Override
//    public void valueForPathChanged(TreePath arg0, Object arg1) {
//    }
//
//    @Override
//    public int getIndexOfChild(Object arg0, Object arg1) {
//        int i = 0;
//        IRelationType child = (IRelationType) arg1;
//
//        if (arg0 == root) {
//            for (IRelationType c : relationTable) {
//                if (c.equals(child)) {
//                    return i;
//                }
//                i++;
//            }
//            return -1;
//        } else {
//            IRelationType parent = (IRelationType) arg0;
//            for (IRelationType c : RemoteUtils.relationTypeRemote.dbGetChildren(parent, LexiconManager.getInstance().getLexicons())) {
//                if (c.equals(child)) {
//                    return i;
//                }
//                i++;
//            }
//            return -1;
//        }
//    }
//
//    @Override
//    public void addTreeModelListener(TreeModelListener dataListener) {
//        listeners.add(TreeModelListener.class, dataListener);
//    }
//
//    @Override
//    public void removeTreeModelListener(TreeModelListener dataListener) {
//        listeners.remove(TreeModelListener.class, dataListener);
//    }
//
//    /**
//     * ustawienie danych do przechowania
//     *
//     * @param relations - dane do ustawienia
//     */
//    public void setData(Collection<IRelationType> relations) {
//        relationTable = new IRelationType[relations.size()];
//        relationTable = relations.toArray(relationTable);
//        // wywołanie wszystkich nasłuchujących
//        for (TreeModelListener listener : listeners.getListeners(TreeModelListener.class)) {
//            listener.treeStructureChanged(null);
//        }
//    }
//
//    private void moveRel_(TreePath path, int offset) {
//        IRelationType parent = null;
//        IRelationType child = null;
//
//        child = (IRelationType) path.getLastPathComponent();
//        int p_len = path.getPathCount();
//        if (path.getPathCount() > 2) {
//            parent = (IRelationType) path.getPath()[p_len - 2];
//        }
//
//        if (parent == null) {
//            int i = 0;
//            while (i < relationTable.length && !relationTable[i].equals(child)) {
//                i++;
//            }
//
//            IRelationType curr = relationTable[i];
//            relationTable[i] = relationTable[i + offset];
//            relationTable[i + offset] = curr;
//        } else {
//            IRelationType[] children = new IRelationType[RemoteUtils.relationTypeRemote.dbGetChildren(parent, LexiconManager.getInstance().getLexicons()).size()];
//            children = (IRelationType[]) RemoteUtils.relationTypeRemote.dbGetChildren(parent, LexiconManager.getInstance().getLexicons()).toArray(new IRelationType[]{});
//
//            int i = 0;
//            while (i < children.length && !children[i].equals(child)) {
//                i++;
//            }
//
//            IRelationType curr = children[i];
//            children[i] = children[i + offset];
//            children[i + offset] = curr;
//
//            for (int j = 0; j < children.length; j++) {
//                IRelationType rt = children[j];
//                rt.setParent(parent);
//            }
//        }
//
//        for (TreeModelListener listener : listeners.getListeners(TreeModelListener.class)) {
//            TreePath p = new TreePath(root);
//            TreeModelEvent e = new TreeModelEvent(this, p);
//            listener.treeStructureChanged(e);
//        }
//    }
//
//    public void moveDown(TreePath path) {
//        moveRel_(path, 1);
//    }
//
//    public void moveUp(TreePath path) {
//        moveRel_(path, -1);
//    }
}
