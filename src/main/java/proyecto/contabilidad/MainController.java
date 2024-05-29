package proyecto.contabilidad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController {
    @FXML
    protected Hyperlink link;
    @FXML
    protected TableView tabla;
    private Diario diario;
    private Usuario usuario;
    private Connection connection;


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

    public void setAtributes(Diario diario,Usuario usuario,Connection connection){
        this.diario = diario;
        this.usuario = usuario;
        this.connection = connection;
    }


}
