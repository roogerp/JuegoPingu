package Modelo;

import java.util.Random;

public class DadoLento extends Dado {

	public DadoLento() {
		super("Dado Lento", 1);
	}

	@Override
	public int tirar(Random r) {
		return r.nextInt(3) + 1;
	}
}