package pl.edu.pwr.wordnetloom.client.plugins.login.window;

import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.login.data.UserSessionData;
import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.user.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginWindow extends DialogWindow implements KeyListener, Loggable {


    public LoginWindow(WebFrame parent) {
        super(parent, "");
        initComponents();
        initListeners();
        initWindowPosition();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
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

        txtUsername.setText("admin@gmail.com");
        txtUsername.setMinimumSize(new java.awt.Dimension(14, 25));
        txtUsername.setPreferredSize(new java.awt.Dimension(14, 25));

        lblPassword.setText("Password:");

        txtPassword.setText("password");
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

        btnCancel.setText("Cancel");
        Icon cancelIcon = IconFontSwing.buildIcon(FontAwesome.TIMES, 11);
        btnCancel.addActionListener(evt -> btnCancelActionPerformed(evt));
        btnCancel.setIcon(cancelIcon);

        javax.swing.GroupLayout btnPanelLayout = new javax.swing.GroupLayout(btnPanel);
        btnPanel.setLayout(btnPanelLayout);
        btnPanelLayout.setHorizontalGroup(
                btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnPanelLayout.createSequentialGroup()
                                .addContainerGap(291, Short.MAX_VALUE)
                                .addComponent(btnSignIn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34))
        );
        btnPanelLayout.setVerticalGroup(
                btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(btnPanelLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnSignIn)
                                        .addComponent(btnCancel))
                                .addContainerGap(142, Short.MAX_VALUE))
        );

        getContentPane().add(btnPanel);
        btnPanel.setBounds(0, 200, 520, 60);

        getAccessibleContext().setAccessibleDescription("");

        setBounds(0, 0, 516, 287);
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        System.exit(0);
    }

    public void btnOkActionListener(ActionListener listener) {
        btnSignIn.addActionListener(listener);
    }

    public boolean login() {

        RemoteConnectionProvider
                .getInstance()
                .setUserSessionData(new UserSessionData(txtUsername.getText(), txtPassword.getText(), getSelectedLanguage().getAbbreviation()));

        try {
            User user = RemoteConnectionProvider.getInstance().getUser();
            if (user != null) {
                dispose();
                return true;
            }

        } catch (Exception ex) {
            logger().error("Unable to connect or incorrect login/password", ex);
            RemoteConnectionProvider.getInstance().destroyInstance();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null,
                        "Unable to connect or incorrect login/password",
                        Application.PROGRAM_NAME_VERSION,
                        JOptionPane.ERROR_MESSAGE);

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
