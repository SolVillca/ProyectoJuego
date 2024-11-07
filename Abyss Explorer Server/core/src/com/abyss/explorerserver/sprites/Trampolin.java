package com.abyss.explorerserver.sprites;

import com.abyss.explorerserver.utiles.Render;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

public class Trampolin extends ObjetoInteractivo{

	public Trampolin(World mundo, TiledMap mapa, MapObject objeto) {
        super(mundo, mapa, objeto);
        fixture.setUserData(this);
        setFiltroDeCategoria(Render.app.TRAMPOLIN_BIT);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void colisionPies(Marciano marciano) {
		// TODO Auto-generated method stub
		marciano.cuerpo.setLinearVelocity(marciano.cuerpo.getLinearVelocity().x, 180f);
	}

}
