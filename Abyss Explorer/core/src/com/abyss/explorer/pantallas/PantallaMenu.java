package com.abyss.explorer.pantallas;

import java.util.ArrayList;
import java.util.List;

import com.abyss.explorer.AbyssExplorer;
import com.abyss.explorer.elementos.Imagen;
import com.abyss.explorer.elementos.Menu;
import com.abyss.explorer.elementos.Texto;
import com.abyss.explorer.io.KeyListener;
import com.abyss.explorer.utiles.Config;
import com.abyss.explorer.utiles.Recursos;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaMenu implements Screen{

	Imagen fondo;
	SpriteBatch b;
	
	KeyListener entrada ;
	
	Texto titulo;
	
	Texto opciones[] ;
	String textos[] = {"Inicio", "Salir"};
	Menu menu;
	

	@Override
	public void show() {
		
		fondo = new Imagen(Recursos.FONDOMENU);
		fondo.setSize(Config.ANCHO, Config.ALTO);
		
		titulo = new Texto(Recursos.FUENTEMENU, 80, Color.valueOf("#D2704A") ,false);
		titulo.setTexto(Config.NOMBRE);
		titulo.setPosition( (Config.ANCHO - titulo.getAncho() ) / 2f , 600);
		
		opciones = new Texto[textos.length];
		
		List<Runnable> acciones = new ArrayList<>();
        acciones.add(() -> {
            // Acción para la opción "Jugar"
            Render.app.setScreen(new PantallaNivel());
            
        });
        acciones.add(() -> {
            // Acción para la opción "Salir"
            Gdx.app.exit();});
        
        
        entrada = new KeyListener(this);
        Gdx.input.setInputProcessor(entrada);
        
        
        menu = new Menu(opciones, textos, false, 40, entrada, acciones);
		// OPCIONES DEL MENU
        b = Render.sb;
		
	}

	@Override
	public void render(float delta) {
		
		b.begin();
			fondo.dibujar();
			titulo.dibujar();
			
			menu.dibujar(delta);
			
		b.end();
		
		
	}

	@Override
	public void resize(int width, int height) {
		
		
	}

	@Override
	public void pause() {
		
		
	}

	@Override
	public void resume() {
		
		
	}

	@Override
	public void hide() {
		
		
	}

	@Override
	public void dispose() {
		
		this.dispose();
		titulo.dispose();
		fondo.dispose();
		
		
	}

}
