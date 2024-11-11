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
import com.badlogic.gdx.utils.SerializationException;

//Clase que representa la pantalla del nivel del juego
public class PantallaNivel implements Screen {
	// MUNDO BOX2D
    private World mundo; // Mundo de Box2D
    
    // CREACION DEL MUNDO A PARTIR DE UNA CLASE
    private MundoBox2D mundoBox2D; // Clase que maneja el mundo Box2D

    private Map<Integer, Marciano> jugadores; // Mapa que almacena los jugadores
    private HiloServidor hs; // Hilo del servidor
    private int clienteId; // ID del cliente
    
    
    // MAPA
    private TmxMapLoader cargarMapa; // Cargador de mapas
    private TiledMap mapa; // Mapa del juego
    private OrthogonalTiledMapRenderer renderMapa; // Renderizador del mapa

    
    
    //ARRAY QUE ALMACENA TODOS LOS TIPOS DE MARCIANOS DEL ATLAS MARCIANOS
    private String[] tiposMarciano = {
        Recursos.SPRITE_MARCIANO_PINK,
        Recursos.SPRITE_MARCIANO_GREEN,
        Recursos.SPRITE_MARCIANO_BLUE,
        Recursos.SPRITE_MARCIANO_ORANGE,
        Recursos.SPRITE_MARCIANO_NUDE
    };
    
    private int contadorTipos = 0; // Contador de tipos de marcianos
    
    // Constructor de la clase PantallaNivel
    public PantallaNivel() {
        inicializarMundo(); // Inicializa el mundo
        jugadores = new HashMap<>(); // Inicializa el mapa de jugadores
        hs = new HiloServidor(this); // Crea un nuevo hilo del servidor
        hs.start(); // Inicia el hilo del servidor
    }

    // Método para inicializar el mundo
    private void inicializarMundo() {
        // CARGADO DEL MAPA 
        try {
            cargarMapa = new TmxMapLoader(); // Crea un cargador de mapas
            mapa = cargarMapa.load(Recursos.RUTA_MAPA); // Carga el mapa desde la ruta especificada
            renderMapa = new OrthogonalTiledMapRenderer(mapa); // Crea un renderizador para el mapa
        } catch (SerializationException e) {
            e.printStackTrace(); // Maneja excepciones de serialización
        }

        // CONFIGURACION DE BOX2D
        mundo = new World(new Vector2(0, -100f), true); // Crea un nuevo mundo Box2D con gravedad

        // CREACION DE LAS FIGURAS BOX2D
        mundoBox2D = new MundoBox2D(this); // Inicializa el mundo Box2D
        mundo.setContactListener(new WorldContactListener()); // Establece el listener de contactos

        System.out.println("Se inicializo el mundo"); // Mensaje de depuración
    }

 // Método para crear un nuevo jugador
    public void crearJugador(int clienteId) {
        String tipoMarciano = tiposMarciano[contadorTipos % tiposMarciano.length]; // Selecciona el tipo de marciano
        Marciano nuevoJugador = new Marciano(this, tipoMarciano, 40, 200, 24, 24, clienteId); // Crea un nuevo marciano
        this.clienteId = clienteId; // Asigna el ID del cliente
        jugadores.put(clienteId, nuevoJugador); // Agrega el jugador al mapa
        contadorTipos++; // Incrementa el contador de tipos
        enviarEstadoAClientes(); // Envía el estado a los clientes
        // System.out.println("Se creo jugador"); // Mensaje de depuración
    }

 // Método para actualizar el estado del nivel
    public void update(float delta) {
        manejoEntrada(); // Maneja la entrada del jugador
        mundo.step(1 / 60f, 6, 2); // Actualiza el mundo Box2D
        for (Marciano jugador : jugadores.values()) {
            jugador.update(delta); // Actualiza cada jugador
            jugador.manejarCaida(); // Maneja la caída para reiniciar el contador de saltos
        }

        if (Global.finJuego) {
            hs.notificarGanador(); // Notifica al servidor el ganador
            hs.notificarFinJuego(); // Notifica el fin del juego
            Global.inicioJuego = false; // Reinicia el estado de inicio del juego
            // System.out.println("Juego terminado"); // Mensaje de depuración
        }

        enviarEstadoAClientes(); // Envía el estado actualizado a los clientes
    }

