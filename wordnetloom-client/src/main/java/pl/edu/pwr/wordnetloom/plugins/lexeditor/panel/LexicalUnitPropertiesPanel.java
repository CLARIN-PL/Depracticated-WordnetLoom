package pl.edu.pwr.wordnetloom.plugins.lexeditor.panel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import pl.edu.pwr.wordnetloom.dto.RegisterTypes;
import pl.edu.pwr.wordnetloom.model.Domain;
import pl.edu.pwr.wordnetloom.model.Lexicon;
import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.yiddish.VariantType;
import pl.edu.pwr.wordnetloom.model.yiddish.YiddishSenseExtension;
import pl.edu.pwr.wordnetloom.plugins.lexeditor.LexicalIM;
import pl.edu.pwr.wordnetloom.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.plugins.lexeditor.frames.ExampleFrame;
import pl.edu.pwr.wordnetloom.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.systems.managers.PosManager;
import pl.edu.pwr.wordnetloom.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.systems.ui.*;
import pl.edu.pwr.wordnetloom.utils.Common;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.utils.Messages;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

// !!! w przypadku chęci powrotu do stanu, w którym przycisk zapisu był dezaktywowany dopóki nie wprowadzono jakieś zmiany, należy usunąć komentarze
// przede wszystkim
public class LexicalUnitPropertiesPanel extends JPanel implements CaretListener, ActionListener {

    private static final long serialVersionUID = 8598891792812358941L;
    private Sense unit;
    private LexiconComboBox lexicon;
    private TextFieldPlain lemma;
    private TextFieldPlain variant;
    private TextFieldPlain link;
    private ComboBoxPlain<RegisterTypes> register;
    private PartOfSpeechComboBox partOfSpeech;
    private DomainComboBox domain;
    private TextPanePlain comment;
    private JButton btnCancel;
    private final JButton btnSave;
    private JButton btnGoToLink;
    private JButton btnNewExample;
    private JButton btnEditExample;
    private JButton btnRemoveExample;
    private JList examplesList;
    private DefaultListModel examplesModel;
    private JLabel lblDefinition;
    private JScrollPane definitionScrollPane;
    private TextPanePlain definition;
    private final JTabbedPane tabs = new JTabbedPane();

    /**
     * Mapa zawierająca słuchaczy, którzy będą przypisani do btnSave. Wartości z mapy będą służyć do usuwania odpowiedniego słuchacza z przycisku btnSave
     * po usunięciu wariantu YiddishPropertiesPanel. Mapa przechowuje wartości związane tylko z wariantami tworzonymi w YiddishPropertiesPanel, ponieważ tylko one mogą
     * być usunięte.
     */
    private Map<String, ActionListener> saveButtonListeners = new HashMap<>();

    public LexicalUnitPropertiesPanel(final JFrame frame) {
        setLayout(new BorderLayout(0, 0));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FormLayout(
                new ColumnSpec[]{FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(57dlu;min)"),
                        FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(48dlu;min)"),
                        FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(49dlu;min)"),
                        FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(28dlu;min):grow"),
                        FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(17dlu;min)"),
                        FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(44dlu;default)"),
                        FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(5dlu;default):grow"),},
                new RowSpec[]{RowSpec.decode("1dlu"), RowSpec.decode("6dlu"),
                        RowSpec.decode("fill:max(14dlu;default)"), FormSpecs.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("fill:max(14dlu;default)"), FormSpecs.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("fill:default"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("fill:default"),
                        FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("fill:max(14dlu;default)"),
                        FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("max(36dlu;default):grow"),
                        FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("fill:max(40dlu;pref):grow"),
                        FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("max(14dlu;default)"),
                        FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("max(14dlu;default)"),
                        FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("max(14dlu;default)"),
                        FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("fill:max(14dlu;default)"), RowSpec.decode("max(10dlu;default):grow"),}));

        tabs.setPreferredSize(new Dimension(620, 650));
        tabs.setLayout(new BorderLayout());
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        add(tabs, BorderLayout.CENTER);
        tabs.insertTab("Main", null, mainPanel, "", 0);

        JLabel lblLemma = new JLabel(Labels.LEMMA_COLON);
        lblLemma.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblLemma, "2, 3, left, default");

        lemma = new TextFieldPlain(Labels.VALUE_UNKNOWN);
        lemma.addCaretListener(this);
        mainPanel.add(lemma, "4, 3, 4, 1, fill, fill");
        lemma.setColumns(10);

        JLabel lblVariant = new JLabel(Labels.NUMBER_COLON);
        lblVariant.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblVariant, "8, 3, 3, 1, fill, fill");

