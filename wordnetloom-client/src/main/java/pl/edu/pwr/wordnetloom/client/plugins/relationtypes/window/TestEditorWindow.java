package pl.edu.pwr.wordnetloom.client.plugins.relationtypes.window;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.ComboBoxPlain;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.TextFieldPlain;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

public class TestEditorWindow extends DialogWindow implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final ButtonExt buttonOk, buttonCancel;

    private final TextFieldPlain testText;
    private final ComboBoxPlain testPos;

    private PartOfSpeech lastPos = PartOfSpeechManager.getInstance().getById(1l);
    private String lastText = "";

    private TestEditorWindow(JFrame owner, PartOfSpeech pos, String text) {
        super(owner, Labels.EDIT_TEST, 650, 130);

        lastPos = pos;
        lastText = text;

        this.setResizable(false);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension normal = new Dimension(100, 25);

        testText = new TextFieldPlain(lastText);
        testText.setPreferredSize(normal);
        testPos = new ComboBoxPlain(PartOfSpeechManager.getInstance().getAll().toArray(new PartOfSpeech[]{}));
        testPos.setPreferredSize(normal);

        buttonOk = new ButtonExt(Labels.OK, this, KeyEvent.VK_O);
        buttonCancel = new ButtonExt(Labels.CANCEL, this, KeyEvent.VK_A);

        if (pos != null) {
            testPos.setSelectedItem(pos);
        }

        this.add("", new LabelExt(Labels.PARTS_OF_SPEECH_COLON, 't', testPos));
        this.add("tab", new LabelExt(Labels.TEST_CONTENT_COLON, 't', testText));

        this.add("br", testPos);
        this.add("tab hfill", testText);

        this.add("p center", buttonOk);
        this.add("", buttonCancel);
    }

    static public Pair<String, PartOfSpeech> showModal(JFrame owner, String text, PartOfSpeech pos) {
        TestEditorWindow frame = new TestEditorWindow(owner, pos, text);
        frame.setVisible(true);
        return new Pair<>(frame.lastText, frame.lastPos);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        if (arg0.getSource() == buttonCancel) {
            lastText = null;
            lastPos = null;
            setVisible(false);

        } else if (arg0.getSource() == buttonOk) {
            lastText = testText.getText();
            lastPos = (PartOfSpeech) testPos.getItemAt(testPos.getSelectedIndex());
            setVisible(false);
        }
    }

}
