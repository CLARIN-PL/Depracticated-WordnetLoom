package pl.edu.pwr.wordnetloom.plugins.dictionary.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.AgeDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.DialectalDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.Dictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.DomainDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.DomainModifierDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.GrammaticalGenderDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.InflectionDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.InterfixDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.LexicalCharacteristicDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.PrefixDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.SourceDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.StatusDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.StyleDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.SuffixDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.TranscriptionDictionary;
import pl.edu.pwr.wordnetloom.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;

public class DictionaryPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JList list;
    private DefaultListModel listModel;
    private JButton btnEdit;
    private JButton btnNewButton;
    private JButton btnDelete;
    private JComboBox dictionaries;
    private JFrame parent;

    public DictionaryPanel(JFrame frame) {
        this.parent = frame;
        setLayout(new FormLayout(new ColumnSpec[]{
                ColumnSpec.decode("max(7dlu;default)"),
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("80px"),
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("80px"),
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("93px"),
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("max(180dlu;default)"),
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,},
                new RowSpec[]{
                        FormSpecs.LINE_GAP_ROWSPEC,
                        RowSpec.decode("51px"),
                        FormSpecs.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("max(163dlu;default)"),
                        FormSpecs.RELATED_GAP_ROWSPEC,
                        FormSpecs.DEFAULT_ROWSPEC,
                        FormSpecs.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("max(23dlu;default)"),}));

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Dictionary Type", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(panel, "3, 2, 7, 1, fill, fill");
        panel.setLayout(new FormLayout(
                new ColumnSpec[]{ColumnSpec.decode("320px"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
                        FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(29dlu;default)"),},
                new RowSpec[]{RowSpec.decode("24px"),}));

        dictionaries = new JComboBox();
        dictionaries.addItem("Choose dictionary");

        for (Dictionary.DictionariesTypes i : Dictionary.DictionariesTypes.values()) {
            dictionaries.addItem(i);
        }

        panel.add(dictionaries, "1, 1, fill, center");

        dictionaries.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                reloadData();
            }
        });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportBorder(
                new TitledBorder(null, "Values", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(scrollPane, "3, 4, 7, 1, fill, fill");

        listModel = new DefaultListModel();

        list = new JList(listModel);
        list.addListSelectionListener(e -> {
            try {
                btnEdit.setEnabled(true);
                btnDelete.setEnabled(true);
            } catch (ArrayIndexOutOfBoundsException ex) {

            }
        });

        scrollPane.setViewportView(list);

        btnEdit = new JButton("Edit");
        btnEdit.addActionListener(this);
        btnEdit.setEnabled(false);
        add(btnEdit, "3, 6");

        btnNewButton = new JButton("New");
        add(btnNewButton, "5, 6");
        btnNewButton.addActionListener(this);

        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(this);
        btnDelete.setEnabled(false);
        add(btnDelete, "7, 6");

        reloadData();
    }

    private void reloadData() {
        if (dictionaries.getSelectedItem() instanceof Dictionary.DictionariesTypes) {
            listModel.removeAllElements();
            List dictionariesList;
            switch ((Dictionary.DictionariesTypes) dictionaries.getSelectedItem()) {
                case AgeDictionary:
                    dictionariesList = RemoteUtils.dictionaryRemote.findAllAgeDictionary();
                    break;
                case GrammaticalGenderDictionary:
                    dictionariesList = RemoteUtils.dictionaryRemote.findAllGrammaticalGenderDictionary();
                    break;
                case IterfixDictionary:
                    dictionariesList = RemoteUtils.dictionaryRemote.findAllInterfixDictionary();
                    break;
                case DomainDictionary:
                    dictionariesList = RemoteUtils.dictionaryRemote.findAllDomainDictionary();
                    break;
                case DomainModifierDictionary:
                    dictionariesList = RemoteUtils.dictionaryRemote.findAllDomainModifiersDictionary();
                    break;
                case LexicalCharacteristicDictionary:
                    dictionariesList = RemoteUtils.dictionaryRemote.findAllLexicalCharacteristicDictionary();
                    break;
                case PrefixDictionary:
                    dictionariesList = RemoteUtils.dictionaryRemote.findAllPrefixDictionary();
                    break;
                case SuffixDictionary:
                    dictionariesList = RemoteUtils.dictionaryRemote.findAllSuffixDictionary();
                    break;
                case StatusDictionary:
                    dictionariesList = RemoteUtils.dictionaryRemote.findAllStatusDictionary();
                    break;
                case StyleDictionary:
                    dictionariesList = RemoteUtils.dictionaryRemote.findAllStyleDictionary();
                    break;
                case TranscriptionDictionary:
                    dictionariesList = RemoteUtils.dictionaryRemote.findAllTranscriptionsDictionary();
                    break;
                case InflectionDictionary:
                    dictionariesList = RemoteUtils.dictionaryRemote.findAllInflectionDictionary();
                    break;
                case DialectalDictionary:
                    dictionariesList = RemoteUtils.dictionaryRemote.findAllDialecticalDictionary();
                    break;
                case SourceDictionary:
                    dictionariesList = RemoteUtils.dictionaryRemote.findAllSourceDictionary();
                    break;
                default:
                    dictionariesList = new ArrayList();
            }

            for (Object d : dictionariesList) {
                listModel.addElement(d);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btnNewButton) {
            Dictionary dic = null;
            if (dictionaries.getSelectedItem() instanceof Dictionary.DictionariesTypes) {
                switch ((Dictionary.DictionariesTypes) dictionaries.getSelectedItem()) {
                    case AgeDictionary:
                        dic = new AgeDictionary();
                        break;
                    case GrammaticalGenderDictionary:
                        dic = new GrammaticalGenderDictionary();
                        break;
                    case IterfixDictionary:
                        dic = new InterfixDictionary();
                        break;
                    case DomainDictionary:
                        dic = new DomainDictionary();
                        break;
                    case DomainModifierDictionary:
                        dic = new DomainModifierDictionary();
                        break;
                    case LexicalCharacteristicDictionary:
                        dic = new LexicalCharacteristicDictionary();
                        break;
                    case PrefixDictionary:
                        dic = new PrefixDictionary();
                        break;
                    case SuffixDictionary:
                        dic = new SuffixDictionary();
                        break;
                    case StatusDictionary:
                        dic = new StatusDictionary();
                        break;
                    case StyleDictionary:
                        dic = new StyleDictionary();
                        break;
                    case TranscriptionDictionary:
                        dic = new TranscriptionDictionary();
                        break;
                    case InflectionDictionary:
                        dic = new InflectionDictionary();
                        break;
                    case DialectalDictionary:
                        dic = new DialectalDictionary();
                        break;
                    case SourceDictionary:
                        dic = new SourceDictionary();
                        break;
                    default:
                        break;
                }
                DictionaryItemEditorWindow.showModal(this.parent,
                        dictionaries.getSelectedItem().toString(), dic);
                reloadData();
            }

        } else if (event.getSource() == btnEdit) {
            DictionaryItemEditorWindow.showModal(this.parent,
                    dictionaries.getSelectedItem().toString(),
                    (Dictionary) listModel.getElementAt(list.getSelectedIndex()));
            reloadData();
        } else if (event.getSource() == btnDelete) {

            int result = DialogBox.showYesNoCancel(String.format("Are you sure you want to delete this item?"));
            if (result == DialogBox.YES) {
                Dictionary toRemove = (Dictionary) listModel.getElementAt(list.getSelectedIndex());
                listModel.removeElement(toRemove);
                RemoteUtils.dictionaryRemote.remove(toRemove);
            }
        }
    }

}
