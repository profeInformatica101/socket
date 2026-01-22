package udp.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class UDP_EchoServer {

    static final int MAX_BYTES = 1400;
    static final String COD_TEXTO = "UTF-8";

    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("ERROR, indicar: puerto.");
            System.exit(1);
        }

        int numPuerto = Integer.parseInt(args[0]);

        try (DatagramSocket serverSocket = new DatagramSocket(numPuerto)) {

            System.out.printf("Creado socket de datagramas para puerto %s.%n", numPuerto);

            while (true) {
                System.out.println("Esperando datagramas.");
                processOne(serverSocket); // <--- ahora testeable
            }

        } catch (SocketException ex) {
            System.out.println("Excepción de sockets");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Excepción de E/S");
            ex.printStackTrace();
        }
    }

    public static void processOne(DatagramSocket socket) {
        try {
            byte[] buffer = new byte[MAX_BYTES];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            
            // Recibir el paquete
            socket.receive(packet);
            
            // Procesar el mensaje
            String received = new String(
                packet.getData(), 
                0, 
                packet.getLength(), 
                StandardCharsets.UTF_8
            );
            
            // Mostrar el mensaje recibido
            System.out.printf("Recibido de %s:%d -> %s%n", 
                packet.getAddress().getHostAddress(),
                packet.getPort(),
                received);
            
            // Envolver el mensaje con #
            String response = "#" + received + "#";
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            
            // Enviar la respuesta al mismo cliente
            DatagramPacket responsePacket = new DatagramPacket(
                responseBytes,
                responseBytes.length,
                packet.getAddress(),
                packet.getPort()
            );
            
            socket.send(responsePacket);
            System.out.printf("Enviado a %s:%d -> %s%n", 
                packet.getAddress().getHostAddress(),
                packet.getPort(),
                response);
            
        } catch (IOException e) {
            System.out.println("Error en processOne:");
            e.printStackTrace();
        }
    }
}