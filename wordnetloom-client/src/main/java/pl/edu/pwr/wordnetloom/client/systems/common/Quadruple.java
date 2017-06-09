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
package pl.edu.pwr.wordnetloom.client.systems.common;

/**
 * Klasa bedaca kontenerem na dane, typowa czworka
 *
 * @author Max
 *
 * @param <TA>
 * @param <TB>
 * @param <TC>
 * @param <TD>
 */
public class Quadruple<TA, TB, TC, TD> implements Container {

    private TA a;
    private TB b;
    private TC c;
    private TD d;

    /**
     * Konstruktor
     *
     * @param a - nowa wartosc pierwszego elementu
     * @param b - nowa wartosc drugiego elementu
     * @param c - nowa wartosc trzeciego elementu
     * @param d - nowa wartosc czwartego elementu
     */
    public Quadruple(TA a, TB b, TC c, TD d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * Odczytanie obiektu o podanym indeksie
     *
     * @param index - numer obiektu
     * @return obiekt
     */
    public Object get(int index) {
        switch (index) {
            case 0:
                return a;
            case 1:
                return b;
            case 2:
                return c;
            case 3:
                return d;
        }
        return null;
    }

    /**
     * Odczytanie pierwszej przechowywanej wartosci
     *
     * @return pierwsza wartosc
     */
    public TA getA() {
        return a;
    }

    /**
     * Odczytanie drugiej przechowywanej wartosci
     *
     * @return druga przechowywana wartosc
     */
    public TB getB() {
        return b;
    }

    /**
     * Odczytanie trzeciej przechowywanej wartosci
     *
     * @return trzecia przechowywana wartosc
     */
    public TC getC() {
        return c;
    }

    /**
     * Odczytanie czwartej przechowywanej wartosci
     *
     * @return czwarta przechowywana wartosc
     */
    public TD getD() {
        return d;
    }

    public int getCount() {
        return 4;
    }

    public Object getValue(int index) {
        switch (index) {
            case 0:
                return a;
            case 1:
                return b;
            case 2:
                return c;
            case 3:
                return d;
        }
        return null;
    }
}
