package com.abyss.explorerserver.red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

import com.abyss.explorerserver.pantallas.PantallaNivel;
import com.abyss.explorerserver.utiles.Global;

public class HiloServidor extends Thread {
	private DatagramSocket conexion;
	private boolean fin = false;
	
	private int limiteClientes = 2;
	
	private DireccionRed[] clientes = new DireccionRed[limiteClientes];
	private int cantClientes = 0;
	private PantallaNivel app;
	private ConcurrentHashMap<Integer, String> comandosClientes = new ConcurrentHashMap<>();

	public HiloServidor(PantallaNivel pantallaNivel) {
		this.app = pantallaNivel;
		try {
			conexion = new DatagramSocket(9998);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void enviarMsj(String msj, InetAddress ip, int puerto) {
		byte[] data = msj.getBytes();
		// System.out.println("enviar msj server: " + msj);
		DatagramPacket dp = new DatagramPacket(data, data.length, ip, puerto);
		try {
			conexion.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		} while (!fin);
	}

	/*
	 * private void procesarMsj(DatagramPacket dp) { String msj = new
	 * String(dp.getData()).trim(); int nroCliente = obtenerNroCliente(dp);
	 * 
	 * if (cantClientes < 2) { if (msj.equals("Conexion")) {
	 * System.out.println("Llega msj Conexion cliente " + cantClientes );
	 * manejarConexionCliente(dp); } } else { if (nroCliente != -1) {
	 * comandosClientes.put(nroCliente, msj); } } }
	 */

	private void procesarMsj(DatagramPacket dp) {
		String msj = new String(dp.getData()).trim();
		int nroCliente = obtenerNroCliente(dp);
		//System.out.println("procesarMsj hs " + msj);
		//System.out.println(nroCliente); // devuelve -1 si clientes[] es nulo o si el puerto del cliente no coincide con
										// el datagrampacket
		if (cantClientes < limiteClientes) {
			if (msj.equals("Conexion")) {
				System.out.println("Llega msj Conexion cliente " + cantClientes);
				manejarConexionCliente(dp);
			}
		} else {
			if (nroCliente != -1) {
				comandosClientes.put(nroCliente, msj);
				if (msj.startsWith("INPUT:")) {
					// comandosClientes.put(nroCliente, msj);
					// System.out.println("Manejo input");
					// separar msj primero por ; cada uno es un jugador
					// Extraer clienteId y movimiento
				//	String[] partes = msj.split(":");
				//	if (partes.length == 3) {
					//	int clienteId = Integer.parseInt(partes[1]);
					//	String movimiento = partes[2];
						// Almacenar el movimiento en el mapa de comandos
					//	comandosClientes.put(clienteId, movimiento);
						// System.out.println("Movimiento recibido del cliente " + clienteId + ": " +
						// movimiento);
					//}
					String[] jugadoresMovimiento = msj.split(";");
			        for (String jugadorInfo : jugadoresMovimiento) {
			        	String[] partes = msj.split(":");
						if (partes.length == 3) {
							int clienteId = Integer.parseInt(partes[1]);
							String movimiento = partes[2];
							// Almacenar el movimiento en el mapa de comandos
							comandosClientes.put(clienteId, movimiento);
							// System.out.println("Movimiento recibido del cliente " + clienteId + ": " +
							// movimiento);
						}
			        }

				}
			}
		}
	}

	private int obtenerNroCliente(DatagramPacket dp) {
		for (int i = 0; i < clientes.length; i++) {
			// System.out.println("obtenerNroCLiente hs: " + clientes[i]+ " " +
			// dp.getPort());
			if (clientes[i] != null && dp.getPort() == clientes[i].getPuerto()
					&& dp.getAddress().equals(clientes[i].getIp())) {
				return i;
			}
		}
		return -1;
	}

	private void manejarConexionCliente(DatagramPacket dp) {
		System.out.println("manejarConexionClientes " + cantClientes);
		if (cantClientes < limiteClientes) {
			System.out.println(" cant " + cantClientes );
			clientes[cantClientes] = new DireccionRed(dp.getAddress(), dp.getPort());
			System.out.println("OK:" + cantClientes + " " + clientes[cantClientes].getIp() +" " + clientes[cantClientes].getPuerto());
			enviarMsj("OK:" + cantClientes, clientes[cantClientes].getIp(), clientes[cantClientes].getPuerto());
			app.crearJugador(cantClientes);
			cantClientes++;

			if (cantClientes == limiteClientes) {
				//Global.inicioJuego = true;
				//System.out.println(Global.inicioJuego + "  entro");
				for (DireccionRed cliente : clientes) {
					if (cliente != null) {
						System.out.println("Empieza " + cliente.getIp() + " "  + cliente.getPuerto());
						enviarMsj("Empieza", cliente.getIp(), cliente.getPuerto());
					}
				}
			}
		} else if (cantClientes == 0) {
			System.out.println("Esperando clientes");
		} else if (cantClientes > limiteClientes) {
			System.out.println("Esperando servidor, clientes");
		}
	}

	public String obtenerComandoCliente(int clienteId) {
		// System.out.println("comando " + comandosClientes);
		return comandosClientes.remove(clienteId);
	}

	public void enviarEstadoAClientes(String estadoJuego) {
		for (DireccionRed cliente : clientes) {
			// System.out.println("enviarEstadoAClientes hs :" + cliente + " " + clientes );
			if (cliente != null) {
				// System.out.println("hs " + estadoJuego);
				enviarMsj("Estado:" + estadoJuego, cliente.getIp(), cliente.getPuerto());
			}
		}
	} 

	public void notificarFinJuego() {
		for (DireccionRed cliente : clientes) {
			if (cliente != null) {
				enviarMsj("FinJuego", cliente.getIp(), cliente.getPuerto());
				//System.out.println("envio msj finJuego");
				
			}
		}
	}
	
	public void notificarGanador() {
		for (DireccionRed cliente : clientes) {
			if (cliente != null) {
				enviarMsj("LlaveRecogida:" + Global.ganador, cliente.getIp(), cliente.getPuerto());
				//System.out.println("envio msj finJuego");
			}
		}
	}
	

	public void removerCliente(int clienteId) {
		if (clienteId >= 0 && clienteId < clientes.length) {
			System.out.println("Removio cliente");
			clientes[clienteId] = null;
			cantClientes--;
			app.removerJugador(clienteId);
			comandosClientes.remove(clienteId);
			System.out.println(clienteId + " " + clientes[clienteId] + " " + cantClientes + " " + clientes);
		}
	}

	public void detener() {
		fin = true;
		if (conexion != null && !conexion.isClosed()) {
			conexion.close();
		}
	}
}