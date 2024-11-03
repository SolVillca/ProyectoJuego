package com.abyss.explorerserver.sprites;

import com.abyss.explorerserver.pantallas.PantallaNivel;
import com.abyss.explorerserver.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;

public class Pinches extends ObjetoInteractivo {

	public Pinches(PantallaNivel pantalla, MapObject objeto) {
		super(pantalla, objeto);
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
