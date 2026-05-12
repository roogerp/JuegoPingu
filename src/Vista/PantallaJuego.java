package Vista;

import Controlador.GestorPartida;
import Modelo.*;
import Controlador.GestorBBDD;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Optional;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import java.util.List;


public class PantallaJuego {

    @FXML private Text rapido_t, lento_t, peces_t, nieve_t, eventos, dadoResultText;
    @FXML private GridPane tablero;
    @FXML private Button dado;

    private Circle P1, P2, P3, P4; // Círculos que representan a los pingüinos
    private GestorPartida gestorPartida;
    private static final int COLUMNS = 10;
    private static final int ROWS = 5;

    
   // Configuración inicial al abrir la pantalla
    @FXML
    private void initialize() {
        this.gestorPartida = new GestorPartida();
        gestorPartida.nuevaPartida();
                    
     // Ventana emergente para elegir cuántos juegan (2-4)
        List<Integer> opciones = java.util.Arrays.asList(2, 3, 4);
        ChoiceDialog<Integer> dialogNum = new ChoiceDialog<>(2, opciones);
        dialogNum.setTitle("Nueva Partida");
        dialogNum.setHeaderText("Configuración de jugadores");
        dialogNum.setContentText("¿Cuántos pingüinos van a jugar?");

        Optional<Integer> numJugadores = dialogNum.showAndWait();
        
        // Si cancela, por defecto ponemos 2 para que no de error
        int cantidad = numJugadores.orElse(2);

     // Bucle para pedir los nombres de cada jugador según la cantidad elegida
        String[] colores = {"Rojo", "Azul", "Verde", "Amarillo"};
        for (int i = 0; i < cantidad; i++) {
            TextInputDialog nombreDialog = new TextInputDialog("Jugador " + (i + 1));
            nombreDialog.setTitle("Nombre Jugador " + (i + 1));
            nombreDialog.setHeaderText("Color: " + colores[i]);
            nombreDialog.setContentText("Introduce nombre:");
            
            Optional<String> nombre = nombreDialog.showAndWait();
            Pinguino nuevoP = new Pinguino(nombre.orElse("Jugador " + (i + 1)), colores[i]);
            gestorPartida.getPartida().añadirJugador(nuevoP);
        }

     // Limpiar tablero y preparar la parte visual
        tablero.getChildren().clear();               
        crearFichas();
        generarTableroVisual();
        actualizarInterfaz();
    }

    // Crea los círculos de colores según el número de jugadores
    private void crearFichas() {
        int total = gestorPartida.getPartida().getJugadores().size();

        P1 = new Circle(18); P1.setFill(Color.web("#ff416c"));
        configurarFichaInicial(P1, -20, -20);
        tablero.getChildren().add(P1);

        P2 = new Circle(18); P2.setFill(Color.web("#4facfe"));
        configurarFichaInicial(P2, 20, -20);
        tablero.getChildren().add(P2);

        if (total >= 3) {
            P3 = new Circle(18); P3.setFill(Color.web("#00f2fe"));
            configurarFichaInicial(P3, -20, 20);
            tablero.getChildren().add(P3);
        }

        if (total == 4) {
            P4 = new Circle(18); P4.setFill(Color.web("#f1c40f"));
            configurarFichaInicial(P4, 20, 20);
            tablero.getChildren().add(P4);
        }
    }

    // Coloca la ficha en la casilla 0 (salida) con un pequeño desfase visual
    private void configurarFichaInicial(Circle c, int tx, int ty) {
        c.setTranslateX(tx); 
        c.setTranslateY(ty);
        GridPane.setRowIndex(c, 0); 
        GridPane.setColumnIndex(c, 0);
    }
    
