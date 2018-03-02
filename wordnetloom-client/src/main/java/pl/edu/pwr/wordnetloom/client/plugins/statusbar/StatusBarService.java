package pl.edu.pwr.wordnetloom.client.plugins.statusbar;

import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatusBarService extends AbstractService implements Runnable, ActionListener, Loggable {

    private static final int SLEEP_TIME = 5000;

    private Thread thread = null;
    private boolean stop = false;

    public StatusBarService(Workbench workbench) {
        super(workbench);
    }

    @Override
    public void installMenuItems() {
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
                logger().error("Trying to sleep plugin", e);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
    }

    @Override
    public void installViews() {
    }
}
