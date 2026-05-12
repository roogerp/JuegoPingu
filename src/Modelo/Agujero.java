package Modelo;

public class Agujero extends Casilla {

	public Agujero(int posicion) {
		super(posicion);
	}

	@Override
	public void activar(Pinguino jugador, Partida partida) {
		// El jugador retrocede 3 casillas desde su posición actual
		// El método mover() en la clase Pinguino ya debería controlar que no baje de 0
		jugador.mover(jugador.getPosicion() - 3); 
	}
}