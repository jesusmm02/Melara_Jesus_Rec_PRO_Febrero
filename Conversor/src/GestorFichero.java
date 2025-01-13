import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class GestorFichero {

    private String ficheroSeleccionado; // Fichero seleccionado

    private List<Map<String, String>> datos = new ArrayList<>(); // Lista de mapas que almacena los datos del fichero leído

    /**
     * Obtiene el nombre del fichero seleccionado.
     *
     * @return el nombre del fichero, o null si no hay ningún fichero seleccionado.
     */
    public String obtenerFicheroSeleccionado() {
        return ficheroSeleccionado;
    }

    /**
     * Limpia la selección actual de fichero y los datos almacenados.
     */
    public void limpiarSeleccion() {
        ficheroSeleccionado = null;
        datos.clear(); // Elimina todos los datos cargados del fichero.
    }

    /**
     * Permite al usuario leer un fichero dentro de la carpeta seleccionada.
     * Verifica que el fichero existe y delega el procesamiento al método `parsearFichero`.
     *
     * @param scanner lee entrada de fichero por teclado.
     * @param gestorCarpeta objeto para acceder a la carpeta seleccionada.
     */
    public void leerFichero(Scanner scanner, GestorCarpeta gestorCarpeta) {
        if (gestorCarpeta.obtenerCarpetaSeleccionada() == null) {
            System.out.println("\033[33mPrimero debe seleccionar una carpeta.\033[97m");
            return;
        }

        System.out.print("\033[34mIngrese el nombre del fichero:\033[97m ");
        String nombreFichero = scanner.nextLine().trim();
        File fichero = new File(gestorCarpeta.obtenerCarpetaSeleccionada(), nombreFichero);

        // Verifica si el fichero existe y es válido.
        if (fichero.exists() && fichero.isFile()) {
            ficheroSeleccionado = nombreFichero;
            try {
                datos = parsearFichero(fichero); // Procesa el fichero.
                System.out.println("\033[92mFichero leído correctamente.\033[97m");
            } catch (Exception e) {
                System.out.println("\033[91mError al leer el fichero: \033[97m" + e.getMessage());
            }
        } else {
            System.out.println("\033[91mEl fichero no existe o no es válido.\033[97m");
        }
    }

    /**
     * Identifica el formato del fichero y llama al método correspondiente para procesarlo.
     *
     * @param fichero el archivo a procesar.
     * @return una lista de mapas con los datos procesados del fichero.
     * @throws IOException si ocurre un error de lectura o si el formato no es soportado.
     */
    private List<Map<String, String>> parsearFichero(File fichero) throws IOException {
        String extension = obtenerExtension(fichero);
        switch (extension) {
            case "csv":
                return parsearCsv(fichero);
            case "json":
                return parsearJson(fichero);
            case "xml":
                return parsearXml(fichero);
            default:
                throw new IllegalArgumentException("\033[91mFormato de fichero no soportado: \033[97m" + extension);
        }
    }

    /**
     * Procesa un fichero CSV y convierte su contenido en una lista de mapas.
     *
     * @param fichero el archivo CSV a procesar.
     * @return una lista de mapas, donde cada mapa representa una fila del archivo.
     * @throws IOException si ocurre un error durante la lectura del archivo.
     */
    private List<Map<String, String>> parsearCsv(File fichero) throws IOException {
        List<Map<String, String>> resultado = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            // Lee la primera línea de encabezados.
            String[] encabezados = br.readLine().split(",");

            String linea;
            while ((linea = br.readLine()) != null) {
                // Divide cada línea en valores y los asocia con los encabezados.
                String[] valores = linea.split(",");
                Map<String, String> fila = new HashMap<>();

                // Asocia cada valor con el encabezado correspondiente
                for (int i = 0; i < encabezados.length; i++) {
                    fila.put(encabezados[i], valores[i]); // Clave: encabezado, Valor: dato
                }
                resultado.add(fila); // Agrega la fila procesada a la lista de resultado.
            }
        }
        return resultado; // Devuelve la lista de mapas
    }

    /**
     * Procesa un fichero JSON y convierte su contenido en una lista de mapas.
     *
     * @param fichero el archivo JSON a procesar.
     * @return una lista de mapas, donde cada mapa representa un objeto JSON.
     * @throws IOException si ocurre un error durante la lectura o el formato es inválido.
     */
    private List<Map<String, String>> parsearJson(File fichero) throws IOException {
        List<Map<String, String>> resultado = new ArrayList<>();
        StringBuilder contenidoJson = new StringBuilder();

        // Leer el contenido completo del fichero JSON.
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenidoJson.append(linea.trim());
            }
        }

        // Convertir el contenido JSON a un mapa o lista, dependiendo de su estructura
        String contenido = contenidoJson.toString().trim();
        if (contenido.startsWith("{")) {
            // Si es un objeto JSON, lo procesamos directamente como un único objeto
            Map<String, String> mapaJson = parsearObjetoJson(contenido);
            resultado.add(mapaJson);
        } else if (contenido.startsWith("[") && contenido.endsWith("]")) {
            // Si es un array de objetos JSON, lo procesamos como una lista
            contenido = contenido.substring(1, contenido.length() - 1); // Quita los corchetes exteriores
            List<String> objetosJson = separarObjetosJson(contenido);
            for (String objeto : objetosJson) {
                resultado.add(parsearObjetoJson(objeto));
            }
        } else {
            // Si el contenido no es válido, lanzamos una excepción
            throw new IllegalArgumentException("\033[91mFormato JSON inválido: no es ni un objeto ni un array de objetos.\033[97m");
        }

        return resultado;
    }


    /**
     * Método auxiliar que separa objetos JSON dentro de un array.
     *
     * @param contenido el contenido JSON en formato de cadena.
     * @return lista de objetos JSON
     */
    private List<String> separarObjetosJson(String contenido) {
        List<String> objetosJson = new ArrayList<>();
        int nivelLlaves = 0;
        StringBuilder objetoActual = new StringBuilder();

        for (char c : contenido.toCharArray()) {
            if (c == '{') nivelLlaves++;
            if (c == '}') nivelLlaves--;

            objetoActual.append(c); // Agrega el carácter actual al objeto en construcción

            // Si el nivel de llaves es 0 y se cierra un objeto completo
            if (nivelLlaves == 0 && c == '}') {
                objetosJson.add(objetoActual.toString());
                objetoActual.setLength(0);
            }
        }

        return objetosJson;
    }


    /**
     * Método auxiliar que procesa pares clave-valor en un objeto JSON.
     *
     * @param objeto una cadena que representa un objeto JSON.
     * @return un mapa que almacena las claves y valores del objeto
     */
    private Map<String, String> parsearObjetoJson(String objeto) {
        Map<String, String> fila = new HashMap<>(); // Mapa para almacenar las claves y valores del objeto actual
        objeto = objeto.substring(1, objeto.length() - 1).trim();
        String[] pares = objeto.split(","); // Divide el objeto en pares clave-valor separados por comas

        for (String par : pares) {
            int separador = par.indexOf(':'); // Encuentra el carácter ':' que separa clave y valor
            if (separador == -1) throw new IllegalArgumentException("\033[91mFormato inválido: \033[97m" + par);

            // Extrae la clave y el valor
            String clave = par.substring(0, separador).trim();
            String valor = par.substring(separador + 1).trim();

            // Elimina comillas exteriores de la clave
            if (clave.startsWith("\"") && clave.endsWith("\"")) {
                clave = clave.substring(1, clave.length() - 1);
            }

            // Elimina comillas exteriores del valor
            if (valor.startsWith("\"") && valor.endsWith("\"")) {
                valor = valor.substring(1, valor.length() - 1);
            }

            fila.put(clave, valor); // Agrega la clave y el valor al mapa
        }

        return fila;
    }

    /**
     * Procesa un fichero XML y convierte su contenido en una lista de mapas.
     *
     * @param fichero el archivo XML a leer.
     * @return una lista de mapas con los datos del fichero XML.
     * @throws IOException si ocurre un error durante el procesamiento.
     */
    private List<Map<String, String>> parsearXml(File fichero) throws IOException {
        List<Map<String, String>> resultado = new ArrayList<>();
        try {
            // Configura el parser de XML y carga el documento
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.parse(fichero);

            documento.getDocumentElement().normalize(); // Elimina espacios redundantes y ordena nodos

            NodeList nodos = documento.getDocumentElement().getChildNodes(); // Obtiene todos los nodos hijos de la raíz del documento

            for (int i = 0; i < nodos.getLength(); i++) {
                Node nodo = nodos.item(i);

                // Verifica si el nodo es un elemento (ignora comentarios y texto)
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Map<String, String> fila = new HashMap<>();
                    Element elemento = (Element) nodo;

                    // Itera sobre los hijos del elemento actual
                    NodeList hijos = elemento.getChildNodes();
                    for (int j = 0; j < hijos.getLength(); j++) {
                        Node hijo = hijos.item(j);

                        // Solo procesa nodos que sean elementos
                        if (hijo.getNodeType() == Node.ELEMENT_NODE) {
                            fila.put(hijo.getNodeName(), hijo.getTextContent()); // Clave: nombre del hijo, Valor: contenido
                        }
                    }
                    resultado.add(fila); // Agrega el mapa procesado a la lista de resultados.
                }
            }
        } catch (Exception e) {
            throw new IOException("\033[91mError al parsear el archivo XML: \033[97m" + e.getMessage());
        }
        return resultado; // Lista de mapas procesados
    }

    /**
     * Convierte el contenido de los datos leídos en el formato especificado (csv, json, xml)
     * y guarda el archivo convertido en la carpeta seleccionada.
     *
     * @param scanner Objeto Scanner para leer la entrada del usuario.
     * @param gestorCarpeta Instancia de GestorCarpeta para obtener la carpeta seleccionada.
     */
    public void convertirFichero(Scanner scanner, GestorCarpeta gestorCarpeta) {
        if (datos.isEmpty()) {
            System.out.println("\033[33mPrimero debe leer un fichero.\033[97m");
            return;
        }

        System.out.print("\033[34mSeleccione el formato de salida \033[92m(csv, json, xml)\033[97m:");
        String formato = scanner.nextLine().trim().toLowerCase();

        System.out.print("\033[34mIngrese el nombre del fichero de salida (sin extensión): \033[97m");
        String nombreSalida = scanner.nextLine().trim();
        File ficheroSalida = new File(gestorCarpeta.obtenerCarpetaSeleccionada(), nombreSalida + "." + formato);

        try {
            switch (formato) {
                case "csv":
                    escribirCsv(ficheroSalida);
                    break;
                case "json":
                    escribirJson(ficheroSalida);
                    break;
                case "xml":
                    escribirXml(ficheroSalida);
                    break;
                default:
                    System.out.println("\033[91mFormato no soportado.\033[97m");
                    return;
            }
            System.out.println("\033[33mFichero convertido y guardado en:\033[92m " + ficheroSalida.getAbsolutePath() + "\033[97m");
        } catch (Exception e) {
            System.out.println("\033[91mError al convertir el fichero:\033[97m " + e.getMessage());
        }
    }


    /**
     * Escribe los datos en formato CSV en el archivo especificado.
     *
     * @param fichero Archivo donde se escribirá el contenido en formato CSV.
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    private void escribirCsv(File fichero) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fichero))) {
            if (!datos.isEmpty()) {
                String[] encabezados = datos.get(0).keySet().toArray(new String[0]);
                bw.write(String.join(",", encabezados));
                bw.newLine();

                for (Map<String, String> fila : datos) {
                    String linea = Arrays.stream(encabezados)
                            .map(fila::get)
                            .collect(Collectors.joining(","));
                    bw.write(linea);
                    bw.newLine();
                }
            }
        }
    }

    /**
     * Escribe los datos en formato JSON en el archivo especificado.
     *
     * @param fichero Archivo donde se escribirá el contenido en formato JSON.
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    private void escribirJson(File fichero) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");

        for (int i = 0; i < datos.size(); i++) {
            Map<String, String> fila = datos.get(i);
            jsonBuilder.append("  {\n");

            int j = 0;
            for (Map.Entry<String, String> entrada : fila.entrySet()) {
                String clave = entrada.getKey().replace("/", " "); // Reemplaza '/' en claves con espacio
                String valor = entrada.getValue()
                        .replace("/", " ")        // Reemplaza '/' en valores con espacio
                        .replace("\\", "")    // Escapa barras invertidas correctamente
                        .replace("\"", "");   // Escapa comillas dobles correctamente

                jsonBuilder.append("    \"").append(clave).append("\": \"").append(valor).append("\"");
                if (j < fila.size() - 1) {
                    jsonBuilder.append(",");
                }
                jsonBuilder.append("\n");
                j++;
            }

            jsonBuilder.append("  }");
            if (i < datos.size() - 1) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("\n");
        }

        jsonBuilder.append("]");

        // Escribir el JSON al archivo
        try (FileWriter escritor = new FileWriter(fichero)) {
            escritor.write(jsonBuilder.toString());
        }
    }

    /**
     * Escribe los datos en formato XML en el archivo especificado.
     *
     * @param fichero Archivo donde se escribirá el contenido en formato XML.
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    private void escribirXml(File fichero) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichero))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<datos>\n");

            for (Map<String, String> fila : datos) {
                writer.write("  <fila>\n");

                for (Map.Entry<String, String> entrada : fila.entrySet()) {
                    String clave = entrada.getKey().trim();  // Elimina espacios en blanco en la clave
                    String valor = entrada.getValue().trim(); // Elimina espacios en blanco en el valor

                    // Escapar caracteres especiales en XML
                    clave = escaparXml(clave);
                    valor = escaparXml(valor);

                    writer.write("    <" + clave + ">" + valor + "</" + clave + ">\n");
                }

                writer.write("  </fila>\n");
            }

            writer.write("</datos>\n");
        }
    }

    
    /**
     * Sustituye caracteres especiales en una cadena para que sea válida en XML.
     *
     * @param texto Cadena de texto a escapar.
     * @return Cadena de texto con caracteres especiales escapados.
     */
    private String escaparXml(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("{", "")
                .replace("}", "")
                .replace("@", "")
                .replace("/", "")
                .replace(":", "")
                .replace("\"", "")
                .replace("'", "&apos;");
    }


    /**
     * Obtiene la extensión de un archivo a partir de su nombre.
     *
     * @param fichero el archivo del cual obtener la extensión.
     * @return la extensión del archivo, o una cadena vacía si no tiene extensión.
     */
    private String obtenerExtension(File fichero) {
        String nombre = fichero.getName();
        int indiceUltimoPunto = nombre.lastIndexOf('.');
        return indiceUltimoPunto == -1 ? "" : nombre.substring(indiceUltimoPunto + 1);
    }
}