package pl.edu.pwr.wordnetloom.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.felixroske.jfxsupport.FXMLController;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.wordnetloom.login.LoginView;
import pl.edu.pwr.wordnetloom.login.events.LoginSuccessEvent;
import pl.edu.pwr.wordnetloom.perspective.PerspectiveView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@FXMLController
public class ApplicationController {

    @Autowired
    ApplicationView applicationView;

    @Autowired
    LoginView loginView;

    @Autowired
    PerspectiveView perspectiveView;

    @Autowired
    EventBus eventBus;

    @PostConstruct
    public void init() {
        eventBus.register(this);
        ((Pane) applicationView.getView()).getChildren().clear();
        ((Pane) applicationView.getView()).getChildren().add(loginView.getView());
    }

    @PreDestroy
    public void preDestroy() {
        eventBus.unregister(this);
    }

    @Subscribe
    public void onLoginSuccess(LoginSuccessEvent event) {
        ((Pane) applicationView.getView()).getChildren().clear();
        ((Pane) applicationView.getView()).getChildren().add(perspectiveView.getView());
    }
}
