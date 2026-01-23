package com.socket.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

/**
 * Contrato para socket TCP cliente/servidor (encapsula java.net.Socket).
 */
public interface ServicioSocketTCP{
    
    /**
     * Conecta al servidor remoto.
     * @param host Dirección IP o nombre DNS
     * @param puerto Puerto remoto (1-65535)
     * @throws IOException Si falla la conexión
     */
    void conectar(String host, int puerto) throws IOException;
    
    /**
     * Verifica si está conectado.
     */
    boolean estaConectado();
    
    /**
     * Obtiene stream de entrada para recibir datos.
     * @throws IOException Si el socket no está conectado
     */
    InputStream obtenerEntrada() throws IOException;
    
    /**
     * Obtiene stream de salida para enviar datos.
     * @throws IOException Si el socket no está conectado
     */
    OutputStream obtenerSalida() throws IOException;
    
    /**
     * Obtiene configuración de TCP KeepAlive.
     */
    boolean getKeepAlive() throws SocketException;
    
    /**
     * Habilita/deshabilita KeepAlive (detecta conexiones muertas).
     */
    void setKeepAlive(boolean on) throws SocketException;
    
    /**
     * Obtiene configuración del algoritmo de Nagle.
     */
    boolean getTcpNoDelay() throws SocketException;
    
    /**
     * Deshabilita/habilita algoritmo de Nagle (TCP_NODELAY).
     * @param on true = deshabilita Nagle (menor latencia)
     */
    void setTcpNoDelay(boolean on) throws SocketException;
    
    /**
     * Obtiene timeout de lectura en milisegundos.
     */
    int getSoTimeout() throws SocketException;
    
    /**
     * Establece timeout para operaciones de lectura.
     * @param ms Milisegundos (0 = infinito)
     */
    void setSoTimeout(int ms) throws SocketException;
    
    /**
     * Obtiene configuración de reutilización de dirección.
     */
    boolean getReuseAddress() throws SocketException;
    
    /**
     * Permite reutilizar dirección local.
     */
    void setReuseAddress(boolean on) throws SocketException;
    
    /**
     * Obtiene puerto local del socket.
     */
    int getPuertoLocal();
    
    /**
     * Obtiene puerto remoto del socket.
     */
    int getPuertoRemoto();
    
    /**
     * Cierra la conexión y libera recursos.
     */
    void close() throws IOException;
    
    /**
     * Verifica si está cerrado.
     */
    boolean estaCerrado();
}