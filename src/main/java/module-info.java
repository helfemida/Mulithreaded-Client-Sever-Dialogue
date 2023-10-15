module com.example.clientserverconversation {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.clientserverconversation to javafx.fxml;
    exports com.example.clientserverconversation;
}