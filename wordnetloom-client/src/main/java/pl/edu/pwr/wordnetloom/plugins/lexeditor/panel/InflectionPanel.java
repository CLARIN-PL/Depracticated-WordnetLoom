package pl.edu.pwr.wordnetloom.plugins.lexeditor.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import pl.edu.pwr.wordnetloom.model.yiddish.Inflection;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.InflectionDictionary;
import pl.edu.pwr.wordnetloom.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;

public class InflectionPanel extends JPanel {
	private JPanel panel;
	private JScrollPane scroll;
	private JList list;
	private DefaultListModel listModel;
	private JComboBox comboBox;
	private JTextField textField;
	private JButton btnRemove;
	private JButton btnNewButton;

	public InflectionPanel() {
		setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Inflection", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(51, 51, 51)));
		setLayout(new BorderLayout(0, 0));

		listModel = new DefaultListModel();
		list = new JList(listModel);

		scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(200, 70));
		add(scroll, BorderLayout.NORTH);

		panel = new JPanel();
		add(panel);
		FormLayout fl_panel = new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(55dlu;default)"),
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(61dlu;default):grow"),
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("69px"), FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
						ColumnSpec.decode("max(39dlu;default)"), FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("25px"), });
		panel.setLayout(fl_panel);

		comboBox = new JComboBox();
		comboBox.addItem(new CustomDescription<InflectionDictionary>("Select", null));
		for (InflectionDictionary d : RemoteUtils.dictionaryRemote.findAllInflectionDictionary()) {
			comboBox.addItem(d);
		}
		panel.add(comboBox, "2, 2, fill, default");

		textField = new JTextField();
		panel.add(textField, "4, 2, fill, fill");
		textField.setColumns(10);

		btnNewButton = new JButton("Add");
		btnNewButton.setHorizontalAlignment(SwingConstants.RIGHT);
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Inflection i = new Inflection((InflectionDictionary) comboBox.getSelectedItem(), textField.getText());
				listModel.addElement(i);
			}
		});
		panel.add(btnNewButton, "6, 2, fill, fill");

		btnRemove = new JButton("Remove");
		btnRemove.setHorizontalAlignment(SwingConstants.RIGHT);
		btnRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listModel.removeElementAt(list.getSelectedIndex());
			}
		});
		panel.add(btnRemove, "8, 2");
	}

	public void setInflection(Set<Inflection> inflection) {
		for (Inflection i : inflection) {
			listModel.addElement(i);
		}
	}

	public Set<Inflection> getInflection() {
		Set<Inflection> inflection = new LinkedHashSet<Inflection>();
		for (Object i : listModel.toArray()) {
			inflection.add((Inflection) i);
		}
		;
		return inflection;
	}

	public JButton getBtnNewButton() {
		return btnNewButton;
	}

	public JButton getBtnRemove() {
		return btnRemove;
	}

}
