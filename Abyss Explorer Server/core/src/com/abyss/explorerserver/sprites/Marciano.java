package com.abyss.explorerserver.sprites;

import com.abyss.explorerserver.AbyssExplorerServer;
import com.abyss.explorerserver.pantallas.PantallaNivel;
import com.abyss.explorerserver.utiles.Config;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

//Clase que representa un Marciano en el servidor
public class Marciano {
	public World mundo; // Mundo de Box2D donde se encuentra el marciano
    public Body cuerpo; // Cuerpo físico del marciano
    private String tipoMarciano; // Tipo de marciano
    public EstadosMarciano estadoActual; // Estado actual del marciano
    public EstadosMarciano estadoAnterior; // Estado anterior del marciano
    private boolean porDer = true; // Indica si el marciano mira a la derecha
    private boolean muerto = false; // Indica si el marciano está muerto
    private float tiempoMuerto = 0; // Tiempo que el marciano ha estado muerto
    private Checkpoint ultimoCheckpoint; // Último checkpoint alcanzado por el marciano
    private int id;
    private int contSalto = 0;

    // Constructor de la clase Marciano
    public Marciano(PantallaNivel pantalla, String tipoMarciano, float x, float y, float ancho, float alto, int id) {
        this.mundo = pantalla.getMundo(); // Obtiene el mundo de Box2D desde la pantalla
        this.tipoMarciano = tipoMarciano; // Asigna el tipo de marciano
        this.id = id; // Asigna el ID único
        estadoActual = EstadosMarciano.QUIETO; // Estado inicial
        estadoAnterior = EstadosMarciano.QUIETO; // Estado anterior inicial
        System.out.println("marciano"); // Mensaje de depuración
        crearCuerpo(x, y, ancho, alto); // Crea el cuerpo del marciano en el mundo
    }

    // Método para crear el cuerpo del marciano en el mundo
    private void crearCuerpo(float x, float y, float ancho, float alto) {
        // DEFINICIÓN DE LAS PROPIEDADES DEL CUERPO
        BodyDef bd = new BodyDef(); // Crea una definición de cuerpo
        bd.position.set(40, 200); // Establece la posición inicial del cuerpo en el mundo
        bd.type = BodyDef.BodyType.DynamicBody; // Tipo de cuerpo, afectado por gravedad y colisiones
        cuerpo = mundo.createBody(bd); // Crea el cuerpo en el mundo Box2D

        // DEFINICIÓN DEL FIXTURE (FORMA) DEL CUERPO
        FixtureDef fd = new FixtureDef(); // Crea una definición de fixture

        PolygonShape forma = new PolygonShape(); // Forma del cuerpo
        forma.setAsBox(10 / Config.PPM, 13 / Config.PPM);  // Establece las dimensiones del cuerpo
        fd.filter.categoryBits = AbyssExplorerServer.MARCIANO_BIT; // Categoría del marciano
        fd.filter.maskBits = AbyssExplorerServer.DEFAULT_BIT | AbyssExplorerServer.TRAMPOLIN_BIT | AbyssExplorerServer.PINCHES_BIT | AbyssExplorerServer.ENEMIGO_BIT | AbyssExplorerServer.CHECKPOINT_BIT | AbyssExplorerServer.CHECKPOINT_ACTIVADO_BIT | AbyssExplorerServer.FIN_BIT; // Elementos con los que puede colisionar

        fd.shape = forma; // Asigna la forma al fixture
        cuerpo.createFixture(fd).setUserData(this); // Asigna el fixture al cuerpo y establece el usuario de datos

        // CREACIÓN DE UN SENSOR PARA LOS "PIES" DEL MARCIANO
        EdgeShape pies = new EdgeShape(); // Crea una forma de borde para los pies
	    pies.set(new Vector2(-7 / Config.PPM, -13 / Config.PPM), new Vector2(7 / Config.PPM, -13 / Config.PPM)); // Establece el borde de los pies
	    
	    fd.shape = pies; // Asigna la forma de los pies al fixture
        fd.isSensor = true; // Los pies son un sensor para detectar trampolines, pinches, etc.
        fd.filter.categoryBits = AbyssExplorerServer.MARCIANO_PIES_BIT; // Categoría de los pies
        fd.filter.maskBits = AbyssExplorerServer.DEFAULT_BIT | AbyssExplorerServer.AGUA_BIT | AbyssExplorerServer.TRAMPOLIN_BIT | AbyssExplorerServer.PINCHES_BIT | AbyssExplorerServer.ENEMIGO_BIT | AbyssExplorerServer.CHECKPOINT_BIT | AbyssExplorerServer.CHECKPOINT_ACTIVADO_BIT | AbyssExplorerServer.FIN_BIT; // Elementos con los que puede colisionar

        cuerpo.createFixture(fd).setUserData(this); // Asigna el fixture de los pies al cuerpo
        forma.dispose(); // Libera la forma del cuerpo
        pies.dispose(); // Libera la forma de los pies
    }

