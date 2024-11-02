package com.abyss.explorer.sprites;

import com.abyss.explorer.AbyssExplorer;
import com.abyss.explorer.pantallas.PantallaNivel;
import com.abyss.explorer.utiles.Config;
import com.abyss.explorer.utiles.Recursos;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Marciano extends Sprite {
	
	public World mundo;
	public Body cuerpo;
	
	//VARIABLE DONDE SE ALMACENARÁ EL CONJUNTO DE SPRITES
	private TextureRegion marciano;
	
	
	//MOVIMIENTOS MARCIANO
	private Animation<TextureRegion> marcianoAndando;
	private Animation<TextureRegion> marcianoSaltando;
	private TextureRegion marcianoQuieto;
	private TextureRegion marcianoMuerto;
	
	// ESTADO MARCIANO
	public EstadosMarciano estadoActual;
	public EstadosMarciano estadoAnterior;
	

	
	private float tiempoEstado = 0;
	private boolean porDer = true;
	
	private boolean muerto = false;
	public boolean estaMuriendo = false;
	private float tiempoMuerto = 0;
	
	private Checkpoint ultimoCheckpoint;
	
	
	public Marciano (PantallaNivel pantalla, String region, int posAndando, int posQuieto) {
	
		
		super(pantalla.getAtlasJugador().findRegion(region));
		this.mundo = pantalla.getMundo();
		
		//DEFINIR EL ESTADO DEL MARCIANO
		estadoActual = EstadosMarciano.QUIETO;
		estadoAnterior = EstadosMarciano.QUIETO;
		
		setTexture(posAndando, posQuieto);

		// CREACION DEL CUERPO DEL MARCIANO, CONFIGURACION Y COLISION
		estructuraMarciano();
		setBounds(54 / Config.PPM, 2 / Config.PPM, 24 / Config.PPM, 24 / Config.PPM);
		setRegion(marcianoQuieto);
		setSize(26,26);
		
	}
	
	private void setTexture(int posQuieto, int posAndando ) {
		Array<TextureRegion> frames = new Array <TextureRegion>();
		
		// ANIMACION ANDANDO
		frames.add(new TextureRegion(getTexture(), posAndando, 2, 24, 24));
		frames.add(new TextureRegion(getTexture(), posQuieto, 2, 24, 24));
        marcianoAndando = new Animation<>(0.25f, frames, Animation.PlayMode.LOOP);
        
        frames.clear();
        
        // MARCIANO SALTANDO
        frames.add(new TextureRegion(getTexture(), posAndando, 2, 24, 24));
        marcianoSaltando = new Animation<>(0.2f, frames, Animation.PlayMode.NORMAL);
        frames.clear();
        // MARCIANO QUIETO
        marcianoQuieto = new TextureRegion(getTexture(), posQuieto, 2, 24, 24);
        
        //Marciano muerto (faltaaaa) ¡!
        marcianoMuerto = new TextureRegion(getTexture(), posAndando, 2, 24, 24);
	}
	
	private void estructuraMarciano() {
	    // DEFINICIÓN DE LAS PROPIEDADES DEL CUERPO
	    BodyDef bd = new BodyDef();
	    bd.position.set(40, 200); // POSICIÓN INICIAL DEL CUERPO EN EL MUNDO
	    bd.type = BodyDef.BodyType.DynamicBody; // TIPO DE CUERPO, AFECTADO POR GRAVEDAD Y COLISIONES
	    cuerpo = mundo.createBody(bd); // CREO EL CUERPO EN EL MUNDO BOX2D
	    
	    
	    // DEFINICIÓN DEL FIXTURE (FORMA) DEL CUERPO
	    FixtureDef fd = new FixtureDef();
	    
	    PolygonShape forma = new PolygonShape(); // FORMA DEL CUERPO
	    forma.setAsBox(10 / Config.PPM, 13 / Config.PPM); // DIMENSIONES 10 y 13
	    fd.filter.categoryBits = AbyssExplorer.MARCIANO_BIT; // CATEGORÍA DEL MARCIANO
	    fd.filter.maskBits = AbyssExplorer.DEFAULT_BIT  | AbyssExplorer.TRAMPOLIN_BIT | AbyssExplorer.PINCHES_BIT | AbyssExplorer.ENEMIGO_BIT | AbyssExplorer.CHECKPOINT_BIT | AbyssExplorer.CHECKPOINT_ACTIVADO_BIT | AbyssExplorer.FIN_BIT; // ELEMENTOS CON LOS QUE PUEDE COLISIONAR
	    
	    fd.shape = forma;
	    cuerpo.createFixture(fd).setUserData(this); // ASIGNAR EL FIXTURE AL CUERPO

	    
	    // CREACION DE UN SENSOR PARA LOS "PIES" DEL MARCIANO Y DEFINICION DE LOS BORDES (verificar)
	    EdgeShape pies = new EdgeShape();
	    pies.set(new Vector2(-7 / Config.PPM, -13 / Config.PPM), new Vector2(7 / Config.PPM, -13 / Config.PPM)); // BORDE DE LOS PIES
	    
	    fd.shape = pies;
	    fd.isSensor = true; // LOS PIES SON UN SENSOR PARA DETECTAR TRAMPOLINES, PINCHES, ETC
	    fd.filter.categoryBits = Render.app.MARCIANO_PIES_BIT;
	    fd.filter.maskBits = AbyssExplorer.DEFAULT_BIT | AbyssExplorer.AGUA_BIT | AbyssExplorer.TRAMPOLIN_BIT | AbyssExplorer.PINCHES_BIT | AbyssExplorer.ENEMIGO_BIT | AbyssExplorer.CHECKPOINT_BIT | AbyssExplorer.CHECKPOINT_ACTIVADO_BIT  | AbyssExplorer.FIN_BIT; // ELEMENTOS CON LOS QUE PUEDE COLISIONAR
	    
	    
	    cuerpo.createFixture(fd).setUserData(this);
	}
	
	private TextureRegion getFrame(float dt) {
		
		estadoActual = getEstado();
		TextureRegion region = marcianoQuieto;
		// DEFINE LA ANIMACION O TEXTURA DEPENDIENDO DEL ESTADO DEL MARCIANO
		switch(estadoActual) {
			case QUIETO:
				region = marcianoQuieto;
				break;
			case MUERTO:
				region = marcianoMuerto;
				break;
			case ANDANDO:
				region = (TextureRegion) marcianoAndando.getKeyFrame(tiempoEstado, true);
				break;
			case SALTANDO:
				region = (TextureRegion) marcianoSaltando.getKeyFrame(tiempoEstado);
				break;
			case CAYENDO:
				break;
		}
		
		//REALIZAR MOVIMIENTO DE ANDANDO DEPENDIENDO IZQ O DER
		if((cuerpo.getLinearVelocity().x < 0 || !porDer) && region.isFlipX()) {
			region.flip(true, false);
			porDer = false;
		} else if ((cuerpo.getLinearVelocity().x > 0 || porDer) && !region.isFlipX()) {
			region.flip(true, false);
			porDer = true;
		}
		
		tiempoEstado = (estadoActual == estadoAnterior)? tiempoEstado + dt: 0;
		estadoAnterior = estadoActual;
		return region;
	}
	
	private EstadosMarciano getEstado() {
		//System.out.println("Estado actual: " + estadoActual);
		if(muerto)
			return EstadosMarciano.MUERTO;
		
		else if(cuerpo.getLinearVelocity().y > 0 || (cuerpo.getLinearVelocity().y < 0 && estadoAnterior == EstadosMarciano.SALTANDO)) 
			return EstadosMarciano.SALTANDO;
		
		else if(cuerpo.getLinearVelocity().y < 0)
			return EstadosMarciano.CAYENDO;
		
		else if(cuerpo.getLinearVelocity().x != 0)
			return EstadosMarciano.ANDANDO;
		
		else 
			return EstadosMarciano.QUIETO;
	}
	
	public void update (float dt) {
		
		setPosition(cuerpo.getPosition().x - getWidth() / 2, cuerpo.getPosition().y - getHeight() / 2);
		setRegion(getFrame(dt));
				
		 if(muerto) {
			 tiempoMuerto += dt;
			 
			 if (tiempoMuerto >= 1.0f) { //TIEMPO DE ESPERA ANTES DE REAPARECER
				 reaparicion();
	             tiempoMuerto = 0;
	         }
			 
	     } else {
	         setPosition(cuerpo.getPosition().x - getWidth() / 2, cuerpo.getPosition().y - getHeight() / 2);
	     }
		 
		 setRegion(getFrame(dt));
		
		
	}
	
	//METODOS PARA LA SECUENCIA DE MUERTE DEL PERSONAJE
	public boolean isMuerto(){ 
		return muerto; 
	}

	public void setEstaMuerto(boolean muerto) {
		this.muerto = muerto;
		if(muerto) cuerpo.setLinearVelocity(0, 0);
	}
			

	public void setUltimoCheckpoint(Checkpoint checkpoint) {
		ultimoCheckpoint = checkpoint;
	}

	public void reaparicion() {
		cuerpo.setLinearVelocity(0, 0);
		estadoActual = EstadosMarciano.QUIETO;
		muerto = false;
		estaMuriendo = false;

		// Si hay un checkpoint activado, reaparace en su posición; si no, usa la posición inicial
		if (ultimoCheckpoint != null && ultimoCheckpoint.isActivado()) {
			Vector2 posicion = ultimoCheckpoint.getPosicionReaparicion();
			cuerpo.setTransform(posicion, 0);
		} else {
			cuerpo.setTransform(new Vector2(40 / Config.PPM, 200 / Config.PPM), 0);
		}
	}
}
