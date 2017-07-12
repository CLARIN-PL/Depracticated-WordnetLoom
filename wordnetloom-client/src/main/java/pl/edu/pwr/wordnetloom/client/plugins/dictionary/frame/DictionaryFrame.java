package pl.edu.pwr.wordnetloom.client.plugins.dictionary.frame;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import pl.edu.pwr.wordnetloom.client.plugins.dictionary.panel.EditDictionaryPanel;
import pl.edu.pwr.wordnetloom.client.systems.ui.IconDialog;

public class DictionaryFrame extends IconDialog {

    private static final long serialVersionUID = 1L;

    private EditDictionaryPanel panel;

    public DictionaryFrame(JFrame baseFrame, String title) {
        super(baseFrame, title);
        initPanel();
        initAndCalculateWindowSize();
        pack();
    }

    private void initPanel() {
        panel = new EditDictionaryPanel();
        this.add("br center", panel);
    }

    private void initAndCalculateWindowSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 655;
        int height = 295;
        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;
        this.setBounds(x, y, width, height);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public String showModal() {
        this.setVisible(true);
        this.dispose();
        return "";
    }

}
