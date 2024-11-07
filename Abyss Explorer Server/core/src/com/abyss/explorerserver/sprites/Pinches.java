package com.abyss.explorerserver.sprites;

import com.abyss.explorerserver.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

public class Pinches extends ObjetoInteractivo {

	public Pinches(World mundo, TiledMap mapa, MapObject objeto) {
        super(mundo, mapa, objeto);
        fixture.setUserData(this);
        setFiltroDeCategoria(Render.app.PINCHES_BIT);
	}

	@Override
	public void colisionPies(Marciano marciano) {
		// TODO Auto-generated method stub
		Gdx.app.log("Pinches", "Colision");
		marciano.setEstaMuerto(true);
	}


}
