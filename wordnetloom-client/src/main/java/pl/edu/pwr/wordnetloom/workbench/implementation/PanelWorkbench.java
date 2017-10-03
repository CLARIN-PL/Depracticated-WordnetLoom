/*
    Copyright (C) 2011 Łukasz Jastrzębski, Paweł Koczan, Michał Marcińczuk,
                       Bartosz Broda, Maciej Piasecki, Adam Musiał,
                       Radosław Ramocki, Michał Stanek
    Part of the WordnetLoom

    This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 3 of the License, or (at your option)
any later version.

    This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE. 

    See the LICENSE and COPYING files for more details.
 */

package pl.edu.pwr.wordnetloom.workbench.implementation;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXBusyLabel;

import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import pl.edu.pwr.wordnetloom.systems.managers.ConfigurationManager;
import pl.edu.pwr.wordnetloom.utils.GUIUtils;
import pl.edu.pwr.wordnetloom.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.systems.tooltips.ToolTipGenerator;
import pl.edu.pwr.wordnetloom.systems.ui.IconFrame;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Perspective;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Plugin;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Service;
import pl.edu.pwr.wordnetloom.workbench.interfaces.View;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;
import se.datadosen.component.RiverLayout;

/**
 * panele dla środwiska
 * 
 * @author <a href="mailto:lukasz.jastrzebski@pwr.wroc.pl">Lukasz
 *         Jastrzebski</a>
 * @author Max - modyfikacja i rozbudowa
 */
public class PanelWorkbench implements WindowListener, Workbench {

	public static final String WORKBENCH_CONFIG_FILE = "./config/workbench.cfg";
	private static final String PLUGIN_CONFIG_FILE = "./config/plugins.cfg";

	private static final String BUTTON_TOOLTIP_FORMAT = "%s (Ctrl+Shift+%s)";
	private static final String PROGRAM_TITLE = "%s";
	private static final String SHOW_TOOLTIPS_PARAM_NAME = "ShowTooltips";
	private static final String TOOLBAR_TITLE = "Workbench toolbar";
	private static final String PERSPECTIVE_MENU = "Perspektywa";
	private static final String ACTIVE_PERSPECTIVE_NAME = "ActivePerspective";
	private static final int STANDARD_WIDTH = 1000;
	private static final int STANDARD_HEIGHT = 700;

	protected Map<String, Service> services;
	protected Map<String, Perspective> perspectives;

	protected ArrayList<ShortCut> globalShortCuts;

	protected JFrame frame = null;
	private JToolBar toolBar;
	private JPanel mainPane;
	protected JLabel statusBar;
	private JLabel owner;
	protected JPanel statusPanel;
	private MenuHolder menuHolder;
	private static BusyGlassPane busyPanel;

	private static AtomicBoolean busy_lock = new AtomicBoolean(false);

