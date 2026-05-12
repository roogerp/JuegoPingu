package Modelo;

public abstract class Jugador {

	protected String nombre;
	protected String color;
	protected int posicion;
	protected int puntuacion = 0;
	protected Inventario inventario;

	public Jugador(String nombre, String color) {
		this.nombre = nombre;
		this.color = color;
		this.posicion = 0;
		this.inventario = new Inventario();
	}

	public abstract void mover(int pasos);

	public String getNombre() {
		return nombre;
	} 

	public String getColor() {
		return color;
	} 

	public int getPuntuacion() {
		return puntuacion;
	} 

	public void setPuntuacion(int p) {
		this.puntuacion = p;
	} 

	public Inventario getInventario() {
		return inventario;
	}

	public int getPosicion() {
		return posicion;
	}

	public void setPosicion(int p) {
		this.posicion = p;
	}
}