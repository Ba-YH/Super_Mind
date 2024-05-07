module com.cs1.super_mind {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires com.jfoenix;
    requires javafx.swing;
    opens com.cs1.super_mind to javafx.fxml;
    exports com.cs1.super_mind;
}