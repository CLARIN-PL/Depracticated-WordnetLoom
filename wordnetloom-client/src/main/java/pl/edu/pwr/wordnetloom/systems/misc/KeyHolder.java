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

package pl.edu.pwr.wordnetloom.systems.misc;

/**
 * klasa przechowuje kolejne przycisniecia klawiszy i przeksztalca
 * je w string
 * @author Max
 *
 */
public class KeyHolder {

	private StringBuilder sb=new StringBuilder();
	private long lastSave=0;
	
	/**
	 * dodanie kolejnej literki do przechowywanego stringu
	 * @param key - litera do dodania
	 * @return wynikowy string
	 */
	public String addKey(char key) {
		long now=System.currentTimeMillis();
		if (now>lastSave+2000) { // czy miesci się w czasie
			sb=new StringBuilder();
		}
		lastSave=now;
		return sb.append(""+key).toString();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return sb.toString();
	}

	/**
	 * czy klawisz ma poprawny kod
	 * @param key - klawisz
	 * @return TRUE jesli poprawny kod
	 */
	public boolean isProperCode(char key) {
		final String ALLOWED="!$0123456789-ęóąśłżźćńĘÓĄŚŁŻŹĆŃ. >_#@";
		return (key>='a' && key<='z') || (key>='A' && key<='Z') || ALLOWED.indexOf(""+key)!=-1;
	}

}
