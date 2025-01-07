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
                    System.out.println("Función en construcción...");
                    break;

                case "4": // Salir del programa
                    ejecutando = false;
                    break;

                default:
                    System.out.println("Opción no válida. Por favor, elija una opción del menú.");
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
        System.out.println("\nMenú:");
        System.out.println("1. Seleccionar carpeta");
        System.out.println("2. Lectura de fichero");
        System.out.println("3. Conversión a otro formato (FUNCIÓN EN CONSTRUCCIÓN)");
        System.out.println("4. Salir");

        // Si hay una carpeta seleccionada, la muestra junto con su contenido
        if (gestorCarpeta.obtenerCarpetaSeleccionada() != null) {
            System.out.println("\nCarpeta seleccionada: " + gestorCarpeta.obtenerCarpetaSeleccionada());
            gestorCarpeta.listarContenidoCarpeta();
        }

        // Si hay un fichero seleccionado, lo muestra
        if (gestorFichero.obtenerFicheroSeleccionado() != null) {
            System.out.println("Fichero seleccionado: " + gestorFichero.obtenerFicheroSeleccionado());
        }

        System.out.print("\nSeleccione una opción: ");
    }
}