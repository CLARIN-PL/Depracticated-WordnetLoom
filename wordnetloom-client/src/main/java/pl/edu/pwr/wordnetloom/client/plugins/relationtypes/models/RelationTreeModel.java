package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.models;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class RelationTreeModel implements TreeModel {

    @Override
    public Object getRoot() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getChild(Object parent, int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getChildCount(Object parent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isLeaf(Object node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    private EventListenerList listeners = new EventListenerList();
//    private RelationType[] relationTable = new RelationType[0];
//    private Object root = Labels.RELATIONS;
//
//    /**
//     * konstruktor
//     *
//     * @param relations - relacje do przechowania
//     */
//    public RelationTreeModel(Collection<RelationType> relations) {
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
//        RelationType relType = (RelationType) obj;
//        Collection<RelationType> children = RemoteUtils.relationTypeRemote.dbGetChildren(relType, LexiconManager.getInstance().getLexicons());
//        for (RelationType child : children) {
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
//        RelationType relType = (RelationType) obj;
//        return RemoteUtils.relationTypeRemote.dbGetChildren(relType, LexiconManager.getInstance().getLexicons()).size();
//    }
//
//    @Override
//    public boolean isLeaf(Object obj) {
//        if (obj == root) {
//            return relationTable.length == 0;
//        }
//        RelationType relType = (RelationType) obj;
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
//        RelationType child = (RelationType) arg1;
//
//        if (arg0 == root) {
//            for (RelationType c : relationTable) {
//                if (c.equals(child)) {
//                    return i;
//                }
//                i++;
//            }
//            return -1;
//        } else {
//            RelationType parent = (RelationType) arg0;
//            for (RelationType c : RemoteUtils.relationTypeRemote.dbGetChildren(parent, LexiconManager.getInstance().getLexicons())) {
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
//    public void setData(Collection<RelationType> relations) {
//        relationTable = new RelationType[relations.size()];
//        relationTable = relations.toArray(relationTable);
//        // wywołanie wszystkich nasłuchujących
//        for (TreeModelListener listener : listeners.getListeners(TreeModelListener.class)) {
//            listener.treeStructureChanged(null);
//        }
//    }
//
//    private void moveRel_(TreePath path, int offset) {
//        RelationType parent = null;
//        RelationType child = null;
//
//        child = (RelationType) path.getLastPathComponent();
//        int p_len = path.getPathCount();
//        if (path.getPathCount() > 2) {
//            parent = (RelationType) path.getPath()[p_len - 2];
//        }
//
//        if (parent == null) {
//            int i = 0;
//            while (i < relationTable.length && !relationTable[i].equals(child)) {
//                i++;
//            }
//
//            RelationType curr = relationTable[i];
//            relationTable[i] = relationTable[i + offset];
//            relationTable[i + offset] = curr;
//        } else {
//            RelationType[] children = new RelationType[RemoteUtils.relationTypeRemote.dbGetChildren(parent, LexiconManager.getInstance().getLexicons()).size()];
//            children = (RelationType[]) RemoteUtils.relationTypeRemote.dbGetChildren(parent, LexiconManager.getInstance().getLexicons()).toArray(new RelationType[]{});
//
//            int i = 0;
//            while (i < children.length && !children[i].equals(child)) {
//                i++;
//            }
//
//            RelationType curr = children[i];
//            children[i] = children[i + offset];
//            children[i + offset] = curr;
//
//            for (int j = 0; j < children.length; j++) {
//                RelationType rt = children[j];
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
