package pl.edu.pwr.wordnetloom.login;

import com.google.common.eventbus.EventBus;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.wordnetloom.login.events.LoginSuccessEvent;
import pl.edu.pwr.wordnetloom.remote.Loggable;
import pl.edu.pwr.wordnetloom.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.remote.UserSession;
import pl.edu.pwr.wordnetloom.user.model.User;

import java.io.IOException;

@FXMLController
public class LoginController implements Loggable {

    private EventBus eventBus;

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtPassword;

    @Autowired
    public void LoginController(EventBus bus) {
        eventBus = bus;
    }


    public void login(Event event) throws IOException {

        if (authenticate()) {
            eventBus.post(new LoginSuccessEvent());
        } else {
            logger().warn("Unable to connect or incorrect login/password");
        }

    }

    private boolean authenticate() {

        UserSession data = new UserSession(txtLogin.getText(), txtPassword.getText(), "pl");
        RemoteConnectionProvider.getInstance().setUserSession(data);

        try {

            User user = RemoteConnectionProvider.getInstance().getUser();

            if (user != null) {
                return true;
            }

        } catch (Exception ex) {
            return false;
        }
        return false;
    }
}
