package com.abyss.explorer.sprites;

import com.abyss.explorer.pantallas.PantallaNivel;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;

public class Checkpoint extends ObjetoInteractivo {
    private boolean activado;

    public Checkpoint(PantallaNivel pantalla, MapObject objeto) {
        super(pantalla, objeto);
        fixture.setUserData(this);
        setFiltroDeCategoria(Render.app.CHECKPOINT_BIT);

        activado = false;
    }

    @Override
    public void colisionPies(Marciano marciano) {
        if (!activado) {
            activado = true; // Cambiar el estado del checkpoint a activado
            setFiltroDeCategoria(Render.app.CHECKPOINT_ACTIVADO_BIT); // Cambiamos la máscara de colisión
            Gdx.app.log("Checkpoint", "activado");
        }
    }

}

