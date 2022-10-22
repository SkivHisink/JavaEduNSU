module com.example.labwork2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;
    requires org.slf4j;
    requires Java.WebSocket;
    requires org.jfree.jfreechart;
    requires java.desktop;

    opens com.labwork2 to javafx.fxml;
    exports com.labwork2;
}