    // Método para actualizar el estado del marciano
    public void update(float dt) {
        actualizarEstado(); // Actualiza el estado del marciano
        if (muerto) {
            tiempoMuerto += dt; // Incrementa el tiempo muerto
            if (tiempoMuerto >= 1.0f) { // Si ha pasado 1 segundo
                reaparicion(); // Llama al método de reaparición
                tiempoMuerto = 0; // Reinicia el tiempo muerto
            }
        }
    }


    // Método para actualizar el estado del marciano según su velocidad
    private void actualizarEstado() {
        if (muerto) {
            estadoActual = EstadosMarciano.MUERTO; // Si está muerto, establece el estado a MUERTO
        } else if (cuerpo.getLinearVelocity().y > 0 || (cuerpo.getLinearVelocity().y < 0 && estadoAnterior == EstadosMarciano.SALTANDO)) {
            estadoActual = EstadosMarciano.SALTANDO; // Si se está moviendo hacia arriba o saltando, establece el estado a SALTANDO
        } else if (cuerpo.getLinearVelocity().y < 0) {
            estadoActual = EstadosMarciano.CAYENDO; // Si se está moviendo hacia abajo, establece el estado a CAYENDO
        } else if (cuerpo.getLinearVelocity().x != 0) {
            estadoActual = EstadosMarciano.ANDANDO; // Si se está moviendo horizontalmente, establece el estado a ANDANDO
        } else {
            estadoActual = EstadosMarciano.QUIETO; // Si no se está moviendo, establece el estado a QUIETO
        }
        estadoAnterior = estadoActual; // Actualiza el estado anterior
    }
    // Método para obtener el cuerpo del marciano
    public Body getCuerpo() {
        return cuerpo; // Retorna el cuerpo del marciano
    }

    // Método para hacer que el marciano salte
    public void saltar() {
        if (contSalto < 2) { // Permite saltar solo si el contador es menor a 2
            cuerpo.setLinearVelocity(cuerpo.getLinearVelocity().x, 90f); // Establece la velocidad vertical para saltar
            contSalto++; // Incrementa el contador de saltos
        }
    }
    
    public void resetContadorSalto() {
        contSalto = 0; // Reinicia el contador de saltos
    }

    // Método para manejar la caída
    public void manejarCaida() {
        if (cuerpo.getLinearVelocity().y == 0) {
            resetContadorSalto(); // Reinicia el contador si el marciano toca el suelo
        }
    }

    // Método para mover el marciano a la derecha
    public void moverDerecha() {
        cuerpo.setLinearVelocity(60f, cuerpo.getLinearVelocity().y); // Establece la velocidad horizontal hacia la derecha
        porDer = true; // Indica que el marciano está mirando a la derecha
    }

    // Método para mover el marciano a la izquierda
    public void moverIzquierda() {
        cuerpo.setLinearVelocity(-60f, cuerpo.getLinearVelocity().y); // Establece la velocidad horizontal hacia la izquierda
        porDer = false; // Indica que el marciano está mirando a la izquierda
    }

    // Método para verificar si el marciano está muerto
    public boolean isMuerto() {
        return muerto; // Retorna el estado de muerte del marciano
    }

    // Método para establecer el estado de muerte del marciano
    public void setEstaMuerto(boolean muerto) {
        this.muerto = muerto; // Establece el estado de muerte
        if (muerto) cuerpo.setLinearVelocity(0, 0); // Si está muerto, detiene el movimiento
    }

    // Método para establecer el último checkpoint alcanzado
    public void setUltimoCheckpoint(Checkpoint checkpoint) {
        this.ultimoCheckpoint = checkpoint; // Asigna el último checkpoint
    }
    
    public int getId() {
        return id; // Getter para el ID del jugador
    }

    // Método para reaparición del marciano
    public void reaparicion() {
        cuerpo.setLinearVelocity(0, 0); // Detiene el movimiento
        estadoActual = EstadosMarciano.QUIETO; // Establece el estado a QUIETO
        muerto = false; // Cambia el estado de muerte a falso
        if (ultimoCheckpoint != null && ultimoCheckpoint.isActivadoPorJugador(id)) {
            Vector2 posicion = ultimoCheckpoint.getPosicionReaparicion(); // Obtiene la posición de reaparición del checkpoint
            cuerpo.setTransform(posicion, 0); // Establece la posición del cuerpo
        } else {
            cuerpo.setTransform(new Vector2(40 / Config.PPM, 200 / Config.PPM), 0); // Establece la posición por defecto si no hay checkpoint
        }
    }

    // Método para obtener el tipo de marciano
    public String getTipoMarciano() {
        return tipoMarciano; // Retorna el tipo de marciano
    }

    // Método para obtener la posición actual del marciano
    public Vector2 getPosicion() {
        return cuerpo.getPosition(); // Retorna la posición del cuerpo del marciano
    }

    // Método para obtener el estado actual del marciano como cadena
    public String getEstado() {
        return estadoActual.toString(); // Retorna el estado actual como cadena
    }
}