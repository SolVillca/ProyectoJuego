package com.abyss.explorerserver.pantallas;

import java.util.ArrayList;
import java.util.List;

import com.abyss.explorerserver.elementos.Imagen;
import com.abyss.explorerserver.elementos.Menu;
import com.abyss.explorerserver.elementos.Texto;
import com.abyss.explorerserver.io.KeyListener;
import com.abyss.explorerserver.utiles.Config;
import com.abyss.explorerserver.utiles.Global;
import com.abyss.explorerserver.utiles.Recursos;
import com.abyss.explorerserver.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaJugador implements Screen{

	SpriteBatch b; 
	Imagen fondo;
	Imagen jugador, jugador2;
	
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
           // Render.app.setScreen(new PantallaNivel()); // Cambia a la pantalla de nivel
            
        });
        acciones.add(() -> {
            Render.app.setScreen(new PantallaNivel()); // Cambia a la pantalla de nivel
        });
        menu = new Menu(opciones, textos, true, 200, entradas, acciones);
        
        
        
        jugador = new Imagen("Mapa/Characters/tile_0000.png");
        jugador.setSize(48, 48);
        jugador.setPosition((Config.ANCHO - jugador.getWidth())/3f, (Config.ALTO - jugador.getHeight())/3.2f);
        jugador2 = new Imagen("Mapa/Characters/tile_0002.png");
        jugador2.setSize(48, 48);
        jugador2.setPosition((Config.ANCHO - jugador.getWidth())/1f, (Config.ALTO - jugador.getHeight())/3.2f);
        b = Render.sb;
		
	}

	@Override
	public void render(float delta) {
		Render.LimpiarPantalla();
		b.begin();
			fondo.dibujar();
			menu.dibujar(delta);
			jugador.dibujar();
			jugador2.dibujar();
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
