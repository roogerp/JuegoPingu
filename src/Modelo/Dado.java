package Modelo;

import java.util.Random;

public abstract class Dado extends Item {

	public Dado(String nombre, int cantidad) {
		super(nombre, cantidad);
	}

	public abstract int tirar(Random r);
}