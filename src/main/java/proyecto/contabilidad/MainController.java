package proyecto.contabilidad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
    private Diario diario;
    private Usuario usuario;
    private Connection connection;
    private MainController controller;


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

    public void setDiarioContent(){
        createTableColumns();
        tabla.setRowFactory(tableView -> {
            TableRow<Asiento> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && row.getItem() != null) {
                    cambiarAsientoView(row.getItem());
                }
            });
            return row;
        });
        tabla.setItems(createObservable());
    }
    public void createTableColumns(){
        TableColumn numero = new TableColumn("Asiento");
        numero.setCellValueFactory(new PropertyValueFactory<Asiento,Integer>("numero"));
        numero.setMinWidth(100);
        numero.setStyle("-fx-alignment: CENTER;");

        TableColumn fecha = new TableColumn("Fecha");
        fecha.setCellValueFactory(new PropertyValueFactory<Asiento,String>("fecha"));
        fecha.setMinWidth(100);
        fecha.setStyle("-fx-alignment: CENTER;");

        TableColumn descripcion = new TableColumn("Descripción");
        descripcion.setCellValueFactory(new PropertyValueFactory<Asiento,String>("descripcion"));
        descripcion.setMinWidth(450);

        TableColumn balance = new TableColumn("Balance");
        balance.setCellValueFactory(new PropertyValueFactory<Asiento,String>("balance"));
        balance.setMinWidth(150);
        balance.setStyle("-fx-alignment: CENTER;");

        tabla.getColumns().addAll(numero,fecha,descripcion,balance);
    }

    public ObservableList<Asiento> createObservable(){
        Asiento asiento = new Asiento();
        return FXCollections.observableList(asiento.constructAsientos(connection, diario.getId()));
    }

    public void setAtributes(Diario diario,Usuario usuario,Connection connection,MainController controller){
        this.diario = diario;
        this.usuario = usuario;
        this.connection = connection;
        this.controller = controller;
    }

    public void cambiarAsientoView(Asiento asiento){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AsientoView.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(),500,350);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            AsientoController controller = fxmlLoader.getController();
            controller.setGitIcon();
            controller.setAtributes(connection,controller, asiento,this.controller);
            controller.setAsientoContent();
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
