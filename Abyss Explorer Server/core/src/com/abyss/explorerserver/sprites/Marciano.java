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

public class Marciano {
    public World mundo;
    public Body cuerpo;
    private String tipoMarciano;
    public EstadosMarciano estadoActual;
    public EstadosMarciano estadoAnterior;
    private boolean porDer = true;
    private boolean muerto = false;
    private float tiempoMuerto = 0;
    private Checkpoint ultimoCheckpoint;
    private int contSalto = 0;

    public Marciano(PantallaNivel pantalla, String tipoMarciano, float x, float y, float ancho, float alto) {
        this.mundo = pantalla.getMundo();
        this.tipoMarciano = tipoMarciano;
        estadoActual = EstadosMarciano.QUIETO;
        estadoAnterior = EstadosMarciano.QUIETO;
        System.out.println("marciano");
        crearCuerpo(x, y, ancho, alto);
    }

    private void crearCuerpo(float x, float y, float ancho, float alto) {
    	// DEFINICIÓN DE LAS PROPIEDADES DEL CUERPO
	    BodyDef bd = new BodyDef();
	    bd.position.set(40, 200); // POSICIÓN INICIAL DEL CUERPO EN EL MUNDO
	    bd.type = BodyDef.BodyType.DynamicBody; // TIPO DE CUERPO, AFECTADO POR GRAVEDAD Y COLISIONES
	    cuerpo = mundo.createBody(bd); // CREO EL CUERPO EN EL MUNDO BOX2D
	    
	    
	    // DEFINICIÓN DEL FIXTURE (FORMA) DEL CUERPO
	    FixtureDef fd = new FixtureDef();
	    
	    PolygonShape forma = new PolygonShape(); // FORMA DEL CUERPO
	    forma.setAsBox(10 / Config.PPM, 13 / Config.PPM); // DIMENSIONES 10 y 13
	    fd.filter.categoryBits = AbyssExplorerServer.MARCIANO_BIT; // CATEGORÍA DEL MARCIANO
	    fd.filter.maskBits = AbyssExplorerServer.DEFAULT_BIT  | AbyssExplorerServer.TRAMPOLIN_BIT | AbyssExplorerServer.PINCHES_BIT | AbyssExplorerServer.ENEMIGO_BIT | AbyssExplorerServer.CHECKPOINT_BIT | AbyssExplorerServer.CHECKPOINT_ACTIVADO_BIT | AbyssExplorerServer.FIN_BIT; // ELEMENTOS CON LOS QUE PUEDE COLISIONAR
	    
	    fd.shape = forma;
	    cuerpo.createFixture(fd).setUserData(this); // ASIGNAR EL FIXTURE AL CUERPO

	    
	    // CREACION DE UN SENSOR PARA LOS "PIES" DEL MARCIANO Y DEFINICION DE LOS BORDES (verificar)
	    EdgeShape pies = new EdgeShape();
	    pies.set(new Vector2(-7 / Config.PPM, -13 / Config.PPM), new Vector2(7 / Config.PPM, -13 / Config.PPM)); // BORDE DE LOS PIES
	    
	    fd.shape = pies;
	    fd.isSensor = true; // LOS PIES SON UN SENSOR PARA DETECTAR TRAMPOLINES, PINCHES, ETC
	    fd.filter.categoryBits =  AbyssExplorerServer.MARCIANO_PIES_BIT;
	    fd.filter.maskBits = AbyssExplorerServer.DEFAULT_BIT | AbyssExplorerServer.AGUA_BIT | AbyssExplorerServer.TRAMPOLIN_BIT | AbyssExplorerServer.PINCHES_BIT | AbyssExplorerServer.ENEMIGO_BIT | AbyssExplorerServer.CHECKPOINT_BIT | AbyssExplorerServer.CHECKPOINT_ACTIVADO_BIT  | AbyssExplorerServer.FIN_BIT; // ELEMENTOS CON LOS QUE PUEDE COLISIONAR
	    
	    
	    cuerpo.createFixture(fd).setUserData(this);
	    //System.out.println(" Marciano server, estructuraCuerpo");
        forma.dispose();
        pies.dispose();
    }

    public void update(float dt) {
        actualizarEstado();
        if (muerto) {
            tiempoMuerto += dt;
            if (tiempoMuerto >= 1.0f) {
                reaparicion();
                tiempoMuerto = 0;
            }
        }
    }

    private void actualizarEstado() {
        if (muerto) {
            estadoActual = EstadosMarciano.MUERTO;
        } else if (cuerpo.getLinearVelocity().y > 0 || (cuerpo.getLinearVelocity().y < 0 && estadoAnterior == EstadosMarciano.SALTANDO)) {
            estadoActual = EstadosMarciano.SALTANDO;
        } else if (cuerpo.getLinearVelocity().y < 0) {
            estadoActual = EstadosMarciano.CAYENDO;
        } else if (cuerpo.getLinearVelocity().x != 0) {
            estadoActual = EstadosMarciano.ANDANDO;
        } else {
            estadoActual = EstadosMarciano.QUIETO;
        }
        estadoAnterior = estadoActual;
        //System.out.println(estadoAnterior + "   " + estadoActual);
        
    }

    public Body getCuerpo() {
		return cuerpo;
	}

	public void saltar() {
        if (estadoActual != EstadosMarciano.SALTANDO && estadoActual != EstadosMarciano.CAYENDO) {
        	cuerpo.setLinearVelocity(cuerpo.getLinearVelocity().x, 90f);
        }
    }

    public void moverDerecha() {
        cuerpo.setLinearVelocity(60f, cuerpo.getLinearVelocity().y);
        porDer = true;
        //System.out.println("entro marciano mover derecha");
    }

    public void moverIzquierda() {
        cuerpo.setLinearVelocity(-60f, cuerpo.getLinearVelocity().y);
        porDer = false;
    }

    public boolean isMuerto() {
        return muerto;
    }

    public void setEstaMuerto(boolean muerto) {
        this.muerto = muerto;
        if (muerto) cuerpo.setLinearVelocity(0, 0);
    }

    public void setUltimoCheckpoint(Checkpoint checkpoint) {
        ultimoCheckpoint = checkpoint;
    }

    public void reaparicion() {
        cuerpo.setLinearVelocity(0, 0);
        estadoActual = EstadosMarciano.QUIETO;
        muerto = false;
        if (ultimoCheckpoint != null && ultimoCheckpoint.isActivado()) {
            Vector2 posicion = ultimoCheckpoint.getPosicionReaparicion();
            cuerpo.setTransform(posicion, 0);
        } else {
            cuerpo.setTransform(new Vector2(40 / Config.PPM, 200 / Config.PPM), 0);
        }
    }

    public String getTipoMarciano() {
        return tipoMarciano;
    }

    public Vector2 getPosicion() {
        return cuerpo.getPosition();
    }

    public String getEstado() {
        return estadoActual.toString();
    }
}