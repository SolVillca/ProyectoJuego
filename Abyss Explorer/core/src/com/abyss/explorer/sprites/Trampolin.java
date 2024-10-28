package com.abyss.explorer.sprites;

import com.abyss.explorer.pantallas.PantallaNivel;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.maps.MapObject;

public class Trampolin extends ObjetoInteractivo{

	public Trampolin(PantallaNivel pantalla, MapObject objeto) {
		super(pantalla, objeto);
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
