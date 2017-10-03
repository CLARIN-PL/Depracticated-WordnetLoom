package pl.edu.pwr.wordnetloom.plugins.dictionary.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import pl.edu.pwr.wordnetloom.systems.ui.IconDialog;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.Dictionary;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;

public class DictionaryItemEditorWindow extends IconDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton btnOk;
	private JButton btnCancel;
	private Dictionary dictionary;
	private JTextField txtName;
	private JTextPane txtDesc;

	public DictionaryItemEditorWindow(JFrame parent, String title, Dictionary dictionary) {
		super(parent, title, 700, 400);
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(49dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(54dlu;default):grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(78dlu;default):grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(27dlu;default)"),},
			new RowSpec[] {
				RowSpec.decode("max(9dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(17dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(147dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(23dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(14dlu;default)"),}));

		JLabel lblV = new JLabel("Value:");
		getContentPane().add(lblV, "3, 3, right, default");

		txtName = new JTextField();
		getContentPane().add(txtName, "5, 3, 3, 1, fill, fill");
		txtName.setColumns(15);

		JLabel lblNewLabel = new JLabel("Descritption:");
		getContentPane().add(lblNewLabel, "3, 5, right, default");

		JScrollPane pane = new JScrollPane();
		txtDesc = new JTextPane();
		pane.setViewportView(txtDesc);
		getContentPane().add(pane, "5, 5, 3, 2, fill, fill");

		btnOk = new JButton("Ok");
		btnOk.addActionListener(l -> {
			RemoteUtils.dictionaryRemote.saveOrUpdate(getDictionary());
			this.dispose();
		});
		getContentPane().add(btnOk, "5, 7");

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(l -> this.dispose());
		getContentPane().add(btnCancel, "7, 7");

		setDictionary(dictionary);
	}

	static public Dictionary showModal(JFrame parent, String title, Dictionary dictionary) {
		DictionaryItemEditorWindow frame = new DictionaryItemEditorWindow(parent, title, dictionary);
		frame.setVisible(true);
		frame.dispose();
		return frame.getDictionary();
	}

	private void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
		txtName.setText(dictionary.getName());
		txtDesc.setText(dictionary.getDescription());
	}

	private Dictionary getDictionary() {
		dictionary.setName(txtName.getText());
		dictionary.setDescription(txtDesc.getText());
		return dictionary;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
	}

}
