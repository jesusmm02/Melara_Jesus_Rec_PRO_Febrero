package InterfazGrafica;

import java.util.Scanner;

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
        System.out.println("2. Jugar contra IA - NO FUNCIONAL");
        System.out.println("3. Configuración");
        System.out.println("4. Instrucciones del juego");
        System.out.println("5. Créditos");
        System.out.println("6. Salir");
    }

    /**
     * Método donde se pasa la opción del menú que hemos seleccionado y hace la acción solicitada
     * @param opcion
     */
    private void selectorOpcion(int opcion) {
        switch (opcion) {
            case 1:
                jugar();
                break;
            case 2:
                System.out.println("Función en construcción...");
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
     * Método que se encarga de la partida que se esté jugando
     */
    private void jugar() {
        // Inicialización de los contadores de partidas ganadas por los dos jugadores
        int partidasGanadasJugador1 = 0;
        int partidasGanadasJugador2 = 0;

        // Determinar quién inicia.
        if (ordenSalida.equals("Aleatorio")) { //hola
            logicaJuego.reiniciarJuego();
            if (Math.random() < 0.5) { // 50% de probabilidad de que inicie el Jugador 2.
                logicaJuego.cambiarJugador(); // Cambia al Jugador 2.
            }
        } else if (ordenSalida.equals("Sale Perdedor")) { // El perdedor de la última partida inicia la nueva
            if (partidasGanadasJugador1 > partidasGanadasJugador2) {
                logicaJuego.cambiarJugador(); // Cambia al Jugador 2 si el Jugador 1 ganó más partidas
            }
        } else if (ordenSalida.equals("Sale Ganador")) { // El ganador de la última partida inicia
            if (partidasGanadasJugador2 > partidasGanadasJugador1) {
                logicaJuego.cambiarJugador(); // Cambia al Jugador 2 si este ganó más partidas
            }
        } else {
            // Por defecto, empieza el Jugador 1
            logicaJuego.reiniciarJuego();
        }

        boolean juegoTerminado = false; // Controlador para saber si la partida ha terminado


        while (partidasGanadasJugador1 < partidasParaGanar && partidasGanadasJugador2 < partidasParaGanar) {
            while (!juegoTerminado) {
                System.out.println("\nTurno del jugador: " + logicaJuego.obtenerJugadorActual());
                int columna;


                System.out.print("Introduce la columna (1-7): ");
                columna = entrada.nextInt() - 1; // Resta 1 para convertir a índice basado en 0


                if (logicaJuego.jugarTurno(columna)) { // Verifica si el movimiento es válido
                    dibujarTablero();

                    if (logicaJuego.verificarGanador()) {
                        System.out.println("¡El jugador " + logicaJuego.obtenerJugadorActual() + " gana la partida!");
                        if (logicaJuego.obtenerJugadorActual() == colorJugador1) {
                            partidasGanadasJugador1++; // Incrementa el contador de victorias para el Jugador 1
                        } else {
                            partidasGanadasJugador2++; // Incrementa el contador de victorias para el Jugador 2
                        }
                        juegoTerminado = true;
                    } else if (logicaJuego.tableroLleno()) { // Si el tablero está lleno y sin 4 fichas consecutivas de ningún jugador
                        System.out.println("¡Es un empate!");
                        juegoTerminado = true;
                    } else {
                        logicaJuego.cambiarJugador(); // Si no hay ni ganador, ni empate, cambia el turno al otro jugador
                    }
                } else {
                    System.out.println("Movimiento no válido, intenta de nuevo.");
                }
            }

            // Marcador después de cada partida
            System.out.println("Marcador:");
            System.out.println("Jugador 1 (" + colorJugador1 + "): " + partidasGanadasJugador1);
            System.out.println("Jugador 2 (" + colorJugador2 + "): " + partidasGanadasJugador2);

            // // Si aún ningún jugador ha alcanzado las victorias necesarias para ganar el juego, reinicia el juego para otra partida
            if (partidasGanadasJugador1 < partidasParaGanar && partidasGanadasJugador2 < partidasParaGanar) {
                logicaJuego.reiniciarJuego();
                juegoTerminado = false;
            }
        }

        // Se declara al ganador del juego una vez se alcance el número de victorias necesarias para ganar el juego
        if (partidasGanadasJugador1 > partidasGanadasJugador2) {
            System.out.println("¡Jugador 1 gana el juego!\n");
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
     * @param opcion
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
                System.out.println("Opción no válida.");
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
                System.out.println("Opción no válida. Manteniendo configuración anterior.");
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
