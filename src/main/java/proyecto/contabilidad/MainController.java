package proyecto.contabilidad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.sql.Connection;

public class MainController {
    @FXML
    protected Hyperlink link;
    @FXML
    protected TableView tabla;
    @FXML
    protected Button nuevo;
    private Diario diario;
    private Usuario usuario;
    private Connection connection;
    private MainController controller;


    /**
     * Redirecciona a el enlaze de git hub en el navegado predetermidano
     */
    public void link() {
        try {
            Runtime.getRuntime().exec("cmd.exe /c start iexplore https://github.com/Carlos-Juan-Gonzalez");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * settea la imagen de github a el HyperLink
     */
    public void setGitIcon() {
        ImageView imagen = new ImageView();
        imagen.setImage(new Image(GestionContableApp.class.getResource("icons/gitIcon.png").toString()));
        link.setGraphic(imagen);
        link.setBorder(null);
    }

    /**
     * añade mediante un observable el contenido de la tabla
     */
    public void setDiarioContent() {
        tabla.getColumns().clear();
        createTableColumns();
        tabla.setRowFactory(tableView -> {
            TableRow<Asiento> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && row.getItem() != null) {
                    cambiarAsientoViewActualizar(row.getItem());
                }
            });
            return row;
        });
        tabla.setItems(createObservable());
    }

    /**
     * crea y añade las columnas de la TableView
     */
    public void createTableColumns() {
        TableColumn numero = new TableColumn("Asiento");
        numero.setCellValueFactory(new PropertyValueFactory<Asiento, Integer>("numero"));
        numero.setMinWidth(100);
        numero.setStyle("-fx-alignment: CENTER;");

        TableColumn fecha = new TableColumn("Fecha");
        fecha.setCellValueFactory(new PropertyValueFactory<Asiento, String>("fecha"));
        fecha.setMinWidth(100);
        fecha.setStyle("-fx-alignment: CENTER;");

        TableColumn descripcion = new TableColumn("Descripción");
        descripcion.setCellValueFactory(new PropertyValueFactory<Asiento, String>("descripcion"));
        descripcion.setMinWidth(450);

        TableColumn balance = new TableColumn("Balance");
        balance.setCellValueFactory(new PropertyValueFactory<Asiento, String>("balance"));
        balance.setMinWidth(150);
        balance.setStyle("-fx-alignment: CENTER;");

        tabla.getColumns().addAll(numero, fecha, descripcion, balance);
    }

    /**
     * Crea mediante el Modelo Un observable de los asientos del diario activo
     * @return ObservableList<Asiento>: lista observable de asientos
     */
    public ObservableList<Asiento> createObservable() {
        Asiento asiento = new Asiento();
        return FXCollections.observableList(asiento.constructAsientos(connection, diario.getId()));
    }

    /**
     * Settea todos los atributos de clase necesarios
     * para el correcto funcionamiento de la vista
     * @param diario Diario: diario abierto
     * @param usuario Usuario: usuario activo
     * @param connection Connection: conexion a la base de datos
     * @param controller MainController: controller de la vista
     */
    public void setAtributes(Diario diario, Usuario usuario, Connection connection, MainController controller) {
        this.diario = diario;
        this.usuario = usuario;
        this.connection = connection;
        this.controller = controller;
    }

    /**
     * Crea y muestra la vista asientoView
     */
    public void cambiarAsientoView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AsientoView.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 500, 350);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            AsientoController controller = fxmlLoader.getController();
            controller.setGitIcon();
            controller.setAtributes(connection, new Asiento(diario.getId(), createObservable().size() + 1), this.controller);
            controller.createTableColumns();
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Gestor Contable");
            stage.getIcons().add(new Image(GestionContableApp.class.getResource("icons/gestorContableIcon.png").toString()));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea y muestra la vista asientoView en modo actualizar asiento
     * @param asiento Asiento: asiento a actualizar
     */
    public void cambiarAsientoViewActualizar(Asiento asiento) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AsientoView.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 500, 350);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            AsientoController controller = fxmlLoader.getController();
            controller.setGitIcon();
            controller.setAtributes(connection, asiento, this.controller);
            controller.setAsientoContent();
            if(usuario.getPermiso_id() == 3){
                controller.prepareInvitado();
            }
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Gestor Contable");
            stage.getIcons().add(new Image(GestionContableApp.class.getResource("icons/gestorContableIcon.png").toString()));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Carga y muestra la vista LoginView
     */
    @FXML
    public void cerrarSesion(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginView.fxml"));
        try {

            Scene scene = new Scene(fxmlLoader.load(), 500, 350);
            GestionContableController controller = fxmlLoader.getController();
            controller.setGitIcon();
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            Stage stage = (Stage) tabla.getScene().getWindow();
            stage.setTitle("Gestion Contable");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(new Image(GestionContableApp.class.getResource("icons/gestorContableIcon.png").toString()));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void prepareInvitado(){
        nuevo.setVisible(false);
    }

    public Hyperlink getLink() {
        return link;
    }

    public void setLink(Hyperlink link) {
        this.link = link;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Diario getDiario() {
        return diario;
    }

    public void setDiario(Diario diario) {
        this.diario = diario;
    }
}