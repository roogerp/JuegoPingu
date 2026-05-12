package Modelo;

public class Trineo extends Casilla {

	public Trineo(int posicion) {
		super(posicion);
	}

	@Override
	public void activar(Pinguino jugador, Partida partida) {
		// Impulsa al jugador 5 casillas hacia adelante
		// Al usar el método mover(), respetamos la lógica de desplazamiento del modelo
		jugador.mover(jugador.getPosicion() + 5);
	}
}