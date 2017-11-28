package pl.edu.pwr.wordnetloom.ui.login;

import com.google.common.eventbus.EventBus;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.wordnetloom.application.service.SecurityService;
import pl.edu.pwr.wordnetloom.application.utils.Language;
import pl.edu.pwr.wordnetloom.application.utils.Loggable;
import pl.edu.pwr.wordnetloom.ui.login.events.LoginSuccessEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class LoginController implements Loggable, Initializable {

    private EventBus eventBus;

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<Language> cmbLanguage;

    private SecurityService service;

    @Autowired
    public void LoginController(SecurityService srv, EventBus bus) {
        service = srv;
        eventBus = bus;
    }

    public void login(Event event) throws IOException {

        if (service.authenticate(txtLogin.getText(),
                txtPassword.getText(), cmbLanguage.getSelectionModel().getSelectedItem())) {
            eventBus.post(new LoginSuccessEvent());
        } else {
            logger().warn("Unable to connect or incorrect login/password");
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbLanguage.setItems(FXCollections.observableArrayList(Language.values()));
        cmbLanguage.setValue(Language.Polski);
    }
}
