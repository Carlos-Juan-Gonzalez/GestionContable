package proyecto.contabilidad;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import java.io.IOException;


public class AdminMainController extends MainController {

    @FXML
    protected TextField codigo,nombre;
    @FXML
    protected ComboBox permiso,diario_id;
    private MainController controller;

    @FXML
    public void openCrearCuentaView(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CrearCuentaView.fxml"));
        createStage(loader);
    }
    @FXML
    public void openCrearDiarioView(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CrearDiarioView.fxml"));
        createStage(loader);
    }
    @FXML
    public void openCrearUsuarioView(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CrearUsuarioView.fxml"));
        createStage(loader).setComboBoxs();
    }

    public AdminMainController createStage(FXMLLoader loader){
        AdminMainController controller = null;
        try {
            Scene scene = new Scene(loader.load(),400,300);
            controller= loader.getController();
            controller.setController(this.controller);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            Stage stage = new Stage();
            stage.setTitle("Gestor Contable");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return controller;
    }

    @FXML
    public void crearCuenta(){
        if (validarCuenta()){
            Cuenta cuenta = new Cuenta(Integer.parseInt(codigo.getText()),nombre.getText());
            cuenta.insertCuenta(controller.getConnection(),controller.getDiario().getId());
            ((Stage)nombre.getScene().getWindow()).close();
        }
    }


    public boolean validarCuenta(){
        if (codigo.getText() == "" || nombre.getText() == ""){
            return false;
        }else {
            try {
                Integer.getInteger(codigo.getText());
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }

    @FXML
    public void crearDiario(){
        if (validarDiario()){
            Diario diario = new Diario(nombre.getText());
            diario.insertDiario(controller.getConnection());
            ((Stage)nombre.getScene().getWindow()).close();
        }
    }

    public boolean validarDiario(){
        if (nombre.getText() == ""){
            return false;
        }else {
            return true;
        }
    }

    @FXML
    public void crearUsuario(){
        if (validarDiario()){
            Diario diario = new Diario(nombre.getText());
            diario.insertDiario(controller.getConnection());
            ((Stage)nombre.getScene().getWindow()).close();
        }
    }

    public boolean validarUsuario(){
        if (nombre.getText() == ""){
            return false;
        }else {
            return true;
        }
    }

    public void setComboBoxs(){
        permiso.getItems().addAll("admin","usuario","invitado");
        permiso.setValue("usuario");
    }

    public MainController getController() {
        return controller;
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }
}
