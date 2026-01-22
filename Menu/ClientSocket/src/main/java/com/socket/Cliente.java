package com.socket;
import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Cliente {
    public static void main(String[] args) {
        String servidorIP = "localhost";
        int servidorPuerto = 5000;
        
        try {
            // 1. Conectar al servidor
            Socket socket = new Socket(servidorIP, servidorPuerto);
            System.out.println("🔗 Conectado al servidor " + servidorIP + ":" + servidorPuerto);
            
            // 2. Crear flujos de comunicación
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            PrintWriter salida = new PrintWriter(
                socket.getOutputStream(), true
            );
            
            // 3. Leer y mostrar el menú del servidor
            String linea;
            while ((linea = entrada.readLine()) != null && !linea.contains("Escribe el número")) {
                System.out.println("📩 " + linea);
            }
            System.out.println("📩 " + linea);
            
            // 4. Crear scanner para entrada del usuario
            Scanner scanner = new Scanner(System.in);
            String opcion;
            boolean salir = false;
            
            // 5. Interactuar con el menú del servidor
            System.out.println("\n💬 Elige una opción del menú:");
            while (!salir) {
                System.out.print("📝 Opción: ");
                opcion = scanner.nextLine();
                
                // Enviar opción al servidor
                salida.println(opcion);
                
                // Leer respuesta del servidor
                String respuesta = entrada.readLine();
                System.out.println("📩 " + respuesta);
                
                // Salir si la opción fue 5 (salir)
                if (opcion.equalsIgnoreCase("5") || opcion.equalsIgnoreCase("salir")) {
                    salir = true;
                    System.out.println("\n🔌 Cerrando conexión...");
                } else {
                    System.out.println("\n💬 Elige otra opción:");
                }
            }
            
            // 6. Cerrar conexiones
            scanner.close();
            entrada.close();
            salida.close();
            socket.close();
            
        } catch (IOException e) {
            System.out.println("❌ Error en el cliente: " + e.getMessage());
        }
    }
}