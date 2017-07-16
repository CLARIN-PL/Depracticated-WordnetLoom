package pl.edu.pwr.wordnetloom.client.systems.ui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * klasa będąca nadkładką na przycisk, zawiera bardziej rozbudowane konstruktory
 *
 * @author Max
 *
 */
public class ButtonExt extends JButton implements KeyListener {

    private static final long serialVersionUID = 1L;

    /**
     * konstruktor
     *
     * @param caption - edytkieta
     * @param actionListener - nasłuchiwacz
     */
    public ButtonExt(String caption, ActionListener actionListener) {
        super(caption);
        this.addActionListener(actionListener);
        this.addKeyListener(this);
    }

    /**
     * konstruktor
     *
     * @param icon - ikonka dla przycisku
     * @param actionListener - nasłuchiwacz
     */
    public ButtonExt(ImageIcon icon, ActionListener actionListener) {
        super(icon);
        this.addActionListener(actionListener);
        this.addKeyListener(this);
    }

    /**
     * konstruktor
     *
     * @param caption - edytkieta
     * @param actionListener - nasłuchiwacz akcji
     * @param mnemonic - mnemonik
     */
    public ButtonExt(String caption, ActionListener actionListener, int mnemonic) {
        super(caption);
        this.addActionListener(actionListener);
        this.setMnemonic(mnemonic);
        this.addKeyListener(this);
    }

    /**
     * konstruktor
     *
     * @param icon - ikonka dla przycisku
     * @param actionListener - nasłuchiwacz akcji
     * @param mnemonic - mnemonik
     */
    public ButtonExt(ImageIcon icon, ActionListener actionListener, int mnemonic) {
        super(icon);
        this.addActionListener(actionListener);
        this.setMnemonic(mnemonic);
        this.addKeyListener(this);
    }

    /**
     * konstruktor
     *
     * @param caption - edytkieta
     * @param actionListener - nasłuchiwacz akcji
     * @param keyListener - nasluchiwacz klawiszy
     * @param mnemonic - mnemonik
     */
    public ButtonExt(String caption, ActionListener actionListener, KeyListener keyListener, int mnemonic) {
        super(caption);
        this.addActionListener(actionListener);
        this.setMnemonic(mnemonic);
        this.addKeyListener(keyListener);
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyPressed(KeyEvent event) {
        // aby enter działał na przyciskach
        if (event.getSource() == this && event.getModifiers() == 0 && event.getKeyCode() == KeyEvent.VK_ENTER) {
            event.consume();
            this.doClick();
        }
    }

}
