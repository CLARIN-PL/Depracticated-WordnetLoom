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

package pl.edu.pwr.wordnetloom.utils;


public class StringUtils {

//	/**
//	 * Łączy kolekcję napisów w jeden dodając separator między nimi
//	 * @param collection
//	 * @param separator
//	 * @return połączona kolekcja napisów
//	 */
//	public static String implode(java.util.Collection<?> collection, java.lang.String separator){
//		StringBuilder sb = new StringBuilder();
//		Object[] array = collection.toArray();
//		for (int i=0; i<array.length; i++)
//		{
//			if (i>0) sb.append(separator);
//			sb.append(array[i]);
//		}
//		return sb.toString();
//	}
	
	public static String pad(double n, int length, int precision){
		return StringUtils.pad(String.format("%."+precision+"f", n), length);
	}
	
	public static String pad(int n, int length){
		return StringUtils.pad(""+n, length);
	}
	
	public static String pad(String str, int length){
		return StringUtils.pad(str, length, ' ');
	}
	
	public static String pad(String str, int length, char c){
		StringBuilder sb = new StringBuilder();
		for (int i=str.length(); i<length; i++)
			sb.append(c);
		sb.append(str);
		return sb.toString();
	}

}
