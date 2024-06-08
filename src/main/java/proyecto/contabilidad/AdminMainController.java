package proyecto.contabilidad;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import java.io.IOException;
import java.util.List;


public class AdminMainController extends MainController {

    @FXML
    protected TextField codigo,nombre;
    @FXML
    protected Text alert;
    @FXML
    protected ComboBox permiso,diario_id,diarios;
    @FXML
    protected PasswordField contraseña;
    private MainController controller;
    private List<Diario> listDiarios;

    /**
     * instancia la vista CrearCuentaView y llama al metodo para crear el stage
     */
    @FXML
    public void openCrearCuentaView(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CrearCuentaView.fxml"));
        createStage(loader);
    }
    /**
     * instancia la vista CrearDiarioView y llama al metodo para crear el stage
     */
    @FXML
    public void openCrearDiarioView(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CrearDiarioView.fxml"));
        createStage(loader);
    }
    /**
     * instancia la vista CambiarDiarioView y llama al metodo para crear el stage
     */
    @FXML
    public void OpenCambiarDiarioView(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CambiarDiarioView.fxml"));
        createStage(loader).setComboBoxDiarios();
    }
    /**
     * instancia la vista CrearUsuarioView y llama al metodo para crear el stage
     */
    @FXML
    public void openCrearUsuarioView(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CrearUsuarioView.fxml"));
        AdminMainController controller = createStage(loader);
        controller.setComboBoxs();
        controller.setComboBoxDiarios_id();
    }

    /**
     * prepara el stage y carga la escena de fxml pasada por parametros
     * mediante un FXMLLoader
     * @param loader FXMLLoader: loader para cargar la vista
     * @return AdminMainController: controllador de la vista cargada
     */
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

    /**
     * Llama a los metodos necesarios para hacer un insert a la tabla cuentas
     * manda una alerta ante un fallo
     */
    @FXML
    public void crearCuenta(){
        if (validarCuenta()){
            if (cuentaExists()){
                alert.setVisible(true);
                alert.setText("Codigo de cuenta ya existente");
            }else {
                Cuenta cuenta = new Cuenta(Integer.parseInt(codigo.getText()),nombre.getText());
                cuenta.insertCuenta(controller.getConnection(),controller.getDiario().getId());
                ((Stage)nombre.getScene().getWindow()).close();
            }
        }else {
            alert.setVisible(true);
            alert.setText("Hay campos sin contenidos o con formato erroneo");
        }
    }


    /**
     * valida que todos los campos necesarios para la creación de una cuenta
     * tengan contenido
     * @return Boolean: true si todos los campos tienen contenido, false si no
     */
    public boolean validarCuenta(){
        if (codigo.getText().equals("") || nombre.getText().equals("")){
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

    /**
     * Comprueba que si el codigo de cuenta ya existe
     * @return booolean: true si existe, false si no
     */
    public boolean cuentaExists(){
        Cuenta cuenta = new Cuenta();
        try {
            if (cuenta.cuentaExiste(controller.getConnection(),controller.getDiario().getId(),
                    Integer.parseInt(codigo.getText()))){
                return true;
            }else {
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * Llama a los metodos necesarios para hacer un insert a la tabla diarios
     */
    @FXML
    public void crearDiario(){
        if (validarDiario()){
            Diario diario = new Diario(nombre.getText());
            diario.insertDiario(controller.getConnection());
            ((Stage)nombre.getScene().getWindow()).close();
        }
    }

    /**
     * cambia el atributo Diario
     */
    @FXML
    public void cambiarDiario(){
        Diario diario = new Diario();
        diario = new Diario(diario.getDiarioIDByName(controller.getConnection(),diarios.getValue().toString()),diarios.getValue().toString());
        controller.setDiario(diario);
        controller.setDiarioContent();
        ((Stage)diarios.getScene().getWindow()).close();
    }

    /**
     * valida que todos los campos necesarios para la creación de un diario
     * tengan contenido
     * @return Boolean: true si todos los campos tienen contenido, false si no
     */
    public boolean validarDiario(){
        if (nombre.getText() == ""){
            return false;
        }else {
            return true;
        }
    }

    /**
     * Llama a los metodos necesarios para hacer un insert a la tabla usuarios
     */
    @FXML
    public void crearUsuario(){

        if (!validarUsuario()){
            alert.setVisible(true);
            alert.setText("Hay campos sin contenidos o con formato erroneo");
        }else{
            if (usuarioExiste()){
                alert.setVisible(true);
                alert.setText("Ya existe un usuario asociado a ese nombre");
            }else {
                if (contraseñaExiste()){
                    alert.setVisible(true);
                    alert.setText("La contraseña ya esta en uso");
                }
                else {
                    Usuario usuario = new Usuario(permisoIntoID(permiso.getValue().toString()),
                            new Diario().getDiarioIDByName(controller.getConnection(),diario_id.getValue().toString())
                            ,nombre.getText(),contraseña.getText());
                    usuario.insertUsuario(controller.getConnection());
                    ((Stage)nombre.getScene().getWindow()).close();
                }
            }
        }
    }

    /**
     * valida que todos los campos necesarios para la creación de una Usuario
     * tengan contenido
     * @return Boolean: true si todos los campos tienen contenido, false si no
     */
    public boolean validarUsuario(){
        if (nombre.getText().equals("") || contraseña.getText().equals("")){
            return false;
        }else {
            return true;
        }
    }

    /**
     * Comprueba que si el codigo de cuenta ya existe
     * @return booolean: true si existe, false si no
     */
    public boolean usuarioExiste(){
        Usuario usuario = new Usuario();
        try {
            if (usuario.usuarioExiste(controller.getConnection(),nombre.getText(),controller.getDiario().getId())){
                return true;
            }else {
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * Comprueba que si el codigo de cuenta ya existe
     * @return booolean: true si existe, false si no
     */
    public boolean contraseñaExiste(){
        Usuario usuario = new Usuario();
        try {
            if (usuario.contraseñaExiste(controller.getConnection(),contraseña.getText())){
                return true;
            }else {
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * parsea un string de permiso a su id asociado
     * @param permiso String: permiso a parsear
     * @return Int: el id asociado al permiso
     */
    public int permisoIntoID(String permiso){
        switch (permiso){
            case "admin":
                return 1;
            case "usuario":
                return 2;
            case "invitado":
                return 3;
        }
        return 0;
    }

    /**
     * setea la lista de permisos al combobox permisos
     */
    public void setComboBoxs(){
        permiso.getItems().addAll("admin","usuario","invitado");
        permiso.setValue("usuario");
    }

    /**
     * setea la lista de permisos al combobox diarios
     */
    public void setComboBoxDiarios(){
        Diario diario = new Diario();
        diarios.getItems().addAll(diario.constructDiarios(controller.getConnection()));
        diarios.setValue(diario.constructDiarios(controller.getConnection()).get(0));
    }
    /**
     * setea la lista de permisos al combobox diarios_id
     */
    public void setComboBoxDiarios_id(){
        Diario diario = new Diario();
        diario_id.getItems().addAll(diario.constructDiarios(controller.getConnection()));
        diario_id.setValue(diario.constructDiarios(controller.getConnection()).get(0));
    }

    public MainController getController() {
        return controller;
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }
}
