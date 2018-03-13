package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views;

import com.alee.laf.panel.WebPanel;
import jiconfont.icons.FontAwesome;
import org.apache.commons.collections15.set.ListOrderedSet;
import pl.edu.pwr.wordnetloom.client.plugins.candidates.tasks.RebuildGraphsTask;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenersContainer;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComboBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextField;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class CandidatesViewUI extends AbstractViewUI
        implements ListSelectionListener, ActionListener, ChangeListener {

    private final HashMap<Pair<Integer, PartOfSpeech>, RebuildGraphsTask> tasks
            = new HashMap<>();

    private final ListOrderedSet<ListElement> list_elems = new ListOrderedSet<>();

    private final SimpleListenersContainer candidateChanged_ = new SimpleListenersContainer();

    //Choose noun as default
    private final PartOfSpeech pos_default = PartOfSpeechManager.getInstance().getById(2L);
    private int packageNo = 1;
    private int currentMaxPkg = 0;
    private SpinnerListModel packageNoModel = null;
    private DefaultListModel candsModel = null;

    private JList candsList;
    private JTextField filterEdit;
    private JSpinner pckgSpinner;
    private MComboBox posCombo;
    private MButton buttonSearch, buttonShow;
    private MButton buttonAdd;
    private JButton buttonRecalc;
    private JLabel infoLabel;
    private JLabel infoPackages;

    JPanel lazyPanel;
    JPanel content;

    @Override
    protected void initialize(WebPanel content) {
        this.content = content;

        lazyPanel = new JPanel() {
            private static final long serialVersionUID = 6099488245944039713L;

            @Override
            public void paint(Graphics g) {
                super.paint(g);

                if (!isSpinnerLoaded()) {
                    setEnabled(false);
                    spinnerCheck();
                } else {
                    setEnabled(true);
                }
            }
        };
        lazyPanel.setOpaque(false);
        lazyPanel.setLayout(new RiverLayout());

        content.setLayout(new BorderLayout());

        posCombo = new MComboBox();
        posCombo.setPreferredSize(new Dimension(150, 20));
        loadPoses();
        posCombo.setSelectedItem(pos_default);
        posCombo.addActionListener(this);

        packageNoModel = new SpinnerListModel();

        pckgSpinner = new JSpinner(packageNoModel);
        pckgSpinner.setEditor(new JSpinner.ListEditor(pckgSpinner));
        pckgSpinner.addChangeListener(this);
        ((JSpinner.DefaultEditor) pckgSpinner.getEditor()).
                getTextField().addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            ActionEvent ae = new ActionEvent(pckgSpinner, 0, "click");
                            actionPerformed(ae);
                        }
                    }
                });

        buttonShow = new MButton(this)
                .withIcon(FontAwesome.SIGN_IN)
                .withToolTip(Hints.SHOWS_CONTENT_OF_SELECTED_PACKAGE_IN_LIST)
                .withMnemonic(KeyEvent.VK_W);

        buttonAdd = MButton.buildAddButton()
                .withToolTip(Hints.ADDS_CONTENT_OF_SELECTED_PACKAGE_TO_LIST)
                .withActionListener(this);

        buttonRecalc = new MButton(this)
                .withToolTip(Hints.RECALCULATES_GRAPHS_FOR_SELECTED_PACKAGE)
                .withCaption(Labels.RECOUNT);

        infoPackages = new JLabel("(0)");

        JPanel packages = new JPanel();
        packages.setLayout(new RiverLayout());
        packages.add("", new MLabel(Labels.PARTS_OF_SPEECH_COLON, 'm', posCombo));
        packages.add("br hfill", posCombo);
        packages.add("br", new MLabel(Labels.PACKAGE_NUMBER_COLON, 'p', pckgSpinner));
        packages.add("br hfill", pckgSpinner);
        packages.add("", infoPackages);
        packages.add("br center", buttonAdd);
        packages.add("center hfill", buttonShow);
        packages.add("br center", buttonRecalc);

        filterEdit = new MTextField("");
        filterEdit.addActionListener(this);

        buttonSearch = MButton.buildSearchButton(this);

        candsModel = new DefaultListModel();
        candsList = new JList(candsModel);
        candsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        candsList.addListSelectionListener(this);

        infoLabel = new JLabel();
        infoLabel.setText(String.format(Labels.NUMBER_OF_ELEMENTS, "0"));

        JPanel criterias = new JPanel();
        criterias.setLayout(new RiverLayout());
        criterias.add("", new MLabel(Labels.SEARCH_COLON, 'w', filterEdit));
        criterias.add("br hfill", filterEdit);
        criterias.add("br center", buttonSearch);

        int height = packages.getPreferredSize().height;
        packages.setMaximumSize(new Dimension(0, height));
        packages.setMinimumSize(new Dimension(0, height));
        packages.setPreferredSize(new Dimension(0, height));
        JScrollPane scrollPackages = new JScrollPane(packages);
        scrollPackages.setMaximumSize(new Dimension(0, height + 15));
        scrollPackages.setMinimumSize(new Dimension(0, height + 15));
        scrollPackages.setPreferredSize(new Dimension(0, height + 15));
        scrollPackages.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        height = criterias.getPreferredSize().height;
        criterias.setMaximumSize(new Dimension(0, height));
        criterias.setMinimumSize(new Dimension(0, height));
        criterias.setPreferredSize(new Dimension(0, height));
        JScrollPane scrollCriterias = new JScrollPane(criterias);
        scrollCriterias.setMaximumSize(new Dimension(0, height + 15));
        scrollCriterias.setMinimumSize(new Dimension(0, height + 15));
        scrollCriterias.setPreferredSize(new Dimension(0, height + 15));
        scrollCriterias.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel all = new JPanel();
        all.setLayout(new RiverLayout());
        all.add("hfill", scrollPackages);
        all.add("br hfill", scrollCriterias);

        lazyPanel.add("hfill", all);
        lazyPanel.add("br left", new MLabel(Labels.CANDIDATES_COLON, 'j', candsList));
        lazyPanel.add("br hfill vfill", new JScrollPane(candsList));
        lazyPanel.add("br left", infoLabel);

        content.add(lazyPanel, BorderLayout.CENTER);

    }

    boolean spinnerLoaded = false;

    private void loadListData() {
        loadSpinnerModel();

        packageNo = ((Integer) packageNoModel.getValue());

        PartOfSpeech pos = (PartOfSpeech) posCombo.getItemAt(posCombo.getSelectedIndex());
//        Collection<String> words = RemoteUtils.extGraphRemote.dbGetNewWords(packageNo, pos);
//
//        for (String word : words) {
//            ListElement l = new ListElement(word, packageNo, pos);
//            list_elems.add(l);
//        }

        filterEdit.setText("");
        filterList();
    }

    private void filterList() {
        spinnerCheck();

        candsModel.clear();
        String filter = filterEdit.getText();
        for (ListElement e : list_elems) {
            if (e.getWord().startsWith(filter)) {
                candsModel.addElement(e);
            }
        }
    }

    private void spinnerCheck() {
        if (spinnerLoaded) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            workbench.setBusy(true);
        });

        Runnable run = () -> {
            spinnerLoaded = true;
            loadSpinnerModel();
        };
        new Thread(run).start();
    }

    private void loadPoses() {
        PartOfSpeech[] poses = PartOfSpeechManager.getInstance().getAll().toArray(new PartOfSpeech[]{});
        posCombo.removeAllItems();

        for (PartOfSpeech pos : poses) {
            posCombo.addItem(pos);
        }
    }

    private void loadSpinnerModel() {
        ArrayList<Integer> packages = new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;

            @Override
            public int indexOf(Object o) {
                if (o instanceof String) {
                    try {
                        o = Integer.parseInt(((String) o).trim());
                    } catch (NumberFormatException ex) {
                        return -1;
                    }
                }
                return super.indexOf(o);
            }
        };

        //currentMaxPkg = RemoteUtils.extGraphRemote.GetMaxPackageNo((PartOfSpeech) posCombo.getItemAt(posCombo.getSelectedIndex()));
        if (currentMaxPkg == -1) {
            currentMaxPkg = 0;
        }
        infoPackages.setText("(" + currentMaxPkg + ")");

        //Collection<Integer> list_of_packages = RemoteUtils.extGraphRemote.GetPackages((PartOfSpeech) posCombo.getItemAt(posCombo.getSelectedIndex()));
