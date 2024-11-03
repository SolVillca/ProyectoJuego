package com.abyss.explorerserver.utiles;

import com.abyss.explorerserver.AbyssExplorerServer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Render {

	public static SpriteBatch sb = new SpriteBatch();
	public static AbyssExplorerServer app;
	
	public static void LimpiarPantalla () {
		ScreenUtils.clear(1, 1, 1, 1);
	}
	
	public static void begin () {
		sb.begin();
	}
	
	public static void end () {
		sb.end();
	}
}
