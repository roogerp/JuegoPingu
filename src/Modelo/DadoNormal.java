package Modelo;

import java.util.Random;

public class DadoNormal extends Dado {

	public DadoNormal() {
		super("Dado Normal", 1);
	}

	@Override
	public int tirar(Random r) {
		return r.nextInt(6) + 1;
	}
}