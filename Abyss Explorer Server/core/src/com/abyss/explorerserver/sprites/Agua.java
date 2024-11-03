package com.abyss.explorerserver.sprites;

import com.abyss.explorerserver.pantallas.PantallaNivel;
import com.abyss.explorerserver.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;

public class Agua extends ObjetoInteractivo {
	public Agua (PantallaNivel pantalla, MapObject objeto) {
		super(pantalla, objeto);
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
