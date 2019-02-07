package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window;

import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class TestEditorWindow extends DialogWindow implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final MButton buttonOk, buttonCancel;

    private final MTextField testText;
    private final MComboBox posA;
    private final MComboBox posB;

    private PartOfSpeech lastAPos;
    private PartOfSpeech lastBPos;
    private String lastText = "";

    private final RelationTest currentRelationTest;

    private TestEditorWindow(WebFrame owner, RelationTest test) {
        super(owner, Labels.EDIT_TEST, 650, 130);
        currentRelationTest = test;

        lastAPos = test.getSenseApartOfSpeech();
        lastBPos = test.getSenseBpartOfSpeech();
        lastText = test.getTest();

        setResizable(false);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension normal = new Dimension(100, 25);

        testText = new MTextField(lastText);
        testText.setPreferredSize(normal);

        PartOfSpeech[] posArray = PartOfSpeechManager.getInstance().getAll().toArray(new PartOfSpeech[]{});
        posA = new MComboBox(posArray);
        posA.setPreferredSize(normal);
        posA.setRenderer(new PartOfSpeechCellRenderer());

        posB = new MComboBox(posArray);
        posB.setPreferredSize(normal);
        posB.setRenderer(new PartOfSpeechCellRenderer());

        buttonOk = MButton.buildSaveButton()
                .withActionListener(this)
                .withMnemonic(KeyEvent.VK_S);

        buttonCancel = MButton.buildCancelButton()
                .withActionListener(this)
                .withMnemonic(KeyEvent.VK_CANCEL);

        if (lastAPos != null) {
            posA.setSelectedItem(lastAPos);
        }
        if (lastBPos != null) {
            posB.setSelectedItem(lastBPos);
        }


//        this.add("", new MLabel(Labels.PARTS_OF_SPEECH_COLON, 't', posA));
//        this.add("tab", new MLabel(Labels.TEST_CONTENT_COLON, 't', testText));
//
//        this.add("br", posA);
//        this.add("tab hfill", testText);
//
//        this.add("p center", buttonOk);
//        this.add("", buttonCancel);

        String TAB_FILL = RiverLayout.TAB_STOP + " " + RiverLayout.HFILL;
        String TAB_BREAK = RiverLayout.LINE_BREAK + " " + RiverLayout.TAB_STOP;

        MLabel partOfSpeechALabel = new MLabel(Labels.PARTS_OF_SPEECH_COLON, 't', posA);
        MLabel testContentLabel = new MLabel(Labels.TEST_CONTENT_COLON, 't', testText);

        add(RiverLayout.LINE_BREAK, partOfSpeechALabel);
        add(RiverLayout.TAB_STOP + " " + RiverLayout.HFILL, posA);
        add(RiverLayout.LINE_BREAK + " " + RiverLayout.TAB_STOP + " " + RiverLayout.HFILL, posB);
        add(RiverLayout.LINE_BREAK, testContentLabel);
        add(TAB_FILL, testText);
        add(RiverLayout.PARAGRAPH_BREAK + " " + RiverLayout.CENTER, buttonOk);
        add(buttonCancel);
    }

//    static public Pair<String, PartOfSpeech> showModalAndSaveRelation(JFrame owner, String text, PartOfSpeech pos) {
//        TestEditorWindow frame = new TestEditorWindow(owner, pos, text);
//        frame.setVisible(true);
//        return new Pair<>(frame.lastText, frame.lastAPos);
//    }

    public static RelationTest showModal(WebFrame owner, RelationTest test) {
        RelationTest editedTest = test;
        if (editedTest == null) {
            editedTest = new RelationTest();
        }
        TestEditorWindow frame = new TestEditorWindow(owner, editedTest);
        frame.setVisible(true);
//        return new Pair<>(frame.lastText, frame.lastAPos);

        RelationTest relationTest = frame.currentRelationTest;
        relationTest.setSenseApartOfSpeech(frame.lastAPos);
        relationTest.setSenseBpartOfSpeech(frame.lastBPos);
        relationTest.setTest(frame.lastText);
        return relationTest;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        if (arg0.getSource() == buttonCancel) {
            // nie wykonujemy żadnych czynności, tylko zamykamy okno
            setVisible(false);

        } else if (arg0.getSource() == buttonOk) {
            lastText = testText.getText();
            lastAPos = (PartOfSpeech) posA.getSelectedItem();
            lastBPos = (PartOfSpeech) posB.getSelectedItem();

            setVisible(false);
        }
    }


}

class PartOfSpeechCellRenderer implements ListCellRenderer {
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    public PartOfSpeechCellRenderer() {
        super();
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        defaultRenderer.setText(LocalisationManager.getInstance().getLocalisedString(((PartOfSpeech) value).getName()));
        return defaultRenderer;
    }
}
