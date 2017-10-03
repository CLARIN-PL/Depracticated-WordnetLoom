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

package pl.edu.pwr.wordnetloom.systems.managers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * Klasa zajmująca się przechowywaniem konfiguracji. Prawdopodobnie
 * istnieje już podobna klasa w javie, ale gdy ja tworzyłem to o tym
 * nie wiedziałem. Plik konfiguracyjny ma postać:
 * nazwa=wartosc
 * @author Max
 *
 */
// TODO: skasować w przyszłości, użyć gotowego i *sprawdzonego* rozwiązania w postaci java.util.Properties
public class ConfigurationManager {

	private Map<String,String> config = new TreeMap<String,String>();
	private String configFileName = null;

	/**
	 * Konstruktor w którym definiuje się związany plik z konfiguracją
	 * @param configFileName - nazwa pliku z konfiguracja
	 */
	public ConfigurationManager(String configFileName) {
		this.configFileName = configFileName;
	}

	/**
	 * Odczytanie konfiguracji z pliku
	 */
	public void loadConfig() {
		this.config.clear();
		Map<String,String> nowy = loadConfigFromFile(this.configFileName);

		for(Entry<String, String> e : nowy.entrySet()){
			this.config.put(e.getKey(), e.getValue());
		}
	}

	private static Map<String,String> loadConfigFromFile(String configFileName){
		Map<String,String> config = new TreeMap<String, String>();
		String line;
		
		try {
			FileInputStream fi = new FileInputStream(configFileName);
			BufferedReader in=new BufferedReader(new InputStreamReader(fi));
			do {
				line=in.readLine(); // wczytanie linii
				if (line!=null) {
					int pos=line.indexOf('='); // znalezienie podzialu =
					if (pos>0)
						config.put(line.substring(0,pos),line.substring(pos+1)); // dodanie do mapy
				}
			} while (line!=null);
			fi.close();
		} catch (FileNotFoundException e) { // brak plik, moze sie zdarzyc
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return config;
	}

	/**
	 * Zapisanie konfiguracji do pliku
	 */
	public void  saveConfig() {
		// próba odczytania configu
		Map<String,String> old = loadConfigFromFile(configFileName);
		// jeżeli się nie zmienił to nie robimy zmian
		if(old!=null && !old.isEmpty() && old.equals(this.config)){
			return;
		}

		try {
			FileOutputStream fo = new FileOutputStream(configFileName);
			PrintStream out = new PrintStream(fo);
			Set<Map.Entry<String,String>> set = this.config.entrySet();
			for (Iterator<Map.Entry<String,String>> iter = set.iterator(); iter.hasNext();) {
				Map.Entry<String,String> element = iter.next();
				out.print(element.getKey());
				out.print("=");
				out.println(element.getValue());
			}
			fo.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Odczytanie parametru z konfiguracji
	 * @param paramName - nazwa parametru
	 * @return wartosc lub NULL jesli nie istnieje
	 */
	public String get(String paramName) {
		return config.get(paramName);
	}

	/**
	 * Zapisanie parametru do konfiguracji
	 * @param paramName - nazwa parametru 
	 * @param value - wartość
	 */
	public void set(String paramName,String value) {
		config.put(paramName,value);
	}

	/**
	 * Usuniecie parametru
	 * @param paramName - nazwa parametru
	 */
	public void remove(String paramName) {
		config.remove(paramName);
	}
}
