package com.abyss.explorer.sprites;

import com.abyss.explorer.utiles.Config;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Marciano extends Sprite {
	private int clienteId;
	
    private Animation<TextureRegion> marcianoAndando;
    private Animation<TextureRegion> marcianoSaltando;
    private TextureRegion marcianoQuieto;
    private TextureRegion marcianoMuerto;
    
    private String region;
    
    private EstadosMarciano estadoActual;
    private EstadosMarciano estadoAnterior;
    private float tiempoEstado = 0;
    private boolean porDer = true;

    public Marciano(TextureAtlas atlas, String region) {
        super(atlas.findRegion(region));
        this.region = region;
        setTexture(atlas, region);
        estadoActual = EstadosMarciano.QUIETO;
        estadoAnterior = EstadosMarciano.QUIETO;
        setBounds(54 / Config.PPM, 2 / Config.PPM, 24 / Config.PPM, 24 / Config.PPM);
        //setRegion(marcianoQuieto);
		setSize(26,26);
		//System.out.println("Marciano cliente");
		
    }
    
    
    private void setTexture(TextureAtlas atlas, String region) {
        Array<TextureRegion> frames = new Array<>();
        
        // Configurar las animaciones según el tipo de Marciano
        int posAndando = 0, posQuieto = 0;
        
        switch(region) {
            case "martian_green":
                posAndando = 80;
                posQuieto = 54;
                
                break;
            case "martian_pink":
                posAndando = 236;
                posQuieto = 210;
                break;
              
            case "martian_blue":
            	posAndando = 28;
            	posQuieto = 2 ;
            	break;
            	
            case "martian_orange":
            	posAndando = 158;
            	posQuieto = 184 ;
            	break;
            	
            case "martian_nude":
            	posAndando = 132;
            	posQuieto = 106 ;
            	break;
            	
        }
        //System.out.println(region + " Marciano cliente");

        // ANIMACION ANDANDO
        frames.add(new TextureRegion(getTexture(), posAndando, 2, 24, 24));
        frames.add(new TextureRegion(getTexture(), posQuieto, 2, 24, 24));
        marcianoAndando = new Animation<>(0.25f, frames, Animation.PlayMode.LOOP);
        
        frames.clear();
        
        // MARCIANO SALTANDO
        frames.add(new TextureRegion(getTexture(), posAndando, 2, 24, 24));
        marcianoSaltando = new Animation<>(0.2f, frames, Animation.PlayMode.NORMAL);
        
        // MARCIANO QUIETO Y MUERTO
        marcianoQuieto = new TextureRegion(getTexture(), posQuieto, 2, 24, 24);
        marcianoMuerto = new TextureRegion(getTexture(), posAndando, 2, 24, 24);
    }

    public void update(float dt) {
        actualizarAnimacion(dt);
    }

    private void actualizarAnimacion(float dt) {
        TextureRegion region = getFrameActual(dt);
        
        // Actualizar la dirección del sprite
        if ((!porDer) && !region.isFlipX()) {
            region.flip(true, false);
        } else if ((porDer) && region.isFlipX()) {
            region.flip(true, false);
        }

        setRegion(region);
        tiempoEstado = (estadoActual == estadoAnterior) ? tiempoEstado + dt : 0;
        estadoAnterior = estadoActual;
    }

    private TextureRegion getFrameActual(float dt) {
        TextureRegion region;
        
        switch(estadoActual) {
            case ANDANDO:
                region = marcianoAndando.getKeyFrame(tiempoEstado, true);
                break;
            case SALTANDO:
                region = marcianoSaltando.getKeyFrame(tiempoEstado, false);
                break;
            case MUERTO:
                region = marcianoMuerto;
                break;
            case CAYENDO:
                region = marcianoSaltando.getKeyFrame(tiempoEstado, false);
                break;
            case QUIETO:
            default:
                region = marcianoQuieto;
                break;
        }
        
        return region;
    }
    
    public String getRegion() {
    	return region;
    }

    public void actualizarEstado(String estado) {
        try {
            estadoActual = EstadosMarciano.valueOf(estado);
        } catch (IllegalArgumentException e) {
            estadoActual = EstadosMarciano.QUIETO;
        }
    }

    public void setDireccion(boolean haciaDerecha) {
        if (this.porDer != haciaDerecha) {
            this.porDer = haciaDerecha;
            flip(true, false);
        }
    }

    public void setEstadoActual(EstadosMarciano estado) {
        this.estadoActual = estado;
    }

    public EstadosMarciano getEstadoActual() {
        return estadoActual;
    }

    public void resetTiempoEstado() {
        tiempoEstado = 0;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }

    public void dispose() {
        // Liberar recursos si es necesario
        getTexture().dispose();
    }
}