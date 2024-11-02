package com.abyss.explorer.pantallas;

import com.abyss.explorer.elementos.Imagen;
import com.abyss.explorer.elementos.Texto;
import com.abyss.explorer.utiles.Config;
import com.abyss.explorer.utiles.Recursos;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaJuegoTerminado implements Screen {
    private SpriteBatch b;
    
    private Texto estado;
    
    private Viewport ventanaJuego;
    private OrthographicCamera camara;
    
    @Override
    public void show() {
    	camara = new OrthographicCamera();
        ventanaJuego = new FitViewport(Config.ANCHO / Config.PPM, Config.ALTO / Config.PPM, camara);
        
        // Ajustar la cámara
        camara.setToOrtho(false, ventanaJuego.getWorldWidth(), ventanaJuego.getWorldHeight());
        camara.update();

    	
    	
        estado = new Texto(Recursos.FUENTEMENU, 40, Color.valueOf("#D2704A"), false);
        estado.setTexto("Juego Terminado");
        estado.setPosition((Config.ANCHO - estado.getAncho())/2, (Config.ALTO - estado.getAlto())/2);
        
        b = Render.sb;
    }

    @Override
    public void render(float delta) {
        Render.LimpiarPantalla();
        
        camara.update();
        b.setProjectionMatrix(camara.combined);
        
        b.begin();
        	estado.dibujar();
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
        estado.dispose();
    }
}