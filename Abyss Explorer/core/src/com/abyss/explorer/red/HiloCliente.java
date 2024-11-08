package com.abyss.explorer.red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.abyss.explorer.pantallas.PantallaNivel;
import com.abyss.explorer.utiles.Global;
import com.badlogic.gdx.Gdx;

public class HiloCliente extends Thread{
	
	private DatagramSocket conexion;
	private InetAddress ipServidor;
	private int puerto = 9998;
	private boolean fin = false;
	private PantallaNivel app;
	private ConcurrentLinkedQueue<String> colaActualizaciones = new ConcurrentLinkedQueue<>();

	
	public HiloCliente(PantallaNivel pantallaNivel) {
		 this.app = pantallaNivel;
		
		try {
			ipServidor = InetAddress.getByName("255.255.255.255");
			conexion = new DatagramSocket();	//agregar un puerto al datagram es solo valido para el server, el SO asigna un puerto automaticamente. El servidor es el unico q importa asignarle un puerto para q sea el mismo y conocido
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		enviarMsj("Conexion"); //manda msj a toda la red para ver si hay un servidor 
	}
	
	public void enviarMsj(String msj) {
		// TODO Auto-generated method stub
		byte[] data = msj.getBytes();
		DatagramPacket dp = new DatagramPacket(data, data.length, ipServidor, puerto ); //1era vz manda msj a todas las maquinas y va a cambiar si recibe respuesta
		try {
			conexion.send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//Ejecuta en paralelo el hilo
	@Override
	public void run() {
		do{ // el ciclo debe finalizar cuando el juego termine
            byte[] data = new byte[1024]; //se desconoce el tamaño real del datagrama, por eso se pone el mayor nro q soporta
            DatagramPacket dp = new DatagramPacket(data, data.length);
            try {
                conexion.receive(dp);
                //System.out.println(dp);
                if(dp == null) {
                	System.out.println(dp);
                }
                procesarMsj(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!fin) ;
	}
	
	// CONVIERTE EL DATAGRAM PACKET EN STRING	
	private void procesarMsj(DatagramPacket dp) {
	    String msj = new String(dp.getData()).trim();
	    //System.out.println("Mensaje " + msj);
	    if (msj.equals("HEARTBEAT")) {
	        // Responder al latido
	        enviarMsj("HEARTBEAT_ACK");
	    } else if (msj.startsWith("Estado:")) {
	        String estadoJuego = msj.substring(7);
	        //System.out.println("hc  " + estadoJuego);
	        //System.out.println("hc  " + msj);
	       
            app.actualizarEstadoJuego(estadoJuego); // Actualiza el jugador en el hilo principal
	        colaActualizaciones.offer(estadoJuego);
	        
	        //System.out.println("hc  " + colaActualizaciones.poll());
	        //System.out.println("hc  " + colaActualizaciones);
	        
	    } else if (msj.startsWith("OK")) {
	    	String[] partes = msj.split(":");
	        if (partes.length > 1) {
	            int clienteId = Integer.parseInt(partes[1]);
	            app.setClienteId(clienteId); // Método para establecer el clienteId en PantallaNivel
	        }
	        System.out.println("Conectado al servidor");
	        
	    } else if (msj.equals("Empieza")) {
	        Global.inicioJuego = true;
	    } else if(msj.equals("FinJuego")) {
	    	Global.finJuego = true;
	    	detener();
	    	System.out.println("Juego Terminado");
	    } else if(msj.startsWith("LlaveRecogida:")) {
	    	String[] partes = msj.split(":");
	    	System.out.println("Recogio llave: " + msj);
	        if (partes.length > 1) {
	            String marcianoGanador = partes[1];
	            Global.ganador = marcianoGanador; // Método para establecer el clienteId en PantallaNivel
	        }
	    	System.out.println("Juego Terminado");
	    }
	}
	
	public void actualizarEstadoJuego() {
	    String actualizacion;
	    //System.out.println("actualizarEstadoJuego  " + actualizacion );
	    //System.out.println("Entro actualizarEstadoJuego hc");
	    while ((actualizacion = colaActualizaciones.poll()) != null) {
	    	//0:martian_pink:40.0,200.0:QUIETO;
	    	//System.out.println("Entro while actualizarEstadoJuego hc");
	        String[] jugadores = actualizacion.split(";"); // Divide el mensaje en partes por cada jugador
	        for (String jugadorInfo : jugadores) {
	            String[] datos = jugadorInfo.split(":"); // Divide la información del jugador en partes
	            if (datos.length == 4) {
	                int id = Integer.parseInt(datos[0]); // ID del jugador
	                String tipo = datos[1]; // Tipo de jugador
	                String[] posicion = datos[2].split(","); // Posición del jugador
	                float x = Float.parseFloat(posicion[0]); // Coordenada X
	                float y = Float.parseFloat(posicion[1]); // Coordenada Y
	                String estado = datos[3]; // Estado del jugador

	                Gdx.app.postRunnable(() -> {
	                    app.actualizarJugador(id, tipo, x, y, estado); // Actualiza el jugador en el hilo principal
	                });
	            }
	        }
	    }
	}

	public void enviarComando(String comando) {
	    enviarMsj(comando);
	}

	public void detener() {
	    fin = true;
	    if (conexion != null && !conexion.isClosed()) {
	        conexion.close();
	    }
	}

}
