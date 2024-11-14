package com.abyss.explorerserver.red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

import com.abyss.explorerserver.pantallas.PantallaNivel;
import com.abyss.explorerserver.utiles.Global;

public class HiloServidor extends Thread { // Clase que extiende Thread para crear un hilo de servidor
	private DatagramSocket conexion; // Socket para la conexión UDP
	private boolean fin = false; // Bandera para indicar si el hilo debe finalizar
	
	private int limiteClientes = 2; // Límite máximo de clientes permitidos
	
	private DireccionRed[] clientes = new DireccionRed[limiteClientes]; // Arreglo para almacenar las direcciones de los clientes
	private int cantClientes = 0; // Contador de clientes conectados
	private PantallaNivel app; // Referencia a la interfaz de usuario
	
	
	//Mapa Concurrente (Def) : Colección que asocia claves con valores y permite que múltiples hilos lean y escriban en el mapa simultáneamente de forma segura.
	private ConcurrentHashMap<Integer, String> comandosClientes = new ConcurrentHashMap<>(); // Mapa concurrente para almacenar los comandos de los clientes
	
	// Constructor que recibe la interfaz de usuario
	public HiloServidor(PantallaNivel pantallaNivel) { 
		this.app = pantallaNivel; // Inicializa la referencia a la interfaz de usuario
		try {
			conexion = new DatagramSocket(9998); // Crea un socket UDP en el puerto 9998
		} catch (SocketException e) {
			e.printStackTrace(); // Manejo de excepciones si falla la creación del socket
		}
	}
	
