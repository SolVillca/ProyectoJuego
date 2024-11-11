package com.abyss.explorer.sprites;

import com.abyss.explorer.utiles.Config;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

//Clase que representa un Marciano en el juego, extendiendo Sprite
public class Marciano extends Sprite {
	private int clienteId; // ID del cliente asociado al marciano

	// Animaciones y estados del marciano
	private Animation<TextureRegion> marcianoAndando;
	private Animation<TextureRegion> marcianoSaltando;
	private TextureRegion marcianoQuieto;
	private TextureRegion marcianoMuerto;

	private String region; // Nombre de la región del marciano

	// Estados del marciano
	private EstadosMarciano estadoActual;
	private EstadosMarciano estadoAnterior;
	private float tiempoEstado = 0; // Tiempo en el estado actual
	private boolean porDer = true; // Indica si el marciano mira a la derecha


	// Constructor de la clase Marciano
	public Marciano(TextureAtlas atlas, String region) {
		super(atlas.findRegion(region)); // Inicializa el sprite con la región del atlas
		this.region = region; // Asigna la región
		setTexture(atlas, region); // Configura las texturas y animaciones
		estadoActual = EstadosMarciano.QUIETO; // Estado inicial
		estadoAnterior = EstadosMarciano.QUIETO; // Estado anterior
		setBounds(54 / Config.PPM, 2 / Config.PPM, 24 / Config.PPM, 24 / Config.PPM); // Establece los límites del sprite
		setSize(26, 26); // Establece el tamaño del sprite
	}


	// Método para configurar las texturas y animaciones del marciano
    private void setTexture(TextureAtlas atlas, String region) {
        Array<TextureRegion> frames = new Array<>(); // Array para almacenar los frames de las animaciones

        // Configurar las animaciones según el tipo de Marciano
        int posAndando = 0, posQuieto = 0; // Posiciones de las texturas

        // Asigna las posiciones de las texturas según el tipo de marciano
        switch (region) {
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
                posQuieto = 2;
                break;
            case "martian_orange":
                posAndando = 158;
                posQuieto = 184;
                break;
            case "martian_nude":
                posAndando = 132;
                posQuieto = 106;
                break;
        }

        // ANIMACIÓN ANDANDO
        frames.add(new TextureRegion(getTexture(), posAndando, 2, 24, 24)); // Frame de andar
        frames.add(new TextureRegion(getTexture(), posQuieto, 2, 24, 24)); // Frame de quieto
        marcianoAndando = new Animation<>(0.25f, frames, Animation.PlayMode.LOOP); // Crea la animación de andar

        frames.clear(); // Limpia el array de frames

        // MARCIANO SALTANDO
        frames.add(new TextureRegion(getTexture(), posAndando, 2, 24, 24)); // Frame de salto
        marcianoSaltando = new Animation<>(0.2f, frames, Animation.PlayMode.NORMAL); // Crea la animación de salto

        // MARCIANO QUIETO Y MUERTO
        marcianoQuieto = new TextureRegion(getTexture(), posQuieto, 2, 24, 24); // Frame de quieto
        marcianoMuerto = new TextureRegion(getTexture(), posAndando, 2, 24, 24); // Frame de muerto
    }

    // Método para actualizar la animación del marciano
    public void update(float dt) {
        actualizarAnimacion(dt); // Llama al método para actualizar la animación
    }

    // Método para actualizar la animación según el estado actual
    private void actualizarAnimacion(float dt) {
        TextureRegion region = getFrameActual(dt); // Obtiene el frame actual

        // Actualizar la dirección del sprite
        if ((!porDer) && !region.isFlipX()) {
            region.flip (true, false); // Voltea el sprite si está mirando a la izquierda
        } else if ((porDer) && region.isFlipX()) {
            region.flip(true, false); // Voltea el sprite si está mirando a la derecha
        }

        setRegion(region); // Establece la región del sprite
        tiempoEstado = (estadoActual == estadoAnterior) ? tiempoEstado + dt : 0; // Actualiza el tiempo en el estado actual
        estadoAnterior = estadoActual; // Actualiza el estado anterior
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

	// Método para obtener el nombre de la región del marciano
    public String getRegion() {
        return region; // Retorna el nombre de la región
    }

    // Método para actualizar el estado del marciano
    public void actualizarEstado(String estado) {
        try {
            estadoActual = EstadosMarciano.valueOf(estado); // Intenta establecer el nuevo estado
        } catch (IllegalArgumentException e) {
            estadoActual = EstadosMarciano.QUIETO; // Si falla, establece el estado a QUIETO
        }
    }
    
    // Método para obtener la direccion actual del marciano
    public boolean getDireccion() {
    	return porDer;
    }

    // Método para establecer la dirección del marciano
    public void setDireccion(boolean haciaDerecha) {
        if (this.porDer != haciaDerecha) {
            this.porDer = haciaDerecha; // Actualiza la dirección
            flip(true, false); // Voltea el sprite
        }
    }

    // Método para obtener el estado actual del marciano
    public EstadosMarciano getEstadoActual() {
    	return estadoActual; // Retorna el estado actual
    }
    
    // Método para establecer el estado actual del marciano
    public void setEstadoActual(EstadosMarciano estado) {
        this.estadoActual = estado; // Establece el estado actual
    }


 // Método para reiniciar el tiempo en el estado actual
    public void resetTiempoEstado() {
        tiempoEstado = 0; // Reinicia el tiempo
    }

    // Método para establecer la posición del marciano
    public void setPosition(float x, float y) {
        super.setPosition(x - getWidth() / 2, y - getHeight() / 2); // Centra el sprite en la posición
    }

    // Método para liberar recursos
    public void dispose() {
        getTexture().dispose(); // Libera la textura del marciano
    }
}