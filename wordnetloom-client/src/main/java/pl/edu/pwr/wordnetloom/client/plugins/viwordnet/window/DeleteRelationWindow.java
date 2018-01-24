package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.window;

import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdge;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdgeSynset;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphView;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class DeleteRelationWindow extends DialogWindow implements ActionListener {

    /* **/
    private static final long serialVersionUID = 2768814045995557133L;

    private static final int WIDTH = 420;
    private static final int HEIGHT = 160;

    private JLabel question;
    private JComboBox<ViwnEdgeSynset> relations;
    private MButton delete;
    private MButton cancel;

    private Collection<ViwnEdge> removed;

    /**
     * @param baseFrame
     * @param title
     * @param edges
     */
    protected DeleteRelationWindow(WebFrame baseFrame, String title, Collection<ViwnEdgeSynset> edges) {
        super(baseFrame, title);
        initialize(edges);
    }

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

        question = new JLabel(Labels.REMOVE_RELATION);
        add("hfill", question);

        relations = new JComboBox(edges.toArray());
        relations.setRenderer(new DeletionCellRenderer());

        add("br hfill vfill", relations);

        delete = MButton.buildDeleteButton()
                .withActionListener(this)
                .withMnemonic(KeyEvent.VK_U)
                .withCaption(Labels.DELETE_SELECTED);

        add("br center", delete);

        cancel = MButton.buildCancelButton(this);
        add(cancel);
    }


    private void deleteRelation() {
        ViwnEdgeSynset ves = (ViwnEdgeSynset) relations.getSelectedItem();
        SynsetRelation synsetRelation = ves.getSynsetRelation();

        removeRelation(synsetRelation);

        RelationType relationType = RemoteService.relationTypeRemote.findById(synsetRelation.getRelationType().getId());
        RelationType reverseRelationType = RemoteService.relationTypeRemote.findReverseByRelationType(relationType.getId());
        SynsetRelation reverseSynsetRelation = RemoteService.synsetRelationRemote.findRelation(synsetRelation.getChild(), synsetRelation.getParent(), reverseRelationType);

        if(relationType.isAutoReverse()) {
            removeRelation(reverseSynsetRelation);
        } else if(reverseSynsetRelation != null){
            if(DialogBox.showYesNo(Messages.QUESTION_SURE_TO_REMOVE_RELATION) == DialogBox.YES){ //TODO poprawić komunikat
                removeRelation(reverseSynsetRelation);
            }
        }
        // refreshing combobox
        relations.updateUI();
        // if all relations were removed, we close a window
        if(relations.getModel().getSize() == 0){
            setVisible(false);
        }
    }

    private void removeRelation(SynsetRelation synsetRelation) {
        // removing relation from database
        RemoteService.synsetRelationRemote.delete(synsetRelation);

        int relationIndex = getIndexOfRelation(synsetRelation);
        // add deleted relation to result list
        removed.add(relations.getItemAt(relationIndex));
        // removing relation from combobox
        if(relationIndex >= 0){
            relations.removeItemAt(relationIndex);
        }
    }

    private int getIndexOfRelation(SynsetRelation synsetRelation) {
        int relationsSize = relations.getModel().getSize();
        ViwnEdgeSynset edge;
        for(int i =0; i <relationsSize; i++){
            edge = relations.getModel().getElementAt(i);
            if(edge.getSynsetRelation().getId().equals(synsetRelation.getId())){
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
            setVisible(false);
        }
    }

    /**
     * @param baseFrame
     * @param title
     * @param width
     * @param height
     * @param edges
     * @return collection of removed edges
     */
    public static Collection<ViwnEdge> showDeleteSynsetDialog(WebFrame baseFrame, String title, int width,
                                                              int height, Collection<ViwnEdgeSynset> edges) {
        DeleteRelationWindow drf = new DeleteRelationWindow(baseFrame, title, width, height, edges);
        drf.setVisible(true);
        return drf.removed;
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

    private class DeletionCellRenderer extends JLabel implements ListCellRenderer {

        DeletionCellRenderer(){
            super();
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            assert value instanceof ViwnEdgeSynset;
            ViwnEdgeSynset edge = (ViwnEdgeSynset)value;

            String parentNodeLabel = edge.getSynset1().getLabel();
            String childNodeLabel = edge.getSynset2().getLabel();
            String relationTypeName = LocalisationManager.getInstance().getLocalisedString(edge.getRelationType().getName());
            setText(String.format(Labels.RELATION_INFO, relationTypeName, parentNodeLabel, childNodeLabel));

            setupColor(index);

            return this;
        }

        private void setupColor(int index)
        {
            final Color FIRST_COLOR = Color.getHSBColor(0f, 0f, 0.85f);
            final Color SECOND_COLOR = Color.getHSBColor(0f, 0f, 0.95f);
            final Color FOREGROUND_COLOR = Color.BLACK;
            // set background visible
            setOpaque(true);
            Color background;

            if(index % 2 ==0){
                background = FIRST_COLOR;
            } else {
                background = SECOND_COLOR;
            }

            setBackground(background);
            setForeground(FOREGROUND_COLOR);
        }
    }
}
