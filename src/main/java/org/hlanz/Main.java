package org.hlanz;

import org.hlanz.servlets.EnemigoService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        //Hacemos un objeto service como en acceso a datos
        EnemigoService service = new EnemigoService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("|||||||||||| API REST DE ENEMIGOS - PRUEBA SIN SERVIDOR ||||||||||||\n");

        /*
        Vamos a simular las peticiones HTTP que le hariamos a un servidor web

        Lo siguiente seria hacer esto con servlets
        (clase de Java que se ejecuta en un servidor web)
         */
        System.out.println("NOTA: Para probar con tu BD real, primero asegurate de tener:");
        System.out.println("- MongoDB corriendo en localhost:27017");
        System.out.println("- Base de datos 'mydb' con colección 'enemigos'");
        System.out.println("\nOpciones:");
        System.out.println("1. GET - Obtener todos los enemigos");
        System.out.println("2. GET - Obtener un enemigo por ID");
        System.out.println("3. POST - Crear un nuevo enemigo");
        System.out.println("0. Salir\n");

        while (true) {
            System.out.print("Selecciona una opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            System.out.println();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    scanner.close();
                    return;
                case 1:
                    // 1. GET - Obtener todos los enemigos
                    System.out.println("1. GET /enemigoservlet/ - Obtener todos los enemigos:");
                    System.out.println(formatJson(service.obtenerTodos()));
                    System.out.println("\n" + "=".repeat(60) + "\n");
                    break;
                case 2:
                    // 2. GET - Obtener un enemigo específico
                    System.out.print("Ingresa el ID del enemigo (ObjectId de MongoDB): ");
                    String idBuscar = scanner.nextLine();
                    System.out.println("2. GET /enemigoservlet/" + idBuscar + " - Obtener enemigo:");
                    System.out.println(formatJson(service.obtenerPorId(idBuscar)));
                    System.out.println("\n" + "=".repeat(60) + "\n");
                    break;
                case 3:
                    // 3. POST - Crear un nuevo enemigo
                    System.out.println("3. POST /enemigoservlet/ - Crear nuevo enemigo:");
                    String nuevoEnemigoJson = "{\"nombre\":\"Nuevo Enemigo\",\"genero\":\"Masculino\",\"pais\":\"España\",\"afiliacion\":\"Ninguna\",\"activo\":true}";
                    System.out.println("Body enviado: " + formatJson(nuevoEnemigoJson));
                    String respuestaCrear = service.crear(nuevoEnemigoJson);
                    System.out.println("Respuesta: " + formatJson(respuestaCrear));
                    System.out.println("\n" + "=".repeat(60) + "\n");
                    break;
                default:
                    System.out.println("Opción inválida. Intenta de nuevo.\n");
            }
        }//fin while
    }// fin Main

    // Método auxiliar para formatear JSON (indentación simple)
    private static String formatJson(String json) {
        StringBuilder formatted = new StringBuilder();
        int indentLevel = 0;
        boolean inString = false;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
            }

            if (!inString) {
                if (c == '{' || c == '[') {
                    formatted.append(c).append('\n');
                    indentLevel++;
                    formatted.append("  ".repeat(indentLevel));
                } else if (c == '}' || c == ']') {
                    formatted.append('\n');
                    indentLevel--;
                    formatted.append("  ".repeat(indentLevel));
                    formatted.append(c);
                } else if (c == ',') {
                    formatted.append(c).append('\n');
                    formatted.append("  ".repeat(indentLevel));
                } else if (c == ':') {
                    formatted.append(c).append(' ');
                } else if (c != ' ') {
                    formatted.append(c);
                }
            } else {
                formatted.append(c);
            }
        }
        return formatted.toString();
    }//Fin metodo auxiliar
}//fin clase
