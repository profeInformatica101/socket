package udp.socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UDP_EchoServerMockitoTest {

    private DatagramSocket socket;

    @BeforeEach
    void setUp() {
        socket = mock(DatagramSocket.class);
    }

    @Test
    @DisplayName("processOne: llama a receive y responde con #msg# al mismo host/puerto")
    void processOne_wrapsMessageAndSendsBackToSameClient() throws Exception {

        String msg = "hola";
        InetAddress ip = InetAddress.getByName("127.0.0.1");
        int port = 9999;

        doAnswer(invocation -> {
            DatagramPacket p = invocation.getArgument(0);

            byte[] data = msg.getBytes(StandardCharsets.UTF_8);

            // Rellenamos el paquete como lo haría un socket real
            p.setData(data);
            p.setLength(data.length);
            p.setAddress(ip);
            p.setPort(port);
            return null;
        }).when(socket).receive(any(DatagramPacket.class));

        ArgumentCaptor<DatagramPacket> captor = ArgumentCaptor.forClass(DatagramPacket.class);

        // Necesitamos que UDP_EchoServer tenga un método processOne estático
        // Si no existe, necesitamos crearlo o cambiar la llamada
        UDP_EchoServer.processOne(socket);

        // Verifica que se llamó a receive()
        verify(socket, times(1)).receive(any(DatagramPacket.class));

        // Verifica que se llamó a send() y captura el paquete enviado
        verify(socket, times(1)).send(captor.capture());

        DatagramPacket sent = captor.getValue();

        assertEquals(ip, sent.getAddress());
        assertEquals(port, sent.getPort());

        String out = new String(sent.getData(), 0, sent.getLength(), StandardCharsets.UTF_8);
        assertEquals("#" + msg + "#", out);
    }
}