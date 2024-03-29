package pl.edu.pwr.wordnetloom.client.systems.progress;

import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.GUIUtils;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * okienko z paskiem postepu
 *
 * @author Max
 */
public class ProgressFrame extends DialogWindow {

    private static final long serialVersionUID = 1L;
    private final JProgressBar progressBar;
    private final JProgressBar globalProgress;
    private final JLabel infoLabel;
    private final JLabel globalLabel;
    private JButton cancelButton;
    private boolean canceled = false;

    /**
     * konstruktor
     *
     * @param baseFrame        - okno bazowe na ktorym ma byc pasek postepu
     * @param title            - tytul okna
     * @param showCancelButton - TRUE pokazuje dodatkowy przycisk do
     *                         zatrzymywania
     */
    public ProgressFrame(WebFrame baseFrame, String title, boolean showCancelButton) {
        super(baseFrame, title, 300, showCancelButton ? 155 : 125);
        setLayout(new RiverLayout());
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //this.setAlwaysOnTop(true);

        globalLabel = new JLabel(Labels.GLOBAL_PROGRESS);
        infoLabel = new JLabel(" ");
        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setPreferredSize(new Dimension(0, 20));
        globalProgress = new JProgressBar();
        globalProgress.setPreferredSize(new Dimension(0, 20));
        globalProgress.setMinimum(0);

        cancelButton = MButton.buildCancelButton(l -> {
            canceled = true;
            cancelButton.setEnabled(false);
        });

        add("", infoLabel);
        add("br hfill", progressBar);
        add("br", globalLabel);
        add("br hfill vfill", globalProgress);
        if (showCancelButton) {
            add("br center", cancelButton);
        }
    }

    public ProgressFrame(WebFrame baseFrame, String title, boolean showCancelButton,
                         boolean noModal) {
        this(baseFrame, title, showCancelButton);
        super.setModal(!noModal);
    }

    /**
     * ustawenie dostepnosci przycisku anuluj
     *
     * @param enable - TRUE przycisk dostepny, FALSE przycisk nie dostepny
     */
    public void setCancelEnable(boolean enable) {
        cancelButton.setEnabled(enable);
    }

    /**
     * ustawienie parametrow postepu dla akcji bez podziału na kroki
     *
     * @param info - opis akcji
     */
    public void setProgressParams(String info) {
        progressBar.setIndeterminate(true);
        infoLabel.setText(info + ":");
    }

    /**
     * ustawienie parametrow postepu
     *
     * @param value - wartosc aktualna
     * @param max   - maksymalna wartosc
     * @param info  - opis akcji
     */
    public void setProgressParams(int value, int max, String info) {

        try {
            GUIUtils.delegateToEDT(() -> {
                progressBar.setIndeterminate(value == 0 && max == 0);
                progressBar.setMaximum(max);
                progressBar.setValue(value);
                infoLabel.setText(info + ":");
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            Logger.getLogger(ProgressFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ustawienie parametrow globalnego postepu
     *
     * @param value - wartosc aktualna
     * @param max   - maksymalna wartosc
     */
    public void setGlobalProgressParams(int value, int max) {

        try {
            GUIUtils.delegateToEDT(() -> {
                globalProgress.setMaximum(max);
                globalProgress.setValue(value);
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            Logger.getLogger(ProgressFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * utawienie wartosci paska postepu
     *
     * @param value - nowa wartosc
     */
    public void setProgressValue(int value) {

        try {
            GUIUtils.delegateToEDT(() -> {
                progressBar.setValue(value);
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            Logger.getLogger(ProgressFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setGlobalProgressValue(int value) {

        try {
            GUIUtils.delegateToEDT(() -> {
                globalProgress.setValue(value);
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            Logger.getLogger(ProgressFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void close() {
        Runnable run = () -> {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setVisible(false);
            dispose();
        };

        try {
            GUIUtils.delegateToEDT(run);
        } catch (InterruptedException | InvocationTargetException ex) {
            Logger.getLogger(ProgressFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCancel(boolean canceled) {
        this.canceled = canceled;
    }

    public void setButtonLabel(String text) {
        cancelButton.setText(text);
    }

    public void setIndeterminate(boolean newValue) {
        progressBar.setIndeterminate(newValue);
        globalProgress.setIndeterminate(newValue);
    }

    public void setText(String text) {
        infoLabel.setText(text);
    }
}
