package pl.edu.pwr.wordnetloom.client.plugins.lexicon.window;

import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LexiconsWindow extends DialogWindow implements ActionListener {

    private static final long serialVersionUID = 1L;

    private WebLabel infoLabel;

    private final MButton buttonOk = MButton.buildOkButton()
            .withActionListener(this)
            .withWidth(65)
            .withEnabled(true);

    private final Map<Long, WebCheckBox> checkboxes = new HashMap<>();

    private final WebPanel panel = new WebPanel();
    private final GridLayout layout = new GridLayout(3, 2, 3, 5);

    public LexiconsWindow(WebFrame owner, Set<Lexicon> lexicons) {
        super(owner, Labels.LEXICON);
        initInfoLabel(Labels.CHOOSE_LEXICON_TO_WORK);

        panel.setLayout(layout);
        panel.setMargin(20, 20, 10, 10);

        buildCheckBoxesForAvailableLexicons();
        setLexicons(lexicons);
        addLexicons();
        initButton();
        initAndCalculateWindowSize();
        pack();
        setLocationRelativeTo(owner);
    }

    private void buildCheckBoxesForAvailableLexicons() {
        LexiconManager
                .getInstance()
                .getLexicons()
                .forEach(l -> checkboxes.put(l.getId(), new WebCheckBox(l.getName())));
    }

    private void initInfoLabel(String infoMessage) {
        infoLabel = new WebLabel(infoMessage);
        infoLabel.setMargin(5, 5, 5, 5);
        add("br center", infoLabel);
    }

    private void setLexicons(Set<Lexicon> list) {
        list.forEach(item -> {
            checkboxes.get(item.getId()).setSelected(true);
        });
    }

    private void addLexicons() {
        add("br center hfill vfill", panel);

        checkboxes.forEach((k, v) -> panel.add(v));
    }

    private void initButton() {
        add("br center", buttonOk);
    }

    private void initAndCalculateWindowSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 300;
        int height = 60 + 30 * (checkboxes.size() + 1);
        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;
        setBounds(x, y, width, height);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private Set<Lexicon> getSelected() {

        Set<Lexicon> selected = new HashSet<>();

        checkboxes.forEach((k, v) -> {
            if (v.isSelected()) {
                selected.add(LexiconManager.getInstance().findLexiconById(k));
            }
        });

        return selected;
    }

    static public Set<Lexicon> showModal(WebFrame parent, Set<Lexicon> lexicons) {

        LexiconsWindow frame = new LexiconsWindow(parent, lexicons);
        frame.setVisible(true);
        return frame.getSelected();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == buttonOk) {
            setVisible(false);
            dispose();
        }
    }

}
