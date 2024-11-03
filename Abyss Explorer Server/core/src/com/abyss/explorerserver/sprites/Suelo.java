package com.abyss.explorerserver.sprites;

import com.abyss.explorerserver.pantallas.PantallaNivel;
import com.abyss.explorerserver.utiles.Render;
import com.badlogic.gdx.maps.MapObject;

public class Suelo extends ObjetoInteractivo{
	public Suelo (PantallaNivel pantalla, MapObject objeto) {
		super(pantalla, objeto);
		fixture.setUserData(this);
		setFiltroDeCategoria(Render.app.DEFAULT_BIT);
	}

	@Override
	public void colisionPies(Marciano marciano) {
		// TODO Auto-generated method stub
		//Gdx.app.log("Suelo", "Colision");
	}
}

