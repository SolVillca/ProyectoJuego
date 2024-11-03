package com.abyss.explorerserver.sprites;

import com.abyss.explorerserver.pantallas.PantallaNivel;
import com.abyss.explorerserver.utiles.Config;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class ObjetoInteractivo {
	
	protected World mundo;
	protected TiledMap mapa;
	protected Rectangle rectangulo;
	protected Body cuerpo;
	protected Fixture fixture;
	
	protected PantallaNivel pantalla;
	protected MapObject objeto;
	
	public ObjetoInteractivo(PantallaNivel pantalla, MapObject objeto) {
		this.objeto = objeto;
		this.pantalla = pantalla;
		this.mundo = pantalla.getMundo();
		this.mapa = pantalla.getMapa();
		this.rectangulo = ((RectangleMapObject) objeto).getRectangle();
		
		crearObjeto();
		
	}

	public void crearObjeto() {
		// CREACION DE CUERPO Y VARIABLES DE FIXTURES
		BodyDef bd = new BodyDef();
		FixtureDef fd = new FixtureDef();
		PolygonShape forma = new PolygonShape();
		
		//DEFINICION DE TIPO DE CUERPO Y POSICION
		bd.type = BodyDef.BodyType.StaticBody;
		bd.position.set((rectangulo.getX() + rectangulo.getWidth() / 2) / Config.PPM , (rectangulo.getY() + rectangulo.getHeight() / 2) / Config.PPM);
		
		cuerpo = mundo.createBody(bd);
		
		forma.setAsBox(rectangulo.getWidth() / 2 / Config.PPM, rectangulo.getHeight() / 2 / Config.PPM);
		fd.shape = forma;
		fixture = cuerpo.createFixture(fd);
		
	}
	
	public abstract void colisionPies(Marciano marciano);
	
	public void setFiltroDeCategoria(short filBit) {
		Filter filter = new Filter();
		filter.categoryBits = filBit;
		fixture.setFilterData(filter);
	}
	
}
