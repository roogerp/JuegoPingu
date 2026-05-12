package Controlador;

import Modelo.Foca;
import Modelo.Jugador;
import Modelo.Pinguino;
import Modelo.Tablero;

/*
 * Clase encargada de gestionar las acciones de los jugadores.
 * Controla movimientos, eventos e interacciones.
 */

public class GestorJugador {

	public void jugadorUsaItem(String nombreItem) {
		// Gestiona el uso de objetos del inventario
	}

	public void jugadorSeMueve(Jugador j, int pasos, Tablero t) {

		// Calcula la nueva posición del jugador
		int nuevaPos = j.getPosicion() + pasos;

		// Evita salir del tablero por arriba
		if (nuevaPos > 49) {
			nuevaPos = 49;
		}

		// Evita posiciones negativas
		if (nuevaPos < 0) {
			nuevaPos = 0;
		}

		// Actualiza la posición del jugador
		j.setPosicion(nuevaPos);
	}

	public void jugadorFinalizaTurno(Jugador j) {
		// Finaliza el turno del jugador
	}

	public void piguinoEvento(Pinguino p) {
		// Ejecuta eventos especiales del pingüino
	}

	public void pingüinoGuerra(Pinguino p1, Pinguino p2) {
		// Interacción o combate entre pingüinos
	}

	public void focaInteractua(Pinguino p, Foca f) {
		// Interacción entre pingüino y foca
	}
}