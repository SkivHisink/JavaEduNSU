package org.lab.labwork1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Controller
{
    @FXML
    private Label welcomeText;
    @FXML
    private TableView resultTable;
    @FXML
    private ComboBox calculationType;
    private String infoBegin = "INFO:";
    @FXML
    private Label infoText; // like a log for user
    private List<String> val;
    private ObservableList<String> observableList;
    @FXML
    protected void onHelloButtonClick()
    {
        //test button
        welcomeText.setText("Welcome to JavaFX Application!");
        infoText.setText(infoBegin + "Test button is work successfully.");
    }
    public Controller()
    {
        //Generating payment types
        val = new ArrayList<String>();
        val.add("Annuity payment");
        val.add("Different payment");
        observableList = FXCollections.observableList(val);
    }
    @FXML
    protected void onOpenExcelButtonClick() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Excel files (*.xlsx;*.xls)",
                "*.xlsx", "*.xls");
        chooser.getExtensionFilters().add(extFilter);
        extFilter = new FileChooser.ExtensionFilter(
                "Any files (*.*)",
                "*.*");
        chooser.getExtensionFilters().add(extFilter);
        calculationType.setItems(observableList);
        calculationType.setValue(val.get(0));
        File file = chooser.showOpenDialog(new Stage());
        if (file != null)
        {
            FileInputStream fileInputStream = new FileInputStream(file.getPath());
            XSSFWorkbook hssfWorkbook = new XSSFWorkbook(fileInputStream);
        }
    }

}