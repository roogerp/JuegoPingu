package Modelo;

import java.util.Random;

public class DadoRapido extends Dado {

	public DadoRapido() {
		super("Dado Rapido", 1);
	}

	@Override
	public int tirar(Random r) {
		return r.nextInt(6) + 5;
	}
}