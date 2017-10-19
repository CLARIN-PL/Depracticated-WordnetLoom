package pl.edu.pwr.wordnetloom.plugins.lexeditor.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.*;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import pl.edu.pwr.wordnetloom.model.yiddish.YiddishSenseExtension;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.PrefixDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.SuffixDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.particle.InterfixParticle;
import pl.edu.pwr.wordnetloom.model.yiddish.particle.RootParticle;
import pl.edu.pwr.wordnetloom.model.yiddish.particle.SuffixParticle;
import pl.edu.pwr.wordnetloom.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.InterfixDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.particle.Particle;
import pl.edu.pwr.wordnetloom.model.yiddish.particle.PrefixParticle;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;

public class ParticlesPanel extends JPanel {

	private JPanel panel;
	private JComboBox particleType;
	private JComboBox particle;
	private JTextField root;
	private JList list;
	private DefaultListModel listModel;
	private JScrollPane scrollPane;
	private JButton btnRemove;
	private JButton btnNewButton;

	public ParticlesPanel(YiddishSenseExtension ex) {
		setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Particles", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(51, 51, 51)));
		setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		add(panel);
		FormLayout fl_panel = new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(38dlu;default):grow"),
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(42dlu;default):grow"),
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(47dlu;default):grow"),
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("64px"), FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("max(36dlu;default)"),
						FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("25px"), });
		panel.setLayout(fl_panel);

		scrollPane = new JScrollPane();
		panel.add(scrollPane, "2, 2, 9, 1, fill, fill");

		listModel = new DefaultListModel();
		list = new JList(listModel);

		list.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				JList l = (JList) e.getSource();
				ListModel m = l.getModel();
				int index = l.locationToIndex(e.getPoint());
				if (index > -1) {
					Particle p = (Particle) m.getElementAt(index);
					String tip = "";
					if (p instanceof SuffixParticle) {
						tip = ((SuffixParticle) p).getSuffix().getDescription();
					}
					if (p instanceof RootParticle) {
						tip = ((RootParticle) p).getRoot();
					}
					if (p instanceof PrefixParticle) {
						tip = ((PrefixParticle) p).getPrefix().getDescription();
					}
					if (p instanceof InterfixParticle) {
						tip = ((InterfixParticle) p).getInterfix().getDescription();
					}
					l.setToolTipText(tip);
				}
			}
		});

		scrollPane.setViewportView(list);

		particleType = new JComboBox();
		particleType.addItem(new CustomDescription<ParticleElementType>("Select", null));
		for (ParticleElementType d : ParticleElementType.values()) {
			particleType.addItem(d);
		}
		particleType.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				if (e.getItem() != null) {

					if ("Select".equals(e.getItem().toString())) {
						root.setVisible(false);
						particle.setVisible(false);
					}
					if (ParticleElementType.interfix == e.getItem()) {
						root.setVisible(false);
						particle.setVisible(true);
						particle.removeAllItems();
						for (InterfixDictionary p : RemoteUtils.dictionaryRemote.findAllInterfixDictionary()) {
							particle.addItem(p);
						}
					}

					if (ParticleElementType.prefix == e.getItem()) {
						root.setVisible(false);
						particle.setVisible(true);
						particle.removeAllItems();
						for (PrefixDictionary p : RemoteUtils.dictionaryRemote.findAllPrefixDictionary()) {
							particle.addItem(p);
						}
					}

					if (ParticleElementType.suffix == e.getItem()) {
						root.setVisible(false);
						particle.setVisible(true);
						for (SuffixDictionary p : RemoteUtils.dictionaryRemote.findAllSuffixDictionary()) {
							particle.addItem(p);
						}
					}

					if (ParticleElementType.root == e.getItem()) {
						root.setVisible(true);
						particle.setVisible(false);
					}
				}
			}
		});

		panel.add(particleType, "2, 4, fill, default");

		particle = new JComboBox();
		particle.setVisible(false);
		panel.add(particle, "4, 4, fill, default");

		root = new JTextField();
		root.setVisible(false);
		panel.add(root, "6, 4, fill, fill");
		root.setColumns(10);

		btnNewButton = new JButton("Add");
		btnNewButton.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(btnNewButton, "8, 4, fill, fill");

		btnRemove = new JButton("Remove");
		btnRemove.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(btnRemove, "10, 4");

		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Particle p = null;

				if (ParticleElementType.interfix == particleType.getSelectedItem()
						&& particle.getSelectedItem() instanceof InterfixDictionary) {
					p = new InterfixParticle((InterfixDictionary) particle.getSelectedItem());
					listModel.addElement(p);
				}
				if (ParticleElementType.suffix == particleType.getSelectedItem()
						&& particle.getSelectedItem() instanceof SuffixDictionary) {
					p = new SuffixParticle((SuffixDictionary) particle.getSelectedItem());
					listModel.addElement(p);
				}
				if (ParticleElementType.prefix == particleType.getSelectedItem()
						&& particle.getSelectedItem() instanceof PrefixDictionary) {
					p = new PrefixParticle((PrefixDictionary) particle.getSelectedItem());
					listModel.addElement(p);
				}
				if (ParticleElementType.root == particleType.getSelectedItem()) {
					p = new RootParticle(root.getText());
					listModel.addElement(p);
				}
				p.setExtension(ex);
				RemoteUtils.lexicalUnitRemote.saveParticle(p);
			}
		});

		btnRemove.addActionListener(e -> {
            if (list.getSelectedValue() != null) {
                RemoteUtils.lexicalUnitRemote
                        .removeParticle((Particle) listModel.getElementAt(list.getSelectedIndex()));
                listModel.removeElementAt(list.getSelectedIndex());
            }
        });
	}

	public  Set<Particle> getParticles(){
		Set<Particle> particles = new HashSet<>();
		for(int i=0; i<listModel.getSize(); i++){
			particles.add((Particle)listModel.getElementAt(i));
		}
		return particles;
	}

	public void setParticles(Set<Particle> particels) {
	    // wstawiamy do listy, aby móc je posortować. Elementy sortowane są ze względu na kolejność dodania
		List<Particle> particlesList = new ArrayList<>(particels);
		Collections.sort(particlesList, (o1, o2) -> o1.getId() > o2.getId() ? 1 : -1);
		for (Particle p : particlesList) {
			listModel.addElement(p);
		}
	}

	public JButton getBtnNewButton() {
		return btnNewButton;
	}
}
