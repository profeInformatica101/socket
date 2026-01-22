package udp.socket;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class UDP_EchoClientTest {

    @Test
    void testClienteRecibeRespuestaEco() throws Exception {

        int puerto = 5051; // puerto de test (no el real)
        String mensaje = "hola test";

        // -------------------------
        // Servidor UDP falso
        // -------------------------
        Thread servidorFake = new Thread(() -> {
            try (DatagramSocket serverSocket = new DatagramSocket(puerto)) {

                byte[] buffer = new byte[1400];
                DatagramPacket recibido = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(recibido);

                String recibidoTexto = new String(
                        recibido.getData(), 0, recibido.getLength(), "UTF-8"
                );

                String respuesta = "#" + recibidoTexto + "#";
                byte[] out = respuesta.getBytes("UTF-8");

                DatagramPacket enviado = new DatagramPacket(
                        out,
                        out.length,
                        recibido.getAddress(),
                        recibido.getPort()
                );

                serverSocket.send(enviado);

            } catch (Exception e) {
                fail("Error en servidor fake: " + e.getMessage());
            }
        });

        servidorFake.start();

        // -------------------------
        // Simular entrada por teclado
        // -------------------------
        String inputSimulado = mensaje + "\n\n";
        System.setIn(new ByteArrayInputStream(inputSimulado.getBytes()));

        // Capturar salida por consola
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salida));

        // -------------------------
        // Ejecutar cliente
        // -------------------------
        UDP_EchoClient.main(new String[]{"localhost", String.valueOf(puerto)});

        // -------------------------
        // Verificaciones
        // -------------------------
        String salidaTexto = salida.toString();

        assertTrue(
                salidaTexto.contains("#" + mensaje + "#"),
                "La respuesta del servidor no es correcta"
        );
    }
}
