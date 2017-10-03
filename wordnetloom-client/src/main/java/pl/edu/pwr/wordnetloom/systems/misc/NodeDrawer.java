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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

/**
 * klasa rysujaca element drzewa
 * @author Max
 *
 */
public class NodeDrawer {
	final static private int X_DISTANCE=10;
	final static private int Y_DISTANCE=20;
	final static private Color backColor=new Color(200,221,242);
	final static private Color focusColor=new Color(150,210,150);
	
	protected Dimension dimension=new Dimension();
	protected Dimension bound=new Dimension();
	protected Point position=new Point();
	protected int fontHeight=0;	
	protected Collection<String> itemsToDraw=null;
	protected Collection<NodeDrawer> nodes=new ArrayList<NodeDrawer>();
	protected Object tag;
	
	/**
	 * konstruktor
	 * @param items - stringi do narysowania
	 */
	public NodeDrawer(Collection<String> items) {
		itemsToDraw=items;
	}
	
	/**
	 * dodatkowy parametr
	 * @param object - obiekt
	 */
	public void setTag(Object object) {
		this.tag=object;
	}
	
	/**
	 * konstruktor
	 * @param item - teskt do narysowania
	 */
	public NodeDrawer(String item) {
		itemsToDraw=new ArrayList<String>();
		itemsToDraw.add(item);
	}
	
	/**
	 * dodanie nowego wezła
	 * @param node - wezeł
	 * @return dodany węzeł
	 */
	public NodeDrawer addNode(NodeDrawer node) {
		nodes.add(node);
		return node;
	}

	/** 
	 * narysowanie zawartosci w dół
	 * @param g - kontekst graficzny
	 * @param x - polozenie x
	 * @param y - polozenie y
	 * @param globalTag - globalny parametr
	 */
	public void drawY(Graphics g,int x,int y,Object globalTag) {
		// narysowanie wlasciwego
		position.x=x+(bound.width-dimension.width)/2;
		position.y=y;
		if (itemsToDraw==null) return;
		g.setColor(globalTag!=null && globalTag.equals(tag)?focusColor:backColor);
		g.fillRect(position.x,position.y,dimension.width,dimension.height);
		g.setColor(Color.BLACK);
		int pos=fontHeight;
		for (String text : itemsToDraw) {
			g.drawString(text,position.x+2,position.y+pos);
			pos+=fontHeight;
		}
		g.drawRect(position.x,position.y,dimension.width,dimension.height);
		// narysowanie potomkow
		y+=dimension.height+Y_DISTANCE;
		for (NodeDrawer node : nodes) {
			node.drawY(g,x,y,globalTag);
			x+=node.getBound().width+X_DISTANCE;
			drawArrowY(g,this,node);
		}
	}
		
	/** 
	 * probne rysowanie - policznie wymiarow w dół
	 * @param g - kontekst graficzny
	 */
	public void trialDrawY(Graphics g) {
		if (itemsToDraw==null) return;
		
		// policzenie wymiarow
		FontMetrics metrics=g.getFontMetrics();
		fontHeight=metrics.getHeight();
		int pos=fontHeight;
		int length=0;
		for (String text : itemsToDraw) {
			length=Math.max(length,metrics.stringWidth(text));
			pos+=fontHeight;
		}
		dimension.width=length+4;
		dimension.height=pos-fontHeight+4;
		
		// policzenie obrysu z dziecmi
		bound.width=0;
		bound.height=0;
		for (NodeDrawer node : nodes) {
			node.trialDrawY(g);
			bound.width+=node.getBound().width+X_DISTANCE;
			bound.height=Math.max(node.getBound().height,bound.height);
		}
		bound.width=Math.max(dimension.width,bound.width-X_DISTANCE);
		bound.height=dimension.height+Y_DISTANCE+bound.height;
		
		// korekta na odstep
		if (nodes.size()==0)
			bound.height-=Y_DISTANCE;
	}
		
