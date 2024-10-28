package com.abyss.explorer.sprites;

import com.abyss.explorer.pantallas.PantallaNivel;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Gdx;
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

