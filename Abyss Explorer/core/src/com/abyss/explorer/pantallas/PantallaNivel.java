package com.abyss.explorer.pantallas;

import com.abyss.explorer.io.KeyListener;
import com.abyss.explorer.utiles.Config;
import com.abyss.explorer.utiles.Recursos;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaNivel implements Screen{
	
	SpriteBatch b;
	KeyListener teclas;
	
	
	//Pantalla
	private OrthographicCamera camara;
	private Viewport ventanaJuego;
	
	//Mapa
	private TmxMapLoader cargarMapa; //carga el mapa
	private TiledMap mapa;	//mapa en si
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		//Crecion de cámara (ver configuracion)
		camara = new OrthographicCamera();
		ventanaJuego = new FitViewport(Config.ANCHO, Config.ALTO, camara); //completar los argumentos y averiguar bien
		
		cargarMapa = new TmxMapLoader();
		mapa = cargarMapa.load(Recursos.RUTA_MAPA);
		
		//Recibe las entradas del teclado
		teclas = new KeyListener(this);
		Gdx.input.setInputProcessor(teclas);
		
		//Inicializacion de las herramientas
		b = Render.sb;
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		b.begin();
			
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
