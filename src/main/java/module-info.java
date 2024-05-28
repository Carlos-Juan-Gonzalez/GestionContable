module proyecto.contabilidad {
    requires javafx.controls;
    requires javafx.fxml;

    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens proyecto.contabilidad to javafx.fxml;
    exports proyecto.contabilidad;
}