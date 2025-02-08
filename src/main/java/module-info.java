module com.example.mysimplechat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires jbcrypt;

    opens com.example.mysimplechat to javafx.fxml;
    exports com.example.mysimplechat;
    exports com.example.security;
    exports com.example.database;
    exports com.example.validation;
}