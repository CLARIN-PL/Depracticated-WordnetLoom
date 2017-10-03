///*
//    Copyright (C) 2011 Łukasz Jastrzębski, Paweł Koczan, Michał Marcińczuk,
//                       Bartosz Broda, Maciej Piasecki, Adam Musiał,
//                       Radosław Ramocki, Michał Stanek
//    Part of the WordnetLoom
//
//    This program is free software; you can redistribute it and/or modify it
//under the terms of the GNU General Public License as published by the Free
//Software Foundation; either version 3 of the License, or (at your option)
//any later version.
//
//    This program is distributed in the hope that it will be useful, but
//WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
//or FITNESS FOR A PARTICULAR PURPOSE. 
//
//    See the LICENSE and COPYING files for more details.
//*/
//
package pl.edu.pwr.wordnetloom.plugins.relations.views;
//
//import java.awt.Color;
//
//import Domain;
//import Synset;
//import SimpleListenerInterface;
//import AbstractView;
//import Workbench;
//
///**
// * lista synsetów
// * @author Max
// */
public class SynsetsView{
	
}
//
//	static private Color colorOfSecond = new Color(220,220,255); // kolor drugiego okna
//	
//	/**
//	 * kontruktor dla klasy
//	 * @param workbench - wskaznik dla workbencha
//	 * @param title - etykieta dla okienka
//	 * @param viewNumber - numer widoku do odświeżena
//	 */
//	public SynsetsView(Workbench workbench,String title,int viewNumber) {
//		super(workbench,title,new SynsetsViewUI());
//		if (viewNumber==2)
//			getUI().setBackgroundColor(colorOfSecond); // kolor dla drugiego
//	}
//	
//	/**
//	 * dodanie obiektu nasługującego zmian w zaznaczeniu synetu
//	 * @param newListener - sluchacz
//	 */
//	public void addSelectionChangeListener(SimpleListenerInterface newListener) {
//		getUI().addActionListener(newListener);
//	}
//	
//	/**
//	 * odświeżenie listy jednostek
//	 */
//	public void refreshData() {
//		SynsetsViewUI viewUI=(SynsetsViewUI)getUI(); // odczytanie UI
//		viewUI.refreshData();
//	}
//
//	/**
//	 * ustawienie ostatnio używanego synsetu
//	 * @param synset - synset
//	 * @param textFilter - filtr do wyszukiwania
//	 */
//	public void setLastSynset(Synset synset,String textFilter) {
//		SynsetsViewUI viewUI=(SynsetsViewUI)getUI(); // odczytanie UI
//		viewUI.setLastSynset(synset,textFilter);
//	}
//	
//	/**
//	 * odrysowanie zawartości listy bez ponownego odczytu danych
//	 */
//	public void redrawList() {
//		SynsetsViewUI viewUI=(SynsetsViewUI)getUI(); // odczytanie UI
//		viewUI.redrawList();
//	}
//	
//	/**
//	 * Odczytanie aktualnie ustawionego filtra
//	 * @return filter
//	 */
//	public String getFilter() {
//		SynsetsViewUI viewUI=(SynsetsViewUI)getUI(); // odczytanie UI
//		return viewUI.getFilter();
//	}
//
//	/**
//	 * Ustawienie nowej wartosci dla filtra
//	 * @param filter - nowa wartosc dla filtra
//	 */
//	public void setFilter(String filter) {
//		SynsetsViewUI viewUI=(SynsetsViewUI)getUI(); // odczytanie UI
//		viewUI.setFilter(filter);
//	}
//
//	/**
//	 * Odczytanie aktualnie ustawionej dziedziny
//	 * @return dziedzina
//	 */
//	public Domain getDomain() {
//		SynsetsViewUI viewUI=(SynsetsViewUI)getUI(); // odczytanie UI
//		return viewUI.getDomain();
//	}
//	
//	/**
//	 * Ustawienie nowej wartosci dla dziedziny
//	 * @param domain - wartosc dziedziny
//	 */
//	public void setDomain(Domain domain) {
//		SynsetsViewUI viewUI=(SynsetsViewUI)getUI(); // odczytanie UI
//		viewUI.setDomain(domain);
//	}
//	
//	/**
//	 * Odczytanie wybranego statusu
//	 * @return wybrany status
//	 */
//	public int getStatus() {
//		SynsetsViewUI viewUI=(SynsetsViewUI)getUI(); // odczytanie UI
//		return viewUI.getStatus();
//	}
//	
//	/**
//	 * Ustawienie nowego statusu
//	 * @param status - status
//	 */
//	public void setStatus(int status) {
//		SynsetsViewUI viewUI=(SynsetsViewUI)getUI(); // odczytanie UI
//		viewUI.setStatus(status);
//	}
//
//}
