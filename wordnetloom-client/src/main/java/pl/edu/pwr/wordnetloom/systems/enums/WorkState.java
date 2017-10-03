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

package pl.edu.pwr.wordnetloom.systems.enums;

/**
 * @author M.Stanek <mikol@e-informatyka.pl>
 */
public enum WorkState {

    /** Nie zrobiony*/
    TODO("Nie przetworzone"),

    /** W czasie pracy */
    WORKING ("Częściowo przetworzone"),

    /** Zrobiony */
    DONE("Przetworzone"),

    /** Błędny */
    MISTAKE("Błędne"),

    /** Sprawdzony */
    VALIDATED("Sprawdzone"),

    /** Wątpliwy */
    DOUBT("Wątpliwe");

//  ----------------------------------------------------------------------------
    private String name;

    WorkState(String name) {
        this.name = name;
    }

    /**
     * @see java.lang.Enum#toString()
     */
    @Override
	public String toString() {
        return this.name;
    }

	/**
	 *
	 * @param name
	 * @return Workstae
	 */
    public static WorkState decode(String name) {
        WorkState[] d = WorkState.values();
        for (WorkState work : d) {
            if (work.name.equals(name)) {
                return work;
            }
        }

        return WorkState.TODO;
    }
}
