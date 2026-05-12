package Modelo;

public class Pinguino extends Jugador {

	// cada pingüino tiene su propio sistema de objetos
	private Inventario inventario;

	// control para mecánicas de penalización o efectos de estado
	private int turnosSaltados = 0;
	
	public Pinguino(String nombre, String color) {
		super(nombre, color); // Llama al constructor de la clase padre (Jugador)
		this.inventario = new Inventario();
	}

	@Override
	public void mover(int nuevaPosicion) {
		this.posicion = nuevaPosicion;
		// Validación de seguridad, evita que el jugador salga del tablero (posiciones negativas)
		if (this.posicion < 0) {
			this.posicion = 0;
		}
	}

	@Override
	public Inventario getInventario() {
		return inventario;
	}
	
	// Métodos de acceso para gestionar la lógica de turnos en el controlador del juego
	public int getTurnosSaltados() {
	    return turnosSaltados;
	}

	public void setTurnosSaltados(int turnosSaltados) {
	    this.turnosSaltados = turnosSaltados;
	}
}