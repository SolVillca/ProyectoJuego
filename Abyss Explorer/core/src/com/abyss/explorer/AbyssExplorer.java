package com.abyss.explorer;


import com.abyss.explorer.pantallas.PantallaMenu;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AbyssExplorer extends Game {
	
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
