package pl.edu.pwr.wordnetloom.client.systems.errors;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

public class ErrorManager {
    public interface ErrorCondition {
        boolean checkError();
    }

    private final Color WARNING_BORDER_COLOR = Color.ORANGE;
    private final Color ERROR_BORDER_COLOR = Color.RED;

    private final IconBorder ERROR_BORDER;

    public static final int NO_ERROR = 0;
    public static final int WARNING = 1;
    public static final int ERROR = 2;

    private final String ERROR_IMAGE_PATH = "/icons/pl.png";

    private final ImageIcon ERROR_IMAGE = new ImageIcon(getClass().getResource(ERROR_IMAGE_PATH));
    private java.util.List<ComponentInfo> errorComponents;

    public ErrorManager(){
        errorComponents = new ArrayList<>();
        ERROR_BORDER = new IconBorder(ERROR_BORDER_COLOR,ERROR_IMAGE);
    }

    /**
     * Add component to error manager.
     * @param component component
     * @param message information about error or warning, this text will be show, when validation of field failed
     * @param condition condition, when field is fill incorrectly
     */
    public void registerError(JComponent component, String message, ErrorCondition condition){
        ComponentInfo componentInfo = new ComponentInfo(component, condition, message);
        errorComponents.add(componentInfo);
    }

    /**
     * Check correctness of fields and mark incorrectly filled
     * @return result of registered fields validation
     */
    public boolean checkErrors(){
        boolean result = true;
        for (ComponentInfo info : errorComponents) {
            if (info.checkError()) {
                setError(info);
                result = false;
            } else {
                clearError(info);
            }
        }
        return result;
    }

    private void setError(ComponentInfo info){
        JComponent component  = info.getComponent();
        component.setBorder(ERROR_BORDER);
        component.setToolTipText(info.getMessage());

        info.setState(false);
    }

    private void clearError(ComponentInfo info){
        JComponent component = info.getComponent();
        component.setBorder(info.getBorder());
        component.setBackground(info.getColor());
        component.setToolTipText(info.getOriginalTooltip());

        info.setState(true);
    }

    public void clearErrors(){
        errorComponents.forEach(this::clearError);
    }

    private class ComponentInfo {
        private JComponent component;
        private Border originalBorder;
        private String originalTooltip;
        private Color originalBackgroundColor;
        private String message;
        private ErrorCondition condition;
        private boolean state;


        public ComponentInfo(JComponent component, ErrorCondition errorCondition, String message){
            this.component = component;
            originalBackgroundColor = component.getBackground();
            originalBorder = component.getBorder();
            originalTooltip = component.getToolTipText();
            this.message = message;
            this.condition = errorCondition;

            addListener(component);
        }

        /**
         * Add listener which checking action and delete error maker
         * @param component
         */
        private void addListener(JComponent component){
            if(component instanceof JTextField){
                addTextFieldListener((JTextField)component);
            } else if(component instanceof JComboBox){
                addComboBoxListener((JComboBox)component);
            }
        }

        private void addTextFieldListener(JTextField component){
            (component).getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    checkAndClear();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    checkAndClear();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    checkAndClear();
                }
            });
        }

        private void addComboBoxListener(JComboBox component){
            component.addActionListener(e -> checkAndClear());
        }

        private void checkAndClear(){
            if(!state && !checkError()){
                clearError(this);
            }
        }

        public void setState(boolean state){
            this.state = state;
        }

        public JComponent getComponent(){
            return component;
        }

        public Border getBorder(){
            return originalBorder;
        }

        public Color getColor(){
            return originalBackgroundColor;
        }

        public String getMessage(){
            return message;
        }

        public boolean checkError(){
            return condition.checkError();
        }

        public String getOriginalTooltip(){
            return originalTooltip;
        }
    }
}
