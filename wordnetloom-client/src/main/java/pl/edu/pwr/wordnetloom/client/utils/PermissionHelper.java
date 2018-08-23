package pl.edu.pwr.wordnetloom.client.utils;

import pl.edu.pwr.wordnetloom.client.security.UserSessionContext;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.user.model.Role;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.EJBAccessException;
import javax.swing.*;

public class PermissionHelper {

    public interface AccessExceptionHandler{
        void handle();
    }

    private static String getPermissionDeniedText(){
        // TODO dorobić etykietę
        final String PERMISSION_DENIED = "Nie posiadasz uprawnień do wykonania tej akcji";
        return PERMISSION_DENIED;
    }

    public static void handle(AccessExceptionHandler handler){
        try{
            handler.handle();
        } catch (EJBAccessException exception){
            // TODO dorobić etykietę
            DialogBox.showInformation("Nie masz uprawnień do wykonania akcji");
            exception.printStackTrace();
        }
    }

    public static void addNonPermissionListener(JComponent component) {
        throw new NotImplementedException();
    }

    private static void disableComponent(JComponent component){
        component.setEnabled(false);
        if(component instanceof JButton) {
            String tooltipText = component.getToolTipText();
            String newTooltipText = null;
            if(tooltipText == null){
                newTooltipText = getPermissionDeniedText();
            } else {
                newTooltipText = tooltipText + ". " + getPermissionDeniedText();
            }
            component.setToolTipText(newTooltipText);
        }
    }

    private static void enableComponent(JComponent component){
        component.setEnabled(true);
        if(component instanceof JButton){
            String tooltipText = component.getToolTipText();
            if(tooltipText != null && tooltipText.contains(getPermissionDeniedText())){
                String newTooltipText = tooltipText.replace(getPermissionDeniedText(), "");
                component.setToolTipText(newTooltipText);
            }
        }
    }

    private static void setEnableComponents(boolean enabled, JComponent... components){
        for(JComponent component : components){
            if(enabled) {
                enableComponent(component);
            } else {
                disableComponent(component);
            }
        }
    }

    public static boolean checkPermissionToEditAndSetComponents(JComponent... components){
        boolean permissionToEdit = hasPermissionToEdit();
        setEnableComponents(permissionToEdit, components);
        return permissionToEdit;
    }

    public static boolean checkPermissionToEditAndSetComponents(Sense unit, JComponent... components){
        boolean permissionToEdit = hasPermissionToEdit(unit);
        setEnableComponents(permissionToEdit, components);
        return permissionToEdit;
    }

    public static boolean checkPermissionToEditAndSetComponents(Synset synset, JComponent... components){
        boolean permissionToEdit = hasPermissionToEdit(synset.getLexicon());
        setEnableComponents(permissionToEdit, components);
        return permissionToEdit;
    }

    public static void checkPermissionToEditAndSetComponents(AbstractAction... actions){
        if(!hasPermissionToEdit()){
            for(AbstractAction action : actions){
                action.setEnabled(false);
            }
        }
    }

    public static boolean checkPermissionToEditAndSetComponents(Synset synset, AbstractAction... actions){
        boolean permissionToEdit = hasPermissionToEdit(synset.getLexicon());
        for(AbstractAction action : actions){
            action.setEnabled(permissionToEdit);
        }
        return permissionToEdit;
    }

    private static boolean hasPermissionToEdit(){
        return !UserSessionContext.getInstance().hasRole(Role.ANONYMOUS);
    }

    public static boolean hasPermissionToEdit(Sense unit){
        assert unit!= null;
        return hasPermissionToEdit(unit.getLexicon());
    }

    private static boolean hasPermissionToEdit(Lexicon lexicon) {
        if(lexicon != null && lexicon.isOnlyToRead()){
            return UserSessionContext.getInstance().hasRole(Role.ADMIN);
        }
        return hasPermissionToEdit();
    }

    public static boolean isAdministrator() {
        return UserSessionContext.getInstance().hasRole(Role.ADMIN);
    }
}
