package Modelo;

public class Oso extends Casilla {

	public Oso(int posicion) {
		super(posicion);
	}

	@Override
	public void activar(Pinguino jugador, Partida partida) {
		// Lógica de interacción dinámica: El resultado depende del inventario del jugador
		if (jugador.getInventario().usarPez()) {
			// Mecánica de "Soborno": Si tiene el objeto necesario, se consume y evita el castigo
			// (Aquí podrías añadir un mensaje por consola o log de la acción)
		} else {
			// Penalización: Si no tiene el objeto, el jugador regresa al punto de partida
			jugador.setPosicion(0);
		}
	}
}