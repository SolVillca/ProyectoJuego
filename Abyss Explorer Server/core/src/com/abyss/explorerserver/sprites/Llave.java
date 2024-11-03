package com.abyss.explorerserver.sprites;

import com.abyss.explorerserver.pantallas.PantallaNivel;
import com.abyss.explorerserver.utiles.Global;
import com.abyss.explorerserver.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;

public class Llave extends ObjetoInteractivo{

	public Llave(PantallaNivel pantalla, MapObject objeto) {
		super(pantalla, objeto);
		fixture.setUserData(this);
		setFiltroDeCategoria(Render.app.FIN_BIT);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void colisionPies(Marciano marciano) {
		Gdx.app.log("Colision", "Llave");
		objeto.setVisible(false);
		Global.finJuego = true;
		
		
	}

}
