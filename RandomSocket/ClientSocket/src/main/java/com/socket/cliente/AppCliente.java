package com.socket.cliente;

import com.socket.net.impl.ServicioSocketTCP;
import com.socket.net.impl.ServicioTCPSocketImpl;
import com.socket.net.proxy.TCPSocketProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class AppCliente {

    private static final String HOST = "localhost";
    private static final int PUERTO = 7777;
    private static final int TIMEOUT_MS = 5000;

    public static void main(String[] args) {

        ServicioSocketTCP socket = null;
        BufferedReader entradaConsola = null;
        BufferedReader entrada = null;
        PrintWriter salida = null;

        try {
            // Inyección de dependencias: Real + Proxy
            socket = new TCPSocketProxy(new ServicioTCPSocketImpl());

            // Conectar primero (necesario para obtener streams)
            System.out.println("Conectando al servidor...");
            socket.conectar(HOST, PUERTO);
            System.out.println("✓ Conectado a " + HOST + ":" + PUERTO);

            // Configuración del socket
            socket.setSoTimeout(TIMEOUT_MS);
            socket.setTcpNoDelay(true);

            // Consola
            entradaConsola = new BufferedReader(
                    new InputStreamReader(System.in, StandardCharsets.UTF_8)
            );

            // Streams del socket
            entrada = new BufferedReader(
                    new InputStreamReader(socket.obtenerEntrada(), StandardCharsets.UTF_8)
            );
            salida = new PrintWriter(
                    new OutputStreamWriter(socket.obtenerSalida(), StandardCharsets.UTF_8),
                    true
            );

            // Bucle request/response
            while (true) {
                System.out.print("<Cliente> Inserte un número (o 'salir'): ");
                String mensaje = entradaConsola.readLine();

                if (mensaje == null || mensaje.equalsIgnoreCase("salir")) {
                    break;
                }

                salida.println(mensaje);

                String respuesta = entrada.readLine();
                if (respuesta == null) {
                    System.out.println("✗ El servidor cerró la conexión.");
                    break;
                }

                System.out.println(respuesta);
            }

            System.out.println("✓ Conexión finalizada");

        } catch (IOException e) {
            System.err.println("✗ Error: " + e.getMessage());
            e.printStackTrace();

        } finally {
            // Cierre seguro (orden inverso)
            if (salida != null) {
                salida.close();
            }
            if (entrada != null) {
                try { entrada.close(); } catch (IOException ignored) {}
            }
            if (entradaConsola != null) {
                try { entradaConsola.close(); } catch (IOException ignored) {}
            }
            if (socket != null) {
                try { socket.close(); } catch (IOException ignored) {}
            }
        }
    }
}
