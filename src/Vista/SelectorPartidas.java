package Vista;

import Controlador.GestorBBDD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.*;

public class SelectorPartidas {

    private int idSeleccionado = -1;

    public int mostrarVentana() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal
        stage.setTitle("Cargar Partida Guardada");

        ListView<String> listaPartidas = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();

        // 1. Conexión a DB para traer datos REALES
        GestorBBDD db = new GestorBBDD();
        if (db.conectar()) {
            try {
                // Obtenemos la conexión que ya tiene el gestor
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.3.26:1521/XEPDB2", "DM1_2526_GRUP05", "AGRUP05");
                Statement stmt = con.createStatement();
                
                // Buscamos todas las partidas, las más nuevas primero
                ResultSet rs = stmt.executeQuery("SELECT id FROM PARTIDAS ORDER BY id DESC");

                while (rs.next()) {
                    // Añadimos cada ID encontrado en Oracle a la lista visual
                    items.add("Partida ID: " + rs.getInt("id"));
                }
                
                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException e) {
                System.err.println("Error al listar: " + e.getMessage());
            }
            db.desconectar();
        }

        listaPartidas.setItems(items);

        // 2. Botón para confirmar selección
        Button btnCargar = new Button("Cargar Seleccionada");
        btnCargar.setOnAction(e -> {
            String seleccionado = listaPartidas.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                // Filtramos el texto para quedarnos solo con el número ID
                idSeleccionado = Integer.parseInt(seleccionado.replaceAll("[^0-9]", ""));
                stage.close();
            }
        });

        // 3. Diseño de la ventana
        VBox layout = new VBox(10, new Label("Selecciona una partida:"), listaPartidas, btnCargar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #f4f4f4;");

        Scene scene = new Scene(layout, 300, 400);
        stage.setScene(scene);
        stage.showAndWait(); // Pausa el programa hasta que se cierra esta ventana

        return idSeleccionado;
    }
}

