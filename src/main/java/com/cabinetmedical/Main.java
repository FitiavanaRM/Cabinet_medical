package com.cabinetmedical;

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

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Systeme de Gestion de Cabinet Medical");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}