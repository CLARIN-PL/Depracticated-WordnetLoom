package pl.edu.pwr.wordnetloom.client.plugins.lexicon.window;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.errors.ValidationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.sense.dto.SenseCriteriaDTO;
import pl.edu.pwr.wordnetloom.synset.dto.SynsetCriteriaDTO;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class LexiconManagerWindow extends MFrame implements ActionListener {

    public final static int MIN_WINDOW_WIDTH = 600;
    public final static int MIN_WINDOW_HEIGHT = 680;
    public final static int MAX_WINDOW_WIDTH = 600;
    public final static int MAX_WINDOW_HEIGHT = 680;

    private LexiconListPanel lexiconListPanel;
    private LexiconPropertiesPanel lexiconPropertiesPanel;
    private MButton saveButton;
    private MButton cancelButton;

    public LexiconManagerWindow(WebFrame parentFrame) {
        // TODO dodać etykietę
        super(parentFrame, "Zarządzanie leksykonami",MAX_WINDOW_WIDTH, MAX_WINDOW_HEIGHT);
        initView();
    }

    private void initView() {
        setLayout(new RiverLayout());
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));

        lexiconListPanel = new LexiconListPanel(MAX_WINDOW_WIDTH, MAX_WINDOW_HEIGHT / 2,
                lexicon -> lexiconPropertiesPanel.setLexicon(lexicon));
        lexiconPropertiesPanel = new LexiconPropertiesPanel(MAX_WINDOW_WIDTH, MAX_WINDOW_HEIGHT/2);
        saveButton = MButton.buildSaveButton().withActionListener(e->{
            saveLexicon();
        });
        cancelButton = MButton.buildCancelButton().withActionListener(e->{
            // load old lexicon/ clear changes
            Lexicon lexicon = lexiconListPanel.getSelectedLexicon();
            lexiconPropertiesPanel.setLexicon(lexicon);
        });
        add(RiverLayout.HFILL + " " + RiverLayout.VFILL,lexiconListPanel); //TODO ustawić pozycję
        add(RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL, lexiconPropertiesPanel);
        add(RiverLayout.LINE_BREAK + " " + RiverLayout.CENTER, saveButton);
        add(cancelButton);

    }

    private void saveLexicon() {
        Lexicon lexicon = lexiconPropertiesPanel.getLexicon();
        if(lexicon != null){
            Lexicon savedLexicon = RemoteService.lexiconServiceRemote.add(lexicon);
            java.util.List<Lexicon> lexicons = RemoteService.lexiconServiceRemote.findAll();
            LexiconManager.getInstance().load(lexicons);
            lexiconListPanel.refreshAndSelect(savedLexicon);

        }
    }

    public static void showModal(WebFrame parentFrame) {
        LexiconManagerWindow frame = new LexiconManagerWindow(parentFrame);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public interface LexiconLoadListener {
        void load(Lexicon lexicon);
    }

    private class LexiconListPanel extends WebPanel {

        private JList lexiconsList;
        private DefaultListModel model;
        private MComponentGroup buttonsPanel;

        private int width;
        private int height;

        private boolean editMode;
        LexiconLoadListener clickListener;

        public LexiconListPanel(int width, int height,@NotNull LexiconLoadListener listener) {
            this.width = width;
            this.height= height;
            this.clickListener = listener;
            init();
        }

        private void init() {
            this.setLayout(new BorderLayout());
            this.setSize(new Dimension(width, height));
            JScrollPane listScroll = new JScrollPane(createLexiconsList());
            this.add(listScroll, BorderLayout.CENTER);
            this.add(createButtonPanel(), BorderLayout.EAST);
        }

        private JList createLexiconsList() {
            lexiconsList = new JList();
            model = new DefaultListModel();
            refreshList();

            lexiconsList.addListSelectionListener(e->{
                if(clickListener != null){
                    Lexicon selectedLexicon = (Lexicon)lexiconsList.getSelectedValue();
                    if(selectedLexicon != null){
                        clickListener.load(selectedLexicon);
                    }
                }
            });

            return lexiconsList;
        }

        public Lexicon getSelectedLexicon(){
            return (Lexicon)lexiconsList.getSelectedValue();
        }

        // TODO wstawić etykiety
        private MComponentGroup createButtonPanel() {
            MButton addButton = MButton.buildAddButton()
                    .withToolTip("Utwórz nowy leksykon")
                    .withActionListener(e -> addLexicon());
            MButton removeButton = MButton.buildRemoveButton()
                    .withToolTip("Usuń zaznaczony leksykon")
                    .withActionListener(e -> removeLexicon((Lexicon)lexiconsList.getSelectedValue()));
            buttonsPanel = new MComponentGroup(addButton, removeButton)
                    .withVerticalLayout()
                    .withAllComponentsEnabled(true)
                    .withMargin(10);

            return buttonsPanel;

        }

        private void refreshList(){
            lexiconsList.clearSelection();
            model.clear();
            java.util.List<Lexicon> lexicons = LexiconManager.getInstance().getLexicons();

            for (Lexicon lexicon : lexicons) {
                model.addElement(lexicon);
            }
            lexiconsList.setModel(model);
        }

        private void refreshAndSelect(Lexicon lexicon){
            refreshList();
            lexiconsList.setSelectedValue(lexicon, true);
        }

        private void addLexicon() {
            // add new item to lexicons list
            Lexicon lexicon = new Lexicon();
            lexicon.setName(""); // TODO można też wstawić nazwę "nowy"
            clickListener.load(lexicon);

        }

        private void removeLexicon(Lexicon lexicon) {
            // TODO dodać etykietę
            int senseCount = getSenseCount(lexicon);
            int synsetCount = getSynsetCount(lexicon);
            int answer = DialogBox.showYesNo("Czy usunąć ten leksykon?\n" +
                    "Liczba jednostek : " + senseCount + "\nLiczba synsetów : " + synsetCount);
            if(answer == DialogBox.YES){
                System.out.println("Usunięcie leksykonu");
                //TODO zrobić usuwanie leksykonu
            }
        }

        private int getSenseCount(Lexicon lexicon){
            ArrayList<Long> lexiconList = new ArrayList<>();
            lexiconList.add(lexicon.getId());
            SenseCriteriaDTO senseDTO = new SenseCriteriaDTO();
            senseDTO.setLexicons(lexiconList);
            return RemoteService.senseRemote.getCountUnitsByCriteria(senseDTO);
        }

        private int getSynsetCount(Lexicon lexicon){
            SynsetCriteriaDTO synsetDTO = new SynsetCriteriaDTO();
            synsetDTO.setLexiconId(lexicon.getId());
            return RemoteService.synsetRemote.getCountSynsetsByCriteria(synsetDTO);
        }
    }

    private class LexiconPropertiesPanel extends WebPanel {

        private Lexicon lexicon;
        private final MTextField name = new MTextField("");
        private final MTextField identifier = new MTextField("");
        private final MTextField languageName = new MTextField("");
        private final MTextField languageShorcut = new MTextField("");
        private final MTextField version = new MTextField("");
        private final MTextField licence = new MTextField("");
        private final MTextField email = new MTextField("");

        private int width;
        private int height;

        private ValidationManager validationManager;

        public LexiconPropertiesPanel(int width, int height) {
            this.width = width;
            this.height = height;
            initView();
            initValidation();
        }

        private void initValidation(){
            validationManager = new ValidationManager();
            // TODO dodać etykiety do bazy danych
            validationManager.registerError(name, "Pole nie może być puste", ()->name.getText().isEmpty());
            validationManager.registerError(name, "Nazwa jest już zajęta", ()->checkNameIsUsed(name.getText()));
            validationManager.registerError(identifier, "Pole nie może być puste", ()->identifier.getText().isEmpty());
            validationManager.registerError(languageName, "Pole nie może być puste", ()->languageName.getText().isEmpty());
            validationManager.registerError(languageShorcut, "Pole nie może być puste", ()->languageShorcut.getText().isEmpty());
        }

        private void initView(){
            setLayout(new RiverLayout());
            setSize(new Dimension(width, height));
            // TODO dodać etykiety do bazy
            Map<String, Component> components = new LinkedHashMap<>();
            components.put("Nazwa", name);
            components.put("Identyfikator", identifier);
            components.put("Język", languageName);
            components.put("Skrót języka", languageShorcut);
            components.put("Wersja", version);
            components.put("Licencja", licence);
            components.put("Email", email);

            add("", GroupView.createGroupView(components, new Dimension(width - 20, height- 20), 0.1f, 0.9f));
        }

        public void setLexicon(Lexicon lexicon) {
            this.lexicon = lexicon;
            name.setText(lexicon.getName());
            identifier.setText(lexicon.getIdentifier());
            languageName.setText(lexicon.getLanguageName());
            languageShorcut.setText(lexicon.getLanguageShortcut());
            version.setText(lexicon.getLexiconVersion());
            licence.setText(lexicon.getLicense());
            email.setText(lexicon.getEmail());
        }

        public Lexicon getLexicon() {
            if(!validateLexicon()) {
                return null;
            }
            if(lexicon == null){
                lexicon = new Lexicon();
            }
            lexicon.setName(name.getText());
            lexicon.setIdentifier(identifier.getText());
            lexicon.setLanguageName(languageName.getText());
            lexicon.setLanguageShortcut(languageShorcut.getText());
            lexicon.setLexiconVersion(version.getText());
            lexicon.setLicense(licence.getText());
            lexicon.setEmail(email.getText());

            return lexicon;
        }

        private boolean validateLexicon() {
            return validationManager.validate();
        }

        private boolean checkNameIsUsed(String lexiconName) {
            // TODO poprawić komunikat. Przy pustym polu wyswietla komunikat, nazwa jest już zajęta
            java.util.List<Lexicon> lexicons = LexiconManager.getInstance().getLexicons();
            for(Lexicon lex : lexicons) {
                if((lexicon == null || lexicon.getId() == null || !lexicon.getId().equals(lex.getId()))
                        && lex.getName().equals(lexiconName)){
                    return true;
                }
            }
            return false;
        }
    }
}
