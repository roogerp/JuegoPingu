package Modelo;

import java.util.Random;

public class Interrogante extends Casilla {

	public Interrogante(int posicion) {
		super(posicion);
	}

	@Override
	public void activar(Pinguino jugador, Partida partida) {
		Random r = new Random();
		// Generamos un evento aleatorio entre 4 opciones posibles
		int evento = r.nextInt(4);

		switch (evento) {
		case 0:
			// Recompensa defensiva, Proporciona un pez para protegerse de los osos
			jugador.getInventario().añadirPez();
			break;
		case 1:
			// Recompensa ofensiva, Otorga entre 1 y 3 bolas de nieve (munición)
			jugador.getInventario().añadirBolas(r.nextInt(3) + 1);
			break;
		case 2:
			// Modificador de velocidad, Añade un objeto para mover más espacios
			jugador.getInventario().añadirDadoRapido();
			break;
		case 3:
			// Modificador de control, Añade un objeto para movimientos cortos y precisos
			jugador.getInventario().añadirDadoLento();
			break;
		}
	}
}