package com.abyss.explorer.red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

// Importaciones necesarias para la clase HiloCliente
import com.abyss.explorer.pantallas.PantallaNivel;
import com.abyss.explorer.utiles.Global;
import com.badlogic.gdx.Gdx;

// Clase que extiende Thread para manejar la comunicación del cliente
public class HiloCliente extends Thread {
    // Socket para la conexión UDP
    private DatagramSocket conexion;
    // Dirección IP del servidor
    private InetAddress ipServidor;
    // Puerto del servidor
    private int puerto = 9998;
    // Bandera para indicar si el hilo debe finalizar
    private boolean fin = false;
    // Referencia a la pantalla del juego
    private PantallaNivel app;
    
    
    // Cola concurrente para almacenar actualizaciones del estado del juego
    // Def: Estructura de datos que permite la inserción y eliminación de elementos de manera segura entre múltiples hilos. Mantiene el orden de los elementos según el orden en que fueron añadidos
    private ConcurrentLinkedQueue<String> colaActualizaciones = new ConcurrentLinkedQueue<>();

    // Constructor que inicializa el socket y la dirección del servidor
    public HiloCliente(PantallaNivel pantallaNivel) {
        this.app = pantallaNivel; // Asigna la pantalla del juego
        
        try {
            // Obtiene la dirección IP del servidor (broadcast)
            ipServidor = InetAddress.getByName("255.255.255.255");
            // Inicializa el socket UDP
            conexion = new DatagramSocket(); // El sistema asigna un puerto automáticamente
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace(); // Manejo de excepciones al crear el socket
        }
        
        // Envía un mensaje de conexión al servidor
        enviarMsj("Conexion");
    }

    // Método para enviar un mensaje al servidor
    public void enviarMsj(String msj) {
        byte[] data = msj.getBytes(); // Convierte el mensaje a bytes
        // Crea un paquete de datagramas con el mensaje, la dirección IP y el puerto
        DatagramPacket dp = new DatagramPacket(data, data.length, ipServidor, puerto);
        try {
            // Envía el paquete
            conexion.send(dp);
        } catch (IOException e) {
            e.printStackTrace(); // Manejo de excepciones al enviar el paquete
        }
    }

    // Método que se ejecuta al iniciar el hilo
    @Override
    public void run() {
        do {
            byte[] data = new byte[1024]; // Buffer para recibir datos
            DatagramPacket dp = new DatagramPacket(data, data.length); // Paquete para recibir datos
            try {
                // Espera a recibir un paquete
                conexion.receive(dp);
                // Procesa el mensaje recibido
                procesarMsj(dp);
            } catch (SocketException e) {
                // El socket se cerró, salimos del bucle
                if (fin) {
                    break; // Salimos si el cierre fue intencional
                }
                e.printStackTrace(); // Manejo de excepciones al recibir datos
            } catch (IOException e) {
                e.printStackTrace(); // Manejo de excepciones al recibir datos
            }
        } while (!fin); // Continúa hasta que fin sea verdadero

        // Cierra el socket al finalizar el hilo
        if (conexion != null && !conexion.isClosed()) {
            conexion.close(); // Cierra el socket si está abierto
        }
    }

    // Método para procesar el mensaje recibido de un servidor
    private void procesarMsj(DatagramPacket dp) {
        String msj = new String(dp.getData()).trim(); // Convierte el paquete a string
        // Procesa diferentes tipos de mensajes
        if (msj.startsWith("OK")) {
            String[] partes = msj.split(":"); // Divide el mensaje en partes
            if (partes.length > 1) {
                int clienteId = Integer.parseInt(partes[1]); // Extrae el ID del cliente
                app.setClienteId(clienteId); // Establece el clienteId en PantallaNivel
            }
            
        } else if (msj.equals("Empieza")) {
        	Global.inicioJuego = true; // Cambia el estado del juego a iniciado
        	
        } else if (msj.startsWith("Estado:")) {
            String estadoJuego = msj.substring(7); // Extrae el estado del juego
            //app.actualizarEstadoJuego(estadoJuego); // Actualiza el jugador en el hilo principal
            colaActualizaciones.offer(estadoJuego); // Añade el estado a la cola de actualizaciones
            
        } else if (msj.equals("FinJuego")) {
            Global.finJuego = true; // Cambia el estado del juego a finalizado
            
        } else if (msj.startsWith("LlaveRecogida:")) {
            String[] partes = msj.split(":"); // Divide el mensaje en partes
            if (partes.length > 1) {
                String marcianoGanador = partes[1]; // Extrae el nombre del ganador
                Global.ganador = marcianoGanador; // Establece el ganador en la variable global
            }
        }
    }

    // Método para actualizar el estado del juego en la pantalla
    public void actualizarEstadoJuego() {
        String actualizacion;
        // Procesa las actualizaciones en la cola
        while ((actualizacion = colaActualizaciones.poll()) != null) {
            String[] jugadores = actualizacion.split(";"); // Divide el mensaje en partes por cada jugador
            for (String jugadorInfo : jugadores) {
                String[] datos = jugadorInfo.split(":"); // Divide la información del jugador en partes
                if (datos.length == 5) {
                    int id = Integer.parseInt(datos[0]); // ID del jugador
                    String tipo = datos[1]; // Tipo de jugador
                    String[] posicion = datos[2].split(","); // Posición del jugador
                    float x = Float.parseFloat(posicion[0]); // Coordenada X
                    float y = Float.parseFloat(posicion[1]); // Coordenada Y
                    boolean haciaDer = Boolean.parseBoolean(datos[3]); // Coordenada Y
                    String estado = datos[4]; // Estado del jugador
                    //System.out.println(haciaDer + "  hc"); se mantiene en true
                    // Actualiza el jugador en el hilo principal
                    Gdx.app.postRunnable(() -> {
                        app.actualizarJugador(id, tipo, x, y, haciaDer, estado);
                    });
                }
            }
        }
    }

    // Método para enviar un comando al servidor
    public void enviarComando(String comando) {
        enviarMsj(comando); // Llama al método para enviar el mensaje
    }

    // Método para detener el hilo y cerrar el socket
    public void detener() {
        fin = true; // Cambia la bandera para finalizar el hilo
    }
}