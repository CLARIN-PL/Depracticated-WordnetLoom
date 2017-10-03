package pl.edu.pwr.wordnetloom.plugins.lexeditor.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import pl.edu.pwr.wordnetloom.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.model.yiddish.Transcription;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.TranscriptionDictionary;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;

public class TranscriptionPanel extends JPanel {
	private JTextField textField;
	private JPanel panel;
	private JComboBox comboBox;
	private JList list;
	private DefaultListModel listModel;
	private JScrollPane scrollPane;
	private JButton btnNew;
	private JButton btnRemoveButton;

	public TranscriptionPanel() {
		setBorder(new TitledBorder(null, "Transcription", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		add(panel);
		FormLayout fl_panel = new FormLayout(
				new ColumnSpec[] { ColumnSpec.decode("29px:grow"), ColumnSpec.decode("137px:grow"),
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(25dlu;default)"),
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("93px"), },
				new RowSpec[] { FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("25px"), });
		panel.setLayout(fl_panel);

		comboBox = new JComboBox();
		comboBox.addItem(new CustomDescription<TranscriptionDictionary>("Select", null));
		for (TranscriptionDictionary d : RemoteUtils.dictionaryRemote.findAllTranscriptionsDictionary()) {
			comboBox.addItem(d);
		}

		scrollPane = new JScrollPane();
		panel.add(scrollPane, "1, 2, 6, 1, fill, fill");

		listModel = new DefaultListModel();
		list = new JList(listModel);

		scrollPane.setViewportView(list);
		comboBox.setMaximumRowCount(25);
		panel.add(comboBox, "1, 4, fill, fill");

		textField = new JTextField();
		panel.add(textField, "2, 4, fill, fill");
		textField.setColumns(20);

		btnNew = new JButton("Add");
		btnNew.setHorizontalAlignment(SwingConstants.RIGHT);
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Transcription t = new Transcription((TranscriptionDictionary) comboBox.getSelectedItem(),
						textField.getText());
				listModel.addElement(t);
			}
		});
		panel.add(btnNew, "4, 4");

		btnRemoveButton = new JButton("Remove");
		btnRemoveButton.setHorizontalAlignment(SwingConstants.RIGHT);
		btnRemoveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listModel.removeElementAt(list.getSelectedIndex());
			}
		});
		panel.add(btnRemoveButton, "6, 4, fill, fill");
	}

	public void setTranscritptions(Set<Transcription> transcriptions) {
		for (Transcription t : transcriptions) {
			listModel.addElement(t);
		}

	}

	public Set<Transcription> getTranscritptions() {
		Set<Transcription> trans = new LinkedHashSet<Transcription>();
		for (Object t : listModel.toArray()) {
			trans.add((Transcription) t);
		}
		;
		return trans;
	}

	public JButton getBtnNew() {
		return btnNew;
	}

	public JButton getBtnRemove() {
		return btnRemoveButton;
	}

}
