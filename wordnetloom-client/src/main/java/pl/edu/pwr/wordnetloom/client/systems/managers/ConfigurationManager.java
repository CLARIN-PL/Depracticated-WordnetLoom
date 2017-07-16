package pl.edu.pwr.wordnetloom.client.systems.managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa zajmująca się przechowywaniem konfiguracji. Prawdopodobnie istnieje już
 * podobna klasa w javie, ale gdy ja tworzyłem to o tym nie wiedziałem. Plik
 * konfiguracyjny ma postać: nazwa=wartosc
 *
 * @author Max
 *
 */
public class ConfigurationManager {

    private final Map<String, String> config = new TreeMap<>();
    private String configFileName = null;

    public ConfigurationManager(String configFileName) {
        this.configFileName = configFileName;
    }

    public void loadConfiguration() {
        this.config.clear();
        Map<String, String> cfg = loadConfigFromFile(this.configFileName);
        cfg.entrySet().stream().forEach((e) -> {
            this.config.put(e.getKey(), e.getValue());
        });
    }

    private Map<String, String> loadConfigFromFile(String configFileName) {
        Map<String, String> cfg = new TreeMap<>();
        String line;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(ConfigurationManager.class.getClassLoader().getResourceAsStream(configFileName)));
            do {
                line = in.readLine();
                if (line != null) {
                    int pos = line.indexOf('=');
                    if (pos > 0) {
                        cfg.put(line.substring(0, pos), line.substring(pos + 1));
                    }
                }
            } while (line != null);
        } catch (FileNotFoundException e) {
            Logger.getLogger(ConfigurationManager.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(ConfigurationManager.class.getName()).log(Level.SEVERE, null, e);
        }

        return cfg;
    }

    public void saveConfiguration() {
        // próba odczytania configu
        Map<String, String> old = loadConfigFromFile(configFileName);
        // jeżeli się nie zmienił to nie robimy zmian
        if (old != null && !old.isEmpty() && old.equals(this.config)) {
            return;
        }

        try (FileOutputStream fo = new FileOutputStream(configFileName)) {
            PrintStream out = new PrintStream(fo);
            Set<Map.Entry<String, String>> set = this.config.entrySet();
            set.stream().map((element) -> {
                out.print(element.getKey());
                return element;
            }).forEach((element) -> {
                out.print("=");
                out.println(element.getValue());
            });
        } catch (IOException ex) {
            Logger.getLogger(ConfigurationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String get(String paramName) {
        return config.get(paramName);
    }

    public void set(String paramName, String value) {
        config.put(paramName, value);
    }

    public void remove(String paramName) {
        config.remove(paramName);
    }
}
