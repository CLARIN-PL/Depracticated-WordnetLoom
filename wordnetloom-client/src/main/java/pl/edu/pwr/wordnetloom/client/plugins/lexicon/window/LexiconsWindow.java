package pl.edu.pwr.wordnetloom.client.plugins.lexicon.window;

import org.apache.commons.collections15.map.HashedMap;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class LexiconsWindow extends DialogWindow implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final Map<String, Long> lexiconMap = new HashedMap<>();
    private JLabel infoLabel;

    private final MButton buttonOk = MButton.buildOkButton()
            .withActionListener(this)
            .withEnabled(true);

    private JCheckBox[] arrayOfLexiconCheckBoxes;

    public LexiconsWindow(JFrame owner, List<Long> lexiconsFromConfig) {
        super(owner, Labels.LEXICON);
        initInfoLabel(Labels.CHOOSE_LEXICON_TO_WORK);
        initAndLoadCheckBoxes(lexiconsFromConfig);
        initAndCalculateWindowSize();
        initButton();
        pack();

        setLocationRelativeTo(owner);
    }

    private void initInfoLabel(String infoMessage) {
        infoLabel = new JLabel(infoMessage);
        add("br left", infoLabel);
    }

    private void initAndLoadCheckBoxes(List<Long> lexiconsFromConfig) {

        Collection<Lexicon> lexicons = LexiconManager.getInstance().getFullLexicons();
        arrayOfLexiconCheckBoxes = new JCheckBox[lexicons.size()];
        int i = 0;
        for (Lexicon lexicon : lexicons) {
            lexiconMap.put(lexicon.getName(), lexicon.getId());
            arrayOfLexiconCheckBoxes[i] = new JCheckBox(lexicon.getName());
            if (lexiconsFromConfig.contains(lexicon.getId())) {
                arrayOfLexiconCheckBoxes[i].setSelected(true);
            } else {
                arrayOfLexiconCheckBoxes[i].setSelected(false);
            }
            add("br tab left", arrayOfLexiconCheckBoxes[i]);
            i++;
        }
    }

    private void initButton() {
        add("br center", buttonOk);
    }

    private void initAndCalculateWindowSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 300;
        int height = 60 + 30 * (lexiconMap.size() + 1);
        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;
        setBounds(x, y, width, height);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private List<Long> getSelectedLexicons() {
        List selected = new ArrayList();
        for (int i = 0; i < arrayOfLexiconCheckBoxes.length; i++) {
            if (arrayOfLexiconCheckBoxes[i].isSelected()) {
                selected.add(lexiconMap.get(arrayOfLexiconCheckBoxes[i].getName()));
            }
        }
        return selected;
    }

    /**
     * Show lexicon window
     *
     * @return Lexicons as string list
     */
    public List<Long> showModal() {
        setVisible(true);
        dispose();
        return getSelectedLexicons();
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == buttonOk) {
            setVisible(false);
        }
    }

}
