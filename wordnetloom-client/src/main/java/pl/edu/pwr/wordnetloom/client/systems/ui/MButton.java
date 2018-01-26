package pl.edu.pwr.wordnetloom.client.systems.ui;

import com.alee.laf.button.WebButton;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import pl.edu.pwr.wordnetloom.client.utils.Labels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class MButton extends WebButton implements KeyListener {

    private static final long serialVersionUID = 1L;

    public MButton() {
        setMinimumWidth(40);
        addKeyListener(this);
    }

    public MButton(ActionListener listener) {
        setMinimumWidth(40);
        addActionListener(listener);
    }

    public MButton withDefaultIconSize() {
        setPreferredSize(new Dimension(40, 25));
        return this;
    }

    public MButton withCaption(String caption) {
        setText(caption);
        return this;
    }

    public MButton withActionListener(ActionListener listener) {
        addActionListener(listener);
        return this;
    }

    public MButton withKeyListener(KeyListener keyListener) {
        addKeyListener(keyListener);
        return this;
    }

    public MButton withMnemonic(int mnemonic) {
        setMnemonic(mnemonic);
        return this;
    }

    public MButton withIcon(FontAwesome icon) {
        Icon i = IconFontSwing.buildIcon(icon, 12);
        setIcon(i);
        return this;
    }

    public MButton withEnabled(boolean enabled) {
        setEnabled(enabled);
        return this;
    }

    public MButton withToolTip(String toolTip) {
        setToolTipText(toolTip);
        return this;
    }

    public MButton withWidth(int width){
        setPreferredWidth(width);
        return this;
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyPressed(KeyEvent event) {
        // listens for enter key press on button
        if (event.getSource() == this && event.getModifiers() == 0 && event.getKeyCode() == KeyEvent.VK_ENTER) {
            event.consume();
            doClick();
        }
    }

    public static MButton buildSaveButton() {
        return new MButton()
                .withIcon(FontAwesome.FLOPPY_O)
                .withCaption(Labels.SAVE);
    }

    public static MButton buildCancelButton(ActionListener l) {
        return buildCancelButton().withActionListener(l);
    }

    public static MButton buildCancelButton() {
        return new MButton()
                .withMnemonic(KeyEvent.VK_A)
                .withIcon(FontAwesome.TIMES_CIRCLE)
                .withCaption(Labels.CANCEL);
    }

    public static MButton buildClearButton() {
        return new MButton()
                .withIcon(FontAwesome.TIMES)
                .withMnemonic(KeyEvent.VK_C)
                .withCaption(Labels.CLEAR);
    }


    public static MButton buildUpButton() {
        return new MButton()
                .withIcon(FontAwesome.ARROW_UP);
    }

    public static MButton buildDownButton() {
        return new MButton()
                .withIcon(FontAwesome.ARROW_DOWN);
    }

    public static MButton buildDeleteButton(ActionListener l) {
        return buildDeleteButton().withActionListener(l);
    }

    public static MButton buildDeleteButton() {
        return new MButton()
                .withIcon(FontAwesome.TRASH);
    }

    public static MButton buildAddButton(ActionListener l) {
        return buildAddButton().withActionListener(l);
    }

    public static MButton buildAddButton() {
        return new MButton()
                .withIcon(FontAwesome.PLUS);
    }

    public static MButton buildOkButton(ActionListener l) {
        return buildOkButton().withActionListener(l);
    }

    public static MButton buildOkButton() {
        return new MButton()
                .withMnemonic(KeyEvent.VK_O)
                .withCaption(Labels.OK);
    }

    public static MButton buildRemoveButton() {
        return new MButton()
                .withIcon(FontAwesome.MINUS);
    }

    public static MButton buildSearchButton(ActionListener l) {
        return buildSearchButton().withActionListener(l);
    }

    public static MButton buildSearchButton() {
        return new MButton()
                .withMnemonic(KeyEvent.VK_K)
                .withCaption(Labels.SEARCH_NO_COLON)
                .withIcon(FontAwesome.SEARCH);
    }

    public static MButton buildSelectButton(ActionListener l) {
        return buildSelectButton().withActionListener(l);
    }

    public static MButton buildSelectButton() {
        return new MButton()
                .withMnemonic(KeyEvent.VK_W)
                .withCaption(Labels.SELECT);

    }

    public static MButton buildEditButton() {
        return new MButton()
                .withMnemonic(KeyEvent.VK_E)
                .withIcon(FontAwesome.PENCIL);
    }
}
