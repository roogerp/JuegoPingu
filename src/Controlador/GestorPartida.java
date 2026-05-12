package Controlador;

import java.util.Random;

import Modelo.Dado;
import Modelo.Jugador;
import Modelo.Partida;

/*
 * Clase encargada de controlar la lógica principal de la partida.
 * Gestiona turnos, movimientos y guardado/carga de datos.
 */

public class GestorPartida {

	private Partida partida;
	private GestorTablero gestorTablero;
	private GestorJugador gestorJugador;
	private GestorBBDD gestorBBDD;
	private Random random;

	
	
	public GestorPartida() {

		// Inicializa los gestores principales del juego
		this.partida = null;
		this.gestorTablero = new GestorTablero();
		this.gestorJugador = new GestorJugador();
		this.gestorBBDD = new GestorBBDD();

		// Generador de números aleatorios para los dados
		this.random = new Random();
	}

	public void nuevaPartida() {

		// Crea una nueva partida
		this.partida = new Partida();
	}

	
	public void setPartida(Partida partida) {
	    this.partida = partida;
	}

	public int tirarDado(Jugador j, Dado dadoOpcional) {

		// Obtiene el resultado del dado
		int resultado = dadoOpcional.tirar(random);

		// Mueve al jugador según el resultado
		gestorJugador.jugadorSeMueve(j, resultado, this.partida.getTablero());

		return resultado;
	}

	public void ejecutarTurnoCompleto() {
		// Lógica completa de un turno
	}

	public void procesarTurnoJugador(Jugador j) {
		// Gestiona el turno de un jugador
	}

	public void actualizarEstadoTablero() {
		// Actualiza el estado visual/lógico del tablero
	}

	public void siguienteTurno() {
		// Cambia al siguiente jugador
	}

	public Partida getPartida() {
		return this.partida;
	}

	public void guardarPartida() {

		// Guarda la partida en la base de datos
		gestorBBDD.conectar();
		gestorBBDD.guardarBBDD(partida);
		gestorBBDD.desconectar();
	}

	public void cargarPartida(int id) {

		// Carga una partida desde la base de datos
		gestorBBDD.conectar();
		this.partida = gestorBBDD.cargarBBDD(id);
		gestorBBDD.desconectar();
	}
}