# Proyecto Socket UDP (Java + Maven)

Este proyecto implementa una comunicación **Cliente–Servidor mediante sockets UDP en Java**, utilizando **Maven** y un **script Bash** para automatizar la compilación y ejecución.

---

## 📁 Estructura del proyecto

```
UDP/
├── ClienteSocketUDP/
│   ├── pom.xml
│   └── src/main/java/udp/socket/
│       └── UDP_EchoClient.java
│
├── ServerSocketUDP/
│   ├── pom.xml
│   └── src/main/java/udp/socket/
│       └── UDP_EchoServer.java
│
└── EjecutaScript.sh
```

---

## ⚙️ Requisitos

- Java **11 o superior**
- Maven **3.8 o superior**
- Sistema Linux / macOS (bash)

Comprobación rápida:
```bash
java -version
mvn -version
```

---

## ▶️ Ejecución del script

Desde la carpeta raíz del proyecto (`UDP/`):

```bash
chmod +x EjecutaScript.sh
./EjecutaScript.sh
```

### ¿Qué hace el script?
1. Compila el **Servidor UDP** con Maven.
2. Compila el **Cliente UDP** con Maven.
3. Arranca el **servidor UDP en el puerto 5050**.

Salida esperada:
```
Creado socket de datagramas para puerto 5050.
Esperando datagramas.
```

El servidor queda **escuchando** hasta que se interrumpe con `Ctrl + C`.

---

## 🧪 Uso del cliente (otra terminal)

Abrir **otra terminal** y ejecutar:

```bash
cd ClienteSocketUDP
java -cp target/classes udp.socket.UDP_EchoClient localhost 5050
```

Ejemplo:
```
Línea> hola
Datagrama recibido de 127.0.0.1:5050: #hola#
```

Para terminar el cliente, introduce una línea vacía.

---

## 🧪 Prueba rápida sin cliente Java (opcional)

```bash
echo -n "hola" | nc -u localhost 5050
```

Respuesta:
```
#hola#
```

---

## 🧠 Conceptos trabajados

- Sockets UDP en Java
- Cliente / Servidor
- Comunicación sin conexión
- Maven (compilación y dependencias)
- JUnit 5
- Automatización con Bash

---

## 📌 Notas importantes

- UDP no garantiza entrega ni orden de los mensajes.
- El nombre del paquete (`udp.socket`) debe coincidir con la estructura de carpetas.
- En Maven, las clases compiladas se encuentran en `target/classes`.

---

Proyecto orientado a **uso educativo** (FP / Bachillerato / DAW).
