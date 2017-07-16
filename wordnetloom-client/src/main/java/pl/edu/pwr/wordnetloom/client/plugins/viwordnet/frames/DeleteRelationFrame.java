package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.frames;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdge;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdgeSynset;
import pl.edu.pwr.wordnetloom.client.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;
import se.datadosen.component.RiverLayout;

public class DeleteRelationFrame extends DialogWindow implements ActionListener {

    /* **/
    private static final long serialVersionUID = 2768814045995557133L;

    private static final int WIDTH = 420;
    private static final int HEIGHT = 160;

    private JLabel question;
    private JComboBox relations;
    private ButtonExt delete;
    private ButtonExt cancel;

    private HashMap<Integer, ViwnEdgeSynset> toRemove;
    private Collection<ViwnEdge> removed;

    /**
     * @param baseFrame
     * @param title
     * @param edges
     */
    protected DeleteRelationFrame(JFrame baseFrame, String title, Collection<ViwnEdgeSynset> edges) {
        super(baseFrame, title);
        initialize(edges);
    }

    /**
     * @param baseFrame dialog base frame?
     * @param title dialog title
     * @param width dialog width
     * @param height dialog height
     * @param edges edges collection representing collection of relations from
     * which one or more should be removed
     */
    protected DeleteRelationFrame(JFrame baseFrame, String title, int width,
            int height, Collection<ViwnEdgeSynset> edges) {
        super(baseFrame, title, width, height);
        initialize(edges);
    }

    /**
     * method add components to this dialog box
     *
     * @param edges collection of edges being representation of relations to
     * delete
     *
     */
    private void initialize(Collection<ViwnEdgeSynset> edges) {

        toRemove = new HashMap<>();
        Iterator<ViwnEdgeSynset> iter = edges.iterator();
        for (int i = 0; i < edges.size(); ++i) {
            toRemove.put(i, iter.next());
        }
        removed = new HashSet<>();

        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setLayout(new RiverLayout());

        question = new JLabel(Labels.REMOVE_RELATION);
        this.add("hfill", question);

        relations = new JComboBox(toRemove.keySet().toArray(new Integer[]{}));
        relations.setRenderer(new DeletionCellRenderer());

        this.add("br hfill vfill", relations);

        delete = new ButtonExt(Labels.DELETE_SELECTED, this, KeyEvent.VK_U);
        this.add("br center", delete);
        cancel = new ButtonExt(Labels.CANCEL, this, KeyEvent.VK_W);
        this.add(cancel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == delete) {
            ViwnEdgeSynset ves = toRemove.get(relations.getSelectedItem());
//
//            RemoteUtils.synsetRelationRemote.dbDelete(ves.getSynsetRelation());
//
//            boolean autoreverse = false;
//            // check if relation is autoreverse
//            SynsetRelation sr_dto = ves.getSynsetRelation();
//            RelationType rType = RemoteUtils.relationTypeRemote.dbGet(sr_dto.getRelation().getId());
//            Integer rev = null;
//            if (rType.isAutoReverse()) {
//                for (Integer t : toRemove.keySet()) {
//                    SynsetRelation sr_dto_rev = toRemove.get(t).getSynsetRelation();
//                    if (sr_dto_rev.getRelation() == rType.getReverse()) {
//                        // remember reversed
//                        rev = t;
//                        autoreverse = true;
//                    }
//                }
//            }

            /* if below is ugly, but those methods have to be invoked
				 * in that order, better copy some code than repeat
				 * if(autoreverse) three times */
//            if (autoreverse) {
//                // remember that this relation has been removed
//                removed.add(ves);
//                removed.add(toRemove.get(rev));
//                // remove deleted
//                Object i = relations.getSelectedItem();
//                relations.removeItem(i);
//                relations.removeItem(rev);
//                toRemove.remove(i);
//                toRemove.remove(rev);
//                // relation removed successfully, display success message
//                DialogBox.showInformation(Messages.SUCCESS_SELECTED_RELATION_WITH_REVERSED_DELETED);
//            } else {
//                // remember that this relation has been removed
//                removed.add(ves);
//                // remove deleted
//                Object i = relations.getSelectedItem();
//                relations.removeItem(i);
//                toRemove.remove(i);
//                // relation removed successfully, display success message
//                DialogBox.showInformation(Messages.SUCCESS_SELECTED_RELATION_DELETED);
//            }
            if (toRemove.isEmpty()) {
                this.setVisible(false);
            }

        } else if (e.getSource() == cancel) {
            this.setVisible(false);
        }
    }

