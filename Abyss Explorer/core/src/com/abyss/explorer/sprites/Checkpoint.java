package com.abyss.explorer.sprites;

import com.abyss.explorer.utiles.Config;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Checkpoint extends ObjetoInteractivo {
    private boolean activado = false;
    private Vector2 posicionReaparicion; // ALMACENA LA POSICION DEL CHECKPOINT

    public Checkpoint(World mundo, TiledMap mapa, MapObject objeto) {
        super(mundo, mapa, objeto);
        fixture.setUserData(this);
        setFiltroDeCategoria(Render.app.CHECKPOINT_BIT);

        // INICIALIZAR LA POSICION DE REAPARACION CON LA PROP DEL OBJETO
        float x = (float) objeto.getProperties().get("x") - 10/ Config.PPM;
        float y = (float) objeto.getProperties().get("y") + 20 / Config.PPM;
        posicionReaparicion = new Vector2(x, y);
    }

    @Override
    public void colisionPies(Marciano marciano) {
    	// EL CHECKPOINT SE ACTIVARA CON LA COLISION Y SE MARCARÁ COMO PUNTO DE REAPARICION
        if (!activado) {
            activado = true;
            setFiltroDeCategoria(Render.app.CHECKPOINT_ACTIVADO_BIT);
            //marciano.setUltimoCheckpoint(this); // ASIGNA ESTE CHECKPOIN AL MARCIANO
            Gdx.app.log("Checkpoint", "activado en posición: " + posicionReaparicion);
        }
    }

    public boolean isActivado() {
        return activado;
    }

    public Vector2 getPosicionReaparicion() {
        return posicionReaparicion;
    }
}

