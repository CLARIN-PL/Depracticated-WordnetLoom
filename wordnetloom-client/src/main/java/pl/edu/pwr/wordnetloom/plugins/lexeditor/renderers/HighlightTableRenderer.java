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
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.Graphics;
//
//import javax.swing.JCheckBox;
//import javax.swing.JTable;
//import javax.swing.table.DefaultTableCellRenderer;
//
///**
// * klasa renderujaca kolorowe komorki tabeli
// * @author Max
// */
package pl.edu.pwr.wordnetloom.plugins.lexeditor.renderers;
public class HighlightTableRenderer {
	
}
//public class HighlightTableRenderer extends DefaultTableCellRenderer {
//	private static final long serialVersionUID = 1L;
//	final static private Color backgroundColor1=new Color(254,254,254);
//	final static private Color backgroundSelectedColor1=new Color(150,150,255);
//	final static private Color backgroundColor2=new Color(230,230,230);
//	final static private Color backgroundSelectedColor2=new Color(150,150,255);
//	final static private Color warrningColor=new Color(210,50,50);
//	final static private Color markColor=new Color(150,210,150);
//	final static private Color markSelectedColor=new Color(100,210,100);
//	final static private JCheckBox checkBox=new JCheckBox();
//	
//	/*
//	 *  (non-Javadoc)
//	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
//	 */
//	@Override
//	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
//	{
//		Component component=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
//		if (value instanceof Boolean) { // jest to wartosc typu true, false
//			component=checkBox;
//			checkBox.setSelected(((Boolean)value).booleanValue());
//		}
//		// ustawienie odpowiednich kolorow
//		if (getText().startsWith("`"))
//			component.setBackground(hasFocus?markSelectedColor:markColor); 
//		else if (row%2!=0)
//			component.setBackground(hasFocus?backgroundSelectedColor2:backgroundColor2);
//		else
//			component.setBackground(hasFocus?backgroundSelectedColor1:backgroundColor1);
//		
//		if (component==checkBox && !checkBox.isSelected()) { // nie jest poprawna relacja
//			component.setBackground(warrningColor);
//		}
//		return component;
//	}
//	
//	/*
//	 *  (non-Javadoc)
//	 * @see java.awt.Component#paint(java.awt.Graphics)
//	 */
//	@Override
//	public void paint(Graphics g) {
//		String text=getText();
//		boolean markMode=text.startsWith("`");
//		if (markMode) // czy ma byc aktywny
//			setText(text.substring(1,text.length()));
//		super.paint(g); // odrysowanie z nadrzednego
//		if (markMode)
//			this.setText(text);  // przywrocenie tekstu
//	}
//}
