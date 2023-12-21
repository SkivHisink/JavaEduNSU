module org.lab.labwork1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    opens org.lab.exlabwork1 to javafx.fxml;
    exports org.lab.exlabwork1;
    exports org.lab.exlabwork1.Model;
    opens org.lab.exlabwork1.Model to javafx.fxml;
    exports org.lab.exlabwork1.View;
    opens org.lab.exlabwork1.View to javafx.fxml;
    exports org.lab.exlabwork1.Utility;
    opens org.lab.exlabwork1.Utility to javafx.fxml;
}