    private class DeletionCellRenderer extends JLabel implements ListCellRenderer {

        private static final long serialVersionUID = -4423464499504059166L;

        private HashMap<Long, String> synsetCache = new HashMap<>();

        public DeletionCellRenderer() {
            super();
            setOpaque(true);
        }

        private String getLabel(Synset synset) {
            String cached = synsetCache.get(synset.getId());
            if (cached != null) {
                return cached;
            }

            String ret = "";
//            if (Synset.isAbstract(Common.getSynsetAttribute(synset, Synset.ISABSTRACT))) {
//                ret = "S ";
//            }
//            // check if synset isnt null or empty
//            List<Sense> units = RemoteUtils.lexicalUnitRemote.dbFastGetUnits(synset, LexiconManager.getInstance().getLexicons());
//            if (units != null && !units.isEmpty()) {
//                ret += ((Sense) units.iterator().next()).toString();
//                if (units.size() > 1) {
//                    ret += " ...";
//                }
//            } else {
//                ret = "! S.y.n.s.e.t p.u.s.t.y !";
//            }

            //    synsetCache.put(synset.getId(), ret);
            return ret;
        }

        @Override
        public Component getListCellRendererComponent(JList list,
                Object value, int index, boolean isSelected,
                boolean cellHasFocus) {

            if (value instanceof Integer) {
                ViwnEdgeSynset ves = toRemove.get((Integer) value);
                SynsetRelation sr = ves.getSynsetRelation();

                //         setText(String.format(Labels.RELATION_INFO, RelationTypes.get(sr.getRelation().getId()).name(), getLabel(sr.getSynsetFrom()), getLabel(sr.getSynsetTo())));
            }

            Color background;
            Color foreground;

            // check if this cell represents the current DnD drop location
            JList.DropLocation dropLocation = list.getDropLocation();
            if (dropLocation != null
                    && !dropLocation.isInsert()
                    && dropLocation.getIndex() == index) {

                background = Color.LIGHT_GRAY;
                foreground = Color.BLACK;

                // check if this cell is selected
            } else if (isSelected) {
                background = Color.DARK_GRAY;
                foreground = Color.WHITE;

                // unselected, and not the DnD drop location
            } else if (value instanceof Integer) {
                if (((Integer) value) % 2 == 0) {
                    background = Color.getHSBColor(0f, 0f, .85f);
                    foreground = Color.BLACK;
                } else {
                    background = Color.getHSBColor(0f, 0f, .95f);
                    foreground = Color.BLACK;
                }
            } else {
                background = Color.GRAY;
                foreground = Color.BLACK;
            }

            setBackground(background);
            setForeground(foreground);

            return this;
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
    public static Collection<ViwnEdge> showDeleteSynsetDialog(JFrame baseFrame, String title, int width,
            int height, Collection<ViwnEdgeSynset> edges) {
        DeleteRelationFrame drf = new DeleteRelationFrame(baseFrame, title, width, height, edges);
        drf.setVisible(true);
        return drf.removed;
    }

    /**
     * @param baseFrame
     * @param edges
     * @return collection of removed edges
     */
    public static Collection<ViwnEdge> showDeleteSynsetDialog(JFrame baseFrame, Collection<ViwnEdgeSynset> edges) {
        DeleteRelationFrame drf = new DeleteRelationFrame(baseFrame, Labels.REMOVE_RELATION, WIDTH, HEIGHT, edges);
        drf.setVisible(true);
        return drf.removed;
    }

}
