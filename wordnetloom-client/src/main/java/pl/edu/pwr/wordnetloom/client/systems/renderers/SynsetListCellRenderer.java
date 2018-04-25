package pl.edu.pwr.wordnetloom.client.systems.renderers;

import com.alee.laf.label.WebLabel;
import com.alee.laf.scroll.WebScrollPane;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators.SynsetFormat;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

import javax.swing.*;
import java.awt.*;

public class SynsetListCellRenderer extends WebLabel implements ListCellRenderer{

    private final String FONT_NAME = "Courier New";
    private final int FONT_SIZE = 14;
    private final Font FONT = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);

    private WebScrollPane scrollPane;

    public SynsetListCellRenderer(){
        this.setFont(FONT);
    }

    public SynsetListCellRenderer(WebScrollPane scrollPane){
        this();
        this.scrollPane = scrollPane;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if(scrollPane != null){
            setText(SynsetFormat.getHtmlText((Synset)value, scrollPane.getWidth()));
        } else {
            setText(SynsetFormat.getText((Synset)value));
        }

        return this;
    }
}
