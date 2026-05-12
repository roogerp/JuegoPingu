package Modelo;

public abstract class Casilla {

	// Atributo protegido para que las clases hijas puedan acceder a su ubicación
	protected int posicion;

	public Casilla(int posicion) {
		this.posicion = posicion;
	}

	/*Método abstracto que implementa el Patrón de Diseño Command o Strategy.
	 * Cada tipo de casilla (Oso, Hielo, Trineo) definirá su propio comportamiento 
	 * al ser "activada" por un jugador.
	 */
	public abstract void activar(Pinguino jugador, Partida partida);
}