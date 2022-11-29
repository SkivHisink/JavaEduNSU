module com.labwork3.labwork3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;

    opens com.labwork3 to javafx.fxml;
    exports com.labwork3;
}