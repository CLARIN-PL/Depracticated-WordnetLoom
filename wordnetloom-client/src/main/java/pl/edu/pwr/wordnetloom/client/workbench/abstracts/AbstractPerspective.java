package pl.edu.pwr.wordnetloom.client.workbench.abstracts;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JTabbedPane;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.edu.pwr.wordnetloom.client.systems.ui.SplitPaneExt;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ShortCut;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Perspective;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.View;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * Abstrakcyjna klasa perspektywy, ułatwiająca implementację gdyż zawiera w
 * sobie lwią część implementacji
 *
 * @author Max
 */
abstract public class AbstractPerspective implements Perspective, MouseListener {

    private static final String PANEL_NAME = "%s (Ctrl %s)";
    private String perspectiveName = null;      // nazwa perspektywy
    private int indexOfNextView = 0;            // indeks dla kolenego instalowanego indeksu

    private final List<SplitPaneExt> splitters = new ArrayList<>();
    private final List<JTabbedPane> panes = new ArrayList<>();

    // konfiguracja perspektywy
    protected Workbench workbench = null;

    // skróty klawiszowe
    protected Collection<ShortCut> shortCuts = new ArrayList<>();

    // modyfikacja dla skrótów oraz skrót bazowy
    static final protected int MODIFIERS = KeyEvent.CTRL_DOWN_MASK;
    static final protected int KEY_CODE = KeyEvent.VK_1;

    protected JTabbedPane createPane() {
        JTabbedPane pane = new JTabbedPane();
        pane.addMouseListener(this);
        synchronized (panes) {
            panes.add(pane);
        }
        return pane;
    }

    final protected void addSplitter(SplitPaneExt splitter) {
        synchronized (splitters) {
            splitters.add(splitter);
        }
    }

    final protected SplitPaneExt getFirstSplitter() {
        SplitPaneExt result = null;
        synchronized (splitters) {
            if (splitters.size() > 0) {
                result = splitters.get(0);
            }
        }
        return result;
    }

    @Override
    public void installView(View view, int pos) {
        try {
            installPane(view, panes.get(pos));
        } catch (Exception e) {
            Logger.getLogger(AbstractPerspective.class).log(Level.ERROR, "While installing pane " + e);
        }
    }

    /**
     * Konstruktor perspektywy
     *
     * @param name - nazwa perspektywy
     * @param workbench - workbench dla perspektywy
     */
    public AbstractPerspective(String name, Workbench workbench) {
        super();
        this.perspectiveName = name;
        this.workbench = workbench;
    }

    @Override
    public String getName() {
        return this.perspectiveName;
    }

    @Override
    public void init() {
        this.getContent(); // zbudowanie iu perspektywy
    }

    @Override
    final public Collection<ShortCut> getShortCuts() {
        return shortCuts;
    }

    /**
     * dwuklik myszką w zakładke powoduje zwiniecie
     *
     * @param event
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() != 2) {
            return; // czy to byl dwuklik
        }
        // przejście po wszystkich splitterach
        for (SplitPaneExt splitter : splitters) {
            // czy funkcja jest wspierana
            if (!splitter.isOneTouchExpandable()) {
                continue;
            }
            int count = splitter.getComponentCount();
            // przejście po wszystkich komponentach należących do splitera
            for (int i = 0; i < count; i++) {
                // jeśli sygnał pochodzi od komponentu
                // to zwinięcie widoku
                if (splitter.getComponent(i) == event.getSource()) {
                    splitter.collapse(i);
                    return;
                }
            }
        }
    }

    /**
     * Instalacja panelu oraz podłączenie skrótów klawiszowych z nim związanych
     *
     * @param view - widok
     * @param pane - panel do dołączenia
     */
    protected void installPane(View view, JTabbedPane pane) {
        if (pane != null) {
            pane.addTab(String.format(PANEL_NAME, view.getTitle(), new Integer(indexOfNextView + 1)), view.getPanel());
            shortCuts.add(new ShortCut(pane, view.getRootComponent(), MODIFIERS, KEY_CODE + indexOfNextView));
            indexOfNextView++;
        }
        shortCuts.addAll(view.getShortCuts());
    }

    @Override
    public void resetViews() {
        synchronized (splitters) {
            for (int i = 0; i < 2; i++) {
                splitters.forEach((splitter) -> {
                    splitter.resetDividerLocation();
                });
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

}
