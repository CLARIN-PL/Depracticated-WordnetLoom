package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tabbedpane.WebTabbedPane;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.ExampleFrame;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.DictionaryManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.client.systems.renderers.DomainRenderer;
import pl.edu.pwr.wordnetloom.client.systems.renderers.ExampleCellRenderer;
import pl.edu.pwr.wordnetloom.client.systems.renderers.PartOfSpeechRenderer;
import pl.edu.pwr.wordnetloom.client.systems.renderers.RegisterRenderer;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.PermissionHelper;
import pl.edu.pwr.wordnetloom.dictionary.model.Register;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.sense.model.SenseExample;
import se.datadosen.component.RiverLayout;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class LexicalUnitPropertiesPanel extends JPanel {

    private final String DEFAULT_VARIANT = "1";

    private Sense unit;

    private JTextField lemmaTextField;
    private JTextField variantTextField;
    private LexiconComboBox lexiconComboBox;
    private JComboBox partOfSpeechComboBox;
    private JComboBox domainComboBox;
    private JComboBox registerComboBox;
    private JTextArea definitionArea;
    private JScrollPane definitionScroll;
    private JTextArea commentArea;
    private JScrollPane commentScroll;
    private JList examplesList;
    private DefaultListModel examplesModel;
    private JTextField linkTextField;

    private JButton addExampleButton;
    private JButton removeExampleButton;
    private JButton editExampleButton;
    private JButton goToLinkButton;

    private boolean permissionToEdit = false;

    private int width;
    private int height;

    public LexicalUnitPropertiesPanel(WebFrame frame/*, int width, int height*/) {
//        this.width = width;
//        this.height = height;
        initComponents();
    }

    private void initComponents() {
        lemmaTextField = new JTextField(Labels.VALUE_UNKNOWN);
        variantTextField = new JTextField(DEFAULT_VARIANT);
        lexiconComboBox = new LexiconComboBox(Labels.NOT_CHOSEN);
        partOfSpeechComboBox = createPartOfSpeechComboBox();
        domainComboBox = createDomainComboBox();
        registerComboBox = createRegisterComboBox();
        definitionArea = new JTextArea();
        definitionScroll = new JScrollPane(definitionArea);
        commentArea = new JTextArea();
        commentScroll = new JScrollPane(commentArea);
        examplesList = new JList();
    }

    private JComboBox createPartOfSpeechComboBox() {
        List<PartOfSpeech> partOfSpeechList = PartOfSpeechManager.getInstance().getAll();
        return createComboBox(partOfSpeechList, new PartOfSpeechRenderer());
    }

    private JComboBox createDomainComboBox() {
        List<Domain> domains = DomainManager.getInstance().getAllDomains();
        return createComboBox(domains, new DomainRenderer());
    }

    private JComboBox createRegisterComboBox() {
        List<Register> registerList = (List<Register>) RemoteService.dictionaryServiceRemote.findDictionaryByClass(Register.class);
        return createComboBox(registerList, new RegisterRenderer());
    }

    private JComboBox createComboBox(List<?> list, ListCellRenderer renderer) {
        JComboBox comboBox = new JComboBox();
        comboBox.setRenderer(renderer);

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(null);
        for(Object object : list){
            model.addElement(list);
        }
        comboBox.setModel(model);
        return comboBox;
    }

    private JList createExamplesList() {
        JList list = new JList();

        return list;
    }

    public void setSense(Sense unit) {
        throw new NotImplementedException();
    }

    public void save() {
        throw new NotImplementedException();
    }
}


