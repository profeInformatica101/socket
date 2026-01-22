package com.socket;
import java.io.*;
import java.net.*;
public class Servidor {
    public static void main(String[] args) {
        int puerto = 5000;
        
        try {
            // 1. Crear el servidor socket
            ServerSocket servidor = new ServerSocket(puerto);
            System.out.println("🖥️  Servidor iniciado en el puerto " + puerto);
            System.out.println("⏳ Esperando clientes...");
            
            // 2. Aceptar conexión del cliente
            Socket cliente = servidor.accept();
            System.out.println("✅ Cliente conectado: " + cliente.getInetAddress());
            
            // 3. Crear flujos de comunicación
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(cliente.getInputStream())
            );
            PrintWriter salida = new PrintWriter(
                cliente.getOutputStream(), true
            );
            
            // 4. Enviar menú de bienvenida
            enviarMenu(salida);
            
            // 5. Procesar comandos del cliente
            String comando;
            boolean salir = false;
            
            while (!salir && (comando = entrada.readLine()) != null) {
                System.out.println("📩 Cliente ejecuta: " + comando);
                
                // Procesar comando
                String respuesta = procesarComando(comando);
                salida.println(respuesta);
                System.out.println("📤 Servidor responde: " + respuesta);
                
                // Verificar si debe salir
                if (comando.equalsIgnoreCase("5") || comando.equalsIgnoreCase("salir")) {
                    salir = true;
                }
            }
            
            // 6. Cerrar conexiones
            System.out.println("🔌 Cerrando conexión con el cliente");
            entrada.close();
            salida.close();
            cliente.close();
            servidor.close();
            
        } catch (IOException e) {
            System.out.println("❌ Error en el servidor: " + e.getMessage());
        }
    }
    
    /**
     * Envía el menú de opciones al cliente
     */
    private static void enviarMenu(PrintWriter salida) {
        salida.println("=== 🖥️  MENÚ DEL SERVIDOR ===");
        salida.println("1. Hora actual del servidor");
        salida.println("2. Información del sistema");
        salida.println("3. Estadísticas de conexión");
        salida.println("4. Mensaje aleatorio");
        salida.println("5. Salir");
        salida.println("================================");
        salida.println("Escribe el número de opción:");
    }
    
    /**
     * Procesa el comando recibido del cliente
     */
    private static String procesarComando(String comando) {
        switch (comando.toLowerCase()) {
            case "1":
            case "hora":
                return "⏰ Hora actual: " + new java.util.Date();
                
            case "2":
            case "info":
                return "💻 Sistema: " + System.getProperty("os.name") + 
                       " | Java: " + System.getProperty("java.version") +
                       " | Usuario: " + System.getProperty("user.name");
                
            case "3":
            case "stats":
                return "📊 Memoria disponible: " + 
                       (Runtime.getRuntime().freeMemory() / 1024 / 1024) + " MB / " +
                       (Runtime.getRuntime().totalMemory() / 1024 / 1024) + " MB";
                
            case "4":
            case "mensaje":
                String[] mensajes = {
                    "¡Hola! 😊",
                    "¡Qué buen día! 🌞",
                    "¡Sigue programando! 💻",
                    "¡Java es genial! ☕",
                    "¡Never give up! 💪"
                };
                int indice = (int)(Math.random() * mensajes.length);
                return "🎲 Mensaje aleatorio: " + mensajes[indice];
                
            case "5":
            case "salir":
                return "👋 ¡Hasta luego! Gracias por conectarte.";
                
            default:
                return "❌ Comando no reconocido. Usa números 1-5.";
        }
    }
}