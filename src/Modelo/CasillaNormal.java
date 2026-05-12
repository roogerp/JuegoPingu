package Modelo;

public abstract class CasillaNormal extends Casilla {

	public CasillaNormal(int posicion) {
		super(posicion); // Pasa la posición al constructor de la clase base (Casilla)
	}

	@Override
	public void activar(Pinguino jugador, Partida partida) {
		// Define que, por defecto, caer aquí no altera el estado
		// del jugador ni de la partida (ej: la casilla de Nieve).
	}
}