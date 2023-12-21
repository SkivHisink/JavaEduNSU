package org.lab.exlabwork1.View;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.lab.exlabwork1.Control.MainControl;

public class TableViewer {
    private MainControl parent;
    @FXML
    private TableView resultTable;
    @FXML
    private TableColumn column1;
    @FXML
    private TableColumn column2;
    @FXML
    private TableColumn column3;
    @FXML
    private TableColumn column4;
    @FXML
    private TableColumn column5;
    @FXML
    private TableColumn column6;
    @FXML
    private TableColumn column7;


    public TableViewer() {
    }

    public void init(){
        column1.setCellValueFactory(new PropertyValueFactory<>("N"));
        column2.setCellValueFactory(new PropertyValueFactory<>("dayOfUsing"));
        column3.setCellValueFactory(new PropertyValueFactory<>("paymentDateVal"));
        column4.setCellValueFactory(new PropertyValueFactory<>("generalPaymentSize"));
        column5.setCellValueFactory(new PropertyValueFactory<>("percentSum"));
        column6.setCellValueFactory(new PropertyValueFactory<>("sumOfFee"));
        column7.setCellValueFactory(new PropertyValueFactory<>("feeLeft"));
    }
    public TableView getResultTable() {
        return resultTable;
    }


    public void setParent(MainControl parent) {

        this.parent = parent;
    }
}
