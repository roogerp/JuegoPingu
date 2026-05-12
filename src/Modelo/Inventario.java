package Modelo;

import java.util.ArrayList;

public class Inventario {

	// Lista para objetos complejos (dados especiales) y contadores para consumibles simples
	private ArrayList<Item> items;
	private int peces;
	private int bolas;
	private int dados; // Contador para controlar el límite de dados especiales

	public Inventario() {
		items = new ArrayList<>();
		peces = 0;
		bolas = 0;
		dados = 0;
	}

	// Lógica de balance: Limita el máximo de peces a 2 para mantener la dificultad
	public void añadirPez() {
		if (peces < 2) {
			peces++;
		}
	}

	public ArrayList<Item> getDados() {
		return items;
	}

	// Método booleano: Intenta consumir un recurso y devuelve si tuvo éxito (útil para la clase Oso)
	public boolean usarPez() {
		if (peces > 0) {
			peces--;
			return true;
		}
		return false;
	}

	// Gestión de munición con un tope máximo de 6 unidades
	public void añadirBolas(int cantidad) {
		if (bolas + cantidad <= 6) {
			bolas += cantidad;
		}
	}

	// Métodos para añadir dados especiales controlando que no se superen los 3 en total
	public void añadirDadoRapido() {
		if (dados < 3) {
			items.add(new DadoRapido());
			dados++; // Importante: incrementar el contador
		}
	}

	public void añadirDadoLento() {
		if (dados < 3) {
			items.add(new DadoLento());
			dados++; // Importante: incrementar el contador
		}
	}

	public int getPeces() {
		return peces;
	}

	public int getBolas() {
		return bolas;
	}

	public boolean usarBola() {
		if (bolas > 0) {
			bolas--;
			return true;
		}
		return false;
	}
	
    // Setters para posibles eventos de reset o estados de carga de partida
	public void setPeces(int peces) {
	    this.peces = peces;
	}

	public void setBolas(int bolas) {
	    this.bolas = bolas;
	}

	public void setDados(int dados) {
	    this.dados = dados;
	}
}