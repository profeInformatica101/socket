package com.socket.net.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.socket.net.ServicioSocketTCP;

/**
 * Proxy que añade logging, validación y métricas.
 */
public class TCPSocketProxy implements ServicioSocketTCP {
    
    private final ServicioSocketTCP real;
    private final Logger logger;
    private long bytesSent = 0;
    private long bytesReceived = 0;
    
    public TCPSocketProxy(ServicioSocketTCP real) {
        this.real = real;
        this.logger = Logger.getLogger(TCPSocketProxy.class.getName());
    }
    
    @Override
    public void conectar(String host, int puerto) throws IOException {
        logger.info(String.format("Conectando a %s:%d", host, puerto));
        long inicio = System.currentTimeMillis();
        try {
            real.conectar(host, puerto);
            long duracion = System.currentTimeMillis() - inicio;
            logger.info(String.format("Conectado exitosamente en %dms", duracion));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al conectar", e);
            throw e;
        }
    }
    
    @Override
    public boolean estaConectado() {
        return real.estaConectado();
    }
    
    @Override
    public InputStream obtenerEntrada() throws IOException {
        logger.fine("Obteniendo InputStream");
        return new InputStreamMonitor(real.obtenerEntrada());
    }
    
    @Override
    public OutputStream obtenerSalida() throws IOException {
        logger.fine("Obteniendo OutputStream");
        return new OutputStreamMonitor(real.obtenerSalida());
    }
    
    @Override
    public boolean getKeepAlive() throws SocketException {
        return real.getKeepAlive();
    }
    
    @Override
    public void setKeepAlive(boolean on) throws SocketException {
        logger.fine("KeepAlive: " + on);
        real.setKeepAlive(on);
    }
    
    @Override
    public boolean getTcpNoDelay() throws SocketException {
        return real.getTcpNoDelay();
    }
    
    @Override
    public void setTcpNoDelay(boolean on) throws SocketException {
        logger.fine("TcpNoDelay: " + on);
        real.setTcpNoDelay(on);
    }
    
    @Override
    public int getSoTimeout() throws SocketException {
        return real.getSoTimeout();
    }
    
    @Override
    public void setSoTimeout(int ms) throws SocketException {
        logger.fine("SoTimeout: " + ms + "ms");
        real.setSoTimeout(ms);
    }
    
    @Override
    public boolean getReuseAddress() throws SocketException {
        return real.getReuseAddress();
    }
    
    @Override
    public void setReuseAddress(boolean on) throws SocketException {
        logger.fine("ReuseAddress: " + on);
        real.setReuseAddress(on);
    }
    
    @Override
    public int getPuertoLocal() {
        return real.getPuertoLocal();
    }
    
    @Override
    public int getPuertoRemoto() {
        return real.getPuertoRemoto();
    }
    
    @Override
    public void close() throws IOException {
        logger.info(String.format("Cerrando socket. Stats: Enviados=%d bytes, Recibidos=%d bytes", 
            bytesSent, bytesReceived));
        real.close();
    }
    
    @Override
    public boolean estaCerrado() {
        return real.estaCerrado();
    }
    
    // Wrappers para monitorear I/O
    
    private class OutputStreamMonitor extends OutputStream {
        private final OutputStream delegate;
        
        OutputStreamMonitor(OutputStream delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public void write(int b) throws IOException {
            delegate.write(b);
            bytesSent++;
        }
        
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            delegate.write(b, off, len);
            bytesSent += len;
        }
        
        @Override
        public void flush() throws IOException {
            delegate.flush();
        }
        
        @Override
        public void close() throws IOException {
            delegate.close();
        }
    }
    
    private class InputStreamMonitor extends InputStream {
        private final InputStream delegate;
        
        InputStreamMonitor(InputStream delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public int read() throws IOException {
            int data = delegate.read();
            if (data != -1) bytesReceived++;
            return data;
        }
        
        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int count = delegate.read(b, off, len);
            if (count > 0) bytesReceived += count;
            return count;
        }
        
        @Override
        public void close() throws IOException {
            delegate.close();
        }
    }
}