package org.lab.labwork1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        //String path = "D:\\test.xlsx";
        //FileInputStream fileInputStream =new FileInputStream(path);
        //XSSFWorkbook wb
        //XSSFWorkbook hssfWorkbook = new XSSFWorkbook(fileInputStream);
        //Sheet sheet = hssfWorkbook.getSheet("Sheet1");
        //Row row= sheet.getRow(0);
        //System.out.println(row.getCell(0));
        launch();
    }
}