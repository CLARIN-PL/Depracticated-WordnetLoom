package pl.edu.pwr.wordnetloom.client.utils;

import pl.edu.pwr.wordnetloom.client.security.UserSessionContext;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.user.model.Role;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.EJBAccessException;
import javax.swing.*;

public class PermissionHelper {

    public interface AccessExceptionHandler{
        void handle();
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
        final String PERMISSION_DENIED = "Nie posiadasz uprawnień do wykonania taj akcji";
        component.setEnabled(false);
        if(component instanceof JButton) {
            String tooltipText = component.getToolTipText();
            String newTooltipText = null;
            if(tooltipText == null){
                newTooltipText = PERMISSION_DENIED;
            } else {
                newTooltipText = tooltipText + ". " + PERMISSION_DENIED;
            }
            component.setToolTipText(newTooltipText);
        }
    }

    private static void disableComponents(JComponent... components){
        for(JComponent component : components){
            disableComponent(component);
        }
    }

    public static boolean checkPermissionToEditAndSetComponents(JComponent... components){
        boolean permissionToEdit = hasPermissionToEdit();
        if(!permissionToEdit){
            disableComponents(components);
        }
        return permissionToEdit;
    }

    public static void checkPermissionToEditAndSetComponents(AbstractAction... actions){
        if(!hasPermissionToEdit()){
            for(AbstractAction action : actions){
                action.setEnabled(false);
            }
        }
    }

    private static boolean hasPermissionToEdit(){
        return !UserSessionContext.getInstance().hasRole(Role.ANONYMOUS);
    }

    private static boolean hasPermissionToEdit(Lexicon lexicon){

        if(lexicon != null && lexicon.isOnlyToRead()){
            return UserSessionContext.getInstance().hasRole(Role.ADMIN);
        }
        return !UserSessionContext.getInstance().hasRole(Role.ANONYMOUS);
    }
}
