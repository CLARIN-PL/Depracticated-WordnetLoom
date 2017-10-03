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

package pl.edu.pwr.wordnetloom;

import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.sic.main.SingleInstanceController;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import pl.edu.pwr.wordnetloom.systems.enums.RelationTypes;
import pl.edu.pwr.wordnetloom.systems.managers.ConfigurationManager;
import pl.edu.pwr.wordnetloom.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.systems.managers.PosManager;
import pl.edu.pwr.wordnetloom.utils.Messages;
import pl.edu.pwr.wordnetloom.workbench.implementation.PanelWorkbench;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;

/**
 * klasa uruchamiajaca program
 * 
 * @author ehlyast
 * @author Max - modyfikacja i rozbudowa
 */
public class Main {

	private static final Logger logger = Logger.getLogger(Main.class);
	private static String ERROR_MESSAGE = "W systemie może być uruchomiona tylko jedna aplikacja plWordNet.";
	private static final boolean ONE_INSTANCE = true;

	private static final String PROGRAM_VERSION = "plWordNet 2.0.9";
	private static final String PROGRAM_NAME = "WordnetLoom";

	private static ResourceBundle resource;
	private static SingleInstanceController sic = null;

	/**
	 * uruchomienie programu
	 * 
	 * @param args
	 *            - parametry startowe
	 */
	public static void main(String[] args) {

		Thread instancer = new Thread(new Runnable() {
			@Override
			public void run() {
				if (Main.ONE_INSTANCE) {
					sic = new SingleInstanceController(
							new File(System.getProperty("java.io.tmpdir") + Main.PROGRAM_NAME + ".file"),
							Main.class.getName());
					System.out.println("Is other instance running... " + sic.isOtherInstanceRunning());
					if (sic.isOtherInstanceRunning()) {
						JOptionPane.showMessageDialog(null, Main.ERROR_MESSAGE, Main.PROGRAM_NAME,
								JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					} else {
						System.out.println("Registered... " + sic.registerApplication());
					}
				}
			}
		}, "Instancer Thread");

		instancer.start();
		initializLanguage();

		Thread managers = new Thread(new Runnable() {

			@Override
			public void run() {
				// Must be first other Managers depend on it
				LexiconManager.getInstance();
				PosManager.getInstance();
				DomainManager.getInstance();
				RelationTypes.loadRels();
			}
		}, "Mangers Thread");

		managers.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			private boolean first = true;

			@Override
			public void uncaughtException(Thread t, final Throwable tt) {
				if (first) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							JOptionPane.showMessageDialog(null, Messages.ERROR_UNABLE_TO_CONNECT_TO_SERVER,
									Main.PROGRAM_NAME, JOptionPane.ERROR_MESSAGE);
						}
					});
					first = false;
				}
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
				}
				System.exit(1);
			}
		});

		managers.start();

		try {
			instancer.join();
			managers.join();
		} catch (Exception e) {
			Logger.getLogger(Main.class).log(Level.ERROR, "Error while joini instancer and managers" + e);
			System.exit(1);
		}
		IconFontSwing.register(FontAwesome.getIconFont());
		Workbench workbench = null;
		try {
			workbench = new PanelWorkbench(PROGRAM_NAME + " " + PROGRAM_VERSION.split(" ")[1]);
			workbench.setVisible(true);
		} catch (Exception ex) {
			Logger.getLogger(Main.class).log(Level.ERROR, "Workbench initialization error:", ex);
		}

		Properties props = new Properties();
		try {
			props.load(Main.class.getResourceAsStream("/log4j.properties"));
		} catch (IOException ex) {
			logger.error("No log4 properties file", ex);
		}
		PropertyConfigurator.configure(props);
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				logger.error("Uncaught exception", e);
				e.printStackTrace();
			}
		});
	}

	static private void initializLanguage() {
		Locale locale;
		ConfigurationManager config = new ConfigurationManager(PanelWorkbench.WORKBENCH_CONFIG_FILE);
		config.loadConfig();
		String interfaceLanguage = config.get("InterfaceLanguage");
		if (interfaceLanguage == null) {
			locale = new Locale("en");
		} else {
			locale = new Locale(interfaceLanguage);
		}
		if ("pl".equals(interfaceLanguage) || "en".equals(interfaceLanguage)) {
			resource = ResourceBundle.getBundle("lang", locale);
		} else {
			try {
				File folder = new File(System.getProperty("user.dir") + "/config/lang");
				URL[] urls = { folder.toURI().toURL() };
				ClassLoader loader = new URLClassLoader(urls);
				resource = ResourceBundle.getBundle("lang", locale, loader);
			} catch (Exception e) {
				Logger.getLogger(Main.class).log(Level.ERROR, "Bundle initialization error " + e);
				resource = ResourceBundle.getBundle("lang", new Locale("en"));
			}
		}
	}

	public static String getResouce(String key) {
		return resource.getString(key);
	}

}