    // Método para manejar la entrada del jugador
    private void manejoEntrada() {
        for (Map.Entry<Integer, Marciano> entry : jugadores.entrySet()) {
            int clienteId = entry.getKey(); // Obtiene el ID del cliente
            Marciano jugador = entry.getValue(); // Obtiene el jugador correspondiente

            // Recibe comandos como "ARRIBA", "DERECHA", "IZQUIERDA" del cliente           
            if (!jugador.isMuerto()) { // Verifica si el jugador está vivo
                String comando = hs.obtenerComandoCliente(clienteId); // Obtiene el comando del cliente
                if (comando != null) {
                    if (comando.equals("ARRIBA;")) { // Maneja el salto
                        jugador.saltar(); // Hace saltar al jugador
                    }

                    if (comando.equals("DERECHA;")) { // Maneja el movimiento a la derecha
                        //jugador.cuerpo.setLinearVelocity(60f, jugador.cuerpo.getLinearVelocity().y); // Establece la velocidad
                    	jugador.moverDerecha();
                    }
                    if (comando.equals("IZQUIERDA;")) { // Maneja el movimiento a la izquierda
                        //jugador.cuerpo.setLinearVelocity(-60f, jugador.cuerpo.getLinearVelocity().y); // Establece la velocidad
                    	jugador.moverIzquierda();
                    }

                    // Se aplica una fuerza cuando el marciano está cayendo
                    if (jugador.cuerpo.getLinearVelocity().y < 0) {
                        jugador.cuerpo.applyLinearImpulse(new Vector2(0, -9.8f), jugador.cuerpo.getWorldCenter(), true); // Aplica impulso
                    }

                    // EL PERSONAJE SE DETIENE SI NO HAY TECLAS PRESIONADAS
                    if (comando.equals("QUIETO;")) {
                        jugador.cuerpo.setLinearVelocity(0, jugador.cuerpo.getLinearVelocity().y); // Detiene el movimiento
                    }
                }
            }
        }
    }
    
    
    // ENVIA EL ESTADO DEL JUGADOR AL CLIENTE (POSICION Y ESTADO)
    private void enviarEstadoAClientes() {
        String estadoJuego = serializarEstadoJuego(); // Serializa el estado del juego
        hs.enviarEstadoAClientes(estadoJuego); // Envía el estado a los clientes
    }

    // CONSTRUYE UN STRING DONDE SE SERIALIZA LAS ACTUALIZACIONES DEL JUGADOR SEPARANDO POR : LOS DATOS Y ; LOS JUGADORES
    private String serializarEstadoJuego() {
        StringBuilder sb = new StringBuilder(); // Crea un StringBuilder para construir la cadena
        for (Map.Entry<Integer, Marciano> entry : jugadores.entrySet()) { // Itera sobre cada jugador
            int clienteId = entry.getKey(); // Obtiene el ID del cliente (clave)
            Marciano jugador = entry.getValue(); // Obtiene el objeto jugador (valor)
            sb.append(clienteId).append(":") // Añade el ID del cliente
              .append(jugador.getTipoMarciano()).append(":") // Añade el tipo de marciano
              .append(jugador.getCuerpo().getPosition().x). append(",") // Añade la posición X
              .append(jugador.getCuerpo().getPosition().y).append(":") // Añade la posición Y
              .append(jugador.getDireccion()).append(":") // Añade la direccion del jugador
              .append(jugador.getEstado()).append(";"); // Añade el estado del jugador
        }
        return sb.toString(); // Devuelve la cadena construida
    }
    
    
    // GETTERS
    public World getMundo() {
        return mundo; // Devuelve el mundo de Box2D
    }

    public MundoBox2D getMundoBox2D() {
        return mundoBox2D; // Devuelve el mundo Box2D
    }

    public TiledMap getMapa() {
        return mapa; // Devuelve el mapa del juego
    }

    public void removerJugador(int clienteId) {
        Marciano jugador = jugadores.remove(clienteId); // Remueve al jugador del mapa
        if (jugador != null) {
            mundo.destroyBody(jugador.getCuerpo()); // Destruye el cuerpo del jugador en el mundo
        }
    }
    
    public void dispose() {
        hs.detener(); // Detiene el hilo del servidor
        mundo.dispose(); // Libera los recursos del mundo
        mapa.dispose(); // Liberar el mapa
        for (Marciano jugador : jugadores.values()) {
            mundo.destroyBody(jugador.getCuerpo()); // Destruye los cuerpos de los jugadores
        }
        jugadores.clear(); // Limpia la lista de jugadores
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
    	update(delta);
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
    }

}