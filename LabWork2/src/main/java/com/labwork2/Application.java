package com.labwork2;

import com.labwork2.datasource.FinamSelenium;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void stop(){
        if(FinamSelenium.driver != null) {
            FinamSelenium.driver.quit();
        }
        System.out.println("Stage is closing");
        // Save file
    }
    public static void main(String[] args) {
        launch();
    }
}
