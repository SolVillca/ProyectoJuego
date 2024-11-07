package com.abyss.explorerserver;

import com.abyss.explorerserver.utiles.Config;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle(Config.NOMBRE);
		config.setWindowedMode(Config.ANCHO, Config.ALTO);
		config.setResizable(false);
		new Lwjgl3Application(new AbyssExplorerServer(), config);
	}
}
