package com.abyss.explorerserver.sprites;

import com.abyss.explorerserver.utiles.Render;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

public class Suelo extends ObjetoInteractivo {
    public Suelo(World mundo, TiledMap mapa, MapObject objeto) {
        super(mundo, mapa, objeto);
        fixture.setUserData(this);
        setFiltroDeCategoria(Render.app.DEFAULT_BIT);
    }

    @Override
    public void colisionPies(Marciano marciano) {
        // Implementación de la colisión
    }
}
