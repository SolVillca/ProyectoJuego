package com.abyss.explorer.pantallas;

import java.util.HashMap;
import java.util.Map;

import com.abyss.explorer.elementos.Texto;
import com.abyss.explorer.io.KeyListener;
import com.abyss.explorer.red.HiloCliente;
import com.abyss.explorer.sprites.Marciano;
import com.abyss.explorer.utiles.Config;
import com.abyss.explorer.utiles.Global;
import com.abyss.explorer.utiles.Recursos;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaNivel implements Screen {
    private SpriteBatch b;
    private KeyListener teclas;
    private HiloCliente hc;
    private int clienteId; // ID único del cliente

    private Texto espera;
    // ATLAS
    private TextureAtlas atlas;

    // PANTALLA
    private OrthographicCamera camara;
    private Viewport ventanaJuego;

    // MAPA
    private TmxMapLoader cargarMapa; // CARGA EL MAPA DENTRO DEL MUNDO
    private TiledMap mapa; // ES EL MAPA EN SI
    private OrthogonalTiledMapRenderer renderMapa;

    // JUGADORES
    private Map<Integer, Sprite> jugadores;
    private Map<Integer, String> tiposJugadores;
    private int jugadorLocal; // ID del jugador local
    


    @Override
    public void show() {
    	System.out.println("pantallaNivel cliente");
    	espera = new Texto(Recursos.FUENTEMENU, 40, Color.valueOf("#D2704A"), false );
    	espera.setTexto("Esperando rival (Cliente) ...");
    	espera.setPosition((Config.ANCHO - espera.getAncho()) / 2, (Config.ALTO - espera.getAlto()) / 2 ); 
    	
    	
    	// CREACION DE LA CAMARA Y CONFIGURACION DE LA VISTA
        camara = new OrthographicCamera();
        ventanaJuego = new FitViewport(Config.ANCHO / Config.PPM, Config.ALTO / Config.PPM, camara);
        camara.setToOrtho(false, ventanaJuego.getWorldWidth() / 2, ventanaJuego.getWorldHeight() / 2);

        // CARGADO DEL MAPA Y CONFIG DE SU RENDERIZADO
        cargarMapa = new TmxMapLoader();
        mapa = cargarMapa.load(Recursos.RUTA_MAPA);
        renderMapa = new OrthogonalTiledMapRenderer(mapa);


        // Inicialización de sprites y texturas
        atlas = new TextureAtlas(Recursos.SPRITE_MARCIANO);
        jugadores = new HashMap<>();
        tiposJugadores = new HashMap<>();

        // Inicialización de entrada y red
        teclas = new KeyListener(this);
        Gdx.input.setInputProcessor(teclas);

        // Inicialización del cliente de red
        hc = new HiloCliente(this);
        hc.start();
        
        b = Render.sb;
    }

    @Override
    public void render(float delta) {
        if (!Global.inicioJuego) {
            // Mostrar pantalla de espera
            Render.LimpiarPantalla();
            Render.begin();
            	espera.dibujar();
            Render.end();
        } else {
            // Continuar con la lógica del juego
        	Render.LimpiarPantalla();
            update(delta);
            
            for (Sprite sprite : jugadores.values()) {
                if (sprite instanceof Marciano) {
                    ((Marciano) sprite).update(delta); 
                }
            }
            
            // Renderizado del mapa
            renderMapa.render();

            b.setProjectionMatrix(camara.combined);
            b.begin();
            	renderizarJugadores();
            b.end();

            // Enviar inputs al servidor
            enviarInputs();

            if (Global.finJuego) {
            	hc.detener();
            	Sprite sprite = jugadores.get(clienteId);
                if (sprite instanceof Marciano) {
                	Render.app.setScreen(new PantallaJuegoTerminado( ((Marciano) sprite).getRegion()));
                }
            }
        }
    }

    private void update(float delta) {
        actualizarCamara();
        camara.update();
        renderMapa.setView(camara);
        hc.actualizarEstadoJuego();
        
    }

    private void renderizarJugadores() {
    	
    	//System.out.println(jugadores.values() + " rendrizar Jugadores cliente"); jugadores esta vacio
        for (Sprite sprite : jugadores.values()) {
            sprite.draw(b);
           //System.out.println(jugadores.values());
        }
       // System.out.println("Se renderizo jugadores");
    }

    private void actualizarCamara() {
        Sprite jugadorLocalSprite = jugadores.get(clienteId);
        //System.out.println(jugadorLocalSprite);
        if (jugadorLocalSprite != null) {
        	// ACTUALIZAR LA POSICION DE LA CAMARA (modificar)
        	float limiteX = (mapa.getProperties().get("width", Integer.class) * mapa.getProperties().get("tilewidth", Integer.class)) / Config.PPM;
            float limiteY = (mapa.getProperties().get("height", Integer.class) * mapa.getProperties().get("tileheight", Integer.class)) / Config.PPM;
            camara.position.x = Math.min(Math.max(jugadorLocalSprite.getX(), (ventanaJuego.getWorldWidth() / 4)), limiteX - (ventanaJuego.getWorldWidth() / 4));
            camara.position.y = Math.min(Math.max(jugadorLocalSprite.getY(), (ventanaJuego.getWorldHeight() / 4)), limiteY - (ventanaJuego.getWorldHeight() / 4));


        }
    }

    private void enviarInputs() {
        StringBuilder input = new StringBuilder();
        if (teclas.isArriba() && Gdx.input.isKeyJustPressed(Input.Keys.UP)) input.append("ARRIBA;");
        if (teclas.isDerecha()) {
        	Sprite sprite = jugadores.get(clienteId);
            if (sprite instanceof Marciano) {
            	((Marciano) sprite).setDireccion(false);
            	input.append("DERECHA;");
            }
        }
        if (teclas.isIzquierda()) {
        	Sprite sprite = jugadores.get(clienteId);
            if (sprite instanceof Marciano) {
            	((Marciano) sprite).setDireccion(true);
            	input.append("IZQUIERDA;");
            }
        }
        if (!teclas.isDerecha() && !teclas.isIzquierda() && !teclas.isArriba()) input.append("QUIETO;");
        
        if (input.length() > 0) {
        	// error al mandar clienteId
            hc.enviarMsj("INPUT:" + clienteId + ":" + input.toString());
        }
    }

    public void actualizarEstadoJuego(String estado) {
        String[] jugadoresInfo = estado.split(";");
        for (String jugadorInfo : jugadoresInfo) {
            String[] partes = jugadorInfo.split(":");
            if (partes.length >= 4) {
                int id = Integer.parseInt(partes[0]);
                String tipo = partes[1];
                String[] posicion = partes[2].split(",");
                float x = Float.parseFloat(posicion[0]);
                float y = Float.parseFloat(posicion[1]);
                String estadoJugador = partes[3];
                //System.out.println("actualizarEstadoJuego pc " + estadoJugador);
                actualizarJugador(id, tipo, x, y, estadoJugador);
            }
        }
    }

    
    
    public void actualizarJugador(int id, String tipo, float x, float y, String estado) {
        Marciano marciano = (Marciano) jugadores.get(id);
        if (marciano == null || !tipo.equals(tiposJugadores.get(id))) {
            marciano = new Marciano(atlas, tipo); // Asegúrate de que el tipo sea correcto
            jugadores.put(id, marciano);
            tiposJugadores.put(id, tipo);
        }
        marciano.setPosition(x, y);
        marciano.actualizarEstado(estado); // Actualiza el estado del marciano
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
        dispose();
    }

    @Override
    public void dispose() {
        mapa.dispose();
        renderMapa.dispose();
        atlas.dispose();
        hc.detener();
        //hc.interrupt(); // Detener el hilo del cliente
    }

	public World getMundo() {
		// TODO Auto-generated method stub
		return null;
	}

	public TiledMap getMapa() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setClienteId(int id) {
	    this.clienteId = id;
	}
	
}