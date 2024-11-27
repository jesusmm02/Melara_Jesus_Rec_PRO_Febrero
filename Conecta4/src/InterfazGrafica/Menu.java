package InterfazGrafica;

import java.util.Scanner;

import Logica.LogicaJuego;


public class Menu {

    private final Scanner entrada = new Scanner(System.in);
    private final LogicaJuego logicaJuego = new LogicaJuego();

    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = entrada.nextInt();
            navegarAOpcion(opcion);
        } while (opcion != 5);
    }

    private void mostrarMenu() {
        System.out.println("=== CUATRO EN RAYA ===");
        System.out.println("1. Jugar contra humano");
        System.out.println("2. Jugar contra IA - NO FUNCIONAL");
        System.out.println("3. Configuración - NO FUNCIONAL");
        System.out.println("4. Créditos");
        System.out.println("5. Salir");
    }

    private void navegarAOpcion(int opcion) {
        switch (opcion) {
            case 1:
                jugar();
            case 2:
            case 3:
                System.out.println("Función en construcción...");
            case 4:
                mostrarCreditos();
            case 5:
                System.out.println("¡Gracias por jugar!");
        }
    }

    private void jugar() {
        boolean juegoTerminado = false;

        while (!juegoTerminado) {
            System.out.println("\nTurno del jugador: " + logicaJuego.obtenerJugadorActual());


            int columna;
            System.out.print("Introduce la columna (1-7): ");
            columna = entrada.nextInt() - 1;


            // Intentar realizar el movimiento
            if (logicaJuego.jugarTurno(columna)) {
                dibujarTablero();

                // Comprobar si alguien gana o si es un empate
                if (logicaJuego.verificarGanador()) {
                    System.out.println("¡El jugador " + logicaJuego.obtenerJugadorActual() + " gana! ¡¡Enhorabuena!!");
                    juegoTerminado = true;
                } else if (logicaJuego.tableroLleno()) {
                    System.out.println("¡Es un empate!");
                    juegoTerminado = true;
                } else {
                    logicaJuego.cambiarJugador();
                }
            } else {
                System.out.println("Movimiento no válido, intenta de nuevo.");
            }
        }
        logicaJuego.reiniciarJuego();
    }

    private void dibujarTablero() {
        char[][] tablero = logicaJuego.obtenerTablero();
        for (char[] fila : tablero) {
            for (char celda : fila) {
                System.out.print("| " + (celda == ' ' ? ' ' : celda) + " ");
            }
            System.out.println("|");
        }
        System.out.println(" 1   2   3   4   5   6   7");
    }


    private void mostrarCreditos() {
        System.out.println("Cuatro en raya desarrollado por Jesús Melara Martín.");
    }

}
