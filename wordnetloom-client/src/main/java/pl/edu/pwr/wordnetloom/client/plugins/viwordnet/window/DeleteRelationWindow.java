package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window;

import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.plugins.relationtypes.utils.RelationTypeFormat;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdge;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdgeSynset;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashSet;

public class DeleteRelationWindow extends DialogWindow implements ActionListener {

    private static final long serialVersionUID = 2768814045995557133L;

    private static final int WIDTH = 420;
    private static final int HEIGHT = 160;

    private JLabel question;
    private JComboBox<ViwnEdgeSynset> relations;
    private MButton delete;
    private MButton cancel;

    private Collection<ViwnEdge> removed;

    /**
     * @param baseFrame dialog base frame?
     * @param title     dialog title
     * @param width     dialog width
     * @param height    dialog height
     * @param edges     edges collection representing collection of relations from
     *                  which one or more should be removed
     */
    protected DeleteRelationWindow(WebFrame baseFrame, String title, int width,
                                   int height, Collection<ViwnEdgeSynset> edges) {
        super(baseFrame, title, width, height);
        initialize(edges);
    }

    /**
     * @param baseFrame
     * @param edges
     * @return collection of removed edges
     */
    public static Collection<ViwnEdge> showDeleteSynsetDialog(WebFrame baseFrame, Collection<ViwnEdgeSynset> edges) {
        DeleteRelationWindow drf = new DeleteRelationWindow(baseFrame, Labels.REMOVE_RELATION, WIDTH, HEIGHT, edges);
        drf.setVisible(true);
        return drf.removed;
    }

    /**
     * method add components to this dialog box
     *
     * @param edges collection of edges being representation of relations to
     *              delete
     */
    private void initialize(Collection<ViwnEdgeSynset> edges) {
        removed = new HashSet<>();
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new RiverLayout());

        buildUIComponents(edges);
        insertUIComponents();
    }

    private void buildUIComponents(Collection<ViwnEdgeSynset> edges) {
        question = new JLabel(Labels.REMOVE_RELATION);
        relations = new JComboBox(edges.toArray());
        relations.setRenderer(new DeletionCellRenderer());
        delete = MButton.buildDeleteButton()
                .withActionListener(this)
                .withMnemonic(KeyEvent.VK_U)
                .withCaption(Labels.DELETE_SELECTED);
        cancel = MButton.buildCancelButton(this);
    }

    private void insertUIComponents() {
        add("hfill", question);
        add("br hfill vfill", relations);
        add("br center", delete);
        add(cancel);
    }

    private void deleteRelation() {
        SynsetRelation synsetRelation = getSelectedRelation();
        RelationType relationType = getRelationType(synsetRelation.getRelationType().getId());
        if (showSureRelationQuestion(relationType, Messages.QUESTION_SURE_TO_REMOVE_RELATION)) {
            removeRelation(synsetRelation);
            if (relationType.getReverse() != null) {
                RelationType reverseRelationType = getRelationType(relationType.getReverse().getId());
                SynsetRelation reverseSynsetRelation = RemoteService.synsetRelationRemote.findRelation(synsetRelation.getChild(), synsetRelation.getParent(), reverseRelationType);
                if (doRemoveReverseRelation(reverseSynsetRelation,relationType, reverseRelationType)) {
                    removeRelation(reverseSynsetRelation);
                }
            }
            relations.updateUI();
            if (hasRelationsToDelete()) {
                closeWindow();
            }
        }
    }

    private SynsetRelation getSelectedRelation() {
        ViwnEdgeSynset ves = (ViwnEdgeSynset) relations.getSelectedItem();
        assert ves != null;
        return ves.getSynsetRelation();
    }

    private RelationType getRelationType(Long id) {
        return RelationTypeManager.getInstance().get(id);
    }

    private boolean showSureRelationQuestion(RelationType relationType, final String question) {
        String relationName = RelationTypeFormat.getText(relationType);
        String dialogBoxMessage = String.format(question, relationName);
        return DialogBox.showYesNo(dialogBoxMessage) == DialogBox.YES;
    }

    private void closeWindow() {
        setVisible(false);
    }

    private boolean doRemoveReverseRelation(SynsetRelation reverseSynsetRelation,RelationType relationType, RelationType reverseRelationType) {
        // if relation is autoReverse, relation been deleted automatic, else is showing dialog
        // with a question
        return reverseSynsetRelation != null
                && (relationType.isAutoReverse()
                || showSureRelationQuestion(reverseRelationType, Messages.QUESTION_SURE_TO_REMOVE_REVERSE_RELATION));
    }

    private boolean hasRelationsToDelete() {
        return relations.getModel().getSize() == 0;
    }

    private void removeRelation(SynsetRelation synsetRelation) {
        RemoteService.synsetRelationRemote.delete(synsetRelation);

        int relationIndex = getIndexOfRelation(synsetRelation);
        // add deleted relation to result list
        removed.add(relations.getItemAt(relationIndex));
        // removing relation from combobox
        if (relationIndex >= 0) {
            relations.removeItemAt(relationIndex);
        }
    }

    private int getIndexOfRelation(SynsetRelation synsetRelation) {
        int relationsSize = relations.getModel().getSize();
        ViwnEdgeSynset edge;
        for (int i = 0; i < relationsSize; i++) {
            edge = relations.getModel().getElementAt(i);
            if (edge.getSynsetRelation().getId().equals(synsetRelation.getId())) {
                return i;
            }
        }
        return -1; // not found
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == delete) {
            deleteRelation();
        } else if (e.getSource() == cancel) {
            closeWindow();
        }
    }

    private class DeletionCellRenderer extends JLabel implements ListCellRenderer {

        DeletionCellRenderer() {
            super();
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            assert value instanceof ViwnEdgeSynset;
            ViwnEdgeSynset edge = (ViwnEdgeSynset) value;

            String parentNodeLabel = edge.getSynset1().getLabel();
            String childNodeLabel = edge.getSynset2().getLabel();
            String relationTypeName = RelationTypeFormat.getText(edge.getRelationType());
            setText(String.format(Labels.RELATION_INFO, relationTypeName, parentNodeLabel, childNodeLabel));

            setupColor(index);

            return this;
        }

        private void setupColor(int index) {
            final Color FIRST_COLOR = Color.getHSBColor(0f, 0f, 0.85f);
            final Color SECOND_COLOR = Color.getHSBColor(0f, 0f, 0.95f);
            final Color FOREGROUND_COLOR = Color.BLACK;
            // set background visible
            setOpaque(true);
            Color background;

            if (index % 2 == 0) {
                background = FIRST_COLOR;
            } else {
                background = SECOND_COLOR;
            }

            setBackground(background);
            setForeground(FOREGROUND_COLOR);
        }
    }
}
