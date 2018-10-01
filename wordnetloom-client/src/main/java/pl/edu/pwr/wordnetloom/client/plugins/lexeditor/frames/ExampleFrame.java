package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames;

import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.ExamplePanel;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.common.model.Example;
import pl.edu.pwr.wordnetloom.sense.model.SenseExample;

import javax.validation.constraints.NotNull;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExampleFrame extends DialogWindow implements ActionListener {

    private static final long serialVersionUID = -8880344925810883670L;
    private final ExamplePanel panel;
    private Example example;
    private Example toReturn;

    public ExampleFrame(WebFrame baseFrame, String title,@NotNull Example example) {
        super(baseFrame, title, 450, 200);
        this.example = example;
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(baseFrame);
        panel = new ExamplePanel();
        panel.setExample(example);
        panel.getBtnSave().addActionListener(this);
        panel.getBtnCancel().addActionListener(this);
        add(panel, "hfill");
        pack();
    }

    public void setExample(Example example) {
        if (example != null){
            panel.getExampleTextArea().setText(example.getExample());
//            panel.getTypeComboBox().setSelectedItem(example.getType());
        }
    }

    public Example getExample() {
        return toReturn;
    }

    public void changeBtnName() {
        panel.getBtnSave().setText(Labels.CHANGE);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == panel.getBtnSave()) {
            toReturn = example.copy();
            toReturn.setExample(panel.getExampleTextArea().getText());
            toReturn.setType((String) panel.getTypeComboBox().getSelectedItem());
            setVisible(false);
            dispose();
        } else if (event.getSource() == panel.getBtnCancel()) {
            toReturn = example;
            setVisible(false);
            dispose();
        }
    }

    static public Example showModal(WebFrame frame, String title, @NotNull Example example, boolean editable) {
        ExampleFrame modalFrame = new ExampleFrame(frame, title, example);
        if (editable) {
            modalFrame.changeBtnName();
        }
        modalFrame.setExample(example);
        modalFrame.setVisible(true);
        return modalFrame.getExample();
    }
}
