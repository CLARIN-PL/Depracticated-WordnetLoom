package pl.edu.pwr.wordnetloom.client.plugins.core.window;

import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutWindow extends DialogWindow implements ActionListener {

    private static final String TEXT_COPYRIGHT = "Copyright 2005-2017, Wrocław University of Science and Technology";
    private static final String TEXT_AUTHORS_1 = "Bartosz Broda, Łukasz Jastrzębski,";
    private static final String TEXT_AUTHORS_2 = "Paweł Koczan, Michał Marcińczuk,";
    private static final String TEXT_AUTHORS_3 = "Adam Musiał, Maciej Piasecki,";
    private static final String TEXT_AUTHORS_4 = "Radosław Ramocki, Michał Stanek";
    private static final String TEXT_AUTHORS_5 = "Tomasz Naskręt, Roman Dyszlewski";

    private AboutWindow(Workbench workbench) {
        super(workbench.getFrame(), Labels.ABOUT_APP, 360, 470);
        initializeComponents(workbench);
    }

    private void initializeComponents(Workbench workbench) {
        setLocationRelativeTo(workbench.getFrame());
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Container content = getContentPane();
        content.setBackground(new Color(238, 238, 238));
        content.add("center", new JLabel(new ImageIcon(getClass().getClassLoader().getResource("icons/logo.gif")), JLabel.CENTER));
        content.add("br center", new MLabel(Labels.ABOUT_DESCRIPTION).withPlainFont());
        content.add("br", new MLabel(" "));
        content.add("br left", new MLabel(Labels.VERSION_COLON));
        content.add("tab", new MLabel(workbench.getVersion()).withPlainFont());
        content.add("br left", new MLabel(Labels.CREATED_COLON));
        content.add("tab", new MLabel(TEXT_AUTHORS_1).withPlainFont());
        content.add("br tab", new MLabel(TEXT_AUTHORS_2).withPlainFont());
        content.add("br tab", new MLabel(TEXT_AUTHORS_3).withPlainFont());
        content.add("br tab", new MLabel(TEXT_AUTHORS_4).withPlainFont());
        content.add("br tab", new MLabel(TEXT_AUTHORS_5).withPlainFont());
        content.add("br", new MLabel(" "));
        content.add("br center", new MLabel(TEXT_COPYRIGHT));
        content.add("br", new MLabel(Labels.ALL_RIGHTS_RESERVED));
        content.add("p", MButton.buildOkButton(this));
        pack();
    }

    public static void showModal(Workbench workbench) {
        AboutWindow frame = new AboutWindow(workbench);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        setVisible(false);
    }

}
