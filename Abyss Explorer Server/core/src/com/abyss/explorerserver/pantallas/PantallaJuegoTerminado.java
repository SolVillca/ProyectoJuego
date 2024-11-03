package com.abyss.explorerserver.pantallas;

import java.util.ArrayList;
import java.util.List;

import com.abyss.explorerserver.AbyssExplorerServer;
import com.abyss.explorerserver.elementos.Menu;
import com.abyss.explorerserver.elementos.Texto;
import com.abyss.explorerserver.io.KeyListener;
import com.abyss.explorerserver.utiles.Config;
import com.abyss.explorerserver.utiles.Recursos;
import com.abyss.explorerserver.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaJuegoTerminado implements Screen {
	
	SpriteBatch b;
    
    Texto titulo;
    
    Texto opciones[] ;
    String textos [] = {"Volver al menu", "Salir"};
    
    Menu menu;
    private Viewport ventanaJuego;
    private OrthographicCamera camara;
    
    private KeyListener entradas;
    
    @Override
    public void show() {
    	camara = new OrthographicCamera();
        ventanaJuego = new FitViewport(Config.ANCHO / Config.PPM, Config.ALTO / Config.PPM, camara);
        
        // Ajustar la cámara
        camara.setToOrtho(false, ventanaJuego.getWorldWidth(), ventanaJuego.getWorldHeight());
        camara.update();
        
    	
    	
        titulo = new Texto(Recursos.FUENTEMENU, 40, Color.valueOf("#D2704A"), false);
        titulo.setTexto("Juego Terminado");
        titulo.setPosition((Config.ANCHO - titulo.getAncho())/2, 600);
        
        opciones = new Texto[textos.length];
        
        List<Runnable> acciones = new ArrayList<>();
        acciones.add(() -> {
            // Acción para elegir cant jugadores
            Render.app.setScreen(new PantallaJugador());
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
