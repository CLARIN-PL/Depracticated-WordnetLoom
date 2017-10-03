package pl.edu.pwr.wordnetloom.plugins.language;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.Main;
import pl.edu.pwr.wordnetloom.utils.Messages;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class LanguageService  extends AbstractService {

	private static final String EMBEDDED_EN_PROPERTIES = "lang_en.properties";
	private static final String EMBEDDED_PL_PROPERTIES = "lang_pl.properties";
	private static final String PATH_TO_CONFIG_LANG = System.getProperty("user.dir")+"/config/lang";

	private List<JMenuItem> menuItems = Lists.newArrayList();
	private Map<String,String> languages = Maps.newHashMap();
	private JMenuItem selectedItem;
	public LanguageService(Workbench workbench) {
		super(workbench);
	}

	@Override
	public void installViews() {}

	@Override
	public void installMenuItems() {
		initLanguages();
		buildLanguagesMenu();
		for (Iterator<JMenuItem> it = menuItems.iterator(); it.hasNext();) {
			JMenuItem menuItem = (JMenuItem) it.next();
			workbench.installMenu(Labels.HELP, Labels.LANGUAGE, menuItem);
		}
	}

	@Override
	public boolean onClose() {
		return true;
	}

	@Override
	public void onStart() {
	}

	private void buildLanguagesMenu(){
		Iterator<Entry<String, String>> it = languages.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
			final JMenuItem menuItem;
			if(workbench.getParam("InterfaceLanguage").equals(pairs.getKey())){
				menuItem = new JCheckBoxMenuItem(pairs.getValue());
				menuItem.setLocale(new Locale(pairs.getKey()));
				menuItem.setSelected(true);
				selectedItem = menuItem;
			}else{
				menuItem = new JCheckBoxMenuItem(pairs.getValue());
				menuItem.setLocale(new Locale(pairs.getKey()));
			}
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					workbench.setParam("InterfaceLanguage", menuItem.getLocale().getLanguage());
					menuItem.setSelected(true);
					selectedItem.setSelected(false);
					selectedItem = menuItem;
					if(JOptionPane.showOptionDialog(menuItem, 
							Messages.INFO_RESTART_APPLICATION, Labels.APPLICATION_RESTART, 
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,null,null,null) == JOptionPane.OK_OPTION){
						restartApplication();
					}
				}
			});
			menuItems.add(menuItem);
		}
	}

	private void initLanguages(){
		try {
			Properties properties = new Properties();
			properties.load(LanguageService.class.getClassLoader().getResourceAsStream(EMBEDDED_PL_PROPERTIES));
			languages.put(properties.getProperty("locale"), properties.getProperty("language"));
			
			properties = new Properties();
			properties.load(LanguageService.class.getClassLoader().getResourceAsStream(EMBEDDED_EN_PROPERTIES));
			languages.put(properties.getProperty("locale"), properties.getProperty("language"));

			File path = new File(PATH_TO_CONFIG_LANG);
			List<File> outsideFiles = (List<File>) FileUtils.listFiles(path, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
			if(!outsideFiles.isEmpty()){
				for (Iterator<File> it = outsideFiles.iterator(); it.hasNext();) {
					File file = (File) it.next();
					properties = new Properties();
					InputStream inputStream = new FileInputStream(file);
					properties.load(inputStream);
					if(file.getName().contains("lang_" + properties.getProperty("locale") + ".properties")){
						languages.put(properties.getProperty("locale"), properties.getProperty("language"));
					}
					inputStream.close();
				}
			}
		} catch (IOException e) {
			Logger.getLogger(getClass()).log(Level.ERROR, "Language Service error " + e);
		}
	}

	private void restartApplication(){
		final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		try {
			File currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			if(!currentJar.getName().endsWith(".jar")) System.exit(0);
			
			final ArrayList<String> command = new ArrayList<String>();
			command.add(javaBin);
			command.add("-jar");
			command.add(currentJar.getPath());
			
			final ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
		} catch (IOException  e) {
			Logger.getLogger(getClass()).log(Level.ERROR, "Restart error " + e);
		}
		System.exit(0);
	}
}
