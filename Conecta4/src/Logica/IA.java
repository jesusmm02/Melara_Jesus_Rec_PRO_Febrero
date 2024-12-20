package Logica;

public class IA {

    /**
     * Establece el movimiento que hará la IA cuando le toque su turno
     * @param tablero Matriz que representa el tablero de juego
     * @param simboloIA Carácter que representa el símbolo utilizado por la IA
     * @return Columna en la que la IA colocará su ficha
     */
    public int calcularMovimiento(char[][] tablero, char simboloIA) {
        for (int col = 0; col < tablero[0].length; col++) {
            if (tablero[0][col] == ' ') {
                return col;
            }
        }
        // Si no hay movimiento válido, retorna -1.
        return -1;
    }
}
