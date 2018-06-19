package pl.edu.pwr.wordnetloom.client.systems.errors;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import static cern.clhep.Units.s;

public class ErrorProvider {

    public static final int NO_ERROR = 0;
    public static final int WARNING = 1;
    public static final int ERROR = 2;

    private Border originalBorder;
    private Color originalBackgroundColor;

    public ErrorProvider(JComponent component){
        originalBorder = component.getBorder();
        originalBackgroundColor = component.getBackground();
    }

    private Color getColor(final int errorType){
        switch (errorType){
            case NO_ERROR:
                return originalBackgroundColor;
            case WARNING:
//                return Color.ORANGE;
                return originalBackgroundColor;
            case ERROR:
//                return Color.RED;
                return originalBackgroundColor;
            default:
                throw new IllegalArgumentException("Not a valid error type");
        }
    }

    // TODO poustawiać ikony
    private ImageIcon getImage(final int errorType) {
        switch (errorType){
            case NO_ERROR:
                return null;
            case WARNING:
                return  new ImageIcon(Toolkit.getDefaultToolkit().getImage("/icons/error.png"));
            case ERROR:
//                return  Toolkit.getDefaultToolkit().getImage("/icons/error.png");
                return new ImageIcon("/icons/error.png");
            default:
                throw new IllegalArgumentException("Not a valid error type");
        }
    }

    public boolean setError(JComponent component, final int errorType, String message)  {
        component.setBackground(getColor(errorType));
        if(errorType == NO_ERROR){
            component.setBorder(originalBorder);
        } else {
            component.setBorder(new IconBorder(getImage(errorType),originalBorder));
        }
        component.setToolTipText(message);

        return errorType == NO_ERROR;
    }

    public boolean setError(JComponent component, final int errorType){
       return setError(component, errorType);
    }

    public boolean setError(JComponent component, boolean isError, String message) {
        int errorType  = isError ? ERROR : NO_ERROR;
        return setError(component, errorType, message);
    }

    public boolean setError(JComponent component, boolean isError) throws IOException {
        return setError(component, isError, "");
    }
}
