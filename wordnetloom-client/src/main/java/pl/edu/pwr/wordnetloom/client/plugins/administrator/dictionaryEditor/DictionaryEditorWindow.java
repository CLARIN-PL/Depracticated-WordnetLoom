package pl.edu.pwr.wordnetloom.client.plugins.administrator.dictionaryEditor;

import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.systems.ui.MFrame;

import java.awt.*;

public class DictionaryEditorWindow extends MFrame {

    private final static int MAX_WINDOW_WIDTH = 1000;
    private final static int MAX_WINDOW_HEIGHT = 800;
    private final int MIN_WINDOW_WIDTH = 800;
    private final int MIN_WINDOW_HEIGHT = 800;

    private DictionaryListPanel dictionaryListPanel;
    private EditDictionaryPanel editDictionaryPanel;

    private DictionaryEditorWindow(WebFrame parentFrame){
        // TODO dorobić etykiety
        super(parentFrame,"Edycja słowników");
        initView();
    }

    private void initView(){
        setLayout(new BorderLayout());
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // TODO zrobić tworzenie paneli

    }

    public static void showModal(WebFrame parentFrame){
        new DictionaryEditorWindow(parentFrame).setVisible(true);
    }
}
