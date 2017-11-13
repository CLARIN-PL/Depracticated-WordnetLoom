package pl.edu.pwr.wordnetloom.client.plugins.statusbar;

import com.alee.laf.menu.WebMenu;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MMenuItem;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class StatusBarService extends AbstractService implements Runnable, ActionListener {

    private static final int SLEEP_TIME = 5000;

    private Thread thread = null;
    private boolean stop = false;
    private JMenuItem clearCache;

    public StatusBarService(Workbench workbench) {
        super(workbench);
    }

    @Override
    public void installMenuItems() {
        WebMenu other = workbench.getMenu(Labels.SETTINGS);
        if (other == null) {
            return;
        }
        clearCache = new MMenuItem(Labels.CLEAR_CACHE)
                .withMnemonic(KeyEvent.VK_W)
                .withActionListener(this);
        other.add(clearCache);
    }

    @Override
    public boolean onClose() {
        stop = true;
        thread = null;
        return true;
    }

    @Override
    public void onStart() {
        thread = new Thread(this);
        thread.start();
    }


    volatile int memoryUsage;

    @Override
    final public void run() {

        while (!stop) {
            // czy cache jest wlaczony
            memoryUsage = (int) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;

            // gdy cache jest wylaczony
            SwingUtilities.invokeLater(() -> {
                workbench.setStatusText(String.format("<html>" + Labels.MEMORY_USAGE + "</html>", memoryUsage));
            });

            // uspienie pluginu
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                Logger.getLogger(StatusBarService.class).log(Level.ERROR, "Trying to sleep plugin" + e);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == clearCache) {
            Runtime.getRuntime().gc();
            DialogBox.showInformation(Messages.SUCCESS_CACHE_CLEANED);
        }

    }

    @Override
    public void installViews() {
    }
}
