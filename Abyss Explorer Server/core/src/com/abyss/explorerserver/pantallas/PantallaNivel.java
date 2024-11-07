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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class PantallaNivel implements Screen {
    private World mundo;
    private MundoBox2D mundoBox2D;
    private Map<Integer, Marciano> jugadores;
    private HiloServidor hs;
    private TiledMap mapa;

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
        // Carga el mapa
        TmxMapLoader cargarMapa = new TmxMapLoader();
        mapa = cargarMapa.load(Recursos.RUTA_MAPA);

        // Inicializa el mundo de Box2D
        mundo = new World(new Vector2(0, -100f), true);
        mundoBox2D = new MundoBox2D(this);
        mundo.setContactListener(new WorldContactListener());
        System.out.println("Se inicializo el mundo");
    }

    public void crearJugador(int clienteId) {
        String tipoMarciano = tiposMarciano[contadorTipos % tiposMarciano.length];
        Marciano nuevoJugador = new Marciano(this, tipoMarciano, 40, 200, 24, 24);
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
            System.out.println(jugador);
            System.out.println(jugadores.values());
        }

        if (Global.finJuego) {
            hs.notificarFinJuego();
            System.out.println("Juego terminado");
        }

        enviarEstadoAClientes();
    }

    private void manejoEntrada() {
        for (Map.Entry<Integer, Marciano> entry : jugadores.entrySet()) {
            int clienteId = entry.getKey();
            Marciano jugador = entry.getValue();
            //System.out.println("holaaaa entre");
            // Recibe comandos como "ARRIBA", "DERECHA", "IZQUIERDA" del cliente
            String comando = hs.obtenerComandoCliente(clienteId);
            //System.out.println("manejo: " + comando + " id " + clienteId + " jugador" + jugador);
            if (comando != null) {
            	if(comando.equals("ARRIBA;")) {
            		jugador.saltar();
            	}
            	
            	if(comando.equals("DERECHA;")) {
            		 jugador.moverDerecha();
            	}
            	if(comando.equals("IZQUIERDA;")) {
            		jugador.moverIzquierda();
            	}
                /*switch (comando) {
                    case "ARRIBA;":
                        jugador.saltar();
                        break;
                    case "DERECHA;":
                    	//System.out.println("se movio derecha");
                        jugador.moverDerecha();
                        break;
                    case "IZQUIERDA;":
                        jugador.moverIzquierda();
                        break;
                }*/
            }
        }
    }

    private void enviarEstadoAClientes() {
        String estadoJuego = serializarEstadoJuego();
        hs.enviarEstadoAClientes(estadoJuego);
    }

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
        //System.out.println("enviarEstadoAcliente " + sb.toString()); // Debugging
        return sb.toString(); // Devuelve la cadena construida
    }

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
        mapa.dispose();
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
}