package com.abyss.explorerserver.sprites;

import java.util.HashSet;
import java.util.Set;

import com.abyss.explorerserver.utiles.Config;
import com.abyss.explorerserver.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Checkpoint extends ObjetoInteractivo {
    private boolean activado = false;
    private Set<Integer> jugadoresActivados; // Conjunto para rastrear qué jugadores han activado este checkpoint
    private Vector2 posicionReaparicion; // ALMACENA LA POSICION DEL CHECKPOINT

    public Checkpoint(World mundo, TiledMap mapa, MapObject objeto) {
        super(mundo, mapa, objeto);
        jugadoresActivados = new HashSet<>(); // Inicializa el conjunto
        fixture.setUserData(this);
        setFiltroDeCategoria(Render.app.CHECKPOINT_BIT);

        // INICIALIZAR LA POSICION DE REAPARACION CON LA PROP DEL OBJETO
        float x = (float) objeto.getProperties().get("x") - 10/ Config.PPM;
        float y = (float) objeto.getProperties().get("y") + 20 / Config.PPM;
        posicionReaparicion = new Vector2(x, y);
    }

    @Override
    public void colisionPies(Marciano marciano) {
        // Activa el checkpoint solo si no ha sido activado por este jugador
        if (!jugadoresActivados.contains(marciano.getId())) {
            activado = true;
            jugadoresActivados.add(marciano.getId()); // Agrega el ID del jugador al conjunto
            setFiltroDeCategoria(Render.app.CHECKPOINT_ACTIVADO_BIT);
            marciano.setUltimoCheckpoint(this); // Asigna este checkpoint al jugador
            Gdx.app.log("Checkpoint", "activado en la posición: " + posicionReaparicion);
        }
    }

    public boolean isActivadoPorJugador(int jugadorId) {
        return jugadoresActivados.contains(jugadorId); // Verifica si el checkpoint fue activado por el jugador
    }

    public Vector2 getPosicionReaparicion() {
        return posicionReaparicion; // Retorna la posición de reaparición
    }
}