        variant = new TextFieldPlain(Labels.VALUE_UNKNOWN);
        variant.addCaretListener(this);
        mainPanel.add(variant, "12, 3, left, fill");
        variant.setColumns(10);

        JLabel lblLexicon = new JLabel(Labels.LEXICON_COLON);
        lblLexicon.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblLexicon, "2, 5, left, fill");
        lexicon = new LexiconComboBox(Labels.NOT_CHOSEN);
        lexicon.addActionListener(this);
        lexicon.addItemListener(e -> {
            Lexicon lex = lexicon.retriveComboBoxItem();
            if (lex != null) {
                if (lex.getId().equals(3l)) {
                    lockForJidysz(false);
                } else {
                    lockForJidysz(true);
                }
                partOfSpeech.filterByLexicon(lex);
                domain.filterDomainsByLexicon(lex, false);
            }
        });
        mainPanel.add(lexicon, "4, 5, 3, 1, fill, fill");

        JLabel lblPoS = new JLabel(Labels.PARTS_OF_SPEECH_COLON);
        lblPoS.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblPoS, "2, 7, left, default");

        partOfSpeech = new PartOfSpeechComboBox(Labels.NOT_CHOSEN);
        partOfSpeech.withoutFilter();
//		partOfSpeech.addActionListener(this);
        partOfSpeech.addItemListener(e -> {
            PartOfSpeech pos = partOfSpeech.retriveComboBoxItem();
            if (pos != null) {
                domain.filterDomainByPos(pos, false);
                domain.setSelectedIndex(1);
            }
        });
        mainPanel.add(partOfSpeech, "4, 7, 3, 1, fill, fill");

        JLabel lblDomain = new JLabel(Labels.DOMAIN_COLON);
        lblDomain.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblDomain, "2, 9, left, fill");

        domain = new DomainComboBox(Labels.NOT_CHOSEN);
        domain.allDomains(false);
//		domain.addActionListener(this);
        mainPanel.add(domain, "4, 9, 3, 1, fill, fill");

        JLabel lblRegister = new JLabel(Labels.REGISTER_COLON);
        lblRegister.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblRegister, "2, 11, left, fill");

        register = new ComboBoxPlain<>(RegisterTypes.values());
