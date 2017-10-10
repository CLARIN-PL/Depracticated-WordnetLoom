package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.ComboBoxPlain;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.TextFieldPlain;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;
import pl.edu.pwr.wordnetloom.relationtest.model.SenseRelationTest;
import se.datadosen.component.RiverLayout;

public class TestEditorWindow extends DialogWindow implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final ButtonExt buttonOk, buttonCancel;

    private final TextFieldPlain testText;
    private final ComboBoxPlain posA;
    private final ComboBoxPlain posB;

    private PartOfSpeech lastAPos;
    private PartOfSpeech lastBPos;
    private String lastText = "";

    private RelationTest currentRelationTest;

    private TestEditorWindow(JFrame owner, RelationTest test) {
        super(owner, Labels.EDIT_TEST, 650, 130);
        currentRelationTest = test;

        lastAPos = test.getSenseApartOfSpeech();
        lastBPos = test.getSenseBpartOfSpeech();
        /*if(lastAPos == null){
            lastAPos = PartOfSpeechManager.getInstance().getById(1L);
        }
        if(lastBPos == null){
            lastBPos = PartOfSpeechManager.getInstance().getById(1L);
        }*/
        lastText = test.getTest();

        this.setResizable(false);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension normal = new Dimension(100, 25);

        testText = new TextFieldPlain(lastText);
        testText.setPreferredSize(normal);

        PartOfSpeech[] posArray = PartOfSpeechManager.getInstance().getAll().toArray(new PartOfSpeech[]{});
        posA = new ComboBoxPlain(posArray);
        posA.setPreferredSize(normal);
        posA.setRenderer(new PartOfSpeechCellRenderer());

        posB = new ComboBoxPlain(posArray);
        posB.setPreferredSize(normal);
        posB.setRenderer(new PartOfSpeechCellRenderer());

        buttonOk = new ButtonExt(Labels.OK, this, KeyEvent.VK_O);
        buttonCancel = new ButtonExt(Labels.CANCEL, this, KeyEvent.VK_A);

        if(lastAPos != null){
            posA.setSelectedItem(lastAPos);
        }
        if(lastBPos != null){
            posB.setSelectedItem(lastBPos);
        }


//        this.add("", new LabelExt(Labels.PARTS_OF_SPEECH_COLON, 't', posA));
//        this.add("tab", new LabelExt(Labels.TEST_CONTENT_COLON, 't', testText));
//
//        this.add("br", posA);
//        this.add("tab hfill", testText);
//
//        this.add("p center", buttonOk);
//        this.add("", buttonCancel);

        final String TAB_FILL = RiverLayout.TAB_STOP + " " + RiverLayout.HFILL;
        final String TAB_BREAK = RiverLayout.LINE_BREAK + " " + RiverLayout.TAB_STOP;

        LabelExt partOfSpeechALabel = new LabelExt(Labels.PARTS_OF_SPEECH_COLON, 't', posA);
        LabelExt testContentLabel = new LabelExt(Labels.TEST_CONTENT_COLON, 't', testText);

        this.add(RiverLayout.LINE_BREAK, partOfSpeechALabel);
        this.add(RiverLayout.TAB_STOP + " " + RiverLayout.HFILL, posA);
        this.add(RiverLayout.LINE_BREAK + " " + RiverLayout.TAB_STOP + " " + RiverLayout.HFILL, posB);
        this.add(RiverLayout.LINE_BREAK, testContentLabel);
        this.add(TAB_FILL, testText);
        this.add(RiverLayout.PARAGRAPH_BREAK + " " + RiverLayout.CENTER, buttonOk);
        this.add(buttonCancel);
    }

//    static public Pair<String, PartOfSpeech> showModal(JFrame owner, String text, PartOfSpeech pos) {
//        TestEditorWindow frame = new TestEditorWindow(owner, pos, text);
//        frame.setVisible(true);
//        return new Pair<>(frame.lastText, frame.lastAPos);
//    }

    public static RelationTest showModal(JFrame owner, RelationTest test){
        RelationTest editedTest = test;
        if(editedTest == null){
            editedTest = new SenseRelationTest();
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
    private String locale;

    public PartOfSpeechCellRenderer(){
        super();
        locale = RemoteConnectionProvider.getInstance().getLanguage();
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        defaultRenderer.setText(((PartOfSpeech)value).getName(locale));
        return defaultRenderer;
    }
}
