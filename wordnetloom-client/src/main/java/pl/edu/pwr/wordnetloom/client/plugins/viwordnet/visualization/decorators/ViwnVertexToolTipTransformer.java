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
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators;

import org.apache.commons.collections15.Transformer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeCand;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeCandExtension;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeSet;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeSynset;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipGenerator;
import pl.edu.pwr.wordnetloom.client.utils.RemoteUtils;

/**
 * <p>
 * <b>ViwnVertexToolTipTransformer</b></p>
 * <p>
 * this class provides tooltips for vertices at visualization viewer<br>
 * tooltips are different for different types of nodes</p>
 *
 * @author amusial
 */
public class ViwnVertexToolTipTransformer implements Transformer<ViwnNode, String> {

    @Override
    public String transform(ViwnNode vn) {
        String ret = "";
        if (!ToolTipGenerator.getGenerator().hasEnabledTooltips()) {
            return ret;
        }

        if (vn instanceof ViwnNodeCandExtension) {
            ViwnNodeCandExtension cand = (ViwnNodeCandExtension) vn;
            ret = "<html>%s<br><b>Proponowana jednostka: %s<b><br><b>Ocena:</b> %.2f</html>";
            ret = String.format(ret,
                    RemoteUtils.synsetRemote.dbRebuildUnitsStr(cand.getSynset(), LexiconManager.getInstance().getLexicons()),
                    cand.getExtGraphExtension().getLexicalUnit().getLemma(),
                    cand.getExtGraphExtension().getRank());
        } else if (vn instanceof ViwnNodeCand) {
            ViwnNodeCand cand = (ViwnNodeCand) vn;
            ret = "<html>%s<br><b>Ocena:</b> %.2f%s</html>";
            String tmp = "";
            if (cand.getExt().getScore2() != -1) {
                tmp = String.format(" (%.2f)", cand.getExt().getScore2());
            }
            ret = String.format(ret,
                    RemoteUtils.synsetRemote.dbRebuildUnitsStr(cand.getSynset(), LexiconManager.getInstance().getLexicons()),
                    cand.getExt().getScore1(),
                    tmp
            );
        } else if (vn instanceof ViwnNodeSynset) {
            ViwnNodeSynset vns = ((ViwnNodeSynset) vn);
            ret = vns.getUnitsStr();
        } else if (vn instanceof ViwnNodeSet) {
            int cand = 0;
            int syns = 0;

            for (ViwnNode v : ((ViwnNodeSet) vn).getSynsets()) {
                if (v instanceof ViwnNodeCand) {
                    cand++;
                } else if (v instanceof ViwnNodeSynset) {
                    syns++;
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append("<html>");
            sb.append("<b>Liczba synsetów:</b> ").append(syns);
            if (cand != 0) {
                sb.append("<br><b>Liczba kandydatów:</b> ").append(cand);
            }
            sb.append("</html>");
            ret = sb.toString();
        }
        return ret;
    }
}