	// Método para enviar un mensaje a un cliente
	public void enviarMsj(String msj, InetAddress ip, int puerto) { 
		byte[] data = msj.getBytes(); // Convierte el mensaje a un arreglo de bytes
		// System.out.println("enviar msj server: " + msj); // (Comentario) Imprime el mensaje que se va a enviar
		DatagramPacket dp = new DatagramPacket(data, data.length, ip, puerto); // Crea un paquete UDP con el mensaje, dirección IP y puerto
		try {
			conexion.send(dp); // Envía el paquete a través del socket
		} catch (IOException e) {
			e.printStackTrace(); // Manejo de excepciones al enviar datos
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
                    break; // Sale si el cierre fue intencional
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


	private void procesarMsj(DatagramPacket dp) { // Método para procesar el mensaje recibido
		String msj = new String(dp.getData()).trim(); // Convierte los datos del paquete a una cadena y elimina espacios en blanco
		int nroCliente = obtenerNroCliente(dp); // Obtiene el número del cliente basado en la dirección y puerto
		if (cantClientes < limiteClientes) { // Verifica si se puede aceptar más clientes
			if (msj.equals("Conexion")) { // Si el mensaje es "Conexion"
				System.out.println("Llega msj Conexion cliente " + cantClientes); // Imprime que llegó un mensaje de conexión de un cliente
				manejarConexionCliente(dp); // Llama al método para manejar la conexión del cliente
			}
		} else { // Si ya se alcanzó el límite de clientes
			if (nroCliente != -1) { // Verifica si el cliente es válido
				// comandosClientes.put(nroCliente, msj); // (Comentario) Almacena el mensaje en el mapa de comandos
				if (msj.startsWith("INPUT:")) { // Si el mensaje comienza con "INPUT:"
					// comandosClientes.put(nroCliente, msj); // (Comentario) Almacena el mensaje en el mapa de comandos
					String[] jugadoresMovimiento = msj.split(";"); // Divide el mensaje en partes por el delimitador ";"
					// System.out.println(jugadoresMovimiento); // (Comentario) Imprime los movimientos de los jugadores
			        for (String jugadorInfo : jugadoresMovimiento) { // Itera sobre cada información de jugador
			        	String[] partes = msj.split(":"); // Divide el mensaje por el delimitador ":"
						if (partes.length == 3) { // Verifica que haya tres partes
							int clienteId = Integer.parseInt(partes[1]); // Obtiene el ID del cliente
							String movimiento = partes[2]; // Obtiene el movimiento del cliente
							// Almacenar el movimiento en el mapa de comandos
							comandosClientes.put(nroCliente, movimiento); // Almacena el movimiento en el mapa de comandos
						}
			        }
				}
			}
		}
	}


	private int obtenerNroCliente(DatagramPacket dp) { // Método para obtener el número del cliente basado en el paquete recibido
		for (int i = 0; i < clientes.length; i++) { // Itera sobre el arreglo de clientes
			if (clientes[i] != null && dp.getPort() == clientes[i].getPuerto() && dp.getAddress().equals(clientes[i].getIp())) { // Verifica si el cliente coincide
				return i; // Retorna el índice del cliente
			}
		}
		return -1; // Retorna -1 si no se encuentra el cliente
	}

	private void manejarConexionCliente(DatagramPacket dp) { // Método para manejar la conexión de un nuevo cliente
		System.out.println("manejarConexionClientes " + cantClientes); // Mensaje de depuracion
		
		if (cantClientes < limiteClientes) { // Verifica si se puede aceptar más clientes
			clientes[cantClientes] = new DireccionRed(dp.getAddress(), dp.getPort()); // Almacena la dirección del nuevo cliente
			System.out.println("OK:" + cantClientes + " " + clientes[cantClientes].getIp() + " " + clientes[cantClientes].getPuerto()); // Mensaje de depuracion
			enviarMsj("OK:" + cantClientes, clientes[cantClientes].getIp(), clientes[cantClientes].getPuerto()); // Envía un mensaje de confirmación al cliente
			app.crearJugador(cantClientes); // Llama al método para crear un jugador en la interfaz
			cantClientes++; // Incrementa el contador de clientes

			if (cantClientes == limiteClientes) { // Si se alcanzó el límite de clientes
				
				for (DireccionRed cliente : clientes) { // Itera sobre el arreglo de clientes
					if (cliente != null) { // Verifica que el cliente no sea nulo
						System.out.println("Empieza " + cliente.getIp() + " " + cliente.getPuerto()); // Mensaje de depuracion
						
						enviarMsj("Empieza", cliente.getIp(), cliente.getPuerto()); // Envía un mensaje a los clientes para iniciar el juego
					}
				}
			}
		}
	}

	public String obtenerComandoCliente(int clienteId) { // Método para obtener el comando de un cliente específico
		 System.out.println("comando " + comandosClientes); // Imprime los comandos actuales de los clientes
		return comandosClientes.remove(clienteId); // Elimina y retorna el comando del cliente
	}

	public void enviarEstadoAClientes(String estadoJuego) { // Método para enviar el estado del juego a todos los clientes
		for (DireccionRed cliente : clientes) { // Itera sobre el arreglo de clientes
			if (cliente != null) { // Verifica que el cliente no sea nulo
				enviarMsj("Estado:" + estadoJuego, cliente.getIp(), cliente.getPuerto()); // Envía el estado del juego al cliente
			}
		}
	} 

	// Método para notificar a todos los clientes que el juego ha terminado
	public void notificarFinJuego() { 
		for (DireccionRed cliente : clientes) { // Itera sobre el arreglo de clientes
			if (cliente != null) { // Verifica que el cliente no sea nulo
				enviarMsj("FinJuego", cliente.getIp(), cliente.getPuerto()); // Envía un mensaje de fin de juego al cliente
			}
		}
	}
	
	// Método para notificar a todos los clientes quién es el ganador
	public void notificarGanador() { 
		for (DireccionRed cliente : clientes) { // Itera sobre el arreglo de clientes
			if (cliente != null) { // Verifica que el cliente no sea nulo
				enviarMsj("LlaveRecogida:" + Global.ganador, cliente.getIp(), cliente.getPuerto()); // Envía un mensaje con el ganador al cliente
			}
		}
	}

	public void detener() { // Método para detener el hilo
		fin = true; // Establece la bandera de fin a verdadero
	}
}