package com.abyss.explorerserver.red;

import java.net.InetAddress;

public class DireccionRed {
	
	private InetAddress ip;
	private int puerto;
	
	// Resulta mas facil pasar el string y realizar aca la conversion
	public DireccionRed(InetAddress ip, int puerto) {
		

		this.ip = ip;
		this.puerto = puerto;
	}

	public InetAddress getIp() {
		return ip;
	}

	public int getPuerto() {
		return puerto;
	}
	
	
}
