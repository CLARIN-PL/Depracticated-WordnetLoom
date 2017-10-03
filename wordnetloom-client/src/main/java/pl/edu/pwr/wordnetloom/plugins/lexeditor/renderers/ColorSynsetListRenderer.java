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
//package pl.wroc.pwr.ci.plwordnet.plugins.lexeditor.renderers;
//
//import java.awt.Component;
//import java.awt.Dimension;
//import java.awt.FontMetrics;
//import java.awt.Graphics;
//import java.util.ArrayList;
//import java.util.Collection;
//
//import javax.swing.DefaultListCellRenderer;
//import javax.swing.JList;
//
//import GenericListModel;
//
///**
// * klasa wyświetlająca kolorową listę (zgodną z formatem prezentacji synsetów)
// * z zawijaniem wierszy
// * @author Max
// */
package pl.edu.pwr.wordnetloom.plugins.lexeditor.renderers;
public class ColorSynsetListRenderer {
	
}
//public class ColorSynsetListRenderer extends DefaultListCellRenderer {
//		private static final long serialVersionUID = 1L;
//		private String lastHTML=null;
//		final static private String ROOT_REPLACE[]={"<font color=\"0000FF\"><u>","</u></font>"};
//		final static private String HASH_REPLACE[]={"<font color=\"0DAA0D\">","</font>"};
//		private static final String ABSTRACT_SYNSET = "<font color=\"red\">A</font>";
//		private static final String ABSTRACT_MARK = "A";
//		private static final String HTML_START = "<html>";
//		private static final String HTML_END = "</html>";
//
//		/**
//		 * konwertowanie tekstu do HTML-a
//		 * @param strings - kolekcja linii tekstu
//		 * @return sformatowany tekst HTML
//		 */
//		static private String makeHTML(Collection<String> strings) {
//			StringBuilder sb=new StringBuilder(HTML_START); // wstawienie znacznika poczatkowego
//			int index=0,hashReplaceIndex=0,pos;
//			boolean rootEndFound=false;
//			int linesCount=strings.size();
//			for (String string : strings) {
//				if (index++!=0)       // gdy nie jest to pierwsza linia
//					sb.append("<br>&nbsp;&nbsp;"); // dodanie znaku nastepnej linii oraz spacji
//				else { // gdy jest to pierwsza linia
//					if ((pos=string.indexOf("("))!=-1) { // rozpoczecie oznaczania glowki
//						sb.append(string.substring(0,pos+1)); // dodanie tego co przed nawiasem
//						sb.append(ROOT_REPLACE[0]);          // dodanie znacznika
//						string=string.substring(pos+1);     // zostawienie reszty
//					}
//				}
//				if (!rootEndFound && (pos=string.indexOf(" |"))!=-1) { // koniec oznaczania główki
//					StringBuilder lineBuilder=new StringBuilder();     // budowa linii
//					lineBuilder.append(string.substring(0,pos));       // dodanie tego co przed
//					lineBuilder.append(ROOT_REPLACE[1]);                // dodanie znacznika
//					lineBuilder.append(string.substring(pos));         // zostawienei reszty
//					rootEndFound=true;                                 // aby nie szukać nadmiarowo
//					string=lineBuilder.toString();                     // przywrocenie linii
//				}
//
//				while (/*hashReplaceIndex<2 && */(pos=string.indexOf("#"))!=-1) { // czy są hashe i (hashReplaceIndex<2) aby nie szukać nadmiarowo
//					sb.append(string.substring(0,pos));             // ustawienie tego co przed
//					sb.append(HASH_REPLACE[(hashReplaceIndex++)%2]); // wstawienie znacznika
//					string=string.substring(pos+1);                 // zostawienie reszty
//				}
//				if (index==linesCount && string.endsWith(ABSTRACT_MARK)) { // ostatnia linia, wiec moze jest A
//					int strLen=string.length();
//					sb.append(string.substring(0,strLen-1));
//					sb.append(ABSTRACT_SYNSET);
//				} else {
//					sb.append(string);
//				}
//			}
//			return sb.append(HTML_END).toString(); // wstawienie znacznika koncowego
//		}
//		
//		/**
//		 * podzelenie tekstu na osobne linie wedlug slow
//		 * @param g - kontekst graficzny
//		 * @param text - tekst
//		 * @param maxWidth - maksymalna dlugosc
//		 * @return podzielony tekst
//		 */
//	    public static Collection<String> splitString(Graphics g,String text,int maxWidth) {
//	    	String[] array=text.split(" "); // podzielenie na elementy wzlgedem " "
//	    	Collection<String> results=new ArrayList<String>();
//	    	FontMetrics metrics=g.getFontMetrics();
//	    	StringBuilder sb=new StringBuilder();
//	    	int index=0;
//	    	sb.append(array[index++]);
//	    	// dopoki jest krotszy
//	    	while (index<array.length) {
//	    		// czy z nastepnym wyrazem tez biedzie sie miescil
//	    		if (metrics.stringWidth(sb.toString()+array[index])<maxWidth) {
//	    			sb.append(" "); // wstawienie brakujacego odstepu
//	    			sb.append(array[index++]); // bedzie sie miescil, wiec dodajemy
//	    		} else {
//	    			results.add(sb.toString()); // nie miesci sie, dolaczenie tego co jest
//	    			sb=new StringBuilder("  "); // wstawienie odstepu dla lepszego efektu
//	    			sb.append(array[index++]); // utworzonenie nowej linii
//	    		}
//	    	}
//	    	if (sb.length()>0) { // dodanie tego co zostalo
//				results.add(sb.toString()); 
//	    	}
//	    	return results;
//	    }
//
//	    /*
//	     *  (non-Javadoc)
//	     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
//	     */
//		@Override
//	    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//	    	super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
//	    	GenericListModel<?> model=(GenericListModel<?>)list.getModel();
//	    	int width=list.getWidth();
//	    	
//	    	// odczytanie szerokosci okna
//	    	if (model!=null && model.getTag()!=null)
//	    		width=((Integer)model.getTag()).intValue()-5; // dodatkowy margines
//	    		
//	    	Graphics g=list.getGraphics(); // kontekst graficzny
//    		if (g!=null) {
//    			Collection<String> lastStrings=splitString(g,getText(),width);
//	    		int row=lastStrings.size(); // ilosc wierszy
//	    		int h=g.getFontMetrics().getAscent(); // polozenie czionki
//	    		Dimension dim=new Dimension(width,4+row*(h+3));
//	    		this.setPreferredSize(dim);
//	    		this.setSize(dim);
//	    		lastHTML=makeHTML(lastStrings);
//	    	} 
//	    	return this;
//	    }
//
//	    /*
//	     *  (non-Javadoc)
//	     * @see java.awt.Component#paint(java.awt.Graphics)
//	     */
//		@Override
//		public void paint(Graphics g) {
//			String text=getText();
//			setText(lastHTML);  // wylaczenie tekstu
//			super.paint(g); // odrysowanie z nadrzednego
//			setText(text);  // wlaczenie tekstu
//		}
//	}
