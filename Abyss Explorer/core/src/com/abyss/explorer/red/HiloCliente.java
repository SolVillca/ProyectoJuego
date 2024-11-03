package com.abyss.explorer.red;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class HiloCliente extends Thread{
	
	private DatagramSocket conexion;
	private InetAddress ipServer;
	
	public HiloCliente() {
		
		
		try {
			ipServer = InetAddress.getByName("255.255.255.255");
			conexion = new DatagramSocket(9999);
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		enviarMsj("Conexion");
	}
	
	private void enviarMsj(String msj) {
		// TODO Auto-generated method stub
		byte[] data = msj.getBytes();
		DatagramPacket dp = new DatagramPacket(data, data.length );
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}

}
