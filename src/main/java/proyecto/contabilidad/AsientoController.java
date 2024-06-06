package proyecto.contabilidad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AsientoController {
    @FXML
    protected Hyperlink link;
    @FXML
    protected Button action,delete,nuevo,borrar;
    @FXML
    protected TableView tabla;
    @FXML
    protected DatePicker fecha;
    @FXML
    protected TextField descripcion;
    private Asiento asiento;
    private Connection connection;
    private AdminMainController adminparenteController;
    private MainController parenteController;
    private ObservableList<Anotacion> anotaciones = FXCollections.observableList(new ArrayList<>());
    private List<Cuenta> cuentas;


    /**
     * Abre la direccion web de mi github en el navegador
     */
    public void link(){
        try {
            Runtime.getRuntime().exec("cmd.exe /c start iexplore https://github.com/Carlos-Juan-Gonzalez");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * settea el icono en el hyperlink
     */
    public void setGitIcon(){
        ImageView imagen = new ImageView();
        imagen.setImage(new Image(GestionContableApp.class.getResource("icons/gitIcon.png").toString()));
        link.setGraphic(imagen);
        link.setBorder(null);
    }

    /**
     * settea el contenido de la view de creacion y actualización de asientos
     */
    public void setAsientoContent(){
        createTableColumns();
        cambiarAction();
        fecha.setValue(LocalDate.parse(asiento.getFecha()));
        descripcion.setText(asiento.getDescripcion());
        createObservable();
        tabla.setItems(anotaciones);
    }

    /**
     * Cambia el evento onAction del botton action
     */
    public void cambiarAction(){
        action.setText("Actualizar");
        delete.setVisible(true);
        action.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                actualizar();
            }
        });
    }

    /**
     * crea una nueva row añadiento un objeto Anotacion en el observable del tableView
     */
    @FXML
    public void newRow(){
        Anotacion anotacion = new Anotacion();
        anotacion.setOrden(anotaciones.size()+1);
        if (anotaciones.size() == 0){
            anotaciones.add(anotacion);
            tabla.setItems(anotaciones);
        }else{
            if (anotaciones.get(anotaciones.size()-1).getCodigo_cuenta() != 0){
                anotaciones.add(anotacion);
                tabla.setItems(anotaciones);
            }
        }
    }
    /**
     * borra una row borrando un objeto Anotacion en el observable del tableView
     */
    @FXML
    public void deleteRow(){
        if (anotaciones.size() != 0){
            anotaciones.remove(anotaciones.size()-1);
        }
    }

    /**
     * Llama a todos los metodos necesarios para la insercion de un asiento en la tabla asientos
     */
    @FXML void crearAsiento(){
        if(isAllSet()){
            asiento.setDescripcion(descripcion.getText());
            asiento.setFecha(fecha.getValue().toString());
            asiento.insertAsiento(connection);
            if(isBalanced()){
                for (Anotacion a : anotaciones){
                    a.insertAnotacion(connection,asiento.getId());
                }
                if(adminparenteController == null){
                    parenteController.setDiarioContent();
                }else {
                    adminparenteController.setDiarioContent();
                }
                ((Stage)tabla.getScene().getWindow()).close();
            }else{
                balancedAlert();
            }
        }else {
            allSetAler();
        }
    }

    /**
     * Llama a todos los metodos necesarios para la actualizacion de un asiento
     */
    @FXML
    public void actualizar(){
        if(isAllSet()){
            actualizarAsiento();
            if(isBalanced()){
                List<Anotacion> dbAnotaciones = new Anotacion().constructAnotaciones(connection,asiento.getId());
                int indice = (dbAnotaciones.size() < anotaciones.size()) ? anotaciones.size() : dbAnotaciones.size();
                for (int i = 0; i < indice; i++) {
                    if (i <= (anotaciones.size()-1) || dbAnotaciones.size() < anotaciones.size()){
                        if (anotaciones.get(i).exists(connection)){
                            anotaciones.get(i).updateAnotacion(connection);
                        }else {
                            anotaciones.get(i).insertAnotacion(connection,asiento.getId());
                        }
                    }else {
                        dbAnotaciones.get(i).deleteAnotacion(connection);
                    }
                    if(adminparenteController == null){
                        parenteController.setDiarioContent();
                    }else {
                        adminparenteController.setDiarioContent();
                    }

                    ((Stage)tabla.getScene().getWindow()).close();
                }
            }else{
              balancedAlert();
            }
        }else {
            allSetAler();
        }
    }

    /**
     * LLama a todos los metodos del modelo para borrar un asiento en la base de datos
     */
    @FXML
    public void borrarAsiento(){
       if (deleteAlert()){
           asiento.deleteAsiento(connection);
           if(adminparenteController == null){
               parenteController.setDiarioContent();
           }else {
               adminparenteController.setDiarioContent();
           }
           ((Stage)tabla.getScene().getWindow()).close();
       }
    }

    /**
     * LLama a todos los metodos del modelo para actualizar un asiento en la base de datos
     */
    public void actualizarAsiento(){
    asiento.setFecha(fecha.getValue().toString());
    asiento.setDescripcion(descripcion.getText());
    asiento.updateAsiento(connection);
    }

    /**
     * Crea la columnas de la TableView asi como sus CellFactories y sus event OnEditEvent
     */
    public void createTableColumns(){
        TableColumn debe = new TableColumn("Debe");
        debe.setCellValueFactory(new PropertyValueFactory<Anotacion,Integer>("debe"));
        debe.setStyle("-fx-alignment: CENTER;");
        debe.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        debe.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Anotacion, Integer>>) event -> {
            Anotacion anotacion = event.getRowValue();
            anotacion.setHaber(0);
            anotacion.setDebe(event.getNewValue());
            anotaciones.set(anotacion.getOrden()-1, anotacion);
        });
        debe.setMinWidth(100);

        TableColumn haber = new TableColumn("Haber");
        haber.setCellValueFactory(new PropertyValueFactory<Anotacion,Integer>("haber"));
        haber.setStyle("-fx-alignment: CENTER;");
        haber.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        haber.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Anotacion, Integer>>) event -> {
            Anotacion anotacion = event.getRowValue();
            anotacion.setDebe(0);
            anotacion.setHaber(event.getNewValue());
            anotaciones.set(anotacion.getOrden()-1, anotacion);
        });
        haber.setMinWidth(100);

        TableColumn cuenta  = new TableColumn("Cuenta");
        cuenta.setCellValueFactory(new PropertyValueFactory<Anotacion,Integer>("codigo_cuenta"));
        cuenta.setStyle("-fx-alignment: CENTER;");
        cuenta.setCellFactory(ComboBoxTableCell.forTableColumn(createObservableComboBox()));
        cuenta.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Anotacion, Integer>>) event -> {
            Anotacion anotacion = event.getRowValue();
            anotacion.setCodigo_cuenta(event.getNewValue());
            anotacion.setCuenta_id(new Cuenta().getCuentaID(connection, anotacion.getCodigo_cuenta()));
            anotacion.setNombre_cuenta(getCuentaNombreByCodigo(anotacion.getCodigo_cuenta()));
            anotaciones.set(anotacion.getOrden()-1, anotacion);
        });
        cuenta.setMinWidth(100);

        TableColumn nombre = new TableColumn("Nombre");
        nombre.setCellValueFactory(new PropertyValueFactory<Anotacion,String>("nombre_cuenta"));
        nombre.setStyle("-fx-alignment: CENTER;");
        nombre.setMinWidth(200);

        tabla.getColumns().addAll(debe,haber,cuenta,nombre);
    }

    /**
     * Crea mediante el Modelo de Anotacion un observable de anotaciones
     */
    public void createObservable(){
        Anotacion anotacion = new Anotacion();
        this.anotaciones = FXCollections.observableList(anotacion.constructAnotaciones(connection,asiento.getId()));
    }

    /**
     * Crea mediante el Modelo de Cuenta un observable de codigos de cuenta
     * @return ObservableList<Integer>: lista observale de codigos de cuenta
     */
    public ObservableList<Integer> createObservableComboBox(){
        Cuenta cuenta = new Cuenta();
        List<Integer> nombres = new ArrayList<>();
        this.cuentas = cuenta.constructCuentas(connection,asiento.getDiario_id());
        for (Cuenta c : cuentas){
            nombres.add(c.getCodigo());
        }
        return FXCollections.observableList(nombres);
    }

    /**
     * Settea los atributos de clases necesarios para mostrar correctamente la view
     * y trabajar con ella
     * @param connection Connection: conexion a la base de datos
     * @param asiento Asiento: asiento a mostrar en la vista
     * @param parenteController MainController: Controller de el Stage Parent
     */
    public void setAtributes(Connection connection,Asiento asiento,Object parenteController){
        this.asiento = asiento;
        this.connection = connection;
        if (parenteController instanceof AdminMainController){
            this.adminparenteController = (AdminMainController) parenteController;
        }else {
            this.parenteController =(MainController) parenteController;
        }
    }

    /**
     * Busca y devuelve el nombre de cuenta asociado
     * al codigo pasado por parametros
     * @param codigo Int: codigo a buscar su nombre
     * @return String: nombre de la cuenta
     */
    public String getCuentaNombreByCodigo(int codigo){
        for (Cuenta c : cuentas){
            if (c.getCodigo() == codigo){
                return c.getNombre();
            }
        }
        return "";
    }

    /**
     * Comprueba que todos los campos tengan contenido
     * @return boolean: true si todo tiene contenido, false si faltan campos
     */
    public boolean isAllSet(){
        if (fecha.getValue() == null || descripcion.getText() == ""){
            return false;
        }
        if (anotaciones.size() == 0){
            return false;
        }

        return true;
    }

    /**
     * instancia y muesta un alert.AlertType.Error para
     * indicar que hay campos sin contenido en el asiente
     */
    public void allSetAler(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Anotaciones Vacias");
        alert.setHeaderText(null);
        alert.setContentText("Hay campos sin contenido en el asiento.");
        alert.showAndWait();
    }

    /**
     * comprueba que los campos debe y haber esten cuadrados
     * @return boolean: true si estan cuadrados, false si no
     */
    public boolean isBalanced(){
        int debe = 0, haber = 0;
        for (Anotacion anotacion : anotaciones){
            if (anotacion.getDebe() == 0){
                haber += anotacion.getHaber();
            }else {
                debe += anotacion.getDebe();
            }
        }
        if (debe == haber){
            return true;
        }else{
            return false;
        }
    }

    /**
     * instancia y muesta un alert.AlertType.Error para
     * indicar un descuadre en el asiento
     */
    public void balancedAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Descuadre en el asiento");
        alert.setHeaderText(null);
        alert.setContentText("Las anotaciones del asiento estan descuadradas");
        alert.showAndWait();
    }

    /**
     * instancia y muesta un alert.AlertType.Confirmation
     * @return Boolean: true si el ButtonType devuelto es OK, false si es Type Cancel
     */
    public boolean deleteAlert(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación de Borrado");
        alert.setHeaderText(null);
        alert.setContentText("Estas seguro de que desea borrar este asiento");
        Optional<ButtonType> eleccion = alert.showAndWait();
        if (eleccion.get() == ButtonType.OK){
            return true;
        }else {
            return false;
        }
    }

    /**
     * Desactiva los botones de funcionalidad los cuales un usuario invitado
     * no deveria tener acceso
     */
    public void prepareInvitado(){
        action.setVisible(false);
        delete.setVisible(false);
        nuevo.setVisible(false);
        borrar.setVisible(false);
    }

}
