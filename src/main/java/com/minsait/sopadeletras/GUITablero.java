package com.minsait.sopadeletras;

/**
 *
 * @author Daniel Cruz
 */
import com.minsait.sopadeletras.controller.GUITableroController;
import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUITablero extends Application {

    private Stage stage;
    int tamanioTablero;
    FXMLLoader fxmlLoader;
    GUITableroController controller;
    Parent root;
    Scene scene;

    public GUITablero() throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GUITablero.fxml"));
        root = (Parent) fxmlLoader.load();
        controller = fxmlLoader.getController();
    }

    @Override
    public void start(Stage stage) throws Exception {
        scene = new Scene(root);
        this.stage = stage;
        stage.setTitle("Tablero");
        stage.setScene(scene);
        stage.setResizable(true);
    }

    public void crearTablero(int tamanioTablero) {
        controller.inicializarTablero(tamanioTablero);
    }

    public void lanzarVista() {
        stage.show();
    }

    public void empezarProceso(List<String> palabras, String dificultad) {
        controller.colocarPalabras(dificultad, palabras);
        controller.rellenaVacios();
        controller.imprimeTablero();
    }


}
