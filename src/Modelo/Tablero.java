package Modelo;

import java.util.ArrayList;
import java.util.Collections; 
import java.util.Random;

public class Tablero {

    // Lista dinámica que contiene los diferentes tipos de casillas
    private ArrayList<Casilla> casillas;

    public Tablero(int tamaño) {
        casillas = new ArrayList<>();
        generarTablero(tamaño);
    }

    private void generarTablero(int tamaño) {
        Random r = new Random();
        
        // Inicialización: Rellenamos el tablero con un estado base (Nieve)logo basico
        for (int i = 0; i < tamaño; i++) {
            casillas.add(new Nieve(i));
        }

        // Control de Posiciones: Creamos un índice de posiciones disponibles
        // Excluimos la posición 0 para garantizar un inicio seguro para el jugador
        
        ArrayList<Integer> posicionesLibres = new ArrayList<>();
        for (int i = 1; i < tamaño; i++) {
            posicionesLibres.add(i);
        }

        // Aleatorio, Mezclamos las posiciones para distribuir los eventos de forma única en cada partida
        Collections.shuffle(posicionesLibres);

        // Configuración dinámica, Definimos un rango variable de enemigos (Osos) para dar rejugabilidad
        int numOsos = r.nextInt(3) + 4; // Genera entre 4 y 6 osos (Ajuste: r.nextInt(3) da 0-2)

        // Distribución de Eventos: Sustituimos la nieve por casillas especiales según el orden mezclado
        int p = 0; 

        // Colocación de Osos (Obstáculos principales)
        for (int i = 0; i < numOsos; i++) {
            int pos = posicionesLibres.get(p++);
            casillas.set(pos, new Oso(pos));
        }

        // Balance de Juego, Colocamos una cantidad fija de casillas especiales (Agujeros, Trineos, Interrogantes)
        // Se usa un límite de p < posicionesLibres.size() para evitar errores de desbordamiento (IndexOutOfBounds)
        
        for (int i = 0; i < 4; i++) {
            if (p < posicionesLibres.size()) {
                int posA = posicionesLibres.get(p++);
                casillas.set(posA, new Agujero(posA));
            }
            if (p < posicionesLibres.size()) {
                int posT = posicionesLibres.get(p++);
                casillas.set(posT, new Trineo(posT));
            }
            if (p < posicionesLibres.size()) {
                int posI = posicionesLibres.get(p++);
                casillas.set(posI, new Interrogante(posI));
            }
        }
    }

    // Métodos de consulta para que el controlador pueda gestionar el movimiento y las colisiones
    public Casilla getCasilla(int posicion) {
        return casillas.get(posicion);
    }

    public int getTamaño() {
        return casillas.size();
    }

    public ArrayList<Casilla> getCasillas() {
        return casillas;
    }
}
