package com.abyss.explorer.pantallas;

import com.abyss.explorer.elementos.MundoBox2D;
import com.abyss.explorer.elementos.WorldContactListener;
import com.abyss.explorer.io.KeyListener;
import com.abyss.explorer.sprites.Checkpoint;
import com.abyss.explorer.sprites.Marciano;
import com.abyss.explorer.utiles.Config;
import com.abyss.explorer.utiles.Recursos;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaNivel implements Screen{
	
	SpriteBatch b;
	KeyListener teclas;
	
	//ATLAS 
	private TextureAtlas atlas;
	
	//PANTALLA
	private OrthographicCamera camara;
	private Viewport ventanaJuego;
	
	//MAPA
	private TmxMapLoader cargarMapa; //CARGA EL MAPA DENTRO DEL MUNDO
	private TiledMap mapa;	//ES EL MAPA EN SI
	private OrthogonalTiledMapRenderer renderMapa;

	//MUNDO BOX2D
	private World mundo;
	private Box2DDebugRenderer b2dr; //REPRESENTACION GRAFICA DE LOS CUERPOS DENTRO DEL MUNDO
		
	//CREACION DEL MUNDO A PARTIR DE UNA CLASE
	private MundoBox2D mundoBox2D;
	
	//JUGADOR
	private Marciano jugador;
	
		
	
	

	@Override
	public void show() {
		// TODO Auto-generated method stub
		//REVISAR E INVESTIGAR: ACERCAMIENTO DE LA CAMARA 
		
		atlas = new TextureAtlas(Recursos.SPRITE_MARCIANO);
		
		//CREACION DE LA CAMARA Y CONFIGURACION DE LA VISTA (revisar)
		camara = new OrthographicCamera();
		ventanaJuego = new FitViewport(Config.ANCHO/Config.PPM, Config.ALTO/Config.PPM, camara); //completar los argumentos y averiguar bien

		camara.setToOrtho(false, ventanaJuego.getWorldWidth()/2, ventanaJuego.getWorldHeight()/2);
		 
		
		
		//CARGADO DEL MAPA Y CONFIG DE SU RENDERIZADO
		cargarMapa = new TmxMapLoader();
		mapa = cargarMapa.load(Recursos.RUTA_MAPA);
		renderMapa = new OrthogonalTiledMapRenderer(mapa);
		
		
		//COMPROBACION DE CARGA DEL MAPA Y SUS CAPAS
		Gdx.app.log("Mapa", "Cargando mapa desde: " + Recursos.RUTA_MAPA);
		for (MapLayer layer : mapa.getLayers()) {
		    Gdx.app.log("Capa", "Nombre de la capa: " + layer.getName() + " Visible: " + layer.isVisible() );
		}

 
		
		//CONFIGURACION DE BOX2D
		mundo = new World(new Vector2(0, -100f), true);
		b2dr = new Box2DDebugRenderer();
		
		//CREACION DE LAS FIGURAS BOX2D (contemplar objetos rectangle y polygon)
		mundoBox2D = new MundoBox2D(this);
		
		//CREACION DEL PERSONAJE EN EL JUEGO
		jugador = new Marciano(this);

		
		mundo.setContactListener(new WorldContactListener());
		
		//INICIALIZACION DE TECLAS Y CONFIGURACION COMO OBJETO QUE RECIBIRA LAS ENTRADAS
		teclas = new KeyListener(this);
		Gdx.input.setInputProcessor(teclas);
		
		
		//INICIALIZACION DE LAS HERRAMIENTAS EXTRAS
		b = Render.sb;
	}
	
	//GETTERS
	
	public Marciano getJugador() {
		return jugador;
	}
	
	public TiledMap getMapa() {
		return mapa;
	}
	
	public World getMundo() {
		return mundo;
	}

	public MundoBox2D getMundoBox2D() {
		return mundoBox2D;
	}
	
	public TextureAtlas getAtlas() {
		return atlas;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
	    update(delta);
	    Render.LimpiarPantalla(); 
	    
	    renderMapa.render();  // DIBUJAR MAPA
	    b.setProjectionMatrix(camara.combined);  // CONFIGURAR CÁMARA
	    b2dr.render(mundo, camara.combined);  // MOSTRAR REPRESENTACION BOX 2D

	    b.begin();
	        jugador.draw(b);  // DIBUJAR AL JUGADOR
	    b.end();

	}

	public void manejoEntrada(float dt) {
		
		// EL JUGADOR SE MOVERA SOLO SI ESTE SE ENCUENTRA VIVO
	    if(!jugador.isMuerto()) {
	    	
	    	// MOVIMIENTO VERTICAL (SALTO)
	    	if (teclas.isArriba()) { //falta arreglaaaar
		        //jugador.cuerpo.setLinearVelocity(jugador.cuerpo.getLinearVelocity().x, velocidadSalto);
		        jugador.cuerpo.applyLinearImpulse(new Vector2(0, 20f), jugador.cuerpo.getWorldCenter(), true);// Solo afecta la velocidad vertical
		    }

		    // MOVIMIENTO HORIZONTAL HACIA LA DERECHA
		    if (teclas.isDerecha()) {
		        jugador.cuerpo.setLinearVelocity(60f, jugador.cuerpo.getLinearVelocity().y); 
		    }
		    
		    // MOVIMIENTO HORIZONTAL HACIA LA IZQUIERDA
		    if (teclas.isIzquierda()) {
		        jugador.cuerpo.setLinearVelocity(-60f, jugador.cuerpo.getLinearVelocity().y); // Velocidad negativa para moverse a la izquierda
		    }
		    

		    // EL PERSONAJE SE DETIENE SI NO HAY TECLAS PRESIONADAS 
		    if (!teclas.isDerecha() && !teclas.isIzquierda()) {
		        jugador.cuerpo.setLinearVelocity(0, jugador.cuerpo.getLinearVelocity().y); // Se mantiene la velocidad vertical sin cambios
		    }
	    }
	}





	
	public void update(float dt) {	//delta time
		manejoEntrada(dt);
		// ACTUALIZAR EL MUNDO BOX2D
		mundo.step(1/60f, 6, 2);
		jugador.update(dt);
		
		//ACTUALIZAR LA POSICION DE LA CAMARA
		float limiteX = (mapa.getProperties().get("width", Integer.class) * mapa.getProperties().get("tilewidth", Integer.class))/Config.PPM;
		float limiteY = (mapa.getProperties().get("height", Integer.class) * mapa.getProperties().get("tileheight", Integer.class))/Config.PPM;
		camara.position.x = Math.min(Math.max(jugador.cuerpo.getPosition().x, (ventanaJuego.getWorldWidth()/4)), limiteX - (ventanaJuego.getWorldWidth()/4));
		camara.position.y = Math.min(Math.max(jugador.cuerpo.getPosition().y, (ventanaJuego.getWorldHeight()/4)), limiteY - (ventanaJuego.getWorldHeight()/4));
		
		camara.update();
		renderMapa.setView(camara);
		
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
		mapa.dispose();
		b.dispose();
		mundo.dispose();
        renderMapa.dispose();
		b2dr.dispose();
		
	}

}
