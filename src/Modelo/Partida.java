package Modelo;

import java.util.ArrayList;

/*
 * Clase que representa una partida completa.
 * Guarda el tablero, jugadores y control de turnos.
 */

public class Partida {

	private Tablero tablero;
	private ArrayList<Pinguino> jugadores;
	private int turnoActual;

	public Partida() {

		// Crea una partida con tablero por defecto
		this(60);
	}

	public Partida(int tamañoTablero) {

		// Inicializa tablero y lista de jugadores
		this.tablero = new Tablero(tamañoTablero);
		this.jugadores = new ArrayList<>();
		this.turnoActual = 0;
	}

	public void añadirJugador(Pinguino jugador) {

		// Añade un jugador a la partida
		if (jugadores != null) {
			jugadores.add(jugador);
		}
	}

	public Pinguino getJugadorActual() {

		// Devuelve el jugador del turno actual
		return (jugadores != null && !jugadores.isEmpty()) ? jugadores.get(turnoActual) : null;
	}

	public void siguienteTurno() {

		// Cambia al siguiente jugador
		if (!jugadores.isEmpty()) {
			turnoActual = (turnoActual + 1) % jugadores.size();
		}
	}

	public Tablero getTablero() {
		return tablero;
	}

	public ArrayList<Pinguino> getJugadores() {
		return jugadores;
	}
}
