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
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.SourceDictionary;
import pl.edu.pwr.wordnetloom.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;

public class SourcePanel extends JPanel {

	private JPanel panel;
	private JScrollPane scroll;
	private DefaultListModel listModel = new DefaultListModel();
	private JList list = new JList(listModel);
	private JComboBox source;
	private JButton btnRemove;
	private JButton btnNewButton;

	public void setSource(Set<SourceDictionary> src) {
		for (SourceDictionary sd : src) {
			listModel.addElement(sd);
		}
	}

	public Set<SourceDictionary> getSource() {
		Set<SourceDictionary> src = new LinkedHashSet<SourceDictionary>();
		for (Object d : listModel.toArray()) {
			src.add((SourceDictionary) d);
		}
		return src;
	}

	public SourcePanel() {
		setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Source", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(51, 51, 51)));
		setLayout(new BorderLayout(0, 0));

		scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(200, 50));
		add(scroll, BorderLayout.NORTH);

		panel = new JPanel();
		add(panel);
		FormLayout fl_panel = new FormLayout(
				new ColumnSpec[] { ColumnSpec.decode("194px"), FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("max(47dlu;default):grow"), FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("25px"), });
		panel.setLayout(fl_panel);

		source = new JComboBox();
		source.addItem(new CustomDescription<SourceDictionary>("Select", null));
		for (SourceDictionary d : RemoteUtils.dictionaryRemote.findAllSourceDictionary()) {
			source.addItem(d);
		}
		panel.add(source, "1, 2, fill, default");

		btnNewButton = new JButton("Add");
		btnNewButton.setHorizontalAlignment(SwingConstants.RIGHT);
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SourceDictionary m;
				if (source.getSelectedItem() instanceof CustomDescription) {
					m = null;
				} else {
					m = (SourceDictionary) source.getSelectedItem();
				}
				listModel.addElement(m);
			}
		});

		panel.add(btnNewButton, "5, 2, fill, fill");

		btnRemove = new JButton("Remove");
		btnRemove.setHorizontalAlignment(SwingConstants.RIGHT);
		btnRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listModel.removeElementAt(list.getSelectedIndex());
			}
		});
		panel.add(btnRemove, "7, 2");
	}

	public JButton getBtnNewButton() {
		return btnNewButton;
	}
}
