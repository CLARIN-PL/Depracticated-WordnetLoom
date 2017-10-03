package pl.edu.pwr.wordnetloom.plugins.lexeditor.panel;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import pl.edu.pwr.wordnetloom.utils.Labels;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class ExamplePanel extends JPanel {

	private static final long serialVersionUID = -7961855308764934738L;
	private JTextArea exampleTextArea;
	private JButton btnSave;
	private JButton btnCancel;

	public ExamplePanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("450px"),},
			new RowSpec[] {
					FormSpecs.RELATED_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:max(65dlu;default):grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel = new JLabel(Labels.USE_CASE_COLON);
		add(lblNewLabel, "1, 2, fill, fill");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "1, 4, fill, fill");
		
		exampleTextArea = new JTextArea();
		exampleTextArea.setLineWrap(true);
		scrollPane.setViewportView(exampleTextArea);
		
		Box horizontalBox = Box.createHorizontalBox();
		add(horizontalBox, "1, 6, right, default");
		
		btnSave = new JButton(Labels.ADD);
		btnSave.setHorizontalAlignment(SwingConstants.RIGHT);
		horizontalBox.add(btnSave);
		
		Component horizontalStrut = Box.createHorizontalStrut(10);
		horizontalBox.add(horizontalStrut);
		
		btnCancel = new JButton(Labels.CANCEL);
		btnCancel.setHorizontalAlignment(SwingConstants.RIGHT);
		horizontalBox.add(btnCancel);

	}
	public JTextArea getExampleTextArea() {
		return exampleTextArea;
	}
	public JButton getBtnSave() {
		return btnSave;
	}
	public JButton getBtnCancel() {
		return btnCancel;
	}
}
