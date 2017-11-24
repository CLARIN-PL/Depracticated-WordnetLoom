package pl.edu.pwr.wordnetloom;

import com.google.common.eventbus.EventBus;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.edu.pwr.wordnetloom.application.ApplicationView;

@SpringBootApplication
public class ApplicationLoader extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(ApplicationLoader.class, ApplicationView.class, new SplashScreen(), args);
    }

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }
}