    // Dibuja las casillas, iconos (osos, trineos, etc.) y números en el GridPane
    private void generarTableroVisual() {
       
        ArrayList<Casilla> casillas = gestorPartida.getPartida().getTablero().getCasillas();
        int index = 0;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (index >= 50) break;

                StackPane panelCasilla = new StackPane();
                panelCasilla.setStyle("-fx-min-width: 90; -fx-min-height: 90;");
                Text icono = new Text();
                icono.setStyle("-fx-font-size: 30px;");

             // Asignar emoji según el tipo de casilla
                if (index < casillas.size()) {
                    Casilla c = casillas.get(index);
                    if (c instanceof Oso) icono.setText("🐻");
                    else if (c instanceof Agujero) icono.setText("🕳️");
                    else if (c instanceof Trineo) icono.setText("▶▶"); 
                    else if (c instanceof Interrogante) icono.setText("❓");
                    else icono.setText("❄️");
                } else {
                    icono.setText("❄️");
                }

                Text numero = new Text(String.valueOf(index + 1));
                numero.getStyleClass().add("numero-casilla");
                StackPane.setAlignment(numero, Pos.TOP_LEFT);

                panelCasilla.getChildren().addAll(icono, numero);
                tablero.add(panelCasilla, col + 1, row); // col+1 por la columna 0
                index++;
            }
        }

     // Asegurar que las fichas queden por encima del dibujo de las casillas
        if (P1 != null) P1.toFront(); if (P2 != null) P2.toFront();
        if (P3 != null) P3.toFront(); if (P4 != null) P4.toFront();

    }

    @FXML
    private void handleDado(ActionEvent event) {
        Pinguino pinguActual = gestorPartida.getPartida().getJugadorActual();
        if (pinguActual == null) return;
        
     // Comprobar si el pingüino está congelado por una tormenta
        if (pinguActual.getTurnosSaltados() > 0) {
            pinguActual.setTurnosSaltados(pinguActual.getTurnosSaltados() - 1);
            registrarEvento("❄️ " + pinguActual.getNombre() + " está congelado y pierde este turno.");            
            gestorPartida.getPartida().siguienteTurno();
            actualizarInterfaz();
            return;
        }

     // Determinar qué dado usar (prioriza especiales si se seleccionaron)
        Dado dadoUsar = null;
        for (Item item : pinguActual.getInventario().getDados()) {
            if (item instanceof Dado) {
                dadoUsar = (Dado) item; break;
            }
        }
                
        Dado d = (dadoUsar == null) ? new DadoNormal() : dadoUsar;

        int posVieja = pinguActual.getPosicion();
        int resultado = gestorPartida.tirarDado(pinguActual, d);
        
        if (dadoUsar != null) {
            pinguActual.getInventario().getDados().remove(dadoUsar);
        }
        
        dadoResultText.setText("Resultado de la tirada: " + resultado);
        
     // Mover la ficha visualmente
        Circle circuloVisual = obtenerFichaVisual(pinguActual.getColor());
        animarFicha(circuloVisual, posVieja, pinguActual.getPosicion(), false);
    }

    	// Refresca los textos de inventario (peces, bolas, dados) del jugador actual
    private void actualizarInterfaz() {
        Pinguino p = gestorPartida.getPartida().getJugadorActual();
        if (p == null) return;
        
        Inventario inv = p.getInventario();
        int contadorRapido = 0;
        int contadorLento = 0;
        
        for (Item item : inv.getDados()) {
            if (item instanceof DadoRapido) contadorRapido++;
            else if (item instanceof DadoLento) contadorLento++;
        }

        peces_t.setText("Peces: " + inv.getPeces() + "/2"); 
        nieve_t.setText("Bolas Nieve: " + inv.getBolas() + "/6");        
        rapido_t.setText("Dado Rápido: " + contadorRapido + "/3");
        lento_t.setText("Dado Lento: " + contadorLento + "/3");       
        registrarEvento("Turno de: " + p.getColor());
    }

    // Retorna el círculo correspondiente al color del jugador
    private Circle obtenerFichaVisual(String color) {
        if (color == null) return P1;
        switch (color.toLowerCase()) {
            case "rojo": return P1;
            case "azul": return P2;
            case "verde": return P3;
            case "naranja": case "amarillo": return P4;
            default: return P1;
        }
    }

    // Controla el movimiento suave de la ficha por el tablero
    private void animarFicha(Circle ficha, int desde, int hasta, boolean extraTurn) {
        dado.setDisable(true);
        if (hasta >= 50) hasta = 49;
        if (hasta < 0) hasta = 0;

        int r1, c1, r2, c2;

        // Posición desde
        if (desde < 0) {
            r1 = 0; c1 = 0; // casilla de salida
        } else {
            r1 = desde / COLUMNS;
            c1 = (desde % COLUMNS) + 1; // +1 por columna de salida
        }

        // Posición hasta
        if (hasta < 0) {
            r2 = 0; c2 = 0;
        } else {
            r2 = hasta / COLUMNS;
            c2 = (hasta % COLUMNS) + 1; // +1 por columna de salida
        }

        double dx = (c2 - c1) * 90;
        double dy = (r2 - r1) * 90;

        final int posicionFinal = hasta; 
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(400), ficha);
        tt.setByX(dx); tt.setByY(dy);
        tt.setOnFinished(e -> {
            
        	// Reajuste de la posición real en el GridPane al terminar la animación
        	ficha.setTranslateX(0); ficha.setTranslateY(0);
            
        	int r2_final = posicionFinal / COLUMNS;
            int c2_final = (posicionFinal % COLUMNS) + 1;
            GridPane.setRowIndex(ficha, r2_final);
            GridPane.setColumnIndex(ficha, c2_final);
            
            if (posicionFinal >= 49) { 
                javafx.application.Platform.runLater(() -> {
                    finalizarJuego(gestorPartida.getPartida().getJugadorActual());
                });
            } else {
                dado.setDisable(false);
                if (!extraTurn) {
                	// Si no es un movimiento extra, procesamos qué hay en la casilla
                    javafx.application.Platform.runLater(() -> {
                        procesarCasilla(gestorPartida.getPartida().getJugadorActual());
                    });                   
                } else {
                    actualizarInterfaz();
                }
            }
        });
        tt.play();
    }
    
    // Lógica de colisión con obstáculos (Oso, Agujero, Trineo, etc.)
    private void procesarCasilla(Pinguino p) {
        int pos = p.getPosicion();
        if (pos >= 49) {
            finalizarJuego(p);
            return;
        }
        
        Casilla c = gestorPartida.getPartida().getTablero().getCasillas().get(pos);
        
        if (c instanceof Oso) {
            if (p.getInventario().getPeces() > 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("¡Ataque de Oso!");
                alert.setHeaderText("¡Un oso te ha atacado!");
                alert.setContentText("Tienes peces. ¿Quieres usar uno para sobornar al oso?");
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    p.getInventario().usarPez();
                    registrarEvento("🐟 " + p.getNombre() + " sobornó al Oso."); 
                    gestorPartida.getPartida().siguienteTurno();
                    actualizarInterfaz();
                } else {
                    registrarEvento("🐻 ¡El Oso atacó! " + p.getNombre() + " vuelve al inicio."); 
                    p.setPosicion(0);
                    animarFicha(obtenerFichaVisual(p.getColor()), pos, 0, true);
                    gestorPartida.getPartida().siguienteTurno();
                }
            } else {
                registrarEvento("🐻 ¡El Oso atacó! " + p.getNombre() + " vuelve al inicio."); 
                p.setPosicion(0);
                animarFicha(obtenerFichaVisual(p.getColor()), pos, 0, true);
                gestorPartida.getPartida().siguienteTurno();
            }
        } else if (c instanceof Agujero) {
            int agujeroAnterior = 0;
            for (int i = pos - 1; i >= 0; i--) {
                if (gestorPartida.getPartida().getTablero().getCasillas().get(i) instanceof Agujero) {
                    agujeroAnterior = i;
                    break;
                }
            }
            registrarEvento("🕳️ " + p.getNombre() + " cayó al agujero. Retrocede a " + (agujeroAnterior + 1)); 
            p.setPosicion(agujeroAnterior);
            animarFicha(obtenerFichaVisual(p.getColor()), pos, agujeroAnterior, true);
            gestorPartida.getPartida().siguienteTurno();
            
        } else if (c instanceof Trineo) {
            int trineoSiguiente = pos;
            for (int i = pos + 1; i < gestorPartida.getPartida().getTablero().getCasillas().size(); i++) {
                if (gestorPartida.getPartida().getTablero().getCasillas().get(i) instanceof Trineo) {
                    trineoSiguiente = i;
                    break;
                }
            }
            if (trineoSiguiente != pos) {
                registrarEvento("▶▶ ¡Trineo! " + p.getNombre() + " avanza a " + (trineoSiguiente + 1));
                p.setPosicion(trineoSiguiente);
                animarFicha(obtenerFichaVisual(p.getColor()), pos, trineoSiguiente, true);
            } else {
                registrarEvento(p.getNombre() + " está en el último trineo.");
            }
            gestorPartida.getPartida().siguienteTurno();
            
        } else if (c instanceof Interrogante) {
            int randomEvent = (int)(Math.random() * 6); // Rango de 0 a 5
            String msg = "";
            
            if (randomEvent == 0) { p.getInventario().añadirDadoRapido(); msg = "🚀 Encontró Dado Rápido"; }
            else if (randomEvent == 1) { p.getInventario().añadirDadoLento(); msg = "🐢 Encontró Dado Lento"; }
            else if (randomEvent == 2) { p.getInventario().añadirPez(); msg = "🐟 Pescó un Pez"; }
            else if (randomEvent == 3) { p.getInventario().añadirBolas(2); msg = "❄️ Obtuvo 2 Bolas Nieve"; }
            else if (randomEvent == 4) { 
                // EVENTO: Perder objeto
                if (p.getInventario().getBolas() > 0) {
                    p.getInventario().usarBola();
                    msg = "💨 ¡Viento fuerte! Perdió una Bola de Nieve";
                } else if (p.getInventario().getPeces() > 0) {
                    p.getInventario().usarPez();
                    msg = "🦅 ¡Un águila! Perdió un Pez";
                } else {
                    msg = "❓ No tenías objetos, ¡qué suerte!";
                }
            }
            else {
                // EVENTO: Perder turno
                p.setTurnosSaltados(1); 
                msg = "🥶 ¡Tormenta de nieve! Pierdes el próximo turno";
            }
            
            registrarEvento("❓ " + p.getNombre() + ": " + msg); 
            
            gestorPartida.getPartida().siguienteTurno();
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
            pause.setOnFinished(ev -> actualizarInterfaz());
            pause.play();
        } else {
            gestorPartida.getPartida().siguienteTurno();
            actualizarInterfaz();
        }
    }


    // Muestra el ganador y vuelve al menú principal
    private void finalizarJuego(Pinguino ganador) {
        dado.setDisable(true); // Bloquea el dado para que no se pueda seguir jugando
        eventos.setText("¡" + ganador.getNombre().toUpperCase() + " HA GANADO!");
        
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("¡Fin de la partida!");
        alerta.setHeaderText("¡Victoria para el equipo " + ganador.getColor() + "!");
        alerta.setContentText("El pingüino " + ganador.getNombre() + " ha llegado a la meta.");
        
        // Al pulsar OK en la alerta, volvemos al menú
        alerta.showAndWait().ifPresent(response -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/MenuPantalla.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) tablero.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    
    // Botones de la interfaz para usar objetos manualmente
    @FXML private void handleRapido() { 
        Pinguino p = gestorPartida.getPartida().getJugadorActual();
        if (p == null) return;
        
        DadoRapido dr = null;
        for (Item item : p.getInventario().getDados()) {
            if (item instanceof DadoRapido) { dr = (DadoRapido) item; break; }
        }
        
        if (dr != null) {
            p.getInventario().getDados().remove(dr);
            int posVieja = p.getPosicion();
            // Lógica de Dado rápido: 5 a 10
            int resultado = 5 + (int)(Math.random() * 6);
            p.setPosicion(p.getPosicion() + resultado);
            eventos.setText(p.getNombre() + " usó Dado Rápido y avanzó " + resultado);
            animarFicha(obtenerFichaVisual(p.getColor()), posVieja, p.getPosicion(), false);
        } else {
            eventos.setText("No tienes Dados Rápidos!");
        }
    }
    // Botones de la interfaz para usar objetos manualmente
    @FXML private void handleLento() { 
        Pinguino p = gestorPartida.getPartida().getJugadorActual();
        if (p == null) return;
        
        DadoLento dl = null;
        for (Item item : p.getInventario().getDados()) {
            if (item instanceof DadoLento) { dl = (DadoLento) item; break; }
        }
        
        if (dl != null) {
            p.getInventario().getDados().remove(dl);
            int posVieja = p.getPosicion();
            int resultado = 1 + (int)(Math.random() * 3);
            p.setPosicion(p.getPosicion() + resultado);
            eventos.setText(p.getNombre() + " usó Dado Lento y avanzó " + resultado);
            animarFicha(obtenerFichaVisual(p.getColor()), posVieja, p.getPosicion(), false);
        } else {
            eventos.setText("No tienes Dados Lentos!");
        }
    }
    
    @FXML private void handlePeces() { 
        Pinguino p = gestorPartida.getPartida().getJugadorActual();
        if (p == null) return;
        
        if (p.getInventario().usarPez()) {
            eventos.setText(p.getNombre() + " alimentó al oso/foca y se salvó!");
            actualizarInterfaz();
        } else {
            eventos.setText("No tienes Peces!");
        }
    }
    
    @FXML private void handleNieve() { 
        Pinguino p = gestorPartida.getPartida().getJugadorActual();
        if (p == null) return;
        
        if (p.getInventario().usarBola()) {
            eventos.setText(p.getNombre() + " lanzó una bola de nieve!");
            actualizarInterfaz();
        } else {
            eventos.setText("No tienes Bolas de Nieve!");
        }
    }
    
    @FXML
    private void handleBotonCargar() {
        SelectorPartidas selector = new SelectorPartidas();
        int id = selector.mostrarVentana(); 

        if (id != -1) {
            GestorBBDD db = new GestorBBDD();
            if (db.conectar()) {
                Partida p = db.cargarBBDD(id);
                if (p != null) {
                    this.gestorPartida.setPartida(p);
                    
                    
                    tablero.getChildren().clear();
                    crearFichas(); 
                    generarTableroVisual();
                    actualizarInterfaz();
                }
                db.desconectar();
            }
        }
    }

    private void registrarEvento(String mensaje) {
        // Obtenemos la hora para saber cuándo pasó cada cosa
        java.time.LocalTime ahora = java.time.LocalTime.now();
        String horaFormateada = String.format("[%02d:%02d:%02d] ", ahora.getHour(), ahora.getMinute(), ahora.getSecond());

        // Ponemos el mensaje nuevo ARRIBA del historial para que sea lo primero que leas
        String textoAnterior = eventos.getText();
        eventos.setText(horaFormateada + mensaje + "\n" + textoAnterior);
    }


    
    
    @FXML
    private void handleNewGame() {
        // Simplemente volvemos a llamar a initialize para que pida datos y resetee
        initialize();
        registrarEvento("¡Nueva partida iniciada!");
    }

    @FXML private void handleSaveGame() { gestorPartida.guardarPartida(); }
    @FXML private void handleLoadGame() {
        SelectorPartidas selector = new SelectorPartidas();
        int id = selector.mostrarVentana(); // Abre la ventanita con la lista de IDs

        if (id != -1) {
            GestorBBDD db = new GestorBBDD();
            if (db.conectar()) {
                Partida p = db.cargarBBDD(id);
                if (p != null) {
                    
                    this.gestorPartida.setPartida(p);                   
                    tablero.getChildren().clear();
                    crearFichas();
                    generarTableroVisual();

                    for (Pinguino pg : p.getJugadores()) {
                        Circle ficha = obtenerFichaVisual(pg.getColor());
                        int pos = pg.getPosicion();
                        int row = pos / COLUMNS;
                        int col = (pos % COLUMNS) + 1;
                        
                        GridPane.setRowIndex(ficha, row);
                        GridPane.setColumnIndex(ficha, col);
                    }

                    actualizarInterfaz();
                    registrarEvento("¡Partida " + id + " cargada con éxito!");

                } else {
                    registrarEvento("Error: No se pudo cargar la partida " + id);
                }
                db.desconectar();
            }
        }
    }

    @FXML private void handleQuitGame(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/MenuPantalla.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) tablero.getScene().getWindow();


            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al volver al menú: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
