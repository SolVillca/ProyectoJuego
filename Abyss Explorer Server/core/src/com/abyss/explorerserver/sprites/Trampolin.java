package com.abyss.explorerserver.sprites;

import com.abyss.explorerserver.pantallas.PantallaNivel;
import com.abyss.explorerserver.utiles.Render;
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
