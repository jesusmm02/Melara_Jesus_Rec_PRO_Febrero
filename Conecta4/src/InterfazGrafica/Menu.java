package InterfazGrafica;

import java.util.Scanner;

import Logica.IA;
import Logica.LogicaJuego;


public class Menu {

    private final Scanner entrada = new Scanner(System.in);
    private final LogicaJuego logicaJuego = new LogicaJuego();


    /**
     * Método de inicio del programa
     */
    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = entrada.nextInt();
            selectorOpcion(opcion);
        } while (opcion != 6);
    }

    /**
     * Muestra el menú del juego
     */
    private void mostrarMenu() {
        System.out.println("\033[31mXXXX\033[97m - CUATRO EN RAYA - \033[33mOOOO\033[97m");
        System.out.println("1. Jugar contra humano");
        System.out.println("2. Jugar contra IA");
        System.out.println("3. Configuración");
        System.out.println("4. Instrucciones del juego");
        System.out.println("5. Créditos");
        System.out.println("6. Salir");
    }

    /**
     * Método donde se pasa la opción del menú que hemos seleccionado y hace la acción solicitada
     * @param opcion Número que representa la opción seleccionada en el menú principal
     */
    private void selectorOpcion(int opcion) {
        switch (opcion) {
            case 1:
                jugar(false);
                break;
            case 2:
                jugar(true);
                break;
            case 3:
                configuracion();
                break;
            case 4:
                instrucciones();
                break;
            case 5:
                creditos();
                break;
            case 6:
                System.out.println("¡Gracias por jugar!");
                System.out.println("Cerrando juego...");
        }
    }


    /**
     * Método que se encarga del mecanismo de las partidas, elegir posición de la ficha, comprobar si hay fichas consecutivas,
     * elegir el ganador
     * @param contraIA Booleano que establece si la partida es contra la IA o contra un jugador en local
     */
    private void jugar(boolean contraIA) {
        int partidasGanadasJugador1 = 0;
        int partidasGanadasJugador2 = 0;
        IA ia = new IA();

        if (ordenSalida.equals("Aleatorio")) {
            logicaJuego.reiniciarJuego();
            if (Math.random() < 0.5) {
                logicaJuego.cambiarJugador(); // Cambia al Jugador 2.
            }
        } else if (ordenSalida.equals("Sale Perdedor")) {
            if (partidasGanadasJugador1 > partidasGanadasJugador2) {
                logicaJuego.cambiarJugador();
            }
        } else if (ordenSalida.equals("Sale Ganador")) {
            if (partidasGanadasJugador2 > partidasGanadasJugador1) {
                logicaJuego.cambiarJugador();
            }
        } else {
            logicaJuego.reiniciarJuego();
        }

        boolean juegoTerminado = false;

        while (partidasGanadasJugador1 < partidasParaGanar && partidasGanadasJugador2 < partidasParaGanar) {
            while (!juegoTerminado) {
                System.out.println("\nTurno del jugador: " + logicaJuego.obtenerJugadorActual());
                int columna;

                if (contraIA && logicaJuego.obtenerJugadorActual() == colorJugador2) {
                    // Llama a la IA para calcular el movimiento.
                    columna = ia.calcularMovimiento(logicaJuego.obtenerTablero(), colorJugador2);
                    System.out.println("La IA juega en la columna: " + (columna + 1));
                } else {
                    System.out.print("Introduce la columna (1-7): ");
                    columna = entrada.nextInt() - 1;
                }

                if (logicaJuego.jugarTurno(columna)) {
                    dibujarTablero();

                    if (logicaJuego.verificarGanador()) {
                        System.out.println("\033[32m¡El jugador " + logicaJuego.obtenerJugadorActual() + " gana la partida!\033[97m");
                        if (logicaJuego.obtenerJugadorActual() == colorJugador1) {
                            partidasGanadasJugador1++;
                        } else {
                            partidasGanadasJugador2++;
                        }
                        juegoTerminado = true;
                    } else if (logicaJuego.tableroLleno()) {
                        System.out.println("\033[93m¡Es un empate!\033[97m");
                        juegoTerminado = true;
                    } else {
                        logicaJuego.cambiarJugador();
                    }
                } else {
                    System.out.println("\033[91mMovimiento no válido, intenta de nuevo.\033[97m");
                }
            }

            System.out.println("\n\033[96mMarcador:\033[97m");
            System.out.println("\033[31mJugador 1 (" + colorJugador1 + "): " + partidasGanadasJugador1 + "\033[97m");
            System.out.println("\033[33mJugador 2 (" + colorJugador2 + "): " + partidasGanadasJugador2 + "\033[97m");

            // Si aún ningún jugador ha alcanzado las victorias necesarias para ganar el juego, reinicia el juego para otra partida
            if (partidasGanadasJugador1 < partidasParaGanar && partidasGanadasJugador2 < partidasParaGanar) {
                logicaJuego.reiniciarJuego();
                juegoTerminado = false;
            }
        }

        // Se declara al ganador del juego una vez se alcance el número de victorias necesarias para ganar el juego
        if (partidasGanadasJugador1 > partidasGanadasJugador2) {
            System.out.println("\033[92m¡Jugador 1 gana el juego!\033[97m\n");
        } else {
            System.out.println("¡Jugador 2 gana el juego!\n");
        }

        logicaJuego.reiniciarJuego();
    }


    /**
     * Pinta el tablero de juego en la consola
     */
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

    private char colorJugador1 = 'X'; // Valor predeterminado.
    private char colorJugador2 = 'O'; // Valor predeterminado.
    private int partidasParaGanar = 1; // Por defecto, 1 partida.
    private String ordenSalida = "Jugador 1";

    private void configuracion() {
        int opcionConfiguracion;

        do {
            mostrarMenuConfiguracion();
            opcionConfiguracion = entrada.nextInt();
            procesarOpcionConfiguracion(opcionConfiguracion);
        } while (opcionConfiguracion != 4); // Salir del menú de configuración.
    }

    /**
     * Menú de opciones para el apartado de Configuración
     */
    private void mostrarMenuConfiguracion() {
        System.out.println("=== CONFIGURACIÓN ===");
        System.out.println("1. Cambiar color de los jugadores");
        System.out.println("2. Establecer número de partidas para ganar");
        System.out.println("3. Configurar orden de salida");
        System.out.println("4. Volver al menú principal");
    }

    /**
     * Manejo de distintas opciones en cada tipo de configuración
     * @param opcion Número que representa la opción seleccionada en el menú de configuración
     */
    private void procesarOpcionConfiguracion(int opcion) {
        switch (opcion) {
            case 1:
                configurarColores();
                break;
            case 2:
                configurarNumeroPartidas();
                break;
            case 3:
                configurarOrdenSalida();
                break;
            case 4:
                System.out.println("Volviendo al menú principal...");
                break;
            default:
                System.out.println("\033[91mOpción no válida.\033[97m");
                break;
        }
    }

    private void configurarColores() {
        System.out.print("Introduce el símbolo para el Jugador 1: ");
        colorJugador1 = entrada.next().charAt(0);
        System.out.print("Introduce el símbolo para el Jugador 2: ");
        colorJugador2 = entrada.next().charAt(0);

        System.out.println("Colores establecidos:");
        System.out.println("Jugador 1: " + colorJugador1);
        System.out.println("Jugador 2: " + colorJugador2);
    }

    private void configurarNumeroPartidas() {
        System.out.print("Introduce el número de partidas para ganar: ");
        partidasParaGanar = entrada.nextInt();
        System.out.println("Número de partidas establecido: " + partidasParaGanar);
    }

    private void configurarOrdenSalida() {
        System.out.println("Elige el orden de salida:");
        System.out.println("1. Aleatorio");
        System.out.println("2. Sale siempre el ganador de la última partida");
        System.out.println("3. Sale siempre el perdedor de la última partida");
        System.out.println("4. Sale siempre el Jugador 1");
        int opcion = entrada.nextInt();

        switch (opcion) {
            case 1:
                ordenSalida = "Aleatorio";
                break;
            case 2:
                ordenSalida = "Sale Ganador";
                break;
            case 3:
                ordenSalida = "Sale Perdedor";
                break;
            case 4:
                ordenSalida = "Jugador 1";
                break;
            default:
                System.out.println("\033[91mOpción no válida.\033[97m Manteniendo configuración anterior.");
                break;
        }

        System.out.println("Orden de salida configurado: " + ordenSalida);
    }


    /**
     * Método que muestra las reglas e instrucciones del juego
     */
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


    /**
     * Método que muestra los créditos del programa
     */
    private void creditos() {
        System.out.println("\033[96mCuatro en raya desarrollado por Jesús Melara Martín.\033[97m\n");
    }

}
