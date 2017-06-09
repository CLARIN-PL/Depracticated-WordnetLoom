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
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure;

import java.awt.Color;

public class ViwnEdgeCand extends ViwnEdge {

    private boolean hidden;
    private String relationName;
    private final Color color = Color.getHSBColor(245 / 360.0f, 0.38f, 1.0f);

    public ViwnEdgeCand(String relationName) {
        this.relationName = relationName;
    }

    public ViwnEdgeCand() {

    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public Color getColor() {
        if (hidden) {
            return null;
        }
        return color;
    }

    @Override
    public String toString() {
        if (hidden) {
            return null;
        }
        if (relationName == null) {
            return "kandydat";
        } else {
            return relationName;
        }
    }

}
