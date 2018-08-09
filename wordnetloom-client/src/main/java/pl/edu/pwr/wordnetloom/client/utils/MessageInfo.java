package pl.edu.pwr.wordnetloom.client.utils;

import javax.swing.*;
import java.awt.*;

public class MessageInfo {

    final static int INFO_TIMEOUT = 5000;
    final static int FONT_SIZE = 20;
    final static Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE);

     public enum  InfoType{
        SUCCESS,
        ERROR,
         WARNING,
        NONE
    }

    public static void showInfo(String message, JComponent ownerComponent) {
       showInfo(message, ownerComponent, INFO_TIMEOUT, InfoType.NONE);
    }

    public static void showInfo(String message, JComponent ownerComponent, InfoType infoType){
         showInfo(message, ownerComponent, INFO_TIMEOUT, infoType);
    }

    public static void showInfo(String message, JComponent ownerComponent, int timeInMs, InfoType infoType) {
        final JLabel contentComponent = createInfoLabel(message, infoType);
        final int positionX = getCenteredLocationX(contentComponent, ownerComponent, message);
        final int positionY = getLocationY(ownerComponent);
        final Popup popup = PopupFactory.getSharedInstance().getPopup(ownerComponent, contentComponent,positionX, positionY);
        popup.show();
        startTimerToHide(popup, timeInMs);
    }

    private static int getCenteredLocationX(JComponent content, JComponent ownerComponent, String message) {
        int ownerLocation = (int) ownerComponent.getLocationOnScreen().getX();
        int textWidth = content.getFontMetrics(FONT).stringWidth(message) ;
        int halfOwnerWidth = ownerComponent.getWidth() / 2;
        return ownerLocation + halfOwnerWidth - textWidth /2;
    }

    private static int getLocationY(JComponent ownerComponent) {
        final int OFFSET_Y = 30;
        return (int) (ownerComponent.getLocationOnScreen().getY() - OFFSET_Y);
    }

    private static JLabel createInfoLabel(String message, InfoType infoType){
        final JLabel infoLabel = new JLabel(message);
        infoLabel.setFont(FONT);
        Color textColor = getColor(infoType);
        infoLabel.setForeground(textColor);
        return infoLabel;
    }

    private static void startTimerToHide(Popup popup, int timeInMs){
        Timer timer = new Timer(timeInMs, e->popup.hide());
        timer.setRepeats(false);
        timer.start();
    }

    public static Color getColor(InfoType color){
         switch (color){
             case SUCCESS:
                 return Color.GREEN;
             case ERROR:
                 return Color.RED;
             case WARNING:
                 return Color.ORANGE;
             case NONE:
                 return Color.BLACK;
         }
         throw new IllegalArgumentException();
    }
}
