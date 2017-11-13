package pl.edu.pwr.wordnetloom.client.workbench.abstracts;

import com.alee.laf.panel.WebPanel;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenersContainer;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ShortCut;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;

/**
 * abstrakcyjny wygląd widoku
 *
 * @author Max
 */
public abstract class AbstractViewUI implements KeyListener {

    protected SimpleListenersContainer listeners = new SimpleListenersContainer();

    protected Workbench workbench;

    private WebPanel mainContentPanel;
    protected Collection<ShortCut> viewScopeShortCuts = new ArrayList<>();
    protected Collection<ShortCut> perspectiveScopeShortCuts = new ArrayList<>();

    /**
     * Inicjalizacja wygladu
     *
     * @param workbench - sodowisko pracy
     */
    public void init(Workbench workbench) {
        this.workbench = workbench;
        mainContentPanel = new WebPanel();       // przygotowanie głównego panelu
        initialize(mainContentPanel);            // inicjalizacja elementów należących do panelu
        setKeyListeners(mainContentPanel);       // ustawienie słuchaczy dla wszystkich elementów należących do panel
    }

    /**
     * Istawienie nasluchiwania wcisniecia klawisza dla wszystkich dzieci
     *
     * @param component
     */
    private void setKeyListeners(Component component) {
        if (component != null) {
            component.addKeyListener(this);
        }
        if (component instanceof Container) {
            Container container = (Container) component;
            Component[] children = container.getComponents();
            for (Component child : children) {
                setKeyListeners(child);
            }
        }
    }

    /**
     * Utworzenie zawartosci panelu. Za pomocą tej metody dostawne są przyciski
     * i inne elementy interfjesu użytkownika
     *
     * @param content
     */
    protected abstract void initialize(WebPanel content);

    /**
     * Dodanie słuchacza do listy sluchaczy
     *
     * @param newListener - nowy słuchacz
     */
    public void addActionListener(SimpleListenerInterface newListener) {
        listeners.add(newListener);
    }

    /**
     * Ustawienie koloru tła dla panelu oraz elementów do niego należących
     * takich jak inne panele i checkboxy
     *
     * @param color - nowy kolor
     */
    public void setBackgroundColor(Color color) {
        if (mainContentPanel != null) {
            mainContentPanel.setBackground(color);
            Component[] components = mainContentPanel.getComponents();
            if (components != null) {
                for (Component component : components) {
                    if (component instanceof JPanel || component instanceof JCheckBox
                            || component instanceof JTextArea) {
                        component.setBackground(color);
                    }
                }
            }
        }
    }

    /**
     * Instalacja skrotu klawiszowego dla przycisku, który działa w obrębie
     * danego okna
     *
     * @param button    - przycisk
     * @param modifiers - modyfikator
     * @param keyCode   - klawisz
     */
    public void installViewScopeShortCut(AbstractButton button, int modifiers, int keyCode) {
        viewScopeShortCuts.add(new ShortCut(button, modifiers, keyCode));
    }

    /**
     * Instalacja globalnego skrotu klawiszowego, który działa w obrębie całej
     * perspektywy - np. tak rozwiązywane sa przyciski na toolbarze
     *
     * @param button    - przycisk
     * @param modifiers - modyfikacja
     * @param keyCode   - kod
     */
    public void installPerspectiveScopeShortCut(AbstractButton button, int modifiers, int keyCode) {
        perspectiveScopeShortCuts.add(new ShortCut(button, modifiers, keyCode));
    }

    /**
     * Odczytanie glownego elementu okna, na którym ma zostać ustawiony focus w
     * sytuacji gdy następuje przejście pomiędzy widokami za pomocą Ctrl+1,
     * Ctrl+2
     *
     * @return glowny element
     */
    abstract public JComponent getRootComponent();

    /**
     * Wcisnieto jakiś przycisk na kontrolkach, w tym przypadku może to być
     * jakiś skrót klawiaturowy
     *
     * @param arg0
     */
    @Override
    public void keyPressed(KeyEvent arg0) {
        if (arg0.getSource() instanceof JTextField) // dla pol tekstowych nie dziala
        {
            return;
        }
        // przejscie po skrotach lokalnych (globalne zostaną obsłużone przez swinga, gdyż
        // jest to niewidzialne menu)
        for (ShortCut shortCut : viewScopeShortCuts) {
            // wyszykiwanie skrótu
            if (arg0.getModifiers() == shortCut.getModifiers() && arg0.getKeyCode() == shortCut.getKeyCode()) {
                arg0.consume();      // konsumcja zdarzenia
                shortCut.doAction(); // wywołanie akcji związanej ze skrótem
                break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    /**
     * Get main mainContentPanel of the view.
     *
     * @return main panel
     */
    public WebPanel getContent() {
        return mainContentPanel;
    }
}
