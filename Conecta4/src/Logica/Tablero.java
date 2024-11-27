package Logica;

public class Tablero {
    private final int filas = 6;
    private final int columnas = 7;
    private char[][] tablero;

    public Tablero() {
        inicializarTablero();
    }

    public void inicializarTablero() {
        tablero = new char[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                tablero[i][j] = ' '; // Espacio vacío.
            }
        }
    }

    public boolean colocarFicha(int columna, char simboloJugador) {
        if (columna < 0 || columna >= columnas || tablero[0][columna] != ' ') {
            return false; // Movimiento no válido.
        }
        for (int i = filas - 1; i >= 0; i--) {
            if (tablero[i][columna] == ' ') {
                tablero[i][columna] = simboloJugador;
                return true;
            }
        }
        return false;
    }

    public boolean estaLleno() {
        for (int i = 0; i < columnas; i++) {
            if (tablero[0][i] == ' ') {
                return false;
            }
        }
        return true;
    }

    /**
     * Recorre todas las posiciones del tablero
     * @param simboloJugador
     * @return
     */
    public boolean verificarGanador(char simboloJugador) {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (verificarDireccion(i, j, simboloJugador, 1, 0) || // Horizontal (Columnas)
                        verificarDireccion(i, j, simboloJugador, 0, 1) || // Vertical (Filas)
                        verificarDireccion(i, j, simboloJugador, 1, 1) || // Diagonal descendente
                        verificarDireccion(i, j, simboloJugador, 1, -1)) { // Diagonal ascendente
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Evalúa si, desde una posición (fila, columna),
     * es posible formar una secuencia de 4 fichas consecutivas
     * en una dirección específica (dFila, dColumna)
     * @param fila
     * @param columna
     * @param simboloJugador
     * @param dFila
     * @param dColumna
     * @return
     */
    private boolean verificarDireccion(int fila, int columna, char simboloJugador, int dFila, int dColumna) {
        int contador = 0; // Contador de símbolos consecutivos

        // Recorre 4 posiciones consecutivas desde (fila, columna) en la dirección (dFila, dColumna)
        for (int k = 0; k < 4; k++) { // k calcula la posición que se está verificando (nuevaFila, nuevaColumna)

            // k se multiplica para avanzar a la dirección especificada
            int nuevaFila = fila + k * dFila;
            int nuevaColumna = columna + k * dColumna;

            // Se comprueba si la posición está dentro de los límites del tablero y si contiene el símbolo del jugador
            if (nuevaFila >= 0 && nuevaFila < filas && nuevaColumna >= 0 && nuevaColumna < columnas
                    && tablero[nuevaFila][nuevaColumna] == simboloJugador) {
                contador++; // Si se encuentra una ficha válida, incrementa el contador
            } else {
                break; // Si la secuencia de 4 fichas consecutivas ya no es posible desde la posición inicial en esa dirección se sale del bucle
            }
        }
        return contador == 4;
    }

    public char[][] obtenerTablero() {
        return tablero;
    }
}