//		register.addActionListener(this);
        mainPanel.add(register, "4, 11, 3, 1, fill, fill");

        lblDefinition = new JLabel(Labels.DEFINITION_COLON);
        lblDefinition.setVerticalAlignment(SwingConstants.TOP);
        lblDefinition.setHorizontalAlignment(SwingConstants.LEFT);
        mainPanel.add(lblDefinition, "2, 13, left, top");

        definitionScrollPane = new JScrollPane();
        mainPanel.add(definitionScrollPane, "4, 13, 9, 1, default, fill");

        definition = new TextPanePlain();
        definition.addCaretListener(this);
        definitionScrollPane.setViewportView(definition);

        JLabel lblComment = new JLabel(Labels.COMMENT_COLON);
        lblComment.setVerticalAlignment(SwingConstants.TOP);
        lblComment.setHorizontalAlignment(SwingConstants.LEFT);
        mainPanel.add(lblComment, "2, 15, left, default");

        JScrollPane commentScrollPane = new JScrollPane();
        mainPanel.add(commentScrollPane, "4, 15, 9, 1, default, fill");

        comment = new TextPanePlain();
        comment.addCaretListener(this);
        commentScrollPane.setViewportView(comment);

        JScrollPane scrollPaneExamples = new JScrollPane();
        mainPanel.add(scrollPaneExamples, "4, 17, 7, 7, default, fill");

        examplesList = new JList() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }
        };
        examplesList.setCellRenderer(new MyCellRenderer());

        ComponentListener l = new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                examplesList.setFixedCellHeight(10);
                examplesList.setFixedCellHeight(-1);
            }

        };

        examplesList.addComponentListener(l);
        examplesModel = new DefaultListModel();
        scrollPaneExamples.setViewportView(examplesList);

        btnNewExample = new JButton(LexicalIM.getAdd());
        btnNewExample.addActionListener(e -> {

            String exa = ExampleFrame.showModal(frame, Labels.NEW_EXAMPLE, "", false);
            if (exa != null && exa != "") {
                examplesModel.addElement(exa);
//					btnSave.setEnabled(true);
                examplesList.setModel(examplesModel);
            }
        });

        JLabel lblExample = new JLabel(Labels.USE_CASE_COLON);
        lblExample.setVerticalAlignment(SwingConstants.TOP);
        lblExample.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblExample, "2, 17, left, fill");
        mainPanel.add(btnNewExample, "12, 17, fill, fill");

        btnEditExample = new JButton(LexicalIM.getEdit());
        btnEditExample.addActionListener(e -> {
            int idx = examplesList.getSelectedIndex();
            if (idx >= 0) {
                String modified = ExampleFrame.showModal(frame, Labels.EDIT_EXAMPLE,
                        (String) examplesModel.get(idx), true);
                String old = examplesModel.get(idx).toString();
                if (modified != null && !old.equals(modified)) {
                    examplesModel.set(idx, modified);
//						btnSave.setEnabled(true);
                }
            }
        });
        mainPanel.add(btnEditExample, "12, 19, fill, fill");

        btnRemoveExample = new JButton(LexicalIM.getDelete());
        btnRemoveExample.addActionListener(e -> {
            int idx = examplesList.getSelectedIndex();
            if (idx >= 0) {
                examplesModel.remove(idx);
//					btnSave.setEnabled(true);
            }
        });
        mainPanel.add(btnRemoveExample, "12, 21, fill, fill");

        JLabel lblLink = new JLabel(Labels.LINK_COLON);
        lblLink.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblLink, "2, 25, left, fill");

        link = new TextFieldPlain(Labels.VALUE_UNKNOWN);
        link.addCaretListener(this);
        mainPanel.add(link, "4, 25, 7, 1, fill, fill");
        link.setColumns(10);

        btnGoToLink = new JButton(LexicalIM.getRight());
        btnGoToLink.addActionListener(e -> {
            try {
                URI uri = new URI(link.getText());
                (new LinkRunner(uri)).execute();
            } catch (URISyntaxException use) {
            }
        });
        mainPanel.add(btnGoToLink, "12, 25, fill, fill");

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        btnCancel = new JButton(Labels.CANCEL);
        buttons.add(btnCancel);

        btnSave = new JButton(Labels.SAVE);

        buttons.add(btnSave);
        add(buttons, BorderLayout.SOUTH);
    }

    void removeTab(YiddishPropertiesPanel c) {
        btnSave.removeActionListener(btnSave.getActionListeners()[0]);
        tabs.remove(tabs.getSelectedIndex());
        btnSave.removeActionListener(saveButtonListeners.get(c.getUIClassID()));
    }

    public Sense getSense() {
        return unit;
    }

    public void setSense(Sense unit) {
        this.unit = unit;
        refreshData();
    }

    private void lockForJidysz(boolean enabled) {
        register.setEnabled(enabled);
        examplesList.setEnabled(enabled);
        definition.setEnabled(enabled);
        btnEditExample.setEnabled(enabled);
        btnNewExample.setEnabled(enabled);
        btnRemoveExample.setEnabled(enabled);
        btnGoToLink.setEnabled(enabled);
    }

    @Override
    public void caretUpdate(CaretEvent event) {
//		if (event.getSource() instanceof TextFieldPlain) {
//			TextFieldPlain field = (TextFieldPlain) event.getSource();
//			btnSave.setEnabled(btnSave.isEnabled() | field.wasTextChanged());
//		}
//		if (event.getSource() instanceof TextPanePlain) {
//			TextPanePlain field = (TextPanePlain) event.getSource();
//			btnSave.setEnabled(btnSave.isEnabled() | field.wasTextChanged());
//		}
    }

    public void refreshData() {

        if (unit != null) {

            LexicalDA.refresh(unit);
            lemma.setText(formatValue(unit.getLemma().getWord()));
            variant.setText("" + unit.getSenseNumber());
            lexicon.setSelectedItem(unit.getLexicon().toString(), unit.getLexicon());
            partOfSpeech.setSelectedItem(unit.getPartOfSpeech().toString(), unit.getPartOfSpeech());
            String domainToSet = isDomainCorrectPartOfSpeech();

            domain.setSelectedItem(domainToSet == null ? null
                    : new CustomDescription<>(DomainComboBox.nameWithoutPrefix(unit.getDomain().toString()),
                    unit.getDomain()));

            RegisterTypes reg = unit != null ? RegisterTypes.get(Common.getSenseAttribute(unit, Sense.REGISTER))
                    : RegisterTypes.BRAK_REJESTRU;

            definition.setText(formatValue(Common.getSenseAttribute(unit, Sense.DEFINITION)));
            comment.setText(formatValue(Common.getSenseAttribute(unit, Sense.COMMENT)));

            reg = reg != null ? reg : RegisterTypes.BRAK_REJESTRU;
            register.setSelectedItem(reg.name(), reg);

            String use = Common.getSenseAttribute(unit, Sense.USE_CASES);
            if (use != null) {
                String[] exampleString = use.split("\\|");
                for (String anExampleString : exampleString) {
                    examplesModel.addElement(anExampleString);
                }
            }
            examplesList.setModel(examplesModel);

            link.setText(formatValue(Common.getSenseAttribute(unit, Sense.LINK)));
            addYiddishTabs(unit.getYiddishSenseExtension());
        }

//		btnSave.setEnabled(false);
    }

    private void addYiddishTabs(final Set<YiddishSenseExtension> ext) {
        if (ext.isEmpty() && unit.getLexicon().getId().equals(3l)) {
            YiddishSenseExtension yse = new YiddishSenseExtension(unit);
            ext.add(yse);
        }
        ext.stream().forEach(e -> {
            final YiddishPropertiesPanel panel = new YiddishPropertiesPanel(this, e);
            ActionListener listener = e1 -> panel.save();
            if (VariantType.Yiddish_Primary_Lemma == e.getVariant()) {
                tabs.insertTab(e.getVariant().name().replaceAll("_", " "), null, new JScrollPane(panel), "", 1);
            } else { // wariant
                tabs.addTab(e.getVariant().name().replaceAll("_", " "), new JScrollPane(panel));
                saveButtonListeners.put(panel.getUIClassID(), listener); // w przypadku dodawania wariantu zapisujemy sluchacza, aby można go było później usunąć z przycisku btnSave
            }
            btnSave.addActionListener(listener);

        });
    }

    void addTab(YiddishSenseExtension e) {
        final YiddishPropertiesPanel panel = new YiddishPropertiesPanel(this, e);
//		btnSave.addActionListener(l -> panel.save());
        ActionListener listener = e1 -> panel.save();
        btnSave.addActionListener(listener);
        saveButtonListeners.put(panel.getUIClassID(), listener);
        tabs.addTab(e.getVariant().name().replaceAll("_", " "), new JScrollPane(panel));
    }

    private String isDomainCorrectPartOfSpeech() {
        String domainToSet = null;
        if (unit != null) {
            Domain goodDomain = DomainManager.getInstance().getNormalized(unit.getDomain());
            PartOfSpeech goodPOS = PosManager.getInstance().getNormalized(unit.getPartOfSpeech());

            if (goodDomain != null && !goodPOS.contains(goodDomain)) {
                DialogBox.showError(String.format(Messages.ERROR_INCORRECT_DOMAIN, goodDomain.toString(), goodPOS));
            } else {
                domainToSet = unit != null && goodDomain != null ? goodDomain.toString() : null;
            }
        }
        return domainToSet;
    }

    private String formatValue(String value) {
        return (value == null || value.length() == 0) ? null : value;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

//		if (event.getSource() == domain) {
//			btnSave.setEnabled(true);
//		} else if (event.getSource() == lexicon) {
//			btnSave.setEnabled(true);
//		} else if (event.getSource() == register) {
//			btnSave.setEnabled(true);
//		} else if (event.getSource() == link) {
//			btnSave.setEnabled(true);
//		} else if (event.getSource() == examplesList) {
//			btnSave.setEnabled(true);
//		} else if (event.getSource() == partOfSpeech) {
//			btnSave.setEnabled(true);
//		} else if (event.getSource() == partOfSpeech) {
//			btnSave.setEnabled(true);
//		}
    }

    public ComboBoxPlain<Lexicon> getLexicon() {
        return lexicon;
    }

    public TextFieldPlain getLemma() {
        return lemma;
    }

    public TextFieldPlain getVariant() {
        return variant;
    }

    public TextFieldPlain getLink() {
        return link;
    }

    public ComboBoxPlain<RegisterTypes> getRegister() {
        return register;
    }

    public ComboBoxPlain<PartOfSpeech> getPartOfSpeech() {
        return partOfSpeech;
    }

    public ComboBoxPlain<Domain> getDomain() {
        return domain;
    }

    public JTextPane getDefinition() {
        return definition;
    }

    public JTextPane getComment() {
        return comment;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    public DefaultListModel getExamplesModel() {
        return examplesModel;
    }

    public final JButton getBtnSave() {
        return btnSave;
    }

    private static class LinkRunner extends SwingWorker<Void, Void> {

        private final URI uri;

        private LinkRunner(URI u) {
            if (u == null) {
                throw new NullPointerException();
            }
            uri = u;
        }

        @Override
        protected Void doInBackground() throws Exception {
            Desktop desktop = java.awt.Desktop.getDesktop();
            desktop.browse(uri);
            return null;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (ExecutionException | InterruptedException ee) {
                handleException(uri, ee);
            }
        }

        private static void handleException(URI u, Exception e) {
            JOptionPane.showMessageDialog(null, Messages.WRONG_LINK, Labels.ERROR_OCCURED, JOptionPane.ERROR_MESSAGE);
        }
    }

    public class MyCellRenderer implements ListCellRenderer {

        private JPanel p;
        private JTextArea ta;

        MyCellRenderer() {
            p = new JPanel();
            p.setLayout(new BorderLayout());

            // text
            ta = new JTextArea();

            ta.setLineWrap(true);
            ta.setWrapStyleWord(true);

            p.add(ta, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(final JList list, final Object value, final int index,
                                                      final boolean isSelected, final boolean hasFocus) {

            ta.setText((String) value);
            if (isSelected) {
                ta.setBackground(new Color(135, 206, 250));
            } else {
                if (index % 2 == 0)
                    ta.setBackground(Color.LIGHT_GRAY);
                else
                    ta.setBackground(Color.gray);
            }
            int width = list.getWidth();
            if (width > 0)
                ta.setSize(width, Short.MAX_VALUE);
            return p;

        }
    }
}

