package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.forms.util.LayoutStyle;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.LexicalIM;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.ExampleFrame;
import pl.edu.pwr.wordnetloom.client.systems.enums.RegisterTypes;
import pl.edu.pwr.wordnetloom.client.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.PosManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.ComboBoxPlain;
import pl.edu.pwr.wordnetloom.client.systems.ui.DomainComboBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.LexiconComboBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.PartOfSpeechComboBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.TextFieldPlain;
import pl.edu.pwr.wordnetloom.client.systems.ui.TextPanePlain;
import pl.edu.pwr.wordnetloom.client.utils.Common;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.model.wordnet.Domain;
import pl.edu.pwr.wordnetloom.model.wordnet.Lexicon;
import pl.edu.pwr.wordnetloom.model.wordnet.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;

public class LexicalUnitPropertiesPanel extends JPanel implements
        CaretListener, ActionListener {

    private static final long serialVersionUID = 8598891792812358941L;
    private Sense unit;
    private LexiconComboBox lexicon;
    private TextFieldPlain lemma;
    private TextFieldPlain variant;
    private TextFieldPlain link;
    private ComboBoxPlain<Object> register;
    private PartOfSpeechComboBox partOfSpeech;
    private DomainComboBox domain;
    private TextPanePlain comment;
    private final JButton btnCancel;
    private JButton btnSave;
    private JScrollPane commentScrollPane;
    private JScrollPane scrollPaneExamples;
    private final JButton btnGoToLink;
    private final JButton btnNewExample;
    private final JButton btnEditExample;
    private final JButton btnRemoveExample;
    private JList examplesList;
    private DefaultListModel examplesModel;
    private JLabel lblDefinition;
    private JScrollPane definitionScrollPane;
    private TextPanePlain definition;

    public LexicalUnitPropertiesPanel(final JFrame frame) {
        setLayout(new BorderLayout(0, 0));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FormLayout(new ColumnSpec[]{
            ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX()),
            ColumnSpec.decode("max(57dlu;min)"),
            ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX()),
            ColumnSpec.decode("max(48dlu;min)"),
            ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX()),
            ColumnSpec.decode("max(49dlu;min)"),
            ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX()),
            ColumnSpec.decode("max(28dlu;min):grow"),
            ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX()),
            ColumnSpec.decode("max(17dlu;min)"),
            ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX()),
            ColumnSpec.decode("max(44dlu;default)"),
            ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX()),
            ColumnSpec.decode("max(5dlu;default):grow"),},
                new RowSpec[]{
                    RowSpec.decode("1dlu"),
                    RowSpec.decode("6dlu"),
                    RowSpec.decode("fill:max(14dlu;default)"),
                    RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                    RowSpec.decode("fill:max(14dlu;default)"),
                    RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                    RowSpec.decode("fill:default"),
                    RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                    RowSpec.decode("fill:default"),
                    RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                    RowSpec.decode("fill:max(14dlu;default)"),
                    RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                    RowSpec.decode("max(36dlu;default):grow"),
                    RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                    RowSpec.decode("fill:max(40dlu;pref):grow"),
                    RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                    RowSpec.decode("max(14dlu;default)"),
                    RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                    RowSpec.decode("max(14dlu;default)"),
                    RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                    RowSpec.decode("max(14dlu;default)"),
                    RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                    new RowSpec(Sizes.DEFAULT),
                    RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                    RowSpec.decode("fill:max(14dlu;default)"),
                    RowSpec.decode("max(10dlu;default):grow"),}));

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
        variant.setEditable(false);
        mainPanel.add(variant, "12, 3, left, fill");
        variant.setColumns(10);

        JLabel lblLexicon = new JLabel(Labels.LEXICON_COLON);
        lblLexicon.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblLexicon, "2, 5, left, fill");
        lexicon = new LexiconComboBox(Labels.NOT_CHOSEN);
        lexicon.addActionListener(this);
        lexicon.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Lexicon lex = lexicon.retriveComboBoxItem();
                if (lex != null) {
                    partOfSpeech.filterByLexicon(lex);
                    domain.filterDomainsByLexicon(lex, false);
                }
            }
        });
        mainPanel.add(lexicon, "4, 5, 3, 1, fill, fill");

        JLabel lblPoS = new JLabel(Labels.PARTS_OF_SPEECH_COLON);
        lblPoS.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblPoS, "2, 7, left, default");

        partOfSpeech = new PartOfSpeechComboBox(Labels.NOT_CHOSEN);
        partOfSpeech.withoutFilter();
        partOfSpeech.addActionListener(this);
        partOfSpeech.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                PartOfSpeech pos = partOfSpeech.retriveComboBoxItem();
                if (pos != null) {
                    domain.filterDomainByPos(pos, false);
                }
            }
        });
        mainPanel.add(partOfSpeech, "4, 7, 3, 1, fill, fill");

        JLabel lblDomain = new JLabel(Labels.DOMAIN_COLON);
        lblDomain.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblDomain, "2, 9, left, fill");

        domain = new DomainComboBox(Labels.NOT_CHOSEN);
        domain.allDomains(false);
        domain.addActionListener(this);
        mainPanel.add(domain, "4, 9, 3, 1, fill, fill");

        JLabel lblRegister = new JLabel(Labels.REGISTER_COLON);
        lblRegister.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblRegister, "2, 11, left, fill");

        register = new ComboBoxPlain<>(RegisterTypes.values());
        register.addActionListener(this);
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

        commentScrollPane = new JScrollPane();
        mainPanel.add(commentScrollPane, "4, 15, 9, 1, default, fill");

        comment = new TextPanePlain();
        comment.addCaretListener(this);
        commentScrollPane.setViewportView(comment);

        scrollPaneExamples = new JScrollPane();
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
        btnNewExample.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String exa = ExampleFrame.showModal(frame, Labels.NEW_EXAMPLE,
                        "", false);
                if (exa != null && exa != "") {
                    examplesModel.addElement(exa);
                    btnSave.setEnabled(true);
                    examplesList.setModel(examplesModel);
                }
            }
        });

        JLabel lblExample = new JLabel(Labels.USE_CASE_COLON);
        lblExample.setVerticalAlignment(SwingConstants.TOP);
        lblExample.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblExample, "2, 17, left, fill");
        mainPanel.add(btnNewExample, "12, 17, fill, fill");

        btnEditExample = new JButton(LexicalIM.getEdit());
        btnEditExample.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = examplesList.getSelectedIndex();
                if (idx >= 0) {
                    String modified = ExampleFrame.showModal(frame,
                            Labels.EDIT_EXAMPLE,
                            (String) examplesModel.get(idx), true);
                    String old = examplesModel.get(idx).toString();
                    if (modified != null && !old.equals(modified)) {
                        examplesModel.set(idx, modified);
                        btnSave.setEnabled(true);
                    }
                }
            }
        });
        mainPanel.add(btnEditExample, "12, 19, fill, fill");

        btnRemoveExample = new JButton(LexicalIM.getDelete());
        btnRemoveExample.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = examplesList.getSelectedIndex();
                if (idx >= 0) {
                    examplesModel.remove(idx);
                    btnSave.setEnabled(true);
                }
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
        btnGoToLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    URI uri = new java.net.URI(link.getText());
                    (new LinkRunner(uri)).execute();
                } catch (URISyntaxException use) {
                }
            }
        });
        mainPanel.add(btnGoToLink, "12, 25, fill, fill");

        JTabbedPane tabs = new JTabbedPane();
        tabs.setPreferredSize(new Dimension(620, 500));
        tabs.setLayout(new BorderLayout());
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        add(tabs, BorderLayout.CENTER);
        tabs.addTab("Main", mainPanel);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        btnCancel = new JButton(Labels.CANCEL);
        buttons.add(btnCancel);

        btnSave = new JButton(Labels.SAVE);
        buttons.add(btnSave);
        btnSave.addActionListener(this);
        add(buttons, BorderLayout.SOUTH);
    }

    public Sense getSense() {
        return unit;
    }

    public void setSense(Sense unit) {
        this.unit = unit;
        refreshData();
    }

    @Override
    public void caretUpdate(CaretEvent event) {
        if (event.getSource() instanceof TextFieldPlain) {
            TextFieldPlain field = (TextFieldPlain) event.getSource();
            btnSave.setEnabled(btnSave.isEnabled() | field.wasTextChanged());
        }
        if (event.getSource() instanceof TextPanePlain) {
            TextPanePlain field = (TextPanePlain) event.getSource();
            btnSave.setEnabled(btnSave.isEnabled() | field.wasTextChanged());
        }
    }

    public void refreshData() {
        if (unit != null) {
            LexicalDA.refresh(unit);
        }

        lemma.setText(formatValue(unit != null ? unit.getLemma().getWord() : null));
        variant.setText(unit != null ? "" + unit.getSenseNumber() : null);
        lexicon.setSelectedItem(unit != null ? new CustomDescription<>(unit.getLexicon().toString(), unit.getLexicon()) : null);
        partOfSpeech.setSelectedItem(unit != null ? new CustomDescription<>(
                unit.getPartOfSpeech().toString(), unit
                .getPartOfSpeech()) : null);

        String domainToSet = isDomainCorrectPartOfSpeach();

        domain.setSelectedItem(domainToSet == null ? null
                : new CustomDescription<>(DomainComboBox
                        .nameWithoutPrefix(unit.getDomain().toString()), unit
                        .getDomain()));

        definition.setText(formatValue(unit != null ? Common.getSenseAttribute(
                unit, Sense.DEFINITION) : null));
        comment.setText(formatValue(unit != null ? Common.getSenseAttribute(
                unit, Sense.COMMENT) : null));
        register.setSelectedItem(unit != null ? RegisterTypes.get(Common
                .getSenseAttribute(unit, Sense.REGISTER))
                : RegisterTypes.BRAK_REJESTRU);

        if (unit != null) {
            String use = Common.getSenseAttribute(unit, Sense.USE_CASES);
            if (use != null) {
                String[] exampleString = use.split("\\|");
                for (int i = 0; i < exampleString.length; i++) {
                    examplesModel.addElement(exampleString[i]);
                }
            }
            examplesList.setModel(examplesModel);
        }
        link.setText(formatValue(unit != null ? Common.getSenseAttribute(unit, Sense.LINK) : null));
        btnSave.setEnabled(false);
    }

    private String isDomainCorrectPartOfSpeach() {
        String domainToSet = null;
        if (unit != null) {
            Domain goodDomain = DomainManager.getInstance().getNormalized(
                    unit.getDomain());
            PartOfSpeech goodPOS = PosManager.getInstance().getNormalized(
                    unit.getPartOfSpeech());

            if (goodDomain != null && !goodPOS.contains(goodDomain)) {
                DialogBox.showError(String.format(
                        Messages.ERROR_INCORRECT_DOMAIN, goodDomain.toString(),
                        goodPOS));
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

        if (event.getSource() == domain) {
            btnSave.setEnabled(true);
        } else if (event.getSource() == lexicon) {
            btnSave.setEnabled(true);
        } else if (event.getSource() == register) {
            btnSave.setEnabled(true);
        } else if (event.getSource() == link) {
            btnSave.setEnabled(true);
        } else if (event.getSource() == examplesList) {
            btnSave.setEnabled(true);
        } else if (event.getSource() == partOfSpeech) {
            btnSave.setEnabled(true);
        } else if (event.getSource() == partOfSpeech) {
            btnSave.setEnabled(true);
        }
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

    public ComboBoxPlain<Object> getRegister() {
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

    public JButton getBtnSave() {
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
            } catch (ExecutionException ee) {
                handleException(uri, ee);
            } catch (InterruptedException ie) {
                handleException(uri, ie);
            }
        }

        private static void handleException(URI u, Exception e) {
            JOptionPane.showMessageDialog(null, Messages.WRONG_LINK,
                    Labels.ERROR_OCCURED, JOptionPane.ERROR_MESSAGE);
        }
    }

    public class MyCellRenderer implements ListCellRenderer {

        private JPanel p;
        private JTextArea ta;

        public MyCellRenderer() {
            p = new JPanel();
            p.setLayout(new BorderLayout());

            // text
            ta = new JTextArea();

            ta.setLineWrap(true);
            ta.setWrapStyleWord(true);

            p.add(ta, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(final JList list,
                final Object value, final int index, final boolean isSelected,
                final boolean hasFocus) {

            ta.setText((String) value);
            if (isSelected) {
                ta.setBackground(new Color(135, 206, 250));
            } else if (index % 2 == 0) {
                ta.setBackground(Color.LIGHT_GRAY);
            } else {
                ta.setBackground(Color.gray);
            }
            int width = list.getWidth();
            if (width > 0) {
                ta.setSize(width, Short.MAX_VALUE);
            }
            return p;

        }
    }
}
