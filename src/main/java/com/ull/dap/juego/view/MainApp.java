package com.ull.dap.juego.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.ull.dap.juego.control.GestorSonido;

import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL fxmlLocation = getClass().getResource("/com/ull/dap/juego/view/MainView.fxml");
        if (fxmlLocation == null) {
            System.err.println("No se pudo encontrar el archivo FXML. Asegúrate de que la ruta es correcta.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();
        
        primaryStage.setTitle("Simulador de Estrategia");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // Iniciar la música de fondo
        GestorSonido.getInstancia().reproducirMusicaFondo();
    }

    @Override
    public void stop() throws Exception {
        // Detener la música de fondo al cerrar la aplicación
        GestorSonido.getInstancia().detenerMusicaFondo();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
