package pl.edu.pwr.wordnetloom.client.security;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.text.WebPasswordField;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.user.model.User;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChangePasswordWindow extends DialogWindow implements KeyListener, Loggable {

    public ChangePasswordWindow(WebFrame parent) {
        super(parent, "");
        initComponents();
        initListeners();
        initWindowPosition();
    }

    private boolean isPasswordValid() {
        return txtPassword.getText().equals(txtRePassword.getText());
    }

    private void initComponents() {

        panel = new WebPanel();
        lblPassword = new WebLabel();
        lblRePassword = new WebLabel();
        txtPassword = new WebPasswordField();
        txtRePassword = new WebPasswordField();
        btnChange = new WebButton();

        btnChange.setEnabled(false);
        btnChange.setMinimumSize(new Dimension(80, 25));
        btnChange.setMaximumSize(new Dimension(80, 25));

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Change password");
        setModal(true);
        setPreferredSize(new Dimension(350, 110));
        setResizable(false);
        setSize(new Dimension(350, 110));
        getContentPane().setLayout(new RiverLayout());
        getContentPane().add("hfill vfill", panel);


        panel.setLayout(new RiverLayout());
        panel.setMargin(6);

        lblPassword.setText("Password:");
        txtPassword.setText("");

        lblRePassword.setText("Repeat Password:");
        txtRePassword.setText("");

        txtPassword.setMinimumSize(new Dimension(14, 25));
        txtPassword.setPreferredSize(new Dimension(14, 25));


        txtRePassword.setMinimumSize(new Dimension(14, 25));
        txtRePassword.setPreferredSize(new Dimension(14, 25));


        btnChange.setText("Change");
        btnChange.addActionListener(a -> {
            String password = txtRePassword.getText();
            User u = RemoteService.userServiceRemote.changePasswordByEmail(UserSessionContext.getInstance().getUser().getEmail(), password);
            UserSessionContext.initialiseAndGetInstance(u, UserSessionContext.getInstance().getLanguage());
            Application.eventBus.post(new PasswordChangedEvent(password));
            dispose();
        });

        panel.add("left", lblPassword);
        panel.add("tab hfill", txtPassword);
        panel.add("br left", lblRePassword);
        panel.add("tab hfill", txtRePassword);
        panel.add("br br center", btnChange);

    }

    private void initListeners() {
        txtPassword.addKeyListener(this);
        txtRePassword.addKeyListener(this);
        btnChange.addKeyListener(this);
    }

    private void initWindowPosition() {
        setInScreenCenter(350, 110);
    }


    private WebButton btnChange;
    private WebPanel panel;
    private WebLabel lblPassword;
    private WebLabel lblRePassword;
    private WebPasswordField txtPassword;
    private WebPasswordField txtRePassword;


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent evt) {

        if (evt.getModifiers() == 0 && evt.getKeyCode() == KeyEvent.VK_ENTER) {
            evt.consume();
            if (isPasswordValid()) {
                btnChange.doClick();
            }
        }
        if (evt.getModifiers() == 0 && evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            evt.consume();
            dispose();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        btnChange.setEnabled(isPasswordValid());
    }

    public static void showModal(Workbench workbench) {
        ChangePasswordWindow frame = new ChangePasswordWindow(workbench.getFrame());
        frame.setVisible(true);
    }
}
