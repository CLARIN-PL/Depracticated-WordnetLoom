package pl.edu.pwr.wordnetloom;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.pwr.wordnetloom.application.ApplicationView;
import pl.edu.pwr.wordnetloom.ui.splashscreen.SplashScreen;

@SpringBootApplication
public class Application extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(Application.class, ApplicationView.class, new SplashScreen(), args);
    }
}
