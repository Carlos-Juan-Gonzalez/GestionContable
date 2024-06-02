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

public class AsientoController {
    @FXML
    protected Hyperlink link;
    @FXML
    protected Button action;
    @FXML
    protected TableView tabla;
    @FXML
    protected DatePicker fecha;
    @FXML
    protected TextField descripcion;
    private Asiento asiento;
    private Connection connection;
    private AsientoController controller;
    private AdminMainController adminparenteController;
    private MainController parenteController;
    private ObservableList<Anotacion> anotaciones = FXCollections.observableList(new ArrayList<>());
    private List<Cuenta> cuentas;


    public void link(){
        try {
            Runtime.getRuntime().exec("cmd.exe /c start iexplore https://github.com/Carlos-Juan-Gonzalez");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void setGitIcon(){
        ImageView imagen = new ImageView();
        imagen.setImage(new Image(GestionContableApp.class.getResource("icons/gitIcon.png").toString()));
        link.setGraphic(imagen);
        link.setBorder(null);
    }

    public void setAsientoContent(){
        createTableColumns();
        cambiarAction();
        fecha.setValue(LocalDate.parse(asiento.getFecha()));
        descripcion.setText(asiento.getDescripcion());
        createObservable();
        tabla.setItems(anotaciones);
    }

    public void cambiarAction(){
        action.setText("Actualizar Asiento");
        action.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                actualizar();
            }
        });
    }

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
    @FXML
    public void deleteRow(){
        if (anotaciones.size() != 0){
            anotaciones.remove(anotaciones.size()-1);
        }
    }

    @FXML void crearAsiento(){
        if(isAllSet()){
            asiento.setDescripcion(descripcion.getText());
            asiento.setFecha(fecha.getValue().toString());
            asiento.insertAsiento(connection);
            if(isBalanced()){
                for (Anotacion a : anotaciones){
                    a.insertAnotacion(connection,asiento.getId());
                }
                adminparenteController.setDiarioContent();
                ((Stage)tabla.getScene().getWindow()).close();
            }else{
                balancedAlert();
            }
        }else {
            allSetAler();
        }
    }

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
                    adminparenteController.setDiarioContent();
                    ((Stage)tabla.getScene().getWindow()).close();
                }
            }else{
              balancedAlert();
            }
        }else {
            allSetAler();
        }
    }

    public void actualizarAsiento(){
    asiento.setFecha(fecha.getValue().toString());
    asiento.setDescripcion(descripcion.getText());
    asiento.updateAsiento(connection);
    }

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

    public void createObservable(){
        Anotacion anotacion = new Anotacion();
        this.anotaciones = FXCollections.observableList(anotacion.constructAnotaciones(connection,asiento.getId()));
    }
    public ObservableList<Integer> createObservableComboBox(){
        Cuenta cuenta = new Cuenta();
        List<Integer> nombres = new ArrayList<>();
        this.cuentas = cuenta.constructCuentas(connection,asiento.getDiario_id());
        for (Cuenta c : cuentas){
            nombres.add(c.getCodigo());
        }
        return FXCollections.observableList(nombres);
    }

    public void setAtributes(Connection connection, AsientoController controller,Asiento asiento,Object parenteController){
        this.asiento = asiento;
        this.connection = connection;
        this.controller = controller;
        if (parenteController instanceof AdminMainController){
            this.adminparenteController = (AdminMainController) parenteController;
        }else {
            this.parenteController =(MainController) parenteController;
        }
    }

    public String getCuentaNombreByCodigo(int codigo){
        for (Cuenta c : cuentas){
            if (c.getCodigo() == codigo){
                return c.getNombre();
            }
        }
        return "";
    }

    public boolean isAllSet(){
        if (fecha.getValue().toString() == "" || descripcion.getText() == ""){
            return false;
        }
        for (Anotacion anotacion : anotaciones){
            if (anotacion.getCodigo_cuenta() == 0 ||(anotacion.getDebe() == 0 && anotacion.getHaber() == 0)){
                return false;
            }
        }
        return true;
    }
    public void allSetAler(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Anotaciones Vacias");
        alert.setHeaderText(null);
        alert.setContentText("Hay campos sin contenido en el asiento.");
        alert.showAndWait();
    }

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

    public void balancedAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Descuadre en el asiento");
        alert.setHeaderText(null);
        alert.setContentText("Las anotaciones del asiento estan descuadradas");
        alert.showAndWait();
    }

}
