import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        // Instancias de clases
        GestorCarpeta gestorCarpeta = new GestorCarpeta();
        GestorFichero gestorFichero = new GestorFichero();

        // Controlar si el programa sigue ejecutándose
        boolean ejecutando = true;

        while (ejecutando) {
            // Muestra el menú principal con información actualizada de carpetas y archivos
            mostrarMenu(gestorCarpeta, gestorFichero);

            String opcion = teclado.nextLine().trim();

            switch (opcion) {
                case "1": // Seleccionar una carpeta
                    gestorCarpeta.seleccionarCarpeta(teclado);

                    gestorFichero.limpiarSeleccion(); // Se limpia la selección de archivos
                    break;

                case "2": // Leer un fichero de la carpeta seleccionada
                    gestorFichero.leerFichero(teclado, gestorCarpeta);
                    break;

                case "3": // Conversión de formato
                    gestorFichero.convertirFichero(teclado, gestorCarpeta);
                    break;

                case "4": // Salir del programa
                    ejecutando = false;
                    break;

                default:
                    System.out.println("\033[91mOpción no válida. Por favor, elija una opción del menú.\033[97m");
            }
        }

        System.out.println("Cerrando programa...");
    }

    /**
     * Muestra el menú principal del programa y proporciona información actualizada
     * sobre la carpeta y el fichero seleccionados, si los hay.
     * <p>
     * Si hay una carpeta seleccionada, su nombre y contenido se muestran.
     * Si hay un fichero seleccionado, su nombre también se muestra.
     * @param gestorCarpeta objeto que gestiona las acciones con carpetas.
     * @param gestorFichero objeto que gestiona las acciones con ficheros.
     */
    private static void mostrarMenu(GestorCarpeta gestorCarpeta, GestorFichero gestorFichero) {
        System.out.println("\n\033[34m          MENÚ:\033[97m");
        System.out.println("\033[92m1.\033[97m Seleccionar carpeta");
        System.out.println("\033[92m2.\033[97m Lectura de fichero");
        System.out.println("\033[92m3.\033[97m Conversión a otro formato");
        System.out.println("\033[92m4.\033[97m Salir");

        // Si hay una carpeta seleccionada, la muestra junto con su contenido
        if (gestorCarpeta.obtenerCarpetaSeleccionada() != null) {
            System.out.println("\n\033[32mCarpeta seleccionada:\033[37m " + gestorCarpeta.obtenerCarpetaSeleccionada() + "\033[97m");
            gestorCarpeta.listarContenidoCarpeta();
        }

        // Si hay un fichero seleccionado, lo muestra
        if (gestorFichero.obtenerFicheroSeleccionado() != null) {
            System.out.println("\033[92mFichero seleccionado:\033[37m " + gestorFichero.obtenerFicheroSeleccionado() + "\033[97m");
        }

        System.out.print("\nSeleccione una opción: ");
    }
}