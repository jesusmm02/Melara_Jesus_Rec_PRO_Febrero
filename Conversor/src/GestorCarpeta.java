import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class GestorCarpeta {

    private String carpetaSeleccionada; // Ruta de la carpeta seleccionada

    /**
     * Obtiene la ruta de la carpeta actualmente seleccionada.
     *
     * @return la ruta de la carpeta seleccionada como una cadena. Si no se ha seleccionado
     * ninguna carpeta, devuelve null.
     */
    public String obtenerCarpetaSeleccionada() {
        return carpetaSeleccionada;
    }

    /**
     * Permite al usuario seleccionar una carpeta a través de la entrada de teclado.
     * Valida si la ruta proporcionada es una carpeta existente y, de ser válida, la almacena.
     *
     * @param teclado lee entrada de ruta por teclado.
     */
    public void seleccionarCarpeta(Scanner teclado) {
        System.out.print("\033[34mIngrese la ruta de la carpeta: \033[97m");
        String rutaCarpeta = teclado.nextLine().trim();

        File carpeta = new File(rutaCarpeta); // Crea un objeto File con la ruta ingresada

        // Verifica si la ruta corresponde a una carpeta existente
        if (carpeta.exists() && carpeta.isDirectory()) {
            carpetaSeleccionada = rutaCarpeta; // Almacena la ruta si es válida
            System.out.println("\033[32mCarpeta seleccionada correctamente.\033[97m");
        } else {
            // Mensaje de error si la ruta no es válida o no es una carpeta
            System.out.println("\033[91mRuta inválida o no es una carpeta. Inténtelo de nuevo.\033[97m");
        }
    }

    /**
     * Lista los archivos que hay dentro de la carpeta seleccionada.
     * Solo se muestran archivos regulares (no directorios).
     */
    public void listarContenidoCarpeta() {
        try {
            // Utiliza el API de NIO para listar los archivos en la carpeta seleccionada
            Files.list(Paths.get(carpetaSeleccionada))
                    // Filtra solo los elementos que son archivos regulares
                    .filter(Files::isRegularFile)
                    // Por cada archivo encontrado, imprime su nombre
                    .forEach(file -> System.out.println("  - " + file.getFileName()));
        } catch (IOException e) {
            System.out.println("\033[91mError al listar contenidos de la carpeta.\033[97m");
        }
    }
}