package com.cabinetmedical;

import com.cabinetmedical.util.ThemeManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Point d'entree de l'application JavaFX.
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cabinetmedical/fxml/login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        ThemeManager.applyToScene(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Systeme de Gestion de Cabinet Medical");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}