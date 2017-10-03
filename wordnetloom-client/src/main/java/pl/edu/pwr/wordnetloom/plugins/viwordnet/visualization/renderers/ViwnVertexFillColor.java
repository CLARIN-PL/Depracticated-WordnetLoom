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

package pl.edu.pwr.wordnetloom.plugins.viwordnet.visualization.renderers;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.systems.managers.PosManager;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNodeCand;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNodeCandExtension;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNodeSet;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNodeSynset;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNodeWord;
import edu.uci.ics.jung.visualization.picking.PickedInfo;

/**
 * Class responsible for setting color of a vertex. 
 * 
 * @author Michał Marcińczuk <michal.marcinczuk@pwr.wroc.pl>
 *
 */

public final class ViwnVertexFillColor implements Transformer<ViwnNode,Paint>
{
	protected PickedInfo<ViwnNode> pi;
	protected ViwnNode root;
	
	/**
	 * @param pi
	 * @param root_node
	 */
	public ViwnVertexFillColor(PickedInfo<ViwnNode> pi, ViwnNode root_node)	{
		this.pi = pi;
		this.root = root_node;
	}
	
	public Paint transform(ViwnNode v){
		if (pi.isPicked(v)) {
			return ViwnNodeSynset.vertexBackgroundColorSelected;
		} else if (v == root) {
			return ViwnNodeSynset.vertexBackgroundColorRoot;
		} else if (v.isMarked()) {
			return ViwnNodeSynset.vertexBackgroundColorMarked;
		}
		
		if(v instanceof ViwnNodeCandExtension){
			ViwnNodeCandExtension ext = (ViwnNodeCandExtension)v; 
			return ext.getColor();
		} else if (v instanceof ViwnNodeCand) {
			ViwnNodeCand cand = (ViwnNodeCand)v; 
				return cand.getColor();
		} else if (v instanceof ViwnNodeWord) {
			return ((ViwnNodeWord) v).getColor();
		} else if (v instanceof ViwnNodeSynset) {
			ViwnNodeSynset synset = (ViwnNodeSynset)v;
			PartOfSpeech pos = synset.getPos();
			if (pos == null || "! S.y.n.s.e.t p.u.s.t.y !".equals(synset.getLabel()))
				return Color.RED;
			
			if(ViwnNodeSynset.PosBgColors.containsKey(pos)){
				return ViwnNodeSynset.PosBgColors.get(pos);
			} else {
				return ViwnNodeSynset.PosBgColors.get(PosManager.getInstance().getFromID(0));
			}
			
		} else if (v instanceof ViwnNodeSet) {
			return ViwnNodeSet.vertexBackgroundColor;
		}
		
		return new Color(255, 0, 255);
	}
}
