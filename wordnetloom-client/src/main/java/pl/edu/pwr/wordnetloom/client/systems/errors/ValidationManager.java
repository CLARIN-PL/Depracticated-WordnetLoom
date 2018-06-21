package pl.edu.pwr.wordnetloom.client.systems.errors;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ValidationManager {
    private final int NO_ERROR = 0;
    private final int WARNING = 1;
    private final int ERROR = 2;

    private final Color WARNING_BORDER_COLOR = Color.ORANGE;
    private final Color ERROR_BORDER_COLOR = Color.RED;
    private final IconBorder ERROR_BORDER;
    private final String ERROR_IMAGE_PATH = "/icons/pl.png";
    private final ImageIcon ERROR_IMAGE = new ImageIcon(getClass().getResource(ERROR_IMAGE_PATH));
    private java.util.List<ComponentInfo> errorComponents;
    public ValidationManager() {
        errorComponents = new ArrayList<>();
        ERROR_BORDER = new IconBorder(ERROR_BORDER_COLOR, ERROR_IMAGE);
    }

    /**
     * Add component to error manager.
     *
     * @param component component
     * @param message   information about error or warning, this text will be show, when validation of field failed
     * @param condition condition specifying when the field is filled incorrectly
     */
    public void registerError(JComponent component, String message, ValidationCondition condition) {
        ComponentInfo componentInfo = new ComponentInfo(component, condition, message);
        errorComponents.add(componentInfo);
    }

    /**
     * Check correctness of fields and mark incorrectly filled
     *
     * @return result of registered fields validation
     */
    public boolean validate() {
        boolean result = true;
        // map is useful, when we have many condition signed to one component
        // we remeber state of all components in this validation
        // when first condition of component is true(error) and second condition is false (no error)
        // we not clearing error marker
        Map<JComponent, Integer> componentStates = new HashMap<>();
        for (ComponentInfo info : errorComponents) {
            if (!componentStates.containsKey(info.getComponent())) {
                componentStates.put(info.getComponent(), NO_ERROR);
            }
            if (info.checkError()) {
                setError(info);
                result = false;
                componentStates.put(info.getComponent(), ERROR);
            } else if (componentStates.get(info.getComponent()) != ERROR) {
                clearError(info);
            }
        }
        return result;
    }

    private void setError(ComponentInfo info) {
        JComponent component = info.getComponent();
        component.setBorder(ERROR_BORDER);
        component.setToolTipText(info.getMessage());

        info.setState(false);
    }

    private void clearError(ComponentInfo info) {
        JComponent component = info.getComponent();
        component.setBorder(info.getBorder());
        component.setBackground(info.getColor());
        component.setToolTipText(info.getOriginalTooltip());

        info.setState(true);
    }

    public void clearErrors() {
        errorComponents.forEach(this::clearError);
    }

    public interface ValidationCondition {
        boolean validate();
    }

    private class ComponentInfo {
        private JComponent component;
        private Border originalBorder;
        private String originalTooltip;
        private Color originalBackgroundColor;
        private String message;
        private ValidationCondition condition;
        private boolean state;

        ComponentInfo(JComponent component, ValidationCondition validationCondition, String message) {
            this.component = component;
            originalBackgroundColor = component.getBackground();
            originalBorder = component.getBorder();
            originalTooltip = component.getToolTipText();
            this.message = message;
            this.condition = validationCondition;

            addListener(component);
        }

        /**
         * Add listener which checking action and delete error marker
         *
         * @param component
         */
        private void addListener(JComponent component) {
            if (component instanceof JTextField) {
                addTextFieldListener((JTextField) component);
            } else if (component instanceof JComboBox) {
                addComboBoxListener((JComboBox) component);
            }
        }

        private void addTextFieldListener(JTextField component) {
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

        private void addComboBoxListener(JComboBox component) {
            component.addActionListener(e -> checkAndClear());
        }

        private void checkAndClear() {
            if (!state && !checkError()) {
                clearError(this);
            }
        }

        void setState(boolean state) {
            this.state = state;
        }

        public JComponent getComponent() {
            return component;
        }

        Border getBorder() {
            return originalBorder;
        }

        public Color getColor() {
            return originalBackgroundColor;
        }

        String getMessage() {
            return message;
        }

        boolean checkError() {
            return condition.validate();
        }

        String getOriginalTooltip() {
            return originalTooltip;
        }
    }
}
