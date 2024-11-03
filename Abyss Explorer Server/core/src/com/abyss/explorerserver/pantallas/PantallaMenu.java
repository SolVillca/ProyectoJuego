package com.abyss.explorerserver.pantallas;

import java.util.ArrayList;
import java.util.List;

import com.abyss.explorerserver.elementos.Imagen;
import com.abyss.explorerserver.elementos.Menu;
import com.abyss.explorerserver.elementos.Texto;
import com.abyss.explorerserver.io.KeyListener;
import com.abyss.explorerserver.utiles.Config;
import com.abyss.explorerserver.utiles.Recursos;
import com.abyss.explorerserver.utiles.Render;
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
		titulo.setPosition( (Config.ANCHO - titulo.getAncho() ) / 2f , 600); //(Config.ALTO- t.getAlto())/2)
		
		opciones = new Texto[textos.length];
		
		List<Runnable> acciones = new ArrayList<>();
        acciones.add(() -> {
            // Acción para la opción "Jugar"
            Render.app.setScreen(new PantallaJugador());
            
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
		// TODO Auto-generated method stub
		b.begin();
			fondo.dibujar();
			titulo.dibujar();
			
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
		this.dispose();
		titulo.dispose();
		fondo.dispose();
		
		
	}

}
