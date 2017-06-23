/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views;

import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

public class ListElement {

    private final String word;
    private final Integer pkg;
    private final PartOfSpeech pos;
    private final String descrString;
    boolean longDescr;

    public ListElement(String word, Integer pkg, PartOfSpeech pos) {
        this.word = word;
        this.pkg = pkg;
        this.pos = pos;
        this.longDescr = false;

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(word);
        sb.append("<br><font size=-2>");
        sb.append(pos);
        sb.append(" #").append(pkg);
        sb.append("</font>");
        sb.append("</html>");
        descrString = sb.toString();
    }

    public String getWord() {
        return word;
    }

    public Integer getPkg() {
        return pkg;
    }

    public PartOfSpeech getPos() {
        return pos;
    }

    @Override
    public String toString() {
        if (longDescr) {
            return descrString;
        } else {
            return word;
        }
    }

    @Override
    public boolean equals(Object o) {
        ListElement e = (ListElement) o;
        return descrString.equals(e.descrString);
    }

    @Override
    public int hashCode() {
        return descrString.hashCode();
    }

    public void setDescrSize(boolean descrSize) {
        longDescr = descrSize;
    }
}
