package Modelo;

public class Foca extends Jugador {

	private boolean aturdida;

	public Foca(String nombre, String color) {
		super(nombre, color);
		this.aturdida = false;
	}

	@Override
	public void mover(int nuevaPosicion) {
		if (!aturdida) {
			this.posicion = nuevaPosicion;
		}
	}

	public void setAturdida(boolean estado) {
		this.aturdida = estado;
	}
}