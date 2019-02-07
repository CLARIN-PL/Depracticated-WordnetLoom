package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.control;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.panel.WebPanel;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.picking.PickedState;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetPerspective;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events.UpdateGraphAfterRemovingEvent;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.*;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.PermissionHelper;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ServiceManager;
import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class ViwnGraphViewPopupGraphMousePlugin extends AbstractPopupGraphMousePlugin {

    private WebPopupMenu popup = new WebPopupMenu();
    private ViwnGraphViewUI vgvui;
    private WebList synset_list_ = null;

    ViwnGraphViewPopupGraphMousePlugin(ViwnGraphViewUI vgvui) {
        this.vgvui = vgvui;
        Application.eventBus.register(this);
    }

    private void addSynsets(ViwnNode v) {
        popup.setVisible(false);
        java.util.List<ViwnNodeSynset> syns = synset_list_.getSelectedValuesList();
        if(!syns.isEmpty()){
            vgvui.deselectAll();
        }
        vgvui.addSynsetsFromSet(syns);
    }

    private ViWordNetService getViWordNetService(){
        return ServiceManager.getViWordNetService(vgvui.getWorkbench());
    }

    private void showPathToHypernym(ViwnNode vertex) {
        // TODO rozwiązanie tymczasowe
        Synset synset = ((ViwnNodeSynset)vertex).getSynset();
        RelationType type;
        if(synset.getLexicon().getName().equals("Słowosieć")){
            // TODO poprawić kierunek relacji
            type = RemoteService.relationTypeRemote.findByName("hiponimia");
        } else {
            type = RemoteService.relationTypeRemote.findByName("Hyponym");
        }
        showPathTo(vertex, type);
    }

    private void showPathTo(ViwnNode node, RelationType relationType){
        Synset synset = ((ViwnNodeSynset)node).getSynset();
        List<DataEntry> dataEntries = RemoteService.synsetRelationRemote.findPath(synset, relationType, LexiconManager.getInstance().getUserChosenLexiconsIds());
        dataEntries.forEach(dataEntry -> vgvui.addToEntrySet(dataEntry));
        dataEntries.forEach(dataEntry -> vgvui.showRelation(dataEntry.getSynset(), relationType));
    }

    @Override
    protected void handlePopup(MouseEvent e) {
        VisualizationViewer<ViwnNode, ViwnEdge> vv = (VisualizationViewer<ViwnNode, ViwnEdge>) e.getSource();
        Layout<ViwnNode, ViwnEdge> layout = vv.getGraphLayout();
        edu.uci.ics.jung.graph.Graph<ViwnNode, ViwnEdge> graph = layout.getGraph();
        Point2D p = e.getPoint();
        Point2D ivp = p;
        GraphElementAccessor<ViwnNode, ViwnEdge> pickSupport = vv.getPickSupport();
        boolean list_focus = false;
        synset_list_ = null;

        // exit make relation mode
        ViWordNetService s = getViWordNetService();
        if (s.isMakeRelationModeOn()) {
            s.switchMakeRelationMode();
            return;
        }

        if (s.isMergeSynsetsModeOn()) {
            s.switchMergeSynsetsMode();
            return;
        }

        if (pickSupport != null) {

            popup.removeAll();

            ViwnNode vertex = pickSupport.getVertex(layout, ivp.getX(), ivp.getY());
            ViwnEdge edge = pickSupport.getEdge(layout, ivp.getX(), ivp.getY());
            PickedState<ViwnNode> pickedVertexState = vv.getPickedVertexState();

            if (vertex != null && vertex instanceof ViwnNodeWord) {
                handleNodeWordPopup(s, vertex);
            } else if (vertex != null && vertex instanceof ViwnNodeSynset) {
                handleSynsetPopup(vertex, pickedVertexState);
            } else if (vertex != null && vertex instanceof ViwnNodeSet) {
                list_focus = handleSetPopup(vertex);
            } else if (edge != null) {
                // edge clicked
                popup.add(new AbstractAction(Labels.FOLLOW_EGDE) {
                    private static final long serialVersionUID = 5625564155297832427L;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        actionFollowEdge(graph, edge, vv);
                    }
                });

                if (edge instanceof ViwnEdgeSynset) {
                    AbstractAction removeRelationAction = new AbstractAction(Labels.REMOVE_RELATION) {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            actionRemoveRelation(edge);
                        }
                    };
                    popup.add(removeRelationAction);
                    PermissionHelper.checkPermissionToEditAndSetComponents(removeRelationAction);
                }

            } else {
                ViwnNode node = s.getActiveGraphView().getUI().getRootNode();
                if (node != null) {
                    String label = node.getLabel();

                    String filename = label.split(" ")[0] + ".png";
                    popup.add(new AbstractAction(Labels.SAVE_GRAPH_TO_FILE_COLON + filename) {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            s.getActiveGraphView().getUI().saveToFile(filename);
                        }
                    });
                }
            }

            if (popup.getComponentCount() > 0) {
                popup.show(vv, e.getX(), e.getY());
                if (list_focus) {
                    synset_list_.requestFocusInWindow();
                }
            }
        }
    }

    private void actionFollowEdge(Graph<ViwnNode, ViwnEdge> graph, ViwnEdge edge, VisualizationViewer<ViwnNode, ViwnEdge> vv) {
        ViwnNode vn = graph.getDest(edge);
        vgvui.setSelectedNode(vn);
        vgvui.center();
        vv.repaint();
    }

    private void actionRemoveRelation(ViwnEdge edge) {
        Pair<ViwnNode> c = vgvui.getGraph().getEndpoints(edge);
        HashSet<ViwnEdge> rel = new HashSet<>(vgvui.getGraph().findEdgeSet(c.getFirst(), c.getSecond()));
        rel.addAll(vgvui.getGraph().findEdgeSet(c.getSecond(), c.getFirst()));
        getViWordNetService().removeRelation(rel);
    }

    private void handleNodeWordPopup(ViWordNetService s, ViwnNode vertex) {
        popup.add(new AbstractAction(Labels.CREATE_RELATION_WITH) {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                s.setFirstMakeRelation(vertex);
            }
        });
    }

    private boolean handleSetPopup(ViwnNode vertex) {
        boolean list_focus;ViwnNodeSet set = (ViwnNodeSet) vertex;
        DefaultListModel model = new DefaultListModel();

        ArrayList<ViwnNodeSynset> col = new ArrayList<>(set.getSynsets());
        col.sort(new ViwnNodeAlphabeticComparator());
        for (ViwnNodeSynset syns : col) {
            model.addElement(syns);
        }

        list_focus = true;
        synset_list_ = new WebList(model);
        synset_list_.setSelectedIndex(0);

        JScrollPane scroll_pane = new JScrollPane();
        scroll_pane.setPreferredSize(new Dimension(250, 200));
        scroll_pane.getViewport().setView(synset_list_);

        WebPanel panel = new WebPanel(new BorderLayout());

        WebButton but_expand = new MButton()
                .withActionListener(l -> addSynsets(vertex))
                .withCaption(Labels.EXPAND)
                .withMnemonic(KeyEvent.VK_R);

        WebButton but_cancel = MButton.buildCancelButton()
                .withActionListener(l -> popup.setVisible(false));

        WebButton but_all = new MButton()
                .withCaption(Labels.VALUE_ALL)
                .withMnemonic(KeyEvent.VK_W)
                .withActionListener(l -> {

                    popup.setVisible(false);
                    DefaultListModel list = (DefaultListModel) synset_list_.getModel();
                    if (list.size() > 0) {
                        vgvui.deselectAll();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        ViwnNodeSynset obj = (ViwnNodeSynset) list.get(i);
                        vgvui.addSynsetFromSet(obj);
                    }
                    if (list.size() > 0) {
                    /* recreate view with fixing location */
                        vgvui.recreateLayoutWithFix(vertex, (ViwnNode) list.get(list.size() - 1));
                    }
                });

        synset_list_.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addSynsets(vertex);
                }
            }
        });

        panel.add(scroll_pane, BorderLayout.PAGE_START);

        WebPanel inner_panel = new WebPanel(new FlowLayout());
        inner_panel.add(but_expand);
        inner_panel.add(but_cancel);
        inner_panel.add(but_all);

        panel.add(inner_panel, BorderLayout.CENTER);

        popup.add(panel);
        return list_focus;
    }

    private void handleSynsetPopup(ViwnNode vertex, PickedState<ViwnNode> pickedVertexState) {
        popup.add(new WebLabel(Labels.SYNSET_OPTIONS));

        popup.add(new AbstractAction(Labels.PATH_TO_HIPERONIM) {
            private static final long serialVersionUID = 0L;

            @Override
            public void actionPerformed(ActionEvent e) {
               showPathToHypernym(vertex);
            }
        });

        popup.add(createOpenInNewTabAction((ViwnNodeSynset) vertex));

        AbstractAction group_action = new AbstractAction(Labels.GRUPPING) {
            {
                setEnabled(false);
            }

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Iterator<ViwnNode> pick_itr = pickedVertexState.getPicked().iterator();
                ViwnNode set = vertex; // need to remember last synset from set or set
                while (pick_itr.hasNext()) {
                    ViwnNode n = pick_itr.next();
                    if (n instanceof ViwnNodeSynset) {
                        set = vgvui.addSynsetToSet((ViwnNodeSynset) n);
                    }
                }
                /* recreate layout with fixing */
                vgvui.recreateLayoutWithFix(vertex, set);
            }
        };

        if (vgvui.canGroupSynsets()) {
            group_action.setEnabled(true);
        }

        popup.add(group_action);

        // enter make relation mode
        AbstractAction createRelationAction = new AbstractAction(Labels.CREATE_RELATION_WITH) {
            @Override
            public void actionPerformed(ActionEvent e) {
                getViWordNetService().setFirstMakeRelation(vertex);
            }
        };

        AbstractAction mergeAction = new AbstractAction(Labels.SYNSET_MERGE_WITH) {
            @Override
            public void actionPerformed(ActionEvent e) {
                getViWordNetService().setFirstMergeSynsets(vertex);
            }
        };

        // TODO dorobić etykiety
        AbstractAction removeSynsetAction = new AbstractAction("Usuń synset") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Synset synsetToRemove = ((ViwnNodeSynset)vertex).getSynset();
                removeSynset(synsetToRemove);
                updateViewAfterRemovingSynset(synsetToRemove);
            }
        };
        popup.add(createRelationAction);
        popup.add(mergeAction);
        popup.add(removeSynsetAction);

        // split synset options from lexical unit options
        popup.addSeparator();
        popup.add(new WebLabel(Labels.LEXICAL_UNIT_OPTIONS));

        // TODO: make lexical units relations
        // show lexical units of synsets
        final WebMenu createRelationItem = createCreateRelationItem((ViwnNodeSynset) vertex);

        popup.add(createRelationItem);

        JComponent[] components = { createRelationItem};

        Synset synset = ((ViwnNodeSynset) vertex).getSynset();
        checkPermissionToEdit(createRelationAction, mergeAction, components, synset);
    }

    private void removeSynset(Synset synset){
        // TODO dorobić etykiety
        final String message = "Czy na pewno usunąć synset wraz z wszystkimi jednostkami i relacjami?";
        if(DialogBox.showYesNo(message) == DialogBox.YES){
            RemoteService.synsetRemote.remove(synset);
        }
    }

    private void updateViewAfterRemovingSynset(Synset removedSynset){
        Application.eventBus.post(new UpdateGraphAfterRemovingEvent(removedSynset));
    }

    private void checkPermissionToEdit(AbstractAction createRelationAction, AbstractAction mergeAction, JComponent[] components, Synset synset) {
        // merge action is disabled for anonymous user and for only-read lexicons
        PermissionHelper.checkPermissionToEditAndSetComponents(synset, mergeAction);
        // creating relation action is allowed, but when user try connected two synsets from this same read-only lexicon
        // he get a error message. Only administrator can create such relations
        PermissionHelper.checkPermissionToEditAndSetComponents(createRelationAction);
        PermissionHelper.checkPermissionToEditAndSetComponents(synset, components);
    }

    private WebMenu createCreateRelationItem(ViwnNodeSynset vertex) {
        final WebMenu createRelationItem = new WebMenu(Labels.UNIT_CREATE_RELATION_WITH);
        createRelationItem.addItemListener(e1 -> {
            createRelationItem.removeAll();
            final List<Sense> senses = RemoteService.senseRemote.findBySynset(vertex.getSynset(), LexiconManager.getInstance().getUserChosenLexiconsIds());
            String senseText;
            for(Sense sense : senses){
                senseText = getSenseMenuItemText(sense);
                createRelationItem.add(new WebMenuItem(new AbstractAction(senseText) {
                    @Override
                    public void actionPerformed(ActionEvent e1) {
                        getViWordNetService().setFirstMakeRelation(sense);
                    }
                }));
            }
        });
        return createRelationItem;
    }

    private AbstractAction createOpenInNewTabAction(ViwnNodeSynset vertex) {
        return new AbstractAction(Labels.OPEN_IN_NEW_TAB) {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                ViWordNetService s = getViWordNetService();
                Synset synset = vertex.getSynset();
                // get DataEntry from old graph, from which the synset node will be built
                DataEntry synsetDataEntry = getViWordNetService().getSynsetData().getById(synset.getId());
                // creating new view. Newly created graph become active graph
                s.addGraphView();
                // createing new synset node, which will be passed to active graph
                ViwnNodeSynset newSynset = new ViwnNodeSynset(synset, s.getActiveGraphView().getUI());
                // passes DataEntry to new graph
                s.getActiveGraphView().getUI().addToEntrySet(synsetDataEntry);
                // set synset node to graph
                s.getActiveGraphView().getUI().addSynsetNode(newSynset);
                // update name of tab
                ViWordNetPerspective perspective = (ViWordNetPerspective) vgvui.getWorkbench().getActivePerspective();
                perspective.setTabTitle(s.getActiveGraphView().getUI().getRootNode().getLabel());
            }
        };
    }

    private String getSenseMenuItemText(Sense sense){
        String name = sense.getWord().getWord();
        String variant = String.valueOf(sense.getVariant());
        String domain = LocalisationManager.getInstance().getLocalisedString(sense.getDomain().getName());
        return name + " " + variant + " (" + domain + ")";
    }

}
