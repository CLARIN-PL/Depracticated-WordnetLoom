
package pl.edu.pwr.wordnetloom.client.plugins.relations;

import javax.swing.ImageIcon;

/**
 * klasa zarządzające ikonami
 *
 * @author Max
 *
 */
public class RelationsIM {

    private static final ImageIcon toNew = new ImageIcon(RelationsIM.class.getClassLoader().getResource("icons/toNew.gif"));
    private static final ImageIcon toExisten = new ImageIcon(RelationsIM.class.getClassLoader().getResource("icons/toExisten.gif"));
    private static final ImageIcon toMerge = new ImageIcon(RelationsIM.class.getClassLoader().getResource("icons/toMerge.gif"));
    private static final ImageIcon onlyRelation = new ImageIcon(RelationsIM.class.getClassLoader().getResource("icons/onlyRelation.gif"));
    private static final ImageIcon deleteImage = new ImageIcon(RelationsIM.class.getClassLoader().getResource("icons/delete.gif"));
    private static final ImageIcon switchImage = new ImageIcon(RelationsIM.class.getClassLoader().getResource("icons/switch.gif"));
    private static final ImageIcon switchSecondImage = new ImageIcon(RelationsIM.class.getClassLoader().getResource("icons/switchD.gif"));
    private static final ImageIcon switchBothImage = new ImageIcon(RelationsIM.class.getClassLoader().getResource("icons/switchBoth.gif"));
    private static final ImageIcon addImage = new ImageIcon(RelationsIM.class.getClassLoader().getResource("icons/add.gif"));
    private static final ImageIcon newWindow = new ImageIcon(RelationsIM.class.getClassLoader().getResource("icons/newWindow.gif"));

    private RelationsIM() {
    }

    /**
     * ikona utworzenie nowego synsetu
     *
     * @return ikona
     */
    public static ImageIcon getToNew() {
        return toNew;
    }

    /**
     * ikona przeniesienia do istniejacego
     *
     * @return ikona
     */
    public static ImageIcon getToExisten() {
        return toExisten;
    }

    /**
     * ikona połączenie synsetów
     *
     * @return ikona
     */
    public static ImageIcon getToMerge() {
        return toMerge;
    }

    /**
     * ikona utworzenia relacji
     *
     * @return ikona
     */
    public static ImageIcon getOnlyRelation() {
        return onlyRelation;
    }

    /**
     * ikona usun
     *
     * @return ikona
     */
    public static ImageIcon getDelete() {
        return deleteImage;
    }

    /**
     * ikona przełącz
     *
     * @return ikona
     */
    public static ImageIcon getSwitch() {
        return switchImage;
    }

    /**
     * ikona przełącz docelowy
     *
     * @return ikona
     */
    public static ImageIcon getSwitchSecond() {
        return switchSecondImage;
    }

    /**
     * ikona przełącz oba
     *
     * @return ikona
     */
    public static ImageIcon getSwitchBoth() {
        return switchBothImage;
    }

    /**
     * ikona dodaj
     *
     * @return ikona
     */
    public static ImageIcon getAdd() {
        return addImage;
    }

    /**
     * ikona nowe okno
     *
     * @return ikona
     */
    public static ImageIcon getNewWindow() {
        return newWindow;
    }
}
