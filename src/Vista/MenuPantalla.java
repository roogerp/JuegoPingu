package Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.util.ArrayList;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import Modelo.Pinguino;
import Controlador.GestorBBDD;




public class MenuPantalla {

	@FXML
	private TextField userField;
	@FXML
	private PasswordField passField;



	public List<Pinguino> pedirDatosJugadores() {
    
	// 1. Preguntar cuántos jugadores (2, 3 o 4)
    List<Integer> opciones = Arrays.asList(2, 3, 4);
    ChoiceDialog<Integer> dialogNum = new ChoiceDialog<>(2, opciones);
    dialogNum.setTitle("Configuración");
    dialogNum.setHeaderText("¿Cuántos pingüinos van a jugar?");
    dialogNum.setContentText("Número de jugadores:");

    
    Optional<Integer> numJugadores = dialogNum.showAndWait();
    if (!numJugadores.isPresent()) return null;

    
    List<Pinguino> jugadoresNuevos = new ArrayList<>();
    String[] colores = {"Rojo", "Azul", "Verde", "Amarillo"};

    // 2. Pedir nombre para cada uno
    for (int i = 0; i < numJugadores.get(); i++) {
        TextInputDialog nombreDialog = new TextInputDialog("Jugador " + (i + 1));
        nombreDialog.setTitle("Nombre del Jugador " + (i + 1));
        nombreDialog.setHeaderText("Color: " + colores[i]);
        nombreDialog.setContentText("Introduce tu nombre:");
        
        Optional<String> nombre = nombreDialog.showAndWait();
        jugadoresNuevos.add(new Pinguino(nombre.orElse("Jugador " + (i + 1)), colores[i]));
    }
    
    return jugadoresNuevos;
}



	@FXML
	private void handleLogin(ActionEvent event) {
	    String username = userField.getText();
	    String password = passField.getText();

	    if (username.isEmpty() || password.isEmpty()) {
	        System.out.println("Error: Rellena los campos.");
	        return;
	    }

	    // 1. Conectamos con la base de datos para validar
	    GestorBBDD db = new GestorBBDD();
	    if (db.conectar()) {
	        // 2. Validamos al usuario (este método encriptará la pass internamente)
	        if (db.validarUsuario(username, password)) {
	            try {
	                // 3. Si es correcto, cargamos la pantalla del juego
	                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/PantallaJuego.fxml"));
	                Parent root = loader.load();

	                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	                Scene scene = new Scene(root);
	                
	                String css = getClass().getResource("/Vista/estilos.css").toExternalForm();
	                scene.getStylesheets().add(css);

	                stage.setScene(scene);
	                stage.setTitle("Juego de Pingüinos - Bienvenido " + username);
	                stage.show();
	                
	                db.desconectar();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        } else {
	            // Si las credenciales no coinciden en Oracle
	            System.out.println("Acceso denegado: Usuario o contraseña incorrectos.");
	            db.desconectar();
	        }
	    } else {
	        System.out.println("Error: No se pudo conectar con la base de datos.");
	    }
	}


	@FXML
	private void handleRegister() {
	    String username = userField.getText();
	    String password = passField.getText();

	    if (username.isEmpty() || password.isEmpty()) {
	        System.out.println("Error: Rellena los campos para registrarte.");
	        return;
	    }

	    GestorBBDD db = new GestorBBDD();
	    if (db.conectar()) {
	        if (db.registrarUsuario(username, password)) {
	            System.out.println("¡Registro completado! Ahora puedes iniciar sesión.");

	        } else {
	            System.out.println("Error: El usuario ya existe o hubo un problema con Oracle.");
	        }
	        db.desconectar();
	    }
	}


	@FXML
	private void handleNewGame() {
	}

	@FXML
	private void handleSaveGame() {
	}

	@FXML
	private void handleLoadGame() {
	}

	@FXML
	private void handleQuitGame() {
		System.exit(0);
	}
}
