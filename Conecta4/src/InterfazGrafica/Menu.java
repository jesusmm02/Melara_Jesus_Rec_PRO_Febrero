package InterfazGrafica;

import java.util.Scanner;

import Logica.IA;
import Logica.LogicaJuego;


public class Menu {

    private final Scanner teclado = new Scanner(System.in);
    private final LogicaJuego logicaJuego = new LogicaJuego();


    /**
     * Método que inicializa el programa.
     */
    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = teclado.nextInt();
            selectorOpcion(opcion);
        } while (opcion != 6);
    }

    /**
     * Muestra el menú del juego.
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
     * Método donde se pasa la opción del menú que hemos seleccionado y hace la acción solicitada.
     * @param opcion Número que representa la opción seleccionada en el menú principal.
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
     * elegir el ganador.
     * @param contraIA Booleano que establece si la partida es contra la IA o contra un jugador en local.
     */
    private void jugar(boolean contraIA) {
        int partidasGanadasJugador1 = 0;
        int partidasGanadasJugador2 = 0;
        IA ia = new IA();

        if (contraIA) {
            System.out.println("\033[96mHa seleccionado Jugar contra la IA.\033[97m");
        } else {
            System.out.println("\033[96mHa seleccionado Jugar contra humano.\033[97m");
        }

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
                int columna = -1;
                boolean entradaValida = false;

                if (contraIA && logicaJuego.obtenerJugadorActual() == simboloJugador2) {
                    // Llama a la IA para calcular el movimiento.
                    columna = ia.calcularMovimiento(logicaJuego.obtenerTablero(), simboloJugador2);
                    System.out.println("La IA juega en la columna: " + (columna + 1));
                    entradaValida = true;
                } else {
                    while (!entradaValida) {
                        System.out.print("Introduce la columna (1-7): ");
                        try {
                            columna = Integer.parseInt(teclado.next()) - 1;
                            if (columna >= 0 && columna < 7) {
                                entradaValida = true;
                            } else {
                                System.out.println("\033[91mEl número debe estar entre 1 y 7. Intenta de nuevo.\033[97m");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("\033[91mEntrada no válida. Por favor, introduce un número entre 1 y 7.\033[97m");
                        }
                    }
                }

                if (logicaJuego.jugarTurno(columna)) {
                    dibujarTablero();

                    if (logicaJuego.verificarGanador()) {
                        System.out.println("\033[32m¡El jugador " + logicaJuego.obtenerJugadorActual() + " gana la partida!\033[97m");
                        if (logicaJuego.obtenerJugadorActual() == simboloJugador1) {
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
            System.out.println("\033[31mJugador 1 (" + simboloJugador1 + "): " + partidasGanadasJugador1 + "\033[97m");
            System.out.println("\033[33mJugador 2 (" + simboloJugador2 + "): " + partidasGanadasJugador2 + "\033[97m");

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
            System.out.println("\033[92m¡Jugador 2 gana el juego!\033[97m\n");
        }

        logicaJuego.reiniciarJuego();
    }


    /**
     * Pinta el tablero de juego por la terminal.
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

    private char simboloJugador1 = 'X'; // Valor predeterminado.
    private char simboloJugador2 = 'O'; // Valor predeterminado.
    private int partidasParaGanar = 1; // Por defecto, 1 partida.
    private String ordenSalida = "Jugador 1";

    private void configuracion() {
        int opcionConfiguracion;

        do {
            mostrarMenuConfiguracion();
            opcionConfiguracion = teclado.nextInt();
            procesarOpcionConfiguracion(opcionConfiguracion);
        } while (opcionConfiguracion != 4); // Salir del menú de configuración.
    }

    /**
     * Menú de opciones para el apartado de Configuración.
     */
    private void mostrarMenuConfiguracion() {
        System.out.println("\n\033[34m     === CONFIGURACIÓN ===\033[97m");
        System.out.println("1. Cambiar símbolo de los jugadores");
        System.out.println("2. Establecer número de partidas para ganar");
        System.out.println("3. Configurar orden de salida");
        System.out.println("4. Volver al menú principal");
    }

    /**
     * Manejo de distintas opciones en cada tipo de configuración.
     * @param opcion Número que representa la opción seleccionada en el menú de configuración.
     */
    private void procesarOpcionConfiguracion(int opcion) {
        switch (opcion) {
            case 1:
                configurarSimbolos();
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

    /**
     * Cambia los símbolos de los jugadores dependiendo de cuál haya introducido.
     */
    private void configurarSimbolos() {
        boolean simboloValido = false;

        while (!simboloValido) {
            System.out.print("Introduce el símbolo para el Jugador 1 (X u O): ");
            simboloJugador1 = teclado.next().toUpperCase().charAt(0);

            if (simboloJugador1 == 'X' || simboloJugador1 == 'O') {
                simboloValido = true;
            } else {
                System.out.println("\033[91mSímbolo no válido. Solo se permite 'X' u 'O'.\033[97m");
            }
        }

        simboloValido = false; // Reinicia para el segundo jugador.
        while (!simboloValido) {
            System.out.print("Introduce el símbolo para el Jugador 2 (X u O): ");
            simboloJugador2 = teclado.next().toUpperCase().charAt(0);

            if (simboloJugador2 == 'X' || simboloJugador2 == 'O') {
                if (simboloJugador2 != simboloJugador1) {
                    simboloValido = true;
                } else {
                    System.out.println("\033[91mSímbolo no válido. El Jugador 2 no puede usar el mismo símbolo que el Jugador 1.\033[97m");
                }
            } else {
                System.out.println("\033[91mSímbolo no válido. Solo se permite 'X' u 'O'.\033[97m");
            }
        }

        // Actualizar los símbolos en la lógica del juego.
        logicaJuego.setSimboloJugador1(simboloJugador1);
        logicaJuego.setSimboloJugador2(simboloJugador2);

        System.out.println("Símbolos establecidos:");
        System.out.println("Jugador 1: " + simboloJugador1);
        System.out.println("Jugador 2: " + simboloJugador2);
    }

    /**
     * Establece un número de partidas para que el jugador que llegue a él sea el ganador del juego.
     */
    private void configurarNumeroPartidas() {
        System.out.print("Introduce el número de partidas para ganar: ");
        partidasParaGanar = teclado.nextInt();
        System.out.println("Número de partidas establecido: \033[92m" + partidasParaGanar + "\033[97m");
    }

    /**
     * Establece el orden de salida que se hará en la siguiente partida que se juegue.
     */
    private void configurarOrdenSalida() {
        System.out.println("\n\033[34mElige el orden de salida:\033[97m");
        System.out.println("\033[32m1.\033[97m Aleatorio");
        System.out.println("\033[32m2.\033[97m Sale siempre el ganador de la última partida");
        System.out.println("\033[32m3.\033[97m Sale siempre el perdedor de la última partida");
        System.out.println("\033[32m4.\033[97m Sale siempre el Jugador 1");
        int opcion = teclado.nextInt();

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
     * Muestra las reglas e instrucciones del juego.
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
     * Muestra los créditos del programa.
     */
    private void creditos() {
        System.out.println("\033[96mConecta 4 desarrollado por Jesús Melara Martín.\033[97m\n");
    }

}
