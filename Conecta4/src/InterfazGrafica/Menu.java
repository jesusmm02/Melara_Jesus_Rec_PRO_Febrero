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
        } while (opcion != 6);
    }

    private void mostrarMenu() {
        System.out.println("\033[31mXXXX\033[97m - CUATRO EN RAYA - \033[33mOOOO\033[97m");
        System.out.println("1. Jugar contra humano");
        System.out.println("2. Jugar contra IA - NO FUNCIONAL");
        System.out.println("3. Configuración - NO FUNCIONAL");
        System.out.println("4. Instrucciones del juego");
        System.out.println("5. Créditos");
        System.out.println("6. Salir");
    }

    private void navegarAOpcion(int opcion) {
        switch (opcion) {
            case 1:
                jugar();
                break;
            case 2:
            case 3:
                System.out.println("Función en construcción...");
                break;
            case 4:
                instrucciones();
                break;
            case 5:
                mostrarCreditos();
                break;
            case 6:
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
                    System.out.println("\033[32m¡El jugador " + logicaJuego.obtenerJugadorActual() + " gana! ¡¡Enhorabuena!!\033[97m");
                    juegoTerminado = true;
                } else if (logicaJuego.tableroLleno()) {
                    System.out.println("\033[33m¡Es un empate!");
                    juegoTerminado = true;
                } else {
                    logicaJuego.cambiarJugador();
                }
            } else {
                System.out.println("\033[31mMovimiento no válido, intenta de nuevo.\033[97m");
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
        System.out.println("\033[96mCuatro en raya desarrollado por Jesús Melara Martín.\033[97m\n");
    }

    private void instrucciones() {
        System.out.println("\033[34m                                                                              *INSTRUCCIONES*                                                   ");
        System.out.println("                                                                            ___________________                                                ");
        System.out.println("\033[90m           [][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][]");
        System.out.println("\033[90m           []\033[97m               El objetivo de Conecta 4 es alinear cuatro fichas sobre un tablero formado por seis filas y siete columnas. \033[90m                                 []\n" +
                "\033[90m           []\033[97m                            Cada jugador dispone de 21 fichas de un color (por lo general,\033[31m rojas\033[97m o \033[33mamarillas\033[97m).                                              \033[90m[]\n" +
                "\033[90m           []\033[97m                     Por turnos, los jugadores deben introducir una ficha en la columna que prefieran (siempre que no esté completa)                        \033[90m[]\n" +
                "\033[90m           []\033[97m      y ésta caerá a la posición más baja. Gana la partida el primero que consiga alinear cuatro fichas consecutivas de un mismo color en cualquier,        \033[90m[]\n" +
                "\033[90m           []\033[97m               dirección. Si todas las columnas están llenas y nadie ha alineado cuatro fichas, la partida queda en empate.                                 \033[90m[]");
        System.out.println("\033[90m           [][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][]\033[97m\n");
    }

}
