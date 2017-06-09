package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.ExamplePanel;
import pl.edu.pwr.wordnetloom.client.systems.ui.IconDialog;
import pl.edu.pwr.wordnetloom.client.utils.Labels;

public class ExampleFrame extends IconDialog implements ActionListener {

    private static final long serialVersionUID = -8880344925810883670L;
    private final ExamplePanel panel;
    private String example;
    private String toReturn;

    public ExampleFrame(JFrame baseFrame, String title, String example) {
        super(baseFrame, title, 450, 200);
        this.example = example;
        this.setResizable(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(baseFrame);
        panel = new ExamplePanel();
        panel.getBtnSave().addActionListener(this);
        panel.getBtnCancel().addActionListener(this);
        add(panel, "hfill");
        pack();
    }

    public void setExample(String example) {
        panel.getExampleTextArea().setText(example);
    }

    public String getExample() {
        return toReturn;
    }

    public void changeBtnName() {
        panel.getBtnSave().setText(Labels.CHANGE);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == panel.getBtnSave()) {
            toReturn = panel.getExampleTextArea().getText();
            this.setVisible(false);
            this.dispose();
        } else if (event.getSource() == panel.getBtnCancel()) {
            toReturn = example;
            this.setVisible(false);
            this.dispose();
        }
    }

    static public String showModal(JFrame frame, String title, String example, boolean editable) {
        ExampleFrame modalFrame = new ExampleFrame(frame, title, example);
        if (editable) {
            modalFrame.changeBtnName();
        }
        if (example != null && !"".equals(example)) {
            modalFrame.setExample(example);
        }
        modalFrame.setVisible(true);
        return modalFrame.getExample();
    }
}
