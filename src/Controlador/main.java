package Controlador;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * Clase principal del juego.
 * Inicia JavaFX y carga la ventana principal.
 */

public class main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		
		// Carga el archivo FXML de la pantalla principal
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/MenuPantalla.fxml"));
		Parent root = loader.load();

		// Crea la escena principal
		Scene scene = new Scene(root);

		// Aplica los estilos CSS del proyecto
		String css = getClass().getResource("/Vista/estilos.css").toExternalForm();
		scene.getStylesheets().add(css);

		// Configuración de la ventana
		stage.setTitle("Juego Pingüinos");
		stage.setScene(scene);

		// Muestra la ventana
		stage.show();
	}

	// Método que inicia la aplicación
	public static void main(String[] args) {
		launch(args);
	}
}