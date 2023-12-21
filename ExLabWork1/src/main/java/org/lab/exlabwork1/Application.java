package org.lab.exlabwork1;

import javafx.stage.Stage;
import org.lab.exlabwork1.Control.MainControl;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        MainControl control = new MainControl();
        control.run(stage);
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}