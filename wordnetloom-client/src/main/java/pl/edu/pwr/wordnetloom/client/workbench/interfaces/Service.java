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
package pl.edu.pwr.wordnetloom.client.workbench.interfaces;

/**
 * Uniwersalny interfejs zapewniający dostęp do funkcji oferowanych przez usługę
 *
 * NIE NALEŻY IMPLEMENTOWAĆ BEZPOŚREDNIO A WYKORZYSTYWAĆ AbstractService, które
 * uproszcza tworzenie usług
 *
 * @author <a href="mailto:lukasz.jastrzebski@pwr.wroc.pl">Lukasz
 * Jastrzebski</a>
 */
public interface Service {

    /**
     * Odczytanie identyfikatora usługi, w praktyce jest to nazwa klasy
     *
     * @return identyfiikator usługi
     */
    String getId();

    /**
     * Metoda wywoływana w celu instalacji widoków należących do konktretnej
     * usługi
     */
    void installViews();

    /**
     * Metoda wywoływana w celu instalacji elementów menu należących do
     * konktretnej usłgi
     */
    void installMenuItems();

    /**
     * Metoda wywoływana celem sprawdzenia czy można zamknąć usługę najcześciej
     * wywoływana przy zamykaniu aplikacji. Może być wykorzystywana do
     * przypominana o archiwizacji
     *
     * @return czy można zamknąć serwis
     */
    boolean onClose();

    /**
     * Zdarzenie wywoływane przy uruchamianiu usług po instalacji odpowiednich
     * elementów w menu
     */
    void onStart();
}
