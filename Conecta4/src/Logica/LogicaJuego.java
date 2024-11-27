package Logica;

public class LogicaJuego {

    private char jugadorActual = 'X'; // X siempre empieza
    private Tablero tableroJuego;

    public LogicaJuego() {
        tableroJuego = new Tablero();
    }

    public boolean esMovimientoValido(int columna) {
        return columna >= 0 && columna < 7 && tableroJuego.obtenerTablero()[0][columna] == ' ';
    }

    public boolean jugarTurno(int columna) {
        if (!esMovimientoValido(columna)) {
            return false;
        }
        tableroJuego.colocarFicha(columna, jugadorActual);
        return true;
    }

    public boolean verificarGanador() {
        return tableroJuego.verificarGanador(jugadorActual);
    }

    public boolean tableroLleno() {
        return tableroJuego.estaLleno();
    }

    public void cambiarJugador() {
        jugadorActual = (jugadorActual == 'X') ? 'O' : 'X';
    }

    public char obtenerJugadorActual() {
        return jugadorActual;
    }

    public char[][] obtenerTablero() {
        return tableroJuego.obtenerTablero();
    }

    public void reiniciarJuego() {
        tableroJuego.inicializarTablero();
        jugadorActual = 'X';
    }

}
