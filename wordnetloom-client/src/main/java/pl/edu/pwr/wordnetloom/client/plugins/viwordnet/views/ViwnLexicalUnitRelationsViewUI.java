package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.tree.WebTree;
import org.hibernate.dialect.unique.DefaultUniqueDelegate;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * [View User Interface] Displays a list of lexical relations for given unit.
 *
 * @author Michał Marcińczuk <michal.marcinczuk@pwr.wroc.pl>
 */
public class ViwnLexicalUnitRelationsViewUI extends AbstractViewUI implements
        SimpleListenerInterface, ActionListener, TreeSelectionListener {

    private DefaultMutableTreeNode root = null;
    private DefaultMutableTreeNode root_to = null;
    private DefaultMutableTreeNode root_from = null;

    WebTree tree = null;

    MButton addRelation = null;
    MButton delRelation = null;

    Workbench workbench;

    public ViwnLexicalUnitRelationsViewUI(Workbench workbench) {
        super();
        this.workbench = workbench;
    }

    @Override
    public JComponent getRootComponent() {
        return null;
    }

    @Override
    protected void initialize(WebPanel content) {
        content.setLayout(new RiverLayout());

        root = new DefaultMutableTreeNode("");
        tree = new WebTree(root);
        tree.addTreeSelectionListener(this);
        tree.setCellRenderer(new ViwnLexicalUnitRelationRenderer());

        root_from = new DefaultMutableTreeNode(Labels.FROM);
        root_to = new DefaultMutableTreeNode(Labels.TO);

        root.add(root_from);
        root.add(root_to);

        addRelation = MButton.buildAddButton(this)
                .withEnabled(true)
                .withToolTip(Hints.ADD_RELATION_UNITS);

        installViewScopeShortCut(addRelation, 0, KeyEvent.VK_INSERT);

        delRelation = MButton.buildDeleteButton(this)
                .withEnabled(true)
                .withToolTip(Hints.REMOVE_RELTAION_UNITS);

        installViewScopeShortCut(delRelation, 0, KeyEvent.VK_DELETE);

        content.add("hfill vfill", new JScrollPane(tree));
        content.add("br center", addRelation);
        content.add(delRelation);
    }

    @Override
    public void doAction(Object object, int tag) {
        fillTree(object);
    }

    /**
     * @param object - lexical unit - root of the tree
     */
    private void fillTree(Object object) {

        SwingWorker<String, Object> worker = new SwingWorker<String, Object>() {

            @Override
            protected String doInBackground() throws Exception {
//                relations = RemoteService.senseRelationRemote.findFullRelations((Sense)object);
                root_from.removeAllChildren();
                root_to.removeAllChildren();
                List<SenseRelation> relationsFrom =  RemoteService.senseRelationRemote.findRelations((Sense)object, null, true, false);
                List<SenseRelation> relationTo = RemoteService.senseRelationRemote.findRelations((Sense)object, null, false, false);
                fillRootRelations(root_from, relationsFrom, true);
                fillRootRelations(root_to, relationTo, false);

                return null;
            }

            private void fillRootRelations(DefaultMutableTreeNode nodeToFill, List<SenseRelation> relations, boolean displayChild)
            {
                Map<Long, DefaultMutableTreeNode> relationTypeNodeMap = new HashMap<>();
                DefaultMutableTreeNode node;
                DefaultMutableTreeNode senseNode;
                Sense senseToDisplay;
                for(SenseRelation relation : relations) {
                    if(!relationTypeNodeMap.containsKey(relation.getRelationType().getId())){
                        node = new DefaultMutableTreeNode(relation);
                        node.setUserObject(relation);
                        relationTypeNodeMap.put(relation.getRelationType().getId(), node);
                    }

                    if(relation.getChild() != null && relation.getParent() != null){
                        senseToDisplay = displayChild ? relation.getChild() : relation.getParent();
                        senseNode = new DefaultMutableTreeNode(new Pair<>(senseToDisplay, relation));
                        DefaultMutableTreeNode parentNode = relationTypeNodeMap.get(relation.getRelationType().getId());
                        parentNode.add(senseNode);
                    }
                }
                for(Map.Entry<Long, DefaultMutableTreeNode> entry : relationTypeNodeMap.entrySet()){
                    nodeToFill.add(entry.getValue());
                }
            }

            @Override
            protected void done() {
                if (((DefaultMutableTreeNode) tree.getModel().getRoot())
                        .getUserObject() != object) {
                    // TODO: find better solution
                    // ugly hack, to switch button off after changing tree
                    delRelation.setEnabled(false);
                }
                root.setUserObject(object);

                ((DefaultTreeModel) tree.getModel()).reload();
                tree.updateUI();

                expandAll(true);
            }

        };
        worker.execute();
    }

    /**
     * refresh tree with root unchanged
     */
    public void refresh() {
        fillTree(((DefaultMutableTreeNode) root.getRoot()).getUserObject());
    }

    /**
     * If expand is true, expands all nodes in the tree. Otherwise, collapses
     * all nodes in the tree.
     *
     * @param expand
     * @author amusial
     * @author unknown, source from
     * http://www.exampledepot.com/egs/javax.swing.tree/ExpandAll.html
     */
    public void expandAll(boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }

    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    /**
     * @author amusial <b>ViwnLexicalUnitRelationRenderer</b> class providing
     * non ordinary rendering of tree nodes
     */
    private class ViwnLexicalUnitRelationRenderer extends
            DefaultTreeCellRenderer {

        /**
         *
         */
        private static final long serialVersionUID = 3596291005138652114L;

        /**
         * default constructor
         */
        public ViwnLexicalUnitRelationRenderer() {
            super();
        }

        @Override
        public java.awt.Component getTreeCellRendererComponent(JTree tree,
                                                               Object value, boolean sel, boolean expanded, boolean leaf,
                                                               int row, boolean hasFocus) {

            // use ordinary renderer
            super.getTreeCellRendererComponent(tree, value, sel, expanded,
                    leaf, row, hasFocus);

            // and modify default settings
            // check, only for safety
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) value;
                // root element - always visible as directory
                // check row == 0 is enough, but we got dmtn so why don't use
                // it...
                if (dmtn.isRoot()) {
                    setIcon(openIcon);
                } else /* else, because, root shouldn't be relation */ if (dmtn.getUserObject() instanceof SenseRelation) {
                    Long nameId = ((SenseRelation)dmtn.getUserObject()).getRelationType().getName();
                    setText(LocalisationManager.getInstance().getLocalisedString(nameId));
                } else if (dmtn.getUserObject() instanceof Pair<?, ?>) {
                    Pair<Sense, SenseRelation> pair = (Pair<Sense, SenseRelation>) dmtn.getUserObject();
                    Sense sense = pair.getA();
                    String domainText = LocalisationManager.getInstance().getLocalisedString(sense.getDomain().getName());
                    setText(sense.toString() + " " + sense.getVariant() + " (" + domainText + ")");
                }
            }
            return this;
        }
    }

    /**
     * @author amusial ActionListener interface implementation
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // add relation
//        if (ae.getSource() == addRelation) {
//            Point location = addRelation.getLocationOnScreen();
//            // choose second lexical unit
//            ValueContainer<Boolean> created = new ValueContainer<>(false);
//
//            Rectangle r = workbench.getFrame().getBounds();
//            int x = r.x + r.width - AbstractListFrame.WIDTH / 2 - 50;
//
//            Collection<Sense> selectedUnits = UnitsListFrame.showModal(
//                    workbench, x, location.y, true, null, created);
//            Sense lastUnit = null;
//            if (((DefaultMutableTreeNode) tree.getModel().getRoot())
//                    .getUserObject() instanceof Sense) {
//                lastUnit = (Sense) ((DefaultMutableTreeNode) tree.getModel()
//                        .getRoot()).getUserObject();
//            }
//            if (selectedUnits != null && !selectedUnits.isEmpty()
//                    && lastUnit != null) {
//                for (Sense sense : selectedUnits) {
//                    if (sense.getId().equals(lastUnit.getId())) {
//                        DialogBox
//                                .showError(Messages.FAILURE_SOURCE_UNIT_SAME_AS_TARGET);
//                        return;
//                    }
//                }
//                // choose relation type
//                IRelationType rel = RelationTypeFrame.showModal(workbench,
//                        RelationArgument.LEXICAL, lastUnit.getPartOfSpeech(),
//                        RelationTypeFrame.unitToList(lastUnit), selectedUnits);
//
//                if (rel == null) {
//                    return; // no relation type chosen, return
//                }
//                for (Sense selectedUnit : selectedUnits) {
//                    // check if relation already exists
//                    if (LexicalDA.checkIfRelationExists(lastUnit, selectedUnit,
//                            rel)) {
//                        DialogBox.showError(String.format(
//                                Messages.FAILURE_RELATION_EXISTS,
//                                RelationTypes.getFullNameFor(rel.getId()),
//                                lastUnit.getLemma(), selectedUnit.getLemma()));
//                    } else {
//                        LexicalDA.makeRelation(lastUnit, selectedUnit, rel);
//
//                        // reverse relation exists?
//                        if (rel.getReverse() != null) {
//                            // create reverse relation entry?
//                            IRelationType reverse = LexicalDA
//                                    .getReverseRelation(rel);
//                            String reverseName = LexicalDA
//                                    .getRelationName(reverse);
//                            // Pobierz testy dla relacji odwrotnej
//                            Collection<String> tests = LexicalDA.getTests(
//                                    reverse, lastUnit.toString(),
//                                    selectedUnit.toString(),
//                                    lastUnit.getPartOfSpeech());
//                            String test = "\n\n";
//                            for (String i : tests) {
//                                test += i + "\n";
//                            }
//                            if (rel.isAutoReverse()
//                                    || DialogBox.showYesNo(String.format(Messages.QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION
//                                            + test, reverseName)) == DialogBox.YES) {
//                                for (Sense selectedUniti : selectedUnits) {
//                                    if (LexicalDA.checkIfRelationExists(
//                                            selectedUniti, lastUnit, rel)) {
//                                        DialogBox
//                                                .showError(String
//                                                        .format(Messages.FAILURE_RELATION_EXISTS,
//                                                                reverseName,
//                                                                selectedUniti
//                                                                .getLemma(),
//                                                                lastUnit.getLemma()));
//                                    } else {
//                                        LexicalDA.makeRelation(selectedUniti,
//                                                lastUnit, reverse);
//                                    }
//                                }
//                            }
//                        }
//
//                        // show confirmation dialog?
//                        // refresh view
//                        refresh();
//                    }
//                }
//
//            }
//
//            // delete relation
//        } else if (ae.getSource() == delRelation) {
//            @SuppressWarnings("unchecked")
//            SenseRelation relation = ((Pair<Sense, SenseRelation>) ((DefaultMutableTreeNode) ((DefaultMutableTreeNode) tree
//                    .getLastSelectedPathComponent())).getUserObject()).getB();
//
//            if (DialogBox.showYesNo(String.format(
//                    Messages.QUESTION_SURE_TO_REMOVE_RELATION, RelationTypes
//                    .getFullNameFor(relation.getRelation().getId()))) == DialogBox.YES) {
//
//                RemoteUtils.lexicalRelationRemote.dbDelete(relation);
//                if (relation.getRelation().getReverse() != null
//                        && RemoteUtils.lexicalRelationRemote.dbRelationExists(
//                                relation.getSenseTo(), relation.getSenseFrom(),
//                                relation.getRelation().getReverse())
//                        && // if yes, then check if relation is auto reverse of
//                        // user want to delete reverse relation
//                        (relation.getRelation().isAutoReverse() || DialogBox
//                        .showYesNo(String
//                                .format(Messages.QUESTION_SURE_TO_REMOVE_REVERSE_RELATION,
//                                        relation.getRelation()
//                                        .getReverse())) == DialogBox.YES)) {
//
//                    // get relations type reverse type from child to parent
//                    Collection<SenseRelation> rev_rels = RemoteUtils.lexicalRelationRemote
//                            .dbGetRelations(relation.getSenseTo(), relation
//                                    .getSenseFrom(), relation.getRelation()
//                                    .getReverse());
//                    // delete all of them
//                    for (SenseRelation lex_rel : rev_rels) {
//                        RemoteUtils.lexicalRelationRemote.dbDelete(lex_rel);
//                    }
//                }
//                refresh();
//            }
//        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent evt) {
        // Get all nodes whose selection status has changed
        TreePath[] paths = evt.getPaths();

        if (paths.length > 0
                && evt.isAddedPath(0)
                && paths[0].getLastPathComponent() instanceof DefaultMutableTreeNode) {
            if (((DefaultMutableTreeNode) paths[0].getLastPathComponent())
                    .isLeaf()
                    && !((DefaultMutableTreeNode) paths[0]
                    .getLastPathComponent()).isRoot()) {
                // a leaf has been selected, allow delete relation
                delRelation.setEnabled(true);
            } else {
                delRelation.setEnabled(false);
            }
        }

    }

}
