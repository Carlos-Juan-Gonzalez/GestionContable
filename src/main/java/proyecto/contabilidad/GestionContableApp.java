package proyecto.contabilidad;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.sql.Connection;

public class GestionContableApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = indexLoader();
        Scene scene = new Scene(fxmlLoader.load(), 500, 350);
        GestionContableController controller = fxmlLoader.getController();
        controller.setGitIcon();
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("Gestion Contable");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(GestionContableApp.class.getResource("icons/gestorContableIcon.png").toString()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Llama a todos lo metodos necesarios de la clase
     * Conexion para dejar la base de datos preparada
     * para la ejecución del programa
     */
    public static Conexion indexDB(){
        Conexion conexion = new Conexion();
        conexion.setBaseConnection();
        if (!conexion.isCreated()){
            conexion.createDB();
        }

        return conexion;
    }

    public static FXMLLoader indexLoader(){
        Conexion conexion = indexDB();
        if (conexion.isFirstLoging()){
            return new FXMLLoader(GestionContableApp.class.getResource("IndexView.fxml"));
        }else {
            return new FXMLLoader(GestionContableApp.class.getResource("LoginView.fxml"));
        }
    }
}