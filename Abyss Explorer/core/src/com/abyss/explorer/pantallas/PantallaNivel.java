package com.abyss.explorer.pantallas;

import com.abyss.explorer.elementos.MundoBox2D;
import com.abyss.explorer.elementos.WorldContactListener;
import com.abyss.explorer.io.KeyListener;
import com.abyss.explorer.sprites.Marciano;
import com.abyss.explorer.utiles.Config;
import com.abyss.explorer.utiles.Global;
import com.abyss.explorer.utiles.Recursos;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaNivel implements Screen {

    SpriteBatch b;
    KeyListener teclas;

    // ATLAS 
    private TextureAtlas atlas;

    // PANTALLA
    private OrthographicCamera camara;
    private Viewport ventanaJuego;

    // MAPA
    private TmxMapLoader cargarMapa; // CARGA EL MAPA DENTRO DEL MUNDO
    private TiledMap mapa; // ES EL MAPA EN SI
    private OrthogonalTiledMapRenderer renderMapa;

    // MUNDO BOX2D
    private World mundo;
    private Box2DDebugRenderer b2dr; // REPRESENTACION GRAFICA DE LOS CUERPOS DENTRO DEL MUNDO

    // CREACION DEL MUNDO A PARTIR DE UNA CLASE
    private MundoBox2D mundoBox2D;

    // JUGADORES
    private Marciano[] jugadores;

    private int cantJugador;

    public PantallaNivel(int cantJugador) {
        this.cantJugador = cantJugador;
    }

    @Override
    public void show() {
        // CREACION DE LA CAMARA Y CONFIGURACION DE LA VISTA
        camara = new OrthographicCamera();
        ventanaJuego = new FitViewport(Config.ANCHO / Config.PPM, Config.ALTO / Config.PPM, camara);

        camara.setToOrtho(false, ventanaJuego.getWorldWidth() / 2, ventanaJuego.getWorldHeight() / 2);

        // CARGADO DEL MAPA Y CONFIG DE SU RENDERIZADO
        cargarMapa = new TmxMapLoader();
        mapa = cargarMapa.load(Recursos.RUTA_MAPA);
        renderMapa = new OrthogonalTiledMapRenderer(mapa);

        // COMPROBACION DE CARGA DEL MAPA Y SUS CAPAS
        Gdx.app.log("Mapa", "Cargando mapa desde: " + Recursos.RUTA_MAPA);
        for (MapLayer layer : mapa.getLayers()) {
            Gdx.app.log("Capa", "Nombre de la capa: " + layer.getName() + " Visible: " + layer.isVisible());
        }

        // CONFIGURACION DE BOX2D
        mundo = new World(new Vector2(0, -100f), true);
        b2dr = new Box2DDebugRenderer();

        // CREACION DE LAS FIGURAS BOX2D
        mundoBox2D = new MundoBox2D(this);

        // CREACION DE LOS JUGADORES
        atlas = new TextureAtlas(Recursos.SPRITE_MARCIANO);
        jugadores = new Marciano[cantJugador]; // Crear array de jugadores según la cantidad seleccionada

        jugadores[0] = new Marciano(this, Recursos.SPRITE_MARCIANO_PINK, 106, 132); // Jugador 1

        if (cantJugador == 2) {
            jugadores[1] = new Marciano(this, Recursos.SPRITE_MARCIANO_GREEN, 2, 28); // Jugador 2
        }

        mundo.setContactListener(new WorldContactListener());

        // INICIALIZACION DE TECLAS Y CONFIGURACION COMO OBJETO QUE RECIBIRA LAS ENTRADAS
        teclas = new KeyListener(this);
        Gdx.input.setInputProcessor(teclas);

        // INICIALIZACION DE LAS HERRAMIENTAS EXTRAS
        b = Render.sb;
    }

    // GETTERS

    public TiledMap getMapa() {
        return mapa;
    }

    public World getMundo() {
        return mundo;
    }

    public MundoBox2D getMundoBox2D() {
        return mundoBox2D;
    }

    public TextureAtlas getAtlasJugador() { //actualizar
        return atlas;
    }

    @Override
    public void render(float delta) {
        update(delta);
        Render.LimpiarPantalla();

        renderMapa.render(); // DIBUJAR MAPA
        b.setProjectionMatrix(camara.combined); // CONFIGURAR CÁMARA
        b2dr.render(mundo, camara.combined); // MOSTRAR REPRESENTACION BOX 2D

        b.begin();
        for (Marciano jugador : jugadores) {
            jugador.draw(b); // DIBUJAR AL JUGADOR O JUGADORES :)
        }
        b.end();
       
    }

    public int contSalto = 0;
    public int contSalto2 = 0;

    public void manejoEntrada(float dt) {
        for (int i = 0; i < jugadores.length; i++) {
            Marciano jugador = jugadores[i];
          
            // EL JUGADOR SE MOVERA SOLO SI ESTE SE ENCUENTRA VIVO
            if (!jugador.isMuerto()) {
                // Verifica si el jugador está en el suelo
                if (jugador.cuerpo.getLinearVelocity().y == 0) {
                    if (i == 0) {
                        contSalto = 0; // Resetear cont para jugador 1
                    } else {
                        contSalto2 = 0; // Resetear cont para jugador 2
                    }
                }

                // MOVIMIENTO VERTICAL (SALTO)
                if ((i == 0 && teclas.isArriba() && Gdx.input.isKeyJustPressed(Input.Keys.UP)) ||
                    (i == 1 && teclas.isTeclaW() && Gdx.input.isKeyJustPressed(Input.Keys.W))) {
                    if ((i == 0 && contSalto < 2) || (i == 1 && contSalto2 < 2)) {
                        jugador.cuerpo.setLinearVelocity(jugador.cuerpo.getLinearVelocity().x, 80f);
                        if (i == 0) {
                            contSalto++;
                        } else {
                            contSalto2++;
                        }
                    }
                }

                // MOVIMIENTO HORIZONTAL HACIA LA DERECHA
                if ((i == 0 && teclas.isDerecha()) || (i == 1 && teclas.isTeclaD())) {
                    jugador.cuerpo.setLinearVelocity(60f, jugador.cuerpo.getLinearVelocity().y);
                }

                // MOVIMIENTO HORIZONTAL HACIA LA IZQUIERDA
                if ((i == 0 && teclas.isIzquierda()) || (i == 1 && teclas.isTeclaA())) {
                    jugador.cuerpo.setLinearVelocity(-60f, jugador.cuerpo.getLinearVelocity().y);
                }

                if (jugador.cuerpo.getLinearVelocity().y < 0) {
                    jugador.cuerpo.applyLinearImpulse(new Vector2(0, -9.8f), jugador.cuerpo.getWorldCenter(), true);
                }

                // EL PERSONAJE SE DETIENE SI NO HAY TECLAS PRESIONADAS
                if ((i == 0 && !teclas.isDerecha() && !teclas.isIzquierda()) ||
                    (i == 1 && !teclas.isTeclaD() && !teclas.isTeclaA())) {
                    jugador.cuerpo.setLinearVelocity(0, jugador.cuerpo.getLinearVelocity().y);
                }
            }
        }
    }

    public void update(float dt) {
        
            manejoEntrada(dt);
            // ACTUALIZAR EL MUNDO BOX2D
            mundo.step(1 / 60f, 6, 2);
            for (Marciano jugador : jugadores) {
                jugador.update(dt);
            }
            // ACTUALIZAR LA POSICION DE LA CAMARA (modificar)
            float limiteX = (mapa.getProperties().get("width", Integer.class) * mapa.getProperties().get("tilewidth", Integer.class)) / Config.PPM;
            float limiteY = (mapa.getProperties().get("height", Integer.class) * mapa.getProperties().get("tileheight", Integer.class)) / Config.PPM;
            camara.position.x = Math.min(Math.max(jugadores[0].cuerpo.getPosition().x, (ventanaJuego.getWorldWidth() / 4)), limiteX - (ventanaJuego.getWorldWidth() / 4));
            camara.position.y = Math.min(Math.max(jugadores[0].cuerpo.getPosition().y, (ventanaJuego.getWorldHeight() / 4)), limiteY - (ventanaJuego.getWorldHeight() / 4));

            camara.update();
            renderMapa.setView(camara);
            
            if(Global.finJuego) {
            	System.out.println(Global.finJuego);
                Render.app.setScreen(new PantallaJuegoTerminado()); 
            }
        
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
        mundo.dispose();
        renderMapa.dispose();
        b2dr.dispose();
        
        this.dispose();
        
        
    }
}