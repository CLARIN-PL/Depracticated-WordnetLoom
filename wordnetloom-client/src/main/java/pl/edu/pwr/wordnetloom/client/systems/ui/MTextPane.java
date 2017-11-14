package pl.edu.pwr.wordnetloom.client.systems.ui;

import com.alee.laf.text.WebTextPane;

public class MTextPane extends WebTextPane {

    private static final long serialVersionUID = 153285040575089259L;
    private String oldValue = null;

    @Override
    public void setText(String text) {
        super.setText(text);
        oldValue = text;
    }

    public boolean wasTextChanged() {
        String value = getText();
        if (oldValue == null && value == null) {
            return false;
        }
        if (oldValue == null && value != null) {
            return true;
        }
        if (oldValue != null && value == null) {
            return true;
        }
        return !oldValue.equals(value);
    }
}