	private final String version;
	public static ConfigurationManager config;
	/**
	 * konstruktor
	 * 
	 * @param title
	 *            - tytuł dla okna
	 */
	public PanelWorkbench(String title) {
		this.version = title;

		// załadowanie konfiguracji
		config = new ConfigurationManager(WORKBENCH_CONFIG_FILE);
		config.loadConfig();

		// wlaczenie albo wylaczenie tooltipow
		ToolTipGenerator.getGenerator().setEnabledTooltips(
				"1".equals(config.get(SHOW_TOOLTIPS_PARAM_NAME)));

		// utworzenie interfejsu
		createFrame();

		// przygotowanie kontenerów dla usług i perspektyw
		services = new HashMap<String, Service>();
		perspectives = new HashMap<String, Perspective>();

		// instalacja pluginow
		Collection<Plugin> plugins = loadPlugins(PLUGIN_CONFIG_FILE);
		for (Plugin plugin : plugins) {
			final Workbench workbench = this;

			class Tmp implements Runnable {
				public Plugin plugin;

				public void run() {
					plugin.install(workbench);
				}

			}

			Tmp run = new Tmp();
			run.plugin = plugin;

			try {
				SwingUtilities.invokeAndWait(run);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
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

		// wybranie ostatnio używanej perspektywy
		choosePerspective(getParam(ACTIVE_PERSPECTIVE_NAME));
	}

	/**
	 * utworzenie intefejsu
	 */
	private void createFrame() {
		final PanelWorkbench panel = this;

		Runnable run = new Runnable() {
			public void run() {
				globalShortCuts = new ArrayList<ShortCut>();

				// inicjacja menu
				menuHolder = new MenuHolder();

				frame = new IconFrame(STANDARD_WIDTH, STANDARD_HEIGHT);

				// ustawienie tytulu okienka
				updateTitle();

				frame.addWindowListener(panel);
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				frame.setJMenuBar(menuHolder.getMenuBar());

				// ustawienie toolbara
				toolBar = new JToolBar(TOOLBAR_TITLE);
				toolBar.setFloatable(false);
				toolBar.setRollover(false);
				toolBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				toolBar.setLayout(new RiverLayout(2, 2));
				toolBar.setSize(new Dimension(0, 28));
				toolBar.setPreferredSize(new Dimension(0, 28));
				toolBar.setVisible(false);

				frame.setGlassPane(busyPanel = new BusyGlassPane());
				busyPanel.addMouseListener(new MouseAdapter() {});
				busyPanel.addMouseMotionListener(new MouseMotionAdapter() {});
				busyPanel.addKeyListener(new KeyAdapter() {});

				// ustawienie statusu
				statusBar = new JLabel("");
			
				owner = new JLabel(getOwnerFromConfigManager());
				owner.setIcon(IconFontSwing.buildIcon(FontAwesome.USER, 16));
				statusPanel = new JPanel();
				statusPanel.setBorder(BorderFactory.createEtchedBorder());
				statusPanel.setLayout(new RiverLayout());
				statusPanel.add(statusBar, "hfill left");
				statusPanel.add(owner, "right");

				// ustawienie glownego panelu
				mainPane = new JPanel();
				mainPane.setLayout(new BorderLayout());
				mainPane.add(toolBar, BorderLayout.NORTH);
				mainPane.add(new JPanel(), BorderLayout.CENTER);
				mainPane.add(statusPanel, BorderLayout.SOUTH);

				frame.setContentPane(mainPane);
			}
		};

		try {
			SwingUtilities.invokeAndWait(run);
		} catch (InterruptedException e) {
			Logger.getLogger(PanelWorkbench.class).log(Level.ERROR,
					"Error while creating frame" + e);
		} catch (InvocationTargetException e) {
			Logger.getLogger(PanelWorkbench.class).log(Level.ERROR,
					"Error while creating frame" + e);
		}
	}

	public void addGlobalShortCut(ShortCut shortCut) {
		this.globalShortCuts.add(shortCut);
	}


	public void setVisible(final boolean isVisible) {
		Runnable run = new Runnable() {

			public void run() {
				if (isVisible == false) {
					windowClosing(null); // wywoalnie zamykania
				} else {
					frame.setVisible(isVisible);

					// powiadaomienie uslug o wyswietleniu
					Collection<Service> servicesCollection = services.values();
					for (Service service : servicesCollection) {
						service.onStart();
					}

					// powiadomienie aktualnej perspektywy
					Perspective perspective = getActivePerspective();
					if (perspective != null) {
						perspective.refreshViews();
					}
				}
				updateTitle();
			}
		};

		try {
			GUIUtils.delegateToEDT(run);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * Workbench#installPerspective
	 * (Perspective)
	 */
	public void installPerspective(Perspective perspective) {
		class Tmp implements Runnable {
			public Perspective perspective;
			public Workbench workbench;

			public void run() {
				// dodanie perspektywy do listy perspektyw
				perspectives.put(perspective.getName(), perspective);

				// utworzenie zawartosci perspektywy
				perspective.init();

				// odczytanie menu
				JMenu menuPerspective = getMenu(PERSPECTIVE_MENU);
				if (menuPerspective == null)
					return; // brak menu, core nie odpalony

				// nowa pozycja w menu
				JMenuItem perspectiveItem = new PerspectiveRadioMenuItem(
						perspective, workbench);
				perspectiveItem
						.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1
								+ menuPerspective.getItemCount(),
								InputEvent.CTRL_DOWN_MASK
										| InputEvent.SHIFT_DOWN_MASK));

				// dodanie nowej pozycji do menu
				menuPerspective.add(perspectiveItem); // dodanie perspektywy do menu
				menuPerspective.setVisible(perspectives.size() > 1);

				// dodanie do toolbara
				JToggleButton perspectiveButton = new JToggleButton(
						perspective.getName());
				perspectiveButton.setPreferredSize(new Dimension(140, 20));
				perspectiveButton.setSelected(getActivePerspective() == perspective);
				perspectiveButton.setToolTipText(String.format(
						BUTTON_TOOLTIP_FORMAT, perspective.getName(),
						new Integer(perspectives.size())));
				perspectiveButton.addActionListener(new ActionListener() {

					/**
					 * Kliknieto w przycisk na toolbarze
					 */
					public void actionPerformed(ActionEvent arg0) {
						JToggleButton pressButton = (JToggleButton) arg0
								.getSource();
						pressButton.setSelected(true);
						if (!getActivePerspective().getName().equals(
								pressButton.getText()))
							choosePerspective(pressButton.getText());
					}
				});

				// dodanie przycisku do istniejącego toolbara
				toolBar.add(perspectiveButton);
				final int count = perspectives.size();
				toolBar.setVisible(count > 1 && count < 5);

			}
		}
		;

		Tmp run = new Tmp();
		run.perspective = perspective;
		run.workbench = this;

		try {
			GUIUtils.delegateToEDT(run);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * Workbench#choosePerspective
	 * (java.lang.String)
	 */
	public void choosePerspective(final String perspectiveName) {
		Runnable run = new Runnable() {
			public void run() {
				// odczytanie perspektywy o podanej nazwie
				Perspective perspective = perspectives.get(perspectiveName);
				if (perspective != null) { // czy perspektywa istnieje
					// odczytanie ostatniego stanu
					Perspective oldPerspective = perspectives
							.get(getParam(ACTIVE_PERSPECTIVE_NAME));
					Object state = oldPerspective != null ? oldPerspective
							.getState() : null;

					ArrayList<ShortCut> shortCuts = new ArrayList<ShortCut>();
					shortCuts.addAll(perspective.getShortCuts());
					shortCuts.addAll(globalShortCuts);
					menuHolder.setShortCuts(shortCuts); // ładowanie skrótów

					// ustawienie aktywnosci przycisków w toolbarze
					for (Component com : toolBar.getComponents()) {
						if (com instanceof JToggleButton) {
							JToggleButton button = (JToggleButton) com;
							button.setSelected(button.getText().equals(
									perspectiveName));
						}
					}

					// ustawienie parametru aktualna perspektywa
					setParam(ACTIVE_PERSPECTIVE_NAME, perspectiveName);

					// wymiana zawartosci ramki
					mainPane.remove(2); // bez statusu
					mainPane.remove(1); // bez glownego pola
					mainPane.add(perspective.getContent(), BorderLayout.CENTER);
					mainPane.add(statusPanel, BorderLayout.SOUTH);
					mainPane.invalidate();
					frame.setContentPane(mainPane);

					// czy mozna uruchomić komunikat o wyświetleniu
					if (frame.isVisible()) {
						// czy ustawienie state coś dało
						if (!perspective.setState(state)) {
							perspective.refreshViews();
						}
					}
				}
			}
		};

		try {
			GUIUtils.delegateToEDT(run);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * zaykanie okna
	 */
	public void windowClosing(WindowEvent arg0) {
		// sprawdzenie czy mozna zamknac
		Collection<Service> servicesCollection = services.values();
		for (Service service : servicesCollection) {
			if (!service.onClose()) {
				return; // nie mozna zamknac
			}
		}

		// zapisanie konfiguracji
		config.saveConfig();

		// zerowanie zmiennych
		toolBar = null;
		mainPane = null;
		services = null;
		perspectives = null;

		// sprzątanie
		try {
			frame.setVisible(false);
			frame.dispose();
		} catch (Exception ex) {
			DialogBox.showError(ex.toString());
			Logger.getLogger(PanelWorkbench.class).log(Level.ERROR,
					"While closing frame" + ex);
		} finally {
			System.gc();
			System.exit(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * Workbench#setToolbarVisible
	 * (boolean)
	 */
	public void setToolbarVisible(boolean isVisible) {
		toolBar.setVisible(isVisible);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * Workbench#isToolbarVisible
	 * ()
	 */
	public boolean isToolbarVisible() {
		return toolBar.isVisible();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * Workbench#setStatusText
	 * (java.lang.String)
	 */
	public void setStatusText(String text) {
		statusBar.setText(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Workbench#getFrame()
	 */
	public JFrame getFrame() {
		return frame;
	}

	// nie uzywane zdarzenia
	public void windowOpened(WindowEvent arg0) {
		/***/
	}

	public void windowIconified(WindowEvent arg0) {
		/***/
	}

	public void windowDeiconified(WindowEvent arg0) {
		/***/
	}

	public void windowActivated(WindowEvent arg0) {
		/***/
	}

	public void windowDeactivated(WindowEvent arg0) {
		/***/
	}

	public void windowClosed(WindowEvent arg0) {
		/***/
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * Workbench#getMenu(java.
	 * lang.String)
	 */
	public JMenu getMenu(String itemName) {
		return menuHolder.getMenu(itemName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * Workbench#installMenu(javax
	 * .swing.JMenu)
	 */
	public void installMenu(JMenu item) {
		menuHolder.install(item);
	}

	@Override
	public void installMenu(JMenu item, int index) {
		menuHolder.install(item, index);
	}

	@Override
	public void installMenu(String topMenu, String subMenu, JMenuItem item) {
		JMenu top = menuHolder.getMenu(topMenu);
		if (top == null) {
			top = new JMenu(topMenu);
			menuHolder.install(top, 0);
		}

		JMenuItem sub = null;
		for (int i = 0; i < top.getItemCount(); i++) {
			JMenuItem itemTemp = top.getItem(i);
			if (itemTemp != null && itemTemp.getText().equals(subMenu)) {
				sub = itemTemp;
				break;
			}
		}
		if (sub == null) {
			sub = new JMenu(subMenu);
			top.add(sub);
		}

		sub.add(item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * Workbench#getParam(java
	 * .lang.String)
	 */
	public String getParam(String paramName) {
		return config.get(paramName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * Workbench#setParam(java
	 * .lang.String, java.lang.String)
	 */
	public void setParam(String paramName, String value) {
		config.set(paramName, value);
		config.saveConfig();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * Workbench#removeParam(java
	 * .lang.String)
	 */
	public void removeParam(String paramName) {
		config.remove(paramName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Workbench#getVersion()
	 */
	public String getVersion() {
		return this.version;
	}


	public void installService(Service service) {
		services.put(service.getId(), service);
		service.installMenuItems();
		service.installViews();
	}


	public Service getService(String name) {
		return services.get(name);
	}


	public void installView(View view, int pos, String perspectiveName) {
		Perspective perspective = perspectives.get(perspectiveName);
		if (perspective != null)
			perspective.installView(view, pos);
	}


	public Perspective getActivePerspective() {
		return perspectives.get(getParam(ACTIVE_PERSPECTIVE_NAME));
	}

	/**
	 * odczytanie pluginów z pliku konfiguracyjnego
	 * 
	 * @param fileName
	 *            - nazwa pliku konfiguracyjnego
	 * @return kolekcja pluginow
	 */
	static private Collection<Plugin> loadPlugins(String fileName) {
		Collection<Plugin> list = new ArrayList<Plugin>();

		// odczytanie pliku z listą pluginów
		try {
			FileInputStream fi = new FileInputStream(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(fi));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("#"))
					continue;

				// mamy nazwe klasy, trzeba ja utworzyć
				try {
					Class<?> pluginClass = Class.forName(line);
					if (pluginClass != null) { // czy klasa jest w dystrybucji
						list.add((Plugin) pluginClass.newInstance());
					}
				} catch (Exception e) {
					Logger.getLogger(PanelWorkbench.class).log(Level.ERROR,
							"While loading class plugins:" + e);
				}
			}
			fi.close();
		} catch (IOException e) {
			Logger.getLogger(PanelWorkbench.class).log(Level.ERROR,
					"While openning file : " + e);
		}
		return list;
	}

	/**
	 * Uaktualnia nazwę okna aplikacji.
	 */
	public void updateTitle() {
		frame.setTitle(String.format(PROGRAM_TITLE, version));
	}
	@Override
	public void updateOwner(){
		owner.setText(getOwnerFromConfigManager());
		owner.repaint();
	}
	public void setBusy(final boolean busy) {
		if (busy_lock.compareAndSet(false, true)) {
			try {
				SwingUtilities.invokeLater(() -> busyPanel.setVisible(busy));
			} catch (Exception e) {

			} finally {
				busy_lock.set(false);
			}
		}

	}

	public static String getOwnerFromConfigManager() {
		if (config == null)
			return null;
		return config.get("Owner");
	}

	class BusyGlassPane extends JComponent {

		private static final long serialVersionUID = -6906257101036014488L;
		private JXBusyLabel busy_label;

		public BusyGlassPane() {
			setBackground(new Color(0f,0f,0f,0.1f ));
			setLayout(new GridBagLayout());
			busy_label = new JXBusyLabel(new Dimension(70, 70));
			busy_label.getBusyPainter().setPoints(20);
			busy_label.getBusyPainter().setTrailLength(9);
			busy_label.getBusyPainter().setHighlightColor(
					new Color(44, 61, 146).darker());
			busy_label.getBusyPainter().setBaseColor(
					new Color(168, 204, 241).brighter());
			add(busy_label, new GridBagConstraints());
			busy_label.setBusy(true);
		}

		@Override
		protected void paintComponent(Graphics g) {
			// enables anti-aliasing
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			// gets the current clipping area
			Rectangle clip = g.getClipBounds();

			// sets a 65% translucent composite
			AlphaComposite alpha = AlphaComposite.SrcOver.derive(0.65f);
			Composite composite = g2.getComposite();
			g2.setComposite(alpha);

			// fills the background
			g2.setColor(getBackground());
			g2.fillRect(clip.x, clip.y, clip.width, clip.height);

			g2.setComposite(composite);
		}
	}
}
