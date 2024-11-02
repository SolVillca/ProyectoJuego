package com.abyss.explorer;


import com.abyss.explorer.pantallas.PantallaCarga;
import com.abyss.explorer.pantallas.PantallaMenu;
import com.abyss.explorer.pantallas.PantallaNivel;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AbyssExplorer extends Game {
	
	
	//CATEGORIZACION DE OBJETOS PARA MASCARA DE COLISIONES DE BOX 2D
	
	public static final short MARCIANO_PIES_BIT = 1;		//PARA COLISIONES CON PINCHES, TRAMPOLIN Y AGUA, CONTEMPLAR PARA ELEMINAR ENEMIGOS
	public static final short MARCIANO_BIT = 2;
	public static final short AGUA_BIT = 4;
	public static final short TRAMPOLIN_BIT = 8;
	public static final short CHECKPOINT_BIT = 16;
	public static final short CHECKPOINT_ACTIVADO_BIT = 32;
	public static final short PINCHES_BIT = 64;
	public static final short ENEMIGO_BIT = 128;
	public static final short DEFAULT_BIT = 256;
	public static final short FIN_BIT = 512;
	
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		Render.app = this;
		Render.sb = new SpriteBatch();
		//this.setScreen(new PantallaCarga());
		this.setScreen(new PantallaMenu());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		Render.sb.dispose();
		this.getScreen();
	}
}
