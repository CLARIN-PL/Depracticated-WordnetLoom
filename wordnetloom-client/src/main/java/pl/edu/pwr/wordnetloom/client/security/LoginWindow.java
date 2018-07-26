package pl.edu.pwr.wordnetloom.client.security;

import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.google.common.eventbus.Subscribe;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.remote.ConnectionProvider;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.user.model.User;
import pl.edu.pwr.wordnetloom.user.service.UserServiceRemote;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginWindow extends DialogWindow implements KeyListener, Loggable {


    public LoginWindow(WebFrame parent) {
        super(parent, "");
        Application.eventBus.register(this);

        initComponents();
        initListeners();
        initWindowPosition();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void initComponents() {

        lblLogo = new WebLabel();
        fieldsPanel = new WebPanel();
        lblLanguage = new WebLabel();
        txtUsername = new WebTextField();
        lblPassword = new WebLabel();
        txtPassword = new WebPasswordField();
        cmbLanguage = new WebComboBox(Language.values());
        lblUsername = new WebLabel();
        btnPanel = new WebPanel();
        btnSignIn = new WebButton();
        btnSignInAnonymous = new WebButton();
        btnCancel = new WebButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Login to WordnetLoom ");
        setBackground(new java.awt.Color(204, 204, 204));
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setIconImage(Toolkit.getDefaultToolkit().getImage("/icons/wordnet.gif"));
        setIconImages(null);
        setModal(true);
        setName("loginDialogWindow");
        setPreferredSize(new java.awt.Dimension(520, 260));
        setResizable(false);
        setSize(new java.awt.Dimension(520, 260));
        getContentPane().setLayout(null);

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/login-top.png"))); // NOI18N
        lblLogo.setAlignmentY(0.0F);
        lblLogo.setIconTextGap(0);
        getContentPane().add(lblLogo);
        lblLogo.setBounds(0, 0, 520, 70);

        fieldsPanel.setBackground(new java.awt.Color(221, 221, 221));
        fieldsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        lblLanguage.setText("Language:");

        txtUsername.setText("");
        txtUsername.setMinimumSize(new java.awt.Dimension(14, 25));
        txtUsername.setPreferredSize(new java.awt.Dimension(14, 25));

        lblPassword.setText("Password:");

        txtPassword.setText("");
        txtPassword.setMinimumSize(new java.awt.Dimension(14, 25));
        txtPassword.setPreferredSize(new java.awt.Dimension(14, 25));

        lblUsername.setText("Username:");

        javax.swing.GroupLayout fieldsPanelLayout = new javax.swing.GroupLayout(fieldsPanel);
        fieldsPanel.setLayout(fieldsPanelLayout);
        fieldsPanelLayout.setHorizontalGroup(
                fieldsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(fieldsPanelLayout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addGroup(fieldsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblLanguage, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblPassword, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblUsername, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(fieldsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmbLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(82, Short.MAX_VALUE))
        );
        fieldsPanelLayout.setVerticalGroup(
                fieldsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(fieldsPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(fieldsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblUsername))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(fieldsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(lblPassword)
                                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(fieldsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(cmbLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblLanguage))
                                .addContainerGap(23, Short.MAX_VALUE))
        );

        getContentPane().add(fieldsPanel);
        fieldsPanel.setBounds(0, 70, 520, 130);

        btnPanel.setBackground(new java.awt.Color(190, 190, 190));

        btnSignIn.setText("Sign in");
        Icon singInIcon = IconFontSwing.buildIcon(FontAwesome.SIGN_IN, 11);
        btnSignIn.setIcon(singInIcon);

        btnSignIn.addActionListener(e -> {
            if (login()) {
                LocalisationManager.getInstance().load(getSelectedLanguage());
                Application.eventBus.post(new AuthenticationSuccessEvent());
            }
        });

        btnSignInAnonymous.setText("Sign in as anonymous");
        Icon singInIconAnon = IconFontSwing.buildIcon(FontAwesome.USER_SECRET, 11);
        btnSignInAnonymous.setIcon(singInIconAnon);
        btnSignInAnonymous.addActionListener(e -> {
            if (loginAnonymous()) {
                LocalisationManager.getInstance().load(getSelectedLanguage());
                Application.eventBus.post(new AuthenticationSuccessEvent());
            }
        });

        btnCancel.setText("Cancel");
        Icon cancelIcon = IconFontSwing.buildIcon(FontAwesome.TIMES, 11);
        btnCancel.addActionListener(evt -> btnCancelAction(evt));
        btnCancel.setIcon(cancelIcon);

        javax.swing.GroupLayout btnPanelLayout = new javax.swing.GroupLayout(btnPanel);
        btnPanel.setLayout(btnPanelLayout);
        btnPanelLayout.setHorizontalGroup(
                btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnPanelLayout.createSequentialGroup()
                                .addContainerGap(201, Short.MAX_VALUE)
                                .addComponent(btnSignIn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSignInAnonymous, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34))
        );
        btnPanelLayout.setVerticalGroup(
                btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(btnPanelLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnSignIn)
                                        .addComponent(btnCancel)
                                        .addComponent(btnSignInAnonymous)
                                )
                                .addContainerGap(142, Short.MAX_VALUE))
        );

        getContentPane().add(btnPanel);
        btnPanel.setBounds(0, 200, 520, 60);

        getAccessibleContext().setAccessibleDescription("");

        setBounds(0, 0, 516, 287);
    }

    private void btnCancelAction(ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        System.exit(0);
    }

    @Subscribe
    public void onAuthenticateUser(AuthenticateUserEvent event) {
        setVisible(true);
    }

    private boolean login() {

        ConnectionProvider
                .getInstance()
                .setCredentials(txtUsername.getText(), txtPassword.getText());

        try {
            User user = ConnectionProvider.getInstance()
                    .lookupForService(UserServiceRemote.class).findUserByEmail(txtUsername.getText());
            if (user != null) {
                UserSessionContext.initialiseAndGetInstance(user, getSelectedLanguage().getAbbreviation());
                dispose();
                return true;
            } else {
                ConnectionProvider.getInstance().destroyInstance();
            }

        } catch (Exception ex) {
            logger().error("Unable to connect or incorrect login/password", ex);
            ConnectionProvider.getInstance().destroyInstance();
            SwingUtilities.invokeLater(() -> {
                Application.eventBus.post(new AuthenticationFailedEvent("Unable to connect or incorrect login/password"));
            });
            return false;
        }
        return false;
    }

    private boolean loginAnonymous() {

        ConnectionProvider
                .getInstance()
                .setCredentials("anonymous@clarin-pl.eu", "password");

        try {
            User user = ConnectionProvider.getInstance()
                    .lookupForService(UserServiceRemote.class).findUserByEmail("anonymous@clarin-pl.eu");
            if (user != null) {
                UserSessionContext.initialiseAndGetInstance(user, getSelectedLanguage().getAbbreviation());
                dispose();
                return true;
            } else {
                ConnectionProvider.getInstance().destroyInstance();
            }

        } catch (Exception ex) {
            logger().error("Unable to connect or incorrect login/password", ex);
            ConnectionProvider.getInstance().destroyInstance();
            SwingUtilities.invokeLater(() -> {
                Application.eventBus.post(new AuthenticationFailedEvent("Unable to connect or incorrect login/password"));
            });
            return false;
        }
        return false;
    }

    private void initListeners() {
        txtPassword.addKeyListener(this);
        txtUsername.addKeyListener(this);
        cmbLanguage.addKeyListener(this);
        btnSignIn.addKeyListener(this);
    }

    private void initWindowPosition() {
        setInScreenCenter(520, 260);
    }

    public Language getSelectedLanguage() {
        return (Language) cmbLanguage.getSelectedItem();
    }

    private WebButton btnCancel;
    private WebButton btnSignIn;
    private WebButton btnSignInAnonymous;
    private WebPanel btnPanel;
    private WebComboBox cmbLanguage;
    private WebPanel fieldsPanel;
    private WebLabel lblLanguage;
    private WebLabel lblLogo;
    private WebLabel lblPassword;
    private WebLabel lblUsername;
    private WebPasswordField txtPassword;
    private WebTextField txtUsername;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        if (evt.getModifiers() == 0 && evt.getKeyCode() == KeyEvent.VK_ENTER) {
            evt.consume();
            btnSignIn.doClick();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
