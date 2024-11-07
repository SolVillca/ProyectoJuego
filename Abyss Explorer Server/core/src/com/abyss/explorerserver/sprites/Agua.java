package com.abyss.explorerserver.sprites;

import com.abyss.explorerserver.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

public class Agua extends ObjetoInteractivo {
	public Agua (World mundo, TiledMap mapa, MapObject objeto) {
        super(mundo, mapa, objeto);
        fixture.setUserData(this);
        setFiltroDeCategoria(Render.app.AGUA_BIT);
	}

	@Override
	public void colisionPies(Marciano marciano) {
		// TODO Auto-generated method stub
		Gdx.app.log("Agua", "Colision");
		marciano.setEstaMuerto(true);
	}
}