	/** 
	 * narysowanie zawartosci w bok
	 * @param g - kontekst graficzny
	 * @param x - polozenie x
	 * @param y - polozenie y
	 * @param globalTag - globalny parametr
	 */
	public void drawX(Graphics g,int x,int y,Object globalTag) {
		// narysowanie wlasciwego
		position.x=x;
		position.y=y+(bound.height-dimension.height)/2;
		if (itemsToDraw==null) return;
		g.setColor(globalTag==tag?focusColor:backColor);
		g.fillRect(position.x,position.y,dimension.width,dimension.height);
		g.setColor(Color.BLACK);
		int pos=fontHeight;
		for (String text : itemsToDraw) {
			g.drawString(text,position.x+2,position.y+pos);
			pos+=fontHeight;
		}
		g.drawRect(position.x,position.y,dimension.width,dimension.height);
		// narysowanie potomkow
		x+=dimension.width+Y_DISTANCE;
		for (NodeDrawer node : nodes) {
			node.drawX(g,x,y,globalTag);
			y+=node.getBound().height+X_DISTANCE;
			drawArrowX(g,this,node);
		}
	}
		
	/** 
	 * probne rysowanie - policznie wymiarow w bok
	 * @param g - kontekst graficzny
	 */
	public void trialDrawX(Graphics g) {
		if (itemsToDraw==null) return;
		
		// policzenie wymiarow
		FontMetrics metrics=g.getFontMetrics();
		fontHeight=metrics.getHeight();
		int pos=fontHeight;
		int length=0;
		for (String text : itemsToDraw) {
			length=Math.max(length,metrics.stringWidth(text));
			pos+=fontHeight;
		}
		dimension.width=length+4;
		dimension.height=pos-fontHeight+4;
		
		// policzenie obrysu z dziecmi
		bound.width=0;
		bound.height=0;
		for (NodeDrawer node : nodes) {
			node.trialDrawX(g);
			bound.height+=node.getBound().height+X_DISTANCE;
			bound.width=Math.max(node.getBound().width,bound.width);
		}
		bound.height=Math.max(dimension.height,bound.height-X_DISTANCE);
		bound.width=dimension.width+Y_DISTANCE+bound.width;
		
		// korekta na odstep
		if (nodes.size()==0)
			bound.width-=Y_DISTANCE;
	}
		
	/**
	 * odczytanie wymiarow
	 * @return wymiary
	 */
	public Dimension getDimension() {
	  return dimension;
	}
		
	/**
	 * odczytanie obrysu
	 * @return obrys
	 */
	public Dimension getBound() {
	  return bound;
	}
		
	/**
	 * odczytanie polozenia
	 * @return polozenie
	 */
	public Point getPosition() {
	  return position;
	}
		
	/**
	 * narysowanie strzałki poziomej
	 * @param g - kontekst graficzny
	 * @param a - drawer a
	 * @param b - drawer b
	 */
	public static void drawArrowX(Graphics g,NodeDrawer a,NodeDrawer b) {
		int x1=a.getPosition().x+a.getDimension().width;
		int x2=b.getPosition().x;
		int y1=a.getPosition().y+a.getDimension().height/2;
		int y2=b.getPosition().y+b.getDimension().height/2;
		
		int xMiddle1=x1+(x2-x1)/3;
		int xMiddle2=x1+(x2-x1)*2/3;
		g.drawLine(x1,y1,xMiddle1,y1);
		g.drawLine(xMiddle1,y1,xMiddle2,y2);
		g.drawLine(xMiddle2,y2,x2,y2);
		g.drawLine(x2-10,y2-5,x2,y2);
		g.drawLine(x2-10,y2+5,x2,y2);
	}

	/**
	* narysowanie strzałki pionowej
	* @param g - kontekst graficzny
	* @param a - drawer a
	* @param b - drawer b
	*/
	public static void drawArrowY(Graphics g,NodeDrawer a,NodeDrawer b) {
		int x1=a.getPosition().x+a.getDimension().width/2;
		int x2=b.getPosition().x+b.getDimension().width/2;
		int y1=a.getPosition().y+a.getDimension().height;
		int y2=b.getPosition().y;
			
		int yMiddle1=y1+(y2-y1)/3;
		int yMiddle2=y1+(y2-y1)*2/3;
		g.drawLine(x1,y1,x1,yMiddle1);
		g.drawLine(x1,yMiddle1,x2,yMiddle2);
		g.drawLine(x2,yMiddle2,x2,y2);
		g.drawLine(x2-5,y2-10,x2,y2);
		g.drawLine(x2+5,y2-10,x2,y2);
	}
}
