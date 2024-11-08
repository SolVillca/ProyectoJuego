package com.abyss.explorer.pantallas;

import java.util.ArrayList;
import java.util.List;

import com.abyss.explorer.AbyssExplorer;
import com.abyss.explorer.elementos.Menu;
import com.abyss.explorer.elementos.Texto;
import com.abyss.explorer.io.KeyListener;
import com.abyss.explorer.utiles.Config;
import com.abyss.explorer.utiles.Global;
import com.abyss.explorer.utiles.Recursos;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaJuegoTerminado implements Screen {
	AbyssExplorer game;
	
	SpriteBatch b;
    
    Texto titulo;
    
    Texto opciones[] ;
    String textos [] = {"Volver al menu", "Salir"};
    
    Menu menu;
    private Viewport ventanaJuego;
    private OrthographicCamera camara;
    
    private KeyListener entradas;
    
    private String marcianoRegion;
    
    public PantallaJuegoTerminado(String marciano) {
		// TODO Auto-generated constructor stub
    	this.marcianoRegion = marciano;
	}

	@Override
    public void show() {
    	camara = new OrthographicCamera();
        ventanaJuego = new FitViewport(Config.ANCHO / Config.PPM, Config.ALTO / Config.PPM, camara);
        
        // Ajustar la cámara
        camara.setToOrtho(false, ventanaJuego.getWorldWidth(), ventanaJuego.getWorldHeight());
        camara.update();
        
    	
    	
        titulo = new Texto(Recursos.FUENTEMENU, 40, Color.valueOf("#D2704A"), false);
        if(Global.ganador.equals(marcianoRegion)) {
        	System.out.println(marcianoRegion);
        	titulo.setTexto("Juego Terminado : Ganador ");
        } else {
        	
        	titulo.setTexto("Juego Terminado : Perdedor");
        }
        titulo.setPosition((Config.ANCHO - titulo.getAncho())/2, 600);
        
        opciones = new Texto[textos.length];
        
        List<Runnable> acciones = new ArrayList<>();
        acciones.add(() -> {
            // Acción para elegir cant jugadores
            Render.app.setScreen(new PantallaMenu());
        });
        acciones.add(() -> {
            // Acción para la opción "Salir"
            Gdx.app.exit();});
        
        
        
        entradas = new KeyListener(this);
        Gdx.input.setInputProcessor(entradas);
        
        menu = new Menu(opciones, textos, false, 40, entradas, acciones);
        b = Render.sb;
    }

    @Override
    public void render(float delta) {
        Render.LimpiarPantalla();
        
        camara.update();
        b.setProjectionMatrix(camara.combined);
        
        b.begin();
        	titulo.dibujar();
        	menu.dibujar(delta);
        b.end();
        
        
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        titulo.dispose();
    }
}