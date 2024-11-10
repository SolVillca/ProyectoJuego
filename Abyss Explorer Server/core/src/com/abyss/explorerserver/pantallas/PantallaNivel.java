package com.abyss.explorerserver.pantallas;

import java.util.HashMap;
import java.util.Map;

import com.abyss.explorerserver.elementos.MundoBox2D;
import com.abyss.explorerserver.elementos.WorldContactListener;
import com.abyss.explorerserver.red.HiloServidor;
import com.abyss.explorerserver.sprites.Marciano;
import com.abyss.explorerserver.utiles.Global;
import com.abyss.explorerserver.utiles.Recursos;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class PantallaNivel implements Screen {
	// MUNDO BOX2D
    private World mundo;
    
    // CREACION DEL MUNDO A PARTIR DE UNA CLASE
    private MundoBox2D mundoBox2D;

    private Map<Integer, Marciano> jugadores;
    private HiloServidor hs;
    private int clienteId; 
//    private static HiloServidor hs;
    
    
    // MAPA
    private TmxMapLoader cargarMapa;
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderMapa;

    private int contSalto = 0;
    
    
    //ARRAY QUE ALMACENA TODOS LOS TIPOS DE MARCIANOS DEL ATLAS MARCIANOS
    private String[] tiposMarciano = {
        Recursos.SPRITE_MARCIANO_PINK,
        Recursos.SPRITE_MARCIANO_GREEN,
        Recursos.SPRITE_MARCIANO_BLUE,
        Recursos.SPRITE_MARCIANO_ORANGE,
        Recursos.SPRITE_MARCIANO_NUDE
    };
    private int contadorTipos = 0;

    public PantallaNivel() {
        inicializarMundo();
        jugadores = new HashMap<>();
        hs = new HiloServidor(this);
        hs.start();
    }

    private void inicializarMundo() {
    	// CARGADO DEL MAPA 
        cargarMapa = new TmxMapLoader();
        mapa = cargarMapa.load(Recursos.RUTA_MAPA);
        renderMapa = new OrthogonalTiledMapRenderer(mapa);
        
        // CONFIGURACION DE BOX2D
        mundo = new World(new Vector2(0, -100f), true);

        // CREACION DE LAS FIGURAS BOX2D
        mundoBox2D = new MundoBox2D(this);
        mundo.setContactListener(new WorldContactListener());
        
        System.out.println("Se inicializo el mundo");
    }

    public void crearJugador(int clienteId) {
        String tipoMarciano = tiposMarciano[contadorTipos % tiposMarciano.length];
        Marciano nuevoJugador = new Marciano(this, tipoMarciano, 40, 200, 24, 24);
        this.clienteId = clienteId;
        jugadores.put(clienteId, nuevoJugador);
        contadorTipos++;
        enviarEstadoAClientes();
        //System.out.println("Se creo jugador");
    }

    public void update(float delta) {
    	//System.out.println("hola update " + delta);
        manejoEntrada();
        mundo.step(1 / 60f, 6, 2);
        for (Marciano jugador : jugadores.values()) {
            jugador.update(delta);
            //System.out.println(jugador);
            //System.out.println(jugadores.values());
        }

        if (Global.finJuego) {
            hs.notificarGanador();
            hs.notificarFinJuego();
            Global.inicioJuego = false;
            //System.out.println("Juego terminado");
           // for (Marciano jugador : jugadores.values()) {
             //   hs.removerCliente(clienteId);
                //System.out.println(jugador);
                //System.out.println(jugadores.values());
            //}
        }

        enviarEstadoAClientes();
    }

    private void manejoEntrada() {
        for (Map.Entry<Integer, Marciano> entry : jugadores.entrySet()) {
            int clienteId = entry.getKey();
            Marciano jugador = entry.getValue();
            //System.out.println("holaaaa entre");
            // Recibe comandos como "ARRIBA", "DERECHA", "IZQUIERDA" del cliente
            
            if(!jugador.isMuerto()) {
            	String comando = hs.obtenerComandoCliente(clienteId);
                //System.out.println("manejo: " + comando + " id " + clienteId + " jugador" + jugador);
                if (comando != null) {
                	if(comando.equals("ARRIBA;") && contSalto < 2) {
                		jugador.saltar();
                		contSalto++;
                		//System.out.println(contSalto);
                		//jugador.cuerpo.setLinearVelocity(jugador.cuerpo.getLinearVelocity().x, 80f);
                	}
                	
                	if(comando.equals("DERECHA;")) {
                		 //jugador.moverDerecha();
                		 jugador.cuerpo.setLinearVelocity(60f, jugador.cuerpo.getLinearVelocity().y);
                	}
                	if(comando.equals("IZQUIERDA;")) {
                		//jugador.moverIzquierda();
                		jugador.cuerpo.setLinearVelocity(-60f, jugador.cuerpo.getLinearVelocity().y);
                	}
                	
                	
                	// Se aplica una fuerza cuando el marciano está cayendo
                	if (jugador.cuerpo.getLinearVelocity().y < 0) {
                        jugador.cuerpo.applyLinearImpulse(new Vector2(0, -9.8f), jugador.cuerpo.getWorldCenter(), true);
                    }

                    // EL PERSONAJE SE DETIENE SI NO HAY TECLAS PRESIONADAS
                    if (comando.equals("QUIETO;"))  {
                        jugador.cuerpo.setLinearVelocity(0, jugador.cuerpo.getLinearVelocity().y);
                    }
                    
                    if(jugador.cuerpo.getLinearVelocity().y == 0) {
                    	contSalto = 0;
                    	//System.out.println(contSalto + " toco suelo");
                    }
                }
            }
        }
    }
    
    
    // ENVIA EL ESTADO DEL JUGADOR AL CLIENTE (POSICION Y ESTADO)
    private void enviarEstadoAClientes() {
        String estadoJuego = serializarEstadoJuego();
        hs.enviarEstadoAClientes(estadoJuego);
    }
    
    
    // CONSTRUYE UN STRING DONDE SE SERIALIZA LAS ACTUALIZACIONES DEL JUGADOR SEPARANDO POR : LOS DATOS Y ; LOS JUGADORES
    private String serializarEstadoJuego() {
        StringBuilder sb = new StringBuilder(); // Crea un StringBuilder para construir la cadena
        for (Map.Entry<Integer, Marciano> entry : jugadores.entrySet()) { // Itera sobre cada jugador
            int clienteId = entry.getKey(); // Obtiene el ID del cliente (clave)
            Marciano jugador = entry.getValue(); // Obtiene el objeto jugador (valor)
            sb.append(clienteId).append(":") // Añade el ID del cliente
              .append(jugador.getTipoMarciano()).append(":") // Añade el tipo de marciano
              .append(jugador.getCuerpo().getPosition().x).append(",") // Añade la posición X
              .append(jugador.getCuerpo().getPosition().y).append(":") // Añade la posición Y
              .append(jugador.getEstado()).append(";"); // Añade el estado del jugador
        }
        return sb.toString(); // Devuelve la cadena construida
    }
    
    
    // GETTERS
    public World getMundo() {
        return mundo;
    }

    public MundoBox2D getMundoBox2D() {
        return mundoBox2D;
    }

    public TiledMap getMapa() {
        return mapa;
    }

    public void removerJugador(int clienteId) {
        Marciano jugador = jugadores.remove(clienteId);
        if (jugador != null) {
            mundo.destroyBody(jugador.getCuerpo());
        }
    }

    public void dispose() {
        hs.detener();
        mundo.dispose();
        //mapa.dispose();
        for (Marciano jugador : jugadores.values()) {
            mundo.destroyBody(jugador.getCuerpo());
        }
        jugadores.clear();
    }

    @Override
    public void show() {
        // Método para mostrar la pantalla
    }

    @Override
    public void render(float delta) {
        // Método para renderizar la pantalla
    	update(delta);
    }

    @Override
    public void resize(int width, int height) {
        // Método para manejar el cambio de tamaño de la pantalla
    }

    @Override
    public void pause() {
        // Método para pausar la pantalla
    }

    @Override
    public void resume() {
        // Método para reanudar la pantalla
    }

    @Override
    public void hide() {
        // Método para ocultar la pantalla
    }

	/*public static HiloServidor getHs() {
		return hs;
	}*/

}