package pl.edu.pwr.wordnetloom.plugins.dictionary.frame;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import pl.edu.pwr.wordnetloom.systems.ui.IconDialog;
import pl.edu.pwr.wordnetloom.plugins.dictionary.panel.DictionaryPanel;

public class DictionaryFrame extends IconDialog {

	private static final long serialVersionUID = 1L;

	private DictionaryPanel panel;

	public DictionaryFrame(JFrame baseFrame, String title) {
		super(baseFrame, title);
		initPanel(baseFrame);
		initAndCalculateWindowSize();
		pack();
	}

	private void initPanel(JFrame frm) {
		panel = new DictionaryPanel(frm);
		this.add("br center", panel);
	}

	private void initAndCalculateWindowSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 700;
		int height = 400;
		int x = (screenSize.width - width) / 2;
		int y = (screenSize.height - height) / 2;
		this.setBounds(x, y, width, height);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public String showModal() {
		this.setVisible(true);
		this.dispose();
		return "";
	}

}
