package Logica;

public class Tablero {
    private final int filas = 6; // Número de filas en el tablero
    private final int columnas = 7; // Número de columnas en el tablero.
    private char[][] tablero; // Matriz que representa el tablero de juego.

    // Constructor de la clase.
    public Tablero() {
        inicializarTablero();
    }

    /**
     * Inicializa el tablero con espacios vacíos.
     * Crea una matriz de caracteres y asigna ' ' (espacio vacío) a cada posición.
     */
    public void inicializarTablero() {
        tablero = new char[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                tablero[i][j] = ' '; // Espacio vacío.
            }
        }
    }

    /**
     * Intenta colocar la ficha del jugador en la columna especificada.
     * La ficha se coloca en la posición más baja disponible de la columna.
     * @param columna Número de la columna donde el jugador quiere colocar su ficha.
     * @param simboloJugador Carácter que representa la ficha del jugador.
     * @return true si la ficha se colocó con éxito; false si el movimiento no es válido.
     */
    public boolean colocarFicha(int columna, char simboloJugador) {
        // Verifica si la columna está dentro del rango y no está llena en la parte superior.
        if (columna < 0 || columna >= columnas || tablero[0][columna] != ' ') {
            return false; // Movimiento no válido.
        }
        // Recorre la columna desde la parte inferior hacia arriba para encontrar la primera posición disponible.
        for (int i = filas - 1; i >= 0; i--) {
            if (tablero[i][columna] == ' ') {
                tablero[i][columna] = simboloJugador; // Coloca la ficha del jugador.
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si el tablero está lleno, es decir, si ya no quedan movimientos posibles.
     * @return true si todas las columnas están llenas; false si hay al menos un espacio vacío.
     */
    public boolean estaLleno() {
        for (int i = 0; i < columnas; i++) {
            if (tablero[0][i] == ' ') { // Si hay al menos un espacio vacío en la fila superior, el tablero no está lleno.
                return false;
            }
        }
        return true; // Todas las columnas están llenas.
    }

    /**
     * Recorre todas las posiciones del tablero
     * @param simboloJugador carácter que simboliza el jugador
     * @return true si el jugador tiene una secuencia de 4 fichas consecutivas;
     *         false en caso contrario.
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
     * Evalúa si desde una posición (fila, columna),
     * es posible formar una secuencia de 4 fichas consecutivas
     * en una dirección específica (dFila, dColumna)
     * @param fila posición inicial en el eje vertical (número de fila en el tablero).
     * @param columna posición inicial en el eje horizontal (número de columna en el tablero).
     * @param simboloJugador carácter que representa la ficha del jugador que se está verificando.
     * @param dFila incremento de la fila en cada paso en la dirección especificada.
     * @param dColumna incremento de la columna en cada paso en la dirección especificada.
     * @return devuelve true si se encuentran 4 fichas consecutivas
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

    /**
     * Matriz que representa el tablero.
     * @return Tablero de juego
     */
    public char[][] obtenerTablero() {
        return tablero;
    }
}
