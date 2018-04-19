package pl.edu.pwr.wordnetloom.client.workbench.implementation;

import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.BusyGlassPane;
import pl.edu.pwr.wordnetloom.client.systems.ui.MFrame;
import pl.edu.pwr.wordnetloom.client.utils.GUIUtils;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public final class PanelWorkbench implements WindowListener, Workbench, Loggable {

    private static final String PLUGIN_CONFIG_FILE = "plugins.cfg";
    private static final int STANDARD_WIDTH = 1000;
    private static final int STANDARD_HEIGHT = 700;

    protected Map<String, Service> services;
    protected Map<String, Perspective> perspectives;

    protected ArrayList<ShortCut> globalShortCuts;

    protected WebFrame frame = null;
    private WebPanel mainPane;

    protected WebPanel statusPanel;
    private MenuHolder menuHolder;
    private static BusyGlassPane busyPanel;

    private static final AtomicBoolean BUSY_LOCK = new AtomicBoolean(false);

    private final String version;

    public PanelWorkbench(String title) {
        version = title;

        createFrame();

        // przygotowanie kontenerów dla usług i perspektyw
        services = new HashMap<>();
        perspectives = new HashMap<>();

        // instalacja pluginow
        Collection<Plugin> plugins = loadPlugins(PLUGIN_CONFIG_FILE);
        for (Plugin plugin : plugins) {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    plugin.install(this);
                });
            } catch (InterruptedException | InvocationTargetException ex) {
                logger().error("Plugin installation Error", ex);
            }

        }

        // instalaja pierwszej napotkanej perspektywy
        if (getActivePerspective() == null) {
            Collection<Perspective> collection = perspectives.values();
            for (Perspective perspective : collection) {
                choosePerspective(perspective.getName());
                break;
            }
        }

        choosePerspective(Labels.WORDNET_VISUALIZATION);
    }

    private void createFrame() {

        try {
            SwingUtilities.invokeAndWait(() -> {
                globalShortCuts = new ArrayList<>();

                // inicjacja menu
                menuHolder = new MenuHolder();

                frame = new MFrame(STANDARD_WIDTH, STANDARD_HEIGHT);
                frame.setTitle(Application.PROGRAM_NAME_VERSION);

                frame.addWindowListener(this);
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.setJMenuBar(menuHolder.getMenuBar());
                frame.setGlassPane(busyPanel = new BusyGlassPane());

                // ustawienie glownego panelu
                mainPane = new WebPanel();
                mainPane.setMargin(3);
                mainPane.setLayout(new BorderLayout());
                mainPane.add(new WebPanel(), BorderLayout.CENTER);
                frame.setContentPane(mainPane);
            });

        } catch (InterruptedException | InvocationTargetException ex) {
            logger().error("Error while creating frame", ex);
        }
    }

    public void addGlobalShortCut(ShortCut shortCut) {
        globalShortCuts.add(shortCut);
    }

    @Override
    public void setVisible(boolean isVisible) {
        try {
            GUIUtils.delegateToEDT(() -> {
                if (isVisible == false) {
                    windowClosing(null); // wywoalnie zamykania
                } else {
                    frame.setVisible(isVisible);

                    // powiadaomienie uslug o wyswietleniu
                    Collection<Service> servicesCollection = services.values();
                    servicesCollection.forEach(Service::onStart);

                    // powiadomienie aktualnej perspektywy
                    Perspective perspective = getActivePerspective();
                    if (perspective != null) {
                        perspective.refreshViews();
                    }
                }
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            logger().error("Error while displaying frame", ex);
        }

    }

    @Override
    public void installPerspective(final Perspective perspective) {
        try {
            GUIUtils.delegateToEDT(() -> {
                perspectives.put(perspective.getName(), perspective);
                perspective.init();
            });

        } catch (InterruptedException | InvocationTargetException ex) {
            logger().error("Error while installing perspective", ex);
        }

    }

    @Override
    public void choosePerspective(String perspectiveName) {

        Runnable run = () -> {
            // odczytanie perspektywy o podanej nazwie
            Perspective perspective = perspectives.get(perspectiveName);
            if (perspective != null) {

                ArrayList<ShortCut> shortCuts = new ArrayList<>();
                shortCuts.addAll(perspective.getShortCuts());
                shortCuts.addAll(globalShortCuts);
                menuHolder.setShortCuts(shortCuts);

                mainPane.add(perspective.getContent(), BorderLayout.CENTER);
                mainPane.invalidate();
                frame.setContentPane(mainPane);

                if (frame.isVisible()) {
                    perspective.refreshViews();
                }
            }
        };

        try {
            GUIUtils.delegateToEDT(run);
        } catch (InterruptedException | InvocationTargetException ex) {
            logger().error("Error while choosing perspective", ex);
        }

    }

    @Override
    public void windowClosing(WindowEvent event) {
        // sprawdzenie czy mozna zamknac
        Collection<Service> servicesCollection = services.values();
        for (Service service : servicesCollection) {
            if (!service.onClose()) {
                return;
            }
        }

        mainPane = null;
        services = null;
        perspectives = null;

        try {
            frame.setVisible(false);
            frame.dispose();
        } catch (Exception ex) {
            DialogBox.showError(ex.toString());
            logger().error("Error while closing frame", ex);

        } finally {
            System.gc();
            System.exit(0);
        }
    }

    @Override
    public WebFrame getFrame() {
        return frame;
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
    }

    @Override
    public WebMenu getMenu(String itemName) {
        return menuHolder.getMenu(itemName);
    }

    @Override
    public void installMenu(WebMenu item, String alignment) {
        menuHolder.install(item, alignment);
    }

    @Override
    public void installMenuComponent(Component c, String alignment) {
        menuHolder.install(c , alignment);
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void installService(Service service) {
        services.put(service.getId(), service);
        service.installMenuItems();
        service.installViews();
    }

    @Override
    public Service getService(String name) {
        return services.get(name);
    }

    @Override
    public void installView(View view, int pos, String perspectiveName) {
        Perspective perspective = perspectives.get(perspectiveName);
        if (perspective != null) {
            perspective.installView(view, pos);
        }
    }

    @Override
    public Perspective getActivePerspective() {
        return perspectives.get(Labels.WORDNET_VISUALIZATION);
    }

    /**
     * odczytanie pluginów z pliku konfiguracyjnego
     *
     * @param fileName - nazwa pliku konfiguracyjnego
     * @return kolekcja pluginow
     */
    private Collection<Plugin> loadPlugins(String fileName) {
        Collection<Plugin> list = new ArrayList<>();

        // odczytanie pliku z listą pluginów
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(PanelWorkbench.class.getClassLoader().getResourceAsStream(fileName)));
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("#")) {
                    continue;
                }

                // mamy nazwe klasy, trzeba ja utworzyć
                try {
                    Class<?> pluginClass = Class.forName(line);
                    if (pluginClass != null) {
                        list.add((Plugin) pluginClass.newInstance());
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    logger().error("While loading class plugins:", e);
                }
            }
        } catch (IOException ex) {
            logger().error("While opening file:", ex);
        }
        return list;
    }

    @Override
    public void setBusy(boolean busy) {
        if (BUSY_LOCK.compareAndSet(false, true)) {
            try {
                SwingUtilities.invokeLater(() -> {
                    busyPanel.setVisible(busy);
                });
            } catch (Exception e) {
            } finally {
                BUSY_LOCK.set(false);
            }
        }

    }
}
