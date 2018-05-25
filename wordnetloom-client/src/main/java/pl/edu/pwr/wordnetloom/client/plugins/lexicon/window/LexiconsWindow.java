package pl.edu.pwr.wordnetloom.client.plugins.lexicon.window;

import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.GroupView;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import se.datadosen.component.RiverLayout;

import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LexiconsWindow extends DialogWindow implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final MButton buttonOk = MButton.buildOkButton()
            .withActionListener(this)
            .withWidth(65)
            .withEnabled(true);

    private final Map<Long, Pair<String, WebCheckBox>> checkboxes = LexiconManager
            .getInstance()
            .getLexicons()
            .stream()
            .collect(Collectors.toMap(Lexicon::getId, lexicon -> new Pair<>(lexicon.getName(), new WebCheckBox())));

    public LexiconsWindow(WebFrame owner, Set<Lexicon> lexicons) {
        super(owner, Labels.LEXICON);
        initAndCalculateWindowSize();
        setLocationRelativeTo(owner);

        setLexicons(lexicons);
        setLayout(new RiverLayout());

        WebPanel panel = new WebPanel();
        panel.setFocusable(false);
        panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), Labels.CHOOSE_LEXICON_TO_WORK, TitledBorder.LEADING,
                TitledBorder.TOP, null, new Color(51, 51, 51)));
        panel.setLayout(new BorderLayout());

        panel.add(GroupView.createGroupView(checkboxes.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getValue().getA(), e -> e.getValue().getB())), null,0.2f,0.8f));

        add("hfill vfill", panel);
        add("br center", buttonOk);
        pack();

    }

    private void setLexicons(Set<Lexicon> list) {
        list.forEach(item ->
                checkboxes.get(item.getId())
                        .getB()
                        .setSelected(true));
    }

    private void initAndCalculateWindowSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 400;
        int height = 220;
        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;
        setBounds(x, y, width, height);
        setPreferredSize(new Dimension(width, height));
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private Set<Lexicon> getSelected() {
        return checkboxes.entrySet()
                .stream()
                .filter(e -> e.getValue().getB().isSelected())
                .map(e -> LexiconManager.getInstance().findLexiconById(e.getKey()))
                .collect(Collectors.toSet());
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
