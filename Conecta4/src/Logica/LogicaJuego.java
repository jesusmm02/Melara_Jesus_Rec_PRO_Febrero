package Logica;

public class LogicaJuego {

    private char jugadorActual; // Jugador actual en la partida.
    private char simboloJugador1 = 'X'; // Símbolo predeterminado del Jugador 1.
    private char simboloJugador2 = 'O'; // Símbolo predeterminado del Jugador 2.
    private Tablero tableroJuego; // Instancia del tablero del juego.

    // Constructor de la clase.
    public LogicaJuego() {
        tableroJuego = new Tablero();
        jugadorActual = simboloJugador1; // Por defecto, empieza el Jugador 1.
    }

    /**
     * Verifica si un movimiento en la columna seleccionada es válido.
     * @param columna Número de la columna donde el jugador quiere jugar.
     * @return true si el movimiento es válido; false en caso contrario.
     */
    public boolean esMovimientoValido(int columna) {
        return columna >= 0 && columna < 7 && tableroJuego.obtenerTablero()[0][columna] == ' ';
    }

    /**
     * Realiza el turno del jugador en la columna indicada.
     * Coloca la ficha del jugador actual en la columna si el movimiento es válido.
     * @param columna Número de la columna donde se coloca la ficha.
     * @return true si el turno fue exitoso; false si el movimiento es inválido.
     */
    public boolean jugarTurno(int columna) {
        if (!esMovimientoValido(columna)) {
            return false;
        }
        tableroJuego.colocarFicha(columna, jugadorActual);
        return true;
    }

    /**
     * Verifica si el jugador actual ha ganado la partida.
     * @return true si el jugador actual ha alineado cuatro fichas consecutivas; false en caso contrario.
     */
    public boolean verificarGanador() {
        return tableroJuego.verificarGanador(jugadorActual);
    }

    /**
     * Comprueba si el tablero está completamente lleno.
     * @return true si no quedan movimientos posibles; false si aún hay espacios vacíos.
     */
    public boolean tableroLleno() {
        return tableroJuego.estaLleno();
    }

    /**
     * Cambia el turno al siguiente jugador.
     * Si el jugador actual es el Jugador 1, pasa al Jugador 2, y viceversa.
     */
    public void cambiarJugador() {
        jugadorActual = (jugadorActual == simboloJugador1) ? simboloJugador2 : simboloJugador1;
    }

    /**
     * Establece un nuevo símbolo para el Jugador 1.
     * También ajusta el jugador actual si coincide con el símbolo modificado.
     * @param simboloJugador1 Nuevo símbolo para el Jugador 1.
     */
    public void setSimboloJugador1(char simboloJugador1) {
        this.simboloJugador1 = simboloJugador1;
        if (jugadorActual == this.simboloJugador1 || jugadorActual == this.simboloJugador2) {
            jugadorActual = simboloJugador1; // Ajusta el jugador actual si coincide.
        }
    }

    /**
     * Establece un nuevo símbolo para el Jugador 2.
     * También ajusta el jugador actual si coincide con el símbolo modificado.
     * @param simboloJugador2 Nuevo símbolo para el Jugador 2.
     */
    public void setSimboloJugador2(char simboloJugador2) {
        this.simboloJugador2 = simboloJugador2;
        if (jugadorActual == this.simboloJugador1 || jugadorActual == this.simboloJugador2) {
            jugadorActual = simboloJugador1; // Ajusta el jugador actual si coincide.
        }
    }

    /**
     * Devuelve el símbolo del jugador que tiene el turno actual.
     * @return Carácter que representa al jugador actual.
     */
    public char obtenerJugadorActual() {
        return jugadorActual;
    }

    /**
     * Obtiene el estado actual del tablero de juego.
     * @return Matriz de caracteres que representa el tablero.
     */
    public char[][] obtenerTablero() {
        return tableroJuego.obtenerTablero();
    }

    /**
     * Reinicia el estado del juego para iniciar una nueva partida.
     * Limpia el tablero y establece al Jugador 1 como el jugador inicial.
     */
    public void reiniciarJuego() {
        tableroJuego.inicializarTablero();
        jugadorActual = simboloJugador1; // Reinicia con el Jugador 1.
    }
}
