package com.socket.net.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.socket.net.ServicioSocketTCP;

/**
 * Implementación de ServicioSocketTCP usando java.net.Socket.
 */
public class ServicioTCPSocketImpl implements ServicioSocketTCP {
    
    private Socket socket;
    
    public ServicioTCPSocketImpl() {
        // Constructor vacío - socket se crea en conectar()
    }
    
    /**
     * Constructor interno para wrappear un Socket existente
     * (usado por el servidor al aceptar conexiones).
     */
    ServicioTCPSocketImpl(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void conectar(String host, int puerto) throws IOException {
        if (socket != null && !socket.isClosed()) {
            throw new IllegalStateException("Socket ya está conectado");
        }
        socket = new Socket(host, puerto);
    }
    
    @Override
    public boolean estaConectado() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
    
    @Override
    public InputStream obtenerEntrada() throws IOException {
        validarConexion();
        return socket.getInputStream();
    }
    
    @Override
    public OutputStream obtenerSalida() throws IOException {
        validarConexion();
        return socket.getOutputStream();
    }
    
    @Override
    public boolean getKeepAlive() throws SocketException {
        validarSocket();
        return socket.getKeepAlive();
    }
    
    @Override
    public void setKeepAlive(boolean on) throws SocketException {
        validarSocket();
        socket.setKeepAlive(on);
    }
    
    @Override
    public boolean getTcpNoDelay() throws SocketException {
        validarSocket();
        return socket.getTcpNoDelay();
    }
    
    @Override
    public void setTcpNoDelay(boolean on) throws SocketException {
        validarSocket();
        socket.setTcpNoDelay(on);
    }
    
    @Override
    public int getSoTimeout() throws SocketException {
        validarSocket();
        return socket.getSoTimeout();
    }
    
    @Override
    public void setSoTimeout(int ms) throws SocketException {
        validarSocket();
        socket.setSoTimeout(ms);
    }
    
    @Override
    public boolean getReuseAddress() throws SocketException {
        validarSocket();
        return socket.getReuseAddress();
    }
    
    @Override
    public void setReuseAddress(boolean on) throws SocketException {
        validarSocket();
        socket.setReuseAddress(on);
    }
    
    @Override
    public int getPuertoLocal() {
        return socket != null ? socket.getLocalPort() : -1;
    }
    
    @Override
    public int getPuertoRemoto() {
        return socket != null ? socket.getPort() : -1;
    }
    
    @Override
    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
    
    @Override
    public boolean estaCerrado() {
        return socket == null || socket.isClosed();
    }
    
    // Métodos auxiliares
    
    private void validarSocket() throws SocketException {
        if (socket == null) {
            throw new SocketException("Socket no inicializado");
        }
        if (socket.isClosed()) {
            throw new SocketException("Socket está cerrado");
        }
    }
    
    private void validarConexion() throws IOException {
        validarSocket();
        if (!socket.isConnected()) {
            throw new IOException("Socket no está conectado");
        }
    }
}