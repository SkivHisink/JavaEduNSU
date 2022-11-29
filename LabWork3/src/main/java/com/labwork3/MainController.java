package com.labwork3;

import com.labwork3.parsers.AlfaCurrencyParser;
import com.labwork3.parsers.OpenCurrencyParser;
import com.labwork3.parsers.SberCurrencyParser;
import com.labwork3.parsers.VtbCurrencyParcer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    public Button ConnectButton;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onConnectButtonClick()
    {
        VtbCurrencyParcer vtb = new VtbCurrencyParcer();
        SberCurrencyParser sber = new SberCurrencyParser();
        AlfaCurrencyParser alfa = new AlfaCurrencyParser();
        OpenCurrencyParser open = new OpenCurrencyParser();
        try {
            vtb.fillCurrancyList();
            vtb.connect();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        try {
            sber.connect();
            sber.fillCurrancyList();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        try {
            alfa.connect();
            alfa.fillCurrancyList();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        try {
            open.connect();
            open.fillCurrancyList();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}