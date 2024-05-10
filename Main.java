package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class responsible for launching the application.
 */
public class Main extends Application {

    /**
     * Method called by the JavaFX runtime to start the application.
     * @param primaryStage The primary stage of the application
     * @throws Exception If an exception occurs during application startup
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Employee.fxml"));
        Parent root = loader.load();

        // Set up the stage
        primaryStage.setTitle("Employee Management System");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    /**
     * Main method to launch the application.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
