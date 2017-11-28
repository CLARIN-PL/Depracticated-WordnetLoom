package pl.edu.pwr.wordnetloom;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class SplashScreen extends de.felixroske.jfxsupport.SplashScreen {

    // private static String DEFAULT_IMAGE = "/splash/javafx.png";

    public SplashScreen() {
    }

    public Parent getParent() {
        ImageView imageView = new ImageView(getClass().getResource(getImagePath()).toExternalForm());
        ProgressBar splashProgressBar = new ProgressBar();
        splashProgressBar.setPrefWidth(imageView.getImage().getWidth());
        VBox vbox = new VBox();
        vbox.getChildren().addAll(new Node[]{imageView, splashProgressBar});
        return vbox;
    }

    public boolean visible() {
        return true;
    }

    public String getImagePath() {
        return "";//DEFAULT_IMAGE;
    }
}