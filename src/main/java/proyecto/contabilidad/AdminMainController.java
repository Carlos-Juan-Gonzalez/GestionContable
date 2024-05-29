package proyecto.contabilidad;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class AdminMainController {
    @FXML
    private Hyperlink link;

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
}
