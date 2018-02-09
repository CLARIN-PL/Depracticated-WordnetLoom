package pl.edu.pwr.wordnetloom.client.systems.ui;

import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.concurrent.ExecutionException;

public  class LinkRunner extends SwingWorker<Void, Void> {

        private final URI uri;

        public LinkRunner(URI u) {
            if (u == null) {
                throw new NullPointerException();
            }
            uri = u;
        }

        @Override
        protected Void doInBackground() throws Exception {
            Desktop desktop = java.awt.Desktop.getDesktop();
            desktop.browse(uri);
            return null;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (ExecutionException | InterruptedException ee) {
                handleException(uri, ee);
            }
        }

        private static void handleException(URI u, Exception e) {
            JOptionPane.showMessageDialog(null, Messages.WRONG_LINK,
                    Labels.ERROR_OCCURED, JOptionPane.ERROR_MESSAGE);
        }
    }