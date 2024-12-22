package Logica;

import java.util.Random;

public class IA {

    /**
     * Establece el movimiento que hará la IA cuando le toque su turno
     * @param tablero Matriz que representa el tablero de juego
     * @param simboloIA Carácter que representa el símbolo utilizado por la IA
     * @return Columna en la que la IA colocará su ficha
     */
    public int calcularMovimiento(char[][] tablero, char simboloIA) {
        Random random = new Random();
        return random.nextInt(tablero[0].length);
        // Si no hay movimiento válido, retorna -1.

    }

}
