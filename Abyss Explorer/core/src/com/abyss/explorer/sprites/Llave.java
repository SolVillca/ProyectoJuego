package com.abyss.explorer.sprites;

import com.abyss.explorer.utiles.Global;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

public class Llave extends ObjetoInteractivo{

	public Llave(World mundo, TiledMap mapa, MapObject objeto) {
        super(mundo, mapa, objeto);
        fixture.setUserData(this);
        setFiltroDeCategoria(Render.app.FIN_BIT);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void colisionPies(Marciano marciano) {
		Gdx.app.log("Colision", "Llave");
		//objeto.setVisible(false);
		Global.finJuego = true;
		
		
	}

}
