package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.tree.WebTree;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.UnitsListFrame;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window.MakeNewLexicalRelationWindow;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.common.ValueContainer;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelation;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;


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

    private WebTree tree = null;

    private MButton addRelationButton = null;
    private MButton deleteRelationButton = null;

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

        addRelationButton = MButton.buildAddButton(this)
                .withEnabled(true)
                .withToolTip(Hints.ADD_RELATION_UNITS);

        installViewScopeShortCut(addRelationButton, 0, KeyEvent.VK_INSERT);

        deleteRelationButton = MButton.buildDeleteButton(this)
                .withEnabled(true)
                .withToolTip(Hints.REMOVE_RELTAION_UNITS);

        installViewScopeShortCut(deleteRelationButton, 0, KeyEvent.VK_DELETE);

        content.add("hfill vfill", new JScrollPane(tree));
        content.add("br center", addRelationButton);
        content.add(deleteRelationButton);
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
                root_from.removeAllChildren();
                root_to.removeAllChildren();
                List<SenseRelation> relationsFrom = RemoteService.senseRelationRemote.findRelations((Sense) object, null, true, false);
                List<SenseRelation> relationTo = RemoteService.senseRelationRemote.findRelations((Sense) object, null, false, false);
                fillRootRelations(root_from, relationsFrom, true);
                fillRootRelations(root_to, relationTo, false);

                return null;
            }

            private void fillRootRelations(DefaultMutableTreeNode nodeToFill, List<SenseRelation> relations, boolean displayChild) {
                Map<Long, DefaultMutableTreeNode> relationTypeNodeMap = new HashMap<>();
                DefaultMutableTreeNode node;
                DefaultMutableTreeNode senseNode;
                Sense senseToDisplay;
                for (SenseRelation relation : relations) {
                    if (!relationTypeNodeMap.containsKey(relation.getRelationType().getId())) {
                        node = new DefaultMutableTreeNode(relation);
                        node.setUserObject(relation);
                        relationTypeNodeMap.put(relation.getRelationType().getId(), node);
                    }

                    if (relation.getChild() != null && relation.getParent() != null) {
                        senseToDisplay = displayChild ? relation.getChild() : relation.getParent();
                        senseNode = new DefaultMutableTreeNode(new Pair<>(senseToDisplay, relation));
                        DefaultMutableTreeNode parentNode = relationTypeNodeMap.get(relation.getRelationType().getId());
                        parentNode.add(senseNode);
                    }
                }
                for (Map.Entry<Long, DefaultMutableTreeNode> entry : relationTypeNodeMap.entrySet()) {
                    nodeToFill.add(entry.getValue());
                }
            }

            @Override
            protected void done() {
                if (((DefaultMutableTreeNode) tree.getModel().getRoot())
                        .getUserObject() != object) {
                    // TODO: find better solution
                    // ugly hack, to switch button off after changing tree
                    deleteRelationButton.setEnabled(false);
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
                    Long nameId = ((SenseRelation) dmtn.getUserObject()).getRelationType().getName();
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

    private void addRelation() {
        Point point = getLocation(addRelationButton, workbench);
        ValueContainer<Boolean> created = new ValueContainer<>(false);

        Collection<Sense> selectedUnits = UnitsListFrame.showModal(workbench, point.x, point.y, true, null, created);
        Sense rootSense = (Sense) root.getUserObject(); //TODO zobaczyć, czy zwróci poprawny obiekt

        if (areSelectedUnits(selectedUnits)) {
            for (Sense sense : selectedUnits) {
                if (sense.getId().equals(rootSense.getId())) {
                    DialogBox.showError(Messages.FAILURE_SOURCE_UNIT_SAME_AS_TARGET);
                    return;
                }
            }
            // show relationtype window
            MakeNewLexicalRelationWindow relationWindow = new MakeNewLexicalRelationWindow(workbench.getFrame(), rootSense.getPartOfSpeech(),
                    rootSense, selectedUnits.iterator().next());
            relationWindow.setVisible(true);
            RelationType relationType = relationWindow.getChosenType();
            Sense parentSense = relationWindow.getParentSense();
            Sense childSense = relationWindow.getChildSense();

            if (RemoteService.senseRelationRemote.relationExists(parentSense, childSense, relationType)) {
                showRelationExistsError(relationType, parentSense, childSense);
                return;
            }
            if (saveRelation(relationType, parentSense, childSense)) {
                refresh();
            }
        }
    }

    private boolean saveRelation(RelationType relationType, Sense parentSense, Sense childSense) {
        boolean saveResult = RemoteService.senseRelationRemote.makeRelation(parentSense, childSense, relationType);
        if (!saveResult) {
            //TODO wyświetlić komunikat o niepowodzeniu zapisu
            return false;
        }
        // sprawdzanie, czy relacja odwrotna istnieje
        if (relationType.getReverse() != null) {
            RelationType reverseRelationType = RemoteService.relationTypeRemote.findReverseByRelationType(relationType.getId());
            String reverseRelationName = LocalisationManager.getInstance().getLocalisedString(reverseRelationType.getName());
            //TODO dorobić pobieranie i ustawianie testów
        }
        return true;
    }

    private void showRelationExistsError(RelationType relationType, Sense parentSense, Sense childSense) {
        String relationTypeName = LocalisationManager.getInstance().getLocalisedString(relationType.getName());
        DialogBox.showError(String.format(Messages.FAILURE_RELATION_EXISTS, relationTypeName,
                parentSense.getWord().getWord(), childSense.getWord().getWord()));
    }

    private boolean areSelectedUnits(Collection<Sense> selectedUnits) {
        return selectedUnits != null && !selectedUnits.isEmpty();
    }

    private void removeRelation() {
        SenseRelation senseRelation = getSelectedSenseRelation();
        if (showSureToRemoveDialogBox(senseRelation.getRelationType().getName(), Messages.QUESTION_SURE_TO_REMOVE_RELATION) ) {
            RemoteService.senseRelationRemote.delete(senseRelation);
            if (checkRemoveReverseRelation(senseRelation)) {
                SenseRelation reverseRelations = RemoteService.senseRelationRemote.findRelation(senseRelation.getChild(), senseRelation.getParent(), senseRelation.getRelationType().getReverse());
                RemoteService.senseRelationRemote.delete(reverseRelations);
            }
            refresh();
        }
    }

    private SenseRelation getSelectedSenseRelation() {
        DefaultMutableTreeNode node = tree.getSelectedNode();
        Pair<Sense, SenseRelation> nodePair = (Pair<Sense, SenseRelation>) node.getUserObject();
        return nodePair.getB();
    }

    private boolean checkRemoveReverseRelation(SenseRelation senseRelation) {
        return senseRelation.getRelationType().getReverse() != null
                && RemoteService.senseRelationRemote.relationExists(senseRelation.getChild(), senseRelation.getParent(), senseRelation.getRelationType().getReverse())
                && (senseRelation.getRelationType().isAutoReverse()
                || showSureToRemoveDialogBox(senseRelation.getRelationType().getReverse().getName(), Messages.QUESTION_SURE_TO_REMOVE_REVERSE_RELATION));

    }

    private boolean showSureToRemoveDialogBox(Long relationNameId, String message) {
        return DialogBox.showYesNo(String.format(message,
                LocalisationManager.getInstance().getLocalisedString(relationNameId))) == DialogBox.YES;
    }

    /**
     * @author amusial ActionListener interface implementation
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == addRelationButton) {
            addRelation();
        } else if (event.getSource() == deleteRelationButton) {
            removeRelation();
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent evt) {
        DefaultMutableTreeNode selectedNode = tree.getSelectedNode();
        if(selectedNode == null)
        {
            return;
        }
        if (selectedNode.isLeaf() && !root.isNodeChild(selectedNode)) {
            deleteRelationButton.setEnabled(true);
        } else {
            deleteRelationButton.setEnabled(false);
        }
    }
}
