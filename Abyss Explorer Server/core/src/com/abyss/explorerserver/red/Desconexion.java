package com.abyss.explorerserver.red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Desconexion extends Thread {
    private static final int HEARTBEAT_INTERVAL = 5000; // Intervalo de latido en milisegundos
    private static final int TIMEOUT = 10000; // Tiempo de espera para considerar que un cliente está desconectado

    private long[] lastHeartbeat = new long[2]; // Almacena el último latido de cada cliente
    private DireccionRed[] clientes; // Arreglo de clientes
    private DatagramSocket conexion; // Socket para recibir mensajes
    private boolean fin = false; // Control de finalización del hilo

    public Desconexion(DireccionRed[] clientes, DatagramSocket conexion) {
        this.clientes = clientes;
        this.conexion = conexion;
    }

    @Override
    public void run() {
        do {
            byte[] data = new byte[1024];
            DatagramPacket dp = new DatagramPacket(data, data.length);
            try {
                conexion.receive(dp);
                procesarMsj(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Enviar latidos a los clientes
            enviarHeartbeat();

            // Verificar si hay clientes desconectados
            verificarDesconexiones();

            try {
                Thread.sleep(HEARTBEAT_INTERVAL); // Espera antes de enviar el siguiente latido
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!fin);
    }

    private void enviarHeartbeat() {
        for (int i = 0; i < clientes.length; i++) {
            if (clientes[i] != null) {
                enviarMsj("HEARTBEAT", clientes[i].getIp(), clientes[i].getPuerto());
                lastHeartbeat[i] = System.currentTimeMillis(); // Actualiza el tiempo del último latido
            }
        }
    }

    private void verificarDesconexiones() {
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < clientes.length; i++) {
            if (clientes[i] != null && (currentTime - lastHeartbeat[i] > TIMEOUT)) {
                System.out.println("Cliente " + i + " desconectado.");
                removerCliente(i);
            }
        }
    }
    
    public void enviarMsj(String msj, InetAddress ip, int puerto) {
        byte[] data = msj.getBytes();
        DatagramPacket dp = new DatagramPacket(data, data.length, ip, puerto);
        try {
            conexion.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void procesarMsj(DatagramPacket dp) {
        String msj = new String(dp.getData()).trim();
        int nroCliente = obtenerNroCliente(dp);

        if (msj.equals("HEARTBEAT_ACK") && nroCliente != -1) {
            // Actualiza el tiempo del último latido para este cliente
            lastHeartbeat[nroCliente] = System.currentTimeMillis();
        }
    }

    private int obtenerNroCliente(DatagramPacket dp) {
        for (int i = 0; i < clientes.length; i++) {
            if (clientes[i] != null && dp.getPort() == clientes[i].getPuerto() && 
                dp.getAddress().equals(clientes[i].getIp())) {
                return i;
            }
        }
        return -1;
    }

    private void removerCliente(int clienteId) {
        if (clienteId >= 0 && clienteId < clientes.length) {
            System.out.println("Removiendo cliente " + clienteId);
            clientes[clienteId] = null; // Elimina el cliente de la lista
            // Aquí puedes agregar lógica adicional para notificar a otros clientes o limpiar recursos
        }
    }

    public void detener() {
        fin = true;
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
    }
}