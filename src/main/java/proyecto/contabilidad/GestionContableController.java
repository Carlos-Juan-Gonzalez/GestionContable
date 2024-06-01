package proyecto.contabilidad;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.sql.Connection;

public class GestionContableController{

    @FXML
    protected Hyperlink link;
    @FXML
    protected ImageView imagen = new ImageView();
    @FXML
    protected TextField usuario;
    @FXML
    protected PasswordField password, passwordValidation;
    @FXML
    protected Text alert;
    private Connection connection;

    public void link(){
        try {
            Runtime.getRuntime().exec("cmd.exe /c start iexplore https://github.com/Carlos-Juan-Gonzalez");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void setGitIcon(){
        imagen.setImage(new Image(GestionContableApp.class.getResource("icons/gitIcon.png").toString()));
        link.setGraphic(imagen);
        link.setBorder(null);
    }

    @FXML
    public void indexContraseña(){
        String contraseña, contraseñavalidador;

        contraseña = password.getText();
        contraseñavalidador  = passwordValidation.getText();

        if (contraseñaValidation(contraseña,contraseñavalidador)){
            Conexion conexion = new Conexion();
            Usuario usuario = new Usuario();

            conexion.setBaseConnection();
            usuario.cambiarContraseñaAdmin(conexion.getConexion(),contraseña);

            conectarUsuario(conexion);
            cambiarLoginView();
        }
    }

    /**
     * comprueba que los dos parametros contraseña tengan el mismo valor
     * @param contraseña String: primera contraseña a validar
     * @param contraseñavalidador String: segunda contraseña a validar
     * @return
     */
    public boolean contraseñaValidation(String contraseña, String contraseñavalidador){
        if (contraseña.isBlank() || contraseñavalidador.isBlank()){
            alert.setText("Por favor rellene todos los campos");
            alert.setVisible(true);
            return false;
        }else {
            if (contraseña.equals(contraseñavalidador)){
                return true;
            }else {
                alert.setText("Las contraseñas no coinciden");
                alert.setVisible(true);
                return false;
            }
        }
    }

    public void conectarUsuario(Conexion conexion){
        conexion.createNewUser(usuario.getText(),password.getText());
        this.connection = conexion.setConnection(usuario.getText(),password.getText());
    }

    public void cambiarLoginView(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(),500,350);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            ((Stage) usuario.getScene().getWindow()).setScene(scene);
            GestionContableController controller = fxmlLoader.getController();
            controller.setGitIcon();
            controller.usuario.setText("admin");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void login(){
        Conexion conexion = new Conexion();
        if (validarLogin(conexion)){
            Usuario usuario = new Usuario();
            Diario diario = new Diario();

            conexion.createNewUser(this.usuario.getText(),password.getText());
            this.connection = conexion.setConnection(this.usuario.getText(),password.getText());
            usuario = usuario.constructUsuario(connection,this.usuario.getText());
            diario = diario.constructDiario(connection, usuario.getDiario_id());
            cambiarMainView(usuario,diario);
        }
    }

    public boolean validarLogin(Conexion conexion){
        Usuario usuario = new Usuario();
        conexion.setBaseConnection();
        if (!validarInputLogin()){
            alert.setText("Por favor rellene todos los campos");
            alert.setVisible(true);
            return false;
        }
        if (!usuario.validarLogin(conexion.getConexion(),this.usuario.getText(),password.getText())){
            alert.setText("Usuario o Contraseña incorrecta");
            alert.setVisible(true);
            return false;
        }
        return true;
    }
    public boolean validarInputLogin(){
        if (usuario.getText().isBlank() ||password.getText().isBlank()){

            return false;
        }else {
            return true;
        }
    }
    public void cambiarMainView(Usuario usuario,Diario diario){
        FXMLLoader fxmlLoader;
        boolean flag = false;
        if (usuario.getPermiso_id() == 1){
            fxmlLoader = new FXMLLoader(getClass().getResource("AdminMainView.fxml"));
            flag = true;
        }else {
            fxmlLoader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        }
        try {
            Scene scene = new Scene(fxmlLoader.load(),800,600);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            if (flag){
                AdminMainController controller = fxmlLoader.getController();
                controller.setGitIcon();
                controller.setAtributes(diario,usuario,connection,controller);
                controller.setDiarioContent();
                ((Stage) this.usuario.getScene().getWindow()).setScene(scene);
            }else{
                MainController controller = fxmlLoader.getController();
                controller.setGitIcon();
                controller.setAtributes(diario,usuario,connection,controller);
                controller.setDiarioContent();
                ((Stage) this.usuario.getScene().getWindow()).setScene(scene);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}