//        if (list_of_packages != null) {
//            packages.addAll(list_of_packages);
//        }
        if (packages.isEmpty()) {
            ArrayList<String> al = new ArrayList<>();
            al.add("");
            packageNoModel.setList(al);
            buttonRecalc.setEnabled(false);
            buttonShow.setEnabled(false);
            buttonAdd.setEnabled(false);
            pckgSpinner.setEnabled(false);

        } else {
            packageNoModel.setList(packages);
            buttonRecalc.setEnabled(true);
            buttonShow.setEnabled(true);
            buttonAdd.setEnabled(true);
            pckgSpinner.setEnabled(true);
        }

        SwingUtilities.invokeLater(() -> {
            content.repaint();
            workbench.setBusy(false);
        });
    }

    @Override
    public JComponent getRootComponent() {
        return candsList;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        spinnerCheck();

        if (!e.getValueIsAdjusting()
                && !candsList.isSelectionEmpty()) {
            ListElement elem = (ListElement) candsList.getSelectedValue();
            candidateChanged_.notifyAllListeners(
                    new Pair<>(elem.getWord(), elem.getPos()),
                    elem.getPkg());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        spinnerCheck();

        if (e.getSource() == filterEdit
                || e.getSource() == buttonSearch) {
            filterList();
            infoLabel.setText(String.format(Labels.NUMBER_OF_ELEMENTS,
                    Integer.toString(candsModel.size())));
        } else if (e.getSource() == posCombo) {
            loadSpinnerModel();
        } else if (e.getSource() == buttonShow
                || e.getSource() == pckgSpinner) {
            if (currentMaxPkg != 0) {
                list_elems.clear();
                loadListData();
                candsList.setSelectedIndex(0);
                infoLabel.setText(String.format(Labels.NUMBER_OF_ELEMENTS,
                        Integer.toString(candsModel.size())));
            }
        } else if (e.getSource() == buttonAdd) {
            if (currentMaxPkg != 0) {
                loadListData();
                for (ListElement el : list_elems) {
                    el.setDescrSize(true);
                }
                candsList.updateUI();
                infoLabel.setText(String.format(Labels.NUMBER_OF_ELEMENTS,
                        Integer.toString(candsModel.size())));
            }
        } else if (e.getSource() == buttonRecalc
                && currentMaxPkg != 0) {
            Integer packageNo = ((Integer) packageNoModel.getValue());
            PartOfSpeech pos = (PartOfSpeech) posCombo.getItemAt(posCombo.getSelectedIndex());

            RebuildGraphsTask t = tasks.get(new Pair<>(packageNo, pos));

            if (t != null) {
                t.show();
                return;
            }

            if (DialogBox.showConfirmDialog(
                    Messages.QUESTION_DO_YOU_WANT_TO_RECALCULATE_GRAPHS,
                    Labels.GRAPHS_CALCULATING,
                    JOptionPane.INFORMATION_MESSAGE,
                    JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                RebuildGraphsTask t1
                        = new RebuildGraphsTask(pos, packageNo, this);
                tasks.put(new Pair<>(packageNo, pos), t1);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        spinnerCheck();

        if (e.getSource() == pckgSpinner) {
        }
        if (e.getSource() instanceof RebuildGraphsTask) {
            synchronized (tasks) {
                RebuildGraphsTask t = (RebuildGraphsTask) e.getSource();
                Pair<Integer, PartOfSpeech> key = new Pair<>(t.getPackageNo(), t.getPos());

                tasks.remove(key);

                String s = Messages.SUCCESS_PACKAGE_RECALCULATION_COMPLETE;
                s = String.format(s, t.getPackageNo());
                DialogBox.showInformation(s);
            }
        }
    }

    public void addCandidateChangedListener(SimpleListenerInterface newListener) {
        candidateChanged_.add(newListener);
    }

    public boolean isSpinnerLoaded() {
        return spinnerLoaded;
    }

    public void setSpinnerLoaded(boolean spinnerLoaded) {
        this.spinnerLoaded = spinnerLoaded;
    }
}
