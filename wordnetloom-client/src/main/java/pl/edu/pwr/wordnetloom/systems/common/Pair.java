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

package pl.edu.pwr.wordnetloom.systems.common;

/**
 * Klasa bedaca kontenerem na dane, typowa para
 * @author Max
 *
 * @param <TA>
 * @param <TB>
 */
// to purge

public class Pair<TA,TB> implements Container {
	private TA a;
	private TB b;
	
	/**
	 * Konstruktor
	 * @param a - nowa wartosc pierwszego elementu
	 * @param b - nowa wartosc drugiego elementu
	 */
	public Pair(TA a,TB b) {
		this.a=a;
		this.b=b;
	}

	/**
	 * Odczytanie pierwszej przechowywanej wartosci
	 * @return pierwsza wartosc
	 */
	public TA getA() {
		return a;
	}

	/**
	 * Odczytanie drugiej przechowywanej wartosci
	 * @return druga przechowywana wartosc
	 */
	public TB getB() {
		return b;
	}

	/*
	 * (non-Javadoc)
	 * @see pl.wroc.pwr.ci.plwordnet.database.dc.common.Container#getCount()
	 */
	public int getCount() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * @see pl.wroc.pwr.ci.plwordnet.database.dc.common.Container#getValue(int)
	 */
	public Object getValue(int index) {
		switch (index) {
			case 0:return a;
			case 1:return b;
		}
		return null;
	}
	
	public int hashCode() {
		return a.hashCode() ^ b.hashCode();
	}
	
	@SuppressWarnings("rawtypes")
	public boolean equals(Object p) {
		return this.a.equals(((Pair)p).a) &&
			this.b.equals(((Pair)p).b);
	}
}
