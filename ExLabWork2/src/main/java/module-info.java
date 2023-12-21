module com.example.labwork2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.slf4j;
    requires Java.WebSocket;
    requires org.jfree.jfreechart;
    requires java.desktop;
    requires org.jfree.chart.fx;
    requires org.apache.commons.io;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires com.fasterxml.jackson.databind;
    opens com.exlabwork2 to javafx.fxml;
    exports com.exlabwork2;
}