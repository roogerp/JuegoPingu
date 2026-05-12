package Modelo;

public class Nieve extends CasillaNormal {

    public Nieve(int posicion) {
        super(posicion);
    }

    @Override
    public void activar(Pinguino jugador, Partida partida) {
        // Casilla normal, no hace nada
    }
}