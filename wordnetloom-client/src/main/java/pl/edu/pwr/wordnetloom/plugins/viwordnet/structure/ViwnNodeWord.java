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

package pl.edu.pwr.wordnetloom.plugins.viwordnet.structure;

import java.awt.Color;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.views.ViwnGraphViewUI;

public class ViwnNodeWord extends ViwnNodeRoot {
	private String word_;
	private int package_no_;
	private PartOfSpeech pos_;
	private final Color color = Color.getHSBColor(245/360.0f, 0.38f, 1.0f);
	
	public ViwnNodeWord(String word, int packageNo, PartOfSpeech pos) {
		word_ = word;
		package_no_ = packageNo;
		pos_ = pos;
	}

	public PartOfSpeech getPos() {
		return pos_;
	}

	public int getPackageNo() {
		return package_no_;
	}

	public String getLabel() {
		return word_;
	}

	public Shape getShape() {
		Ellipse2D p = new Ellipse2D.Float(-40, -20, 80, 40);
		return p;
	}

	public void mouseClick(MouseEvent me, ViwnGraphViewUI ui) {
	}

	public Color getColor() {
		return color;
	}

	public int hashCode() {
		return word_.hashCode();
	}

	public boolean equals(Object o) {
		if (o!=null && o instanceof ViwnNodeWord) {
			return word_.equals(((ViwnNodeWord) o).word_);
		}
		return false;
	}
}
