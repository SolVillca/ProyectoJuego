package com.abyss.explorer.pantallas;

import java.util.ArrayList;
import java.util.List;

import com.abyss.explorer.elementos.Imagen;
import com.abyss.explorer.elementos.Menu;
import com.abyss.explorer.elementos.Texto;
import com.abyss.explorer.io.KeyListener;
import com.abyss.explorer.utiles.Global;
import com.abyss.explorer.utiles.Recursos;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaJugador implements Screen{
	
	SpriteBatch b; 
	Imagen fondo;
	
	Menu menu;
	Texto opciones[] ;
	String[] textos = {"1 Jugador", "2 Jugadores"};
	KeyListener entradas;
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		if(Global.finJuego==true) Global.finJuego = false;
		
		fondo = new Imagen(Recursos.FONDOMENU);
		opciones = new Texto[textos.length];
		
		
		entradas = new KeyListener(this);
		Gdx.input.setInputProcessor(entradas);
		
		List<Runnable> acciones = new ArrayList<>();
        acciones.add(() -> {
            Render.app.setScreen(new PantallaNivel(1)); // Cambia a la pantalla de nivel
            
        });
        acciones.add(() -> {
            Render.app.setScreen(new PantallaNivel(2)); // Cambia a la pantalla de nivel
        });
        menu = new Menu(opciones, textos, 40, entradas, acciones);

        b = Render.sb;
		
	}

	@Override
	public void render(float delta) {
		Render.LimpiarPantalla();
		b.begin();
			fondo.dibujar();
			menu.dibujar(delta);
		b.end();
	
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
