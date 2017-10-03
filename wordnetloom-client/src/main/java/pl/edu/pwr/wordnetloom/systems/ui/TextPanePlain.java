package pl.edu.pwr.wordnetloom.systems.ui;

import javax.swing.JTextPane;

public class TextPanePlain extends JTextPane {

	private static final long serialVersionUID = 153285040575089259L;
	private String oldValue = null;

	@Override
	public void setText(String text) {
		super.setText(text);
		oldValue = text;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return getUI().getPreferredSize(this).width <= getParent().getSize().width;
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
