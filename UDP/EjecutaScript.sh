#!/bin/bash

# ==============================
# Configuración
# ==============================
PUERTO=5051
HOST=localhost

BASE_DIR="$(pwd)"
SERVER_DIR="$BASE_DIR/ServerSocketUDP"
CLIENT_DIR="$BASE_DIR/ClienteSocketUDP"

# ==============================
# Compilar servidor
# ==============================
echo "========================================"
echo " Compilando ServerSocketUDP"
echo "========================================"

cd "$SERVER_DIR" || exit 1
mvn -q clean package || {
  echo "❌ Error compilando el servidor"
  exit 1
}

# ==============================
# Compilar cliente
# ==============================
echo
echo "========================================"
echo " Compilando ClienteSocketUDP"
echo "========================================"

cd "$CLIENT_DIR" || exit 1
mvn -q clean package || {
  echo "❌ Error compilando el cliente"
  exit 1
}

# ==============================
# Lanzar servidor
# ==============================
echo
echo "========================================"
echo " Lanzando servidor UDP en puerto $PUERTO"
echo "========================================"
echo " (Ctrl+C para detener)"
echo " Para lanzar el cliente en otra terminal:"
echo "   cd \"$CLIENT_DIR\" && java -cp target/classes udp.socket.UDP_EchoClient $HOST $PUERTO"
echo

cd "$SERVER_DIR" || exit 1
java -cp target/classes udp.socket.UDP_EchoServer $PUERTO

