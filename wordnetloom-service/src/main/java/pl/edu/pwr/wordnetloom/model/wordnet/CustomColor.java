package pl.edu.pwr.wordnetloom.model.wordnet;

import java.awt.Color;

public class CustomColor {

    public static Color nodeColorSelected = new Color(255, 255, 127);
    public static Color nodeColorFirst = new Color(255, 163, 100);
    public static Color nodeColorSecond = new Color(127, 196, 255);
    public static Color nodeColorOther = new Color(229, 229, 229);
    public static Color nodeColorFinal = new Color(127, 255, 63);

    /**
     * Color for node with 'new word' and synsets containing 'new word'
     */
    public static Color nodeNewWord = new Color(88, 255, 88);

    /**
     * Color for synset node selected as a weak match
     */
    public static Color nodeWeakMatch = new Color(255, 248, 170);

    /**
     * Color for synset node with score >= 1.5
     */
    public static Color nodeSynsetScore15 = new Color(255, 204, 12);

    /**
     * Color for synset node with score >= 2.5
     */
    public static Color nodeSynsetScore25 = new Color(255, 136, 8);

    /**
     * Color for synset node with score >= 3.0
     */
    public static Color nodeSynsetScore30 = new Color(255, 68, 68);

    /**
     * Color for synset node with score >= 4.0
     */
    public static Color nodeSynsetScore40 = new Color(255, 135, 220);

    /**
     * Color for synset node with score >= 5.0
     */
    public static Color nodeSynsetScore50 = new Color(255, 60, 200);

    /**
     * Base color for synset candidate (extension)
     */
    public static Color extensionColorBase = new Color(100, 100, 100);
}
