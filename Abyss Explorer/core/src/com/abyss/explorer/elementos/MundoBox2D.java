package com.abyss.explorer.elementos;

import com.abyss.explorer.pantallas.PantallaNivel;
import com.abyss.explorer.sprites.Agua;
import com.abyss.explorer.sprites.Checkpoint;
import com.abyss.explorer.sprites.Llave;
import com.abyss.explorer.sprites.Pinches;
import com.abyss.explorer.sprites.Suelo;
import com.abyss.explorer.sprites.Trampolin;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.abyss.explorer.utiles.Config;

public class MundoBox2D {

    public MundoBox2D(PantallaNivel pantalla) {
    	
    	//COMPROBACION DE CARGA Y EXISTENCIA CORRECTA DEL MAPA ESTA EN LA CLASE PANTALLA NIVEL
    	
        //World mundo = pantalla.getMundo();
        TiledMap mapa = pantalla.getMapa();
       

        // CREACION DE CUERPO Y VARIABLES DE FIXTURES
        /*BodyDef bd = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fd = new FixtureDef();
        Body cuerpo;

        // ITERACION SOBRE LOS OBJETOS DE LA CAPA SUELO 
        for (MapObject objecto : mapa.getLayers().get(2).getObjects()) {
        	
        	// MANEJO DE OBJETOS DE TIPO RECTANGULO
            if (objecto instanceof RectangleMapObject) {
                Rectangle rectangulo = ((RectangleMapObject) objecto).getRectangle();
                bd.type = BodyDef.BodyType.StaticBody;
                bd.position.set((rectangulo.getX() + rectangulo.getWidth() / 2) / Config.PPM, 
                                (rectangulo.getY() + rectangulo.getHeight() / 2) / Config.PPM);

                cuerpo = mundo.createBody(bd);

                shape.setAsBox(rectangulo.getWidth() / 2 / Config.PPM, 
                               rectangulo.getHeight() / 2 / Config.PPM);
                fd.shape = shape;
                fd.density = 1.0f;
                cuerpo.createFixture(fd);
                
            // MANEJO DE OBJETOS DE TIPO POLIGONO
            } else if (objecto instanceof PolygonMapObject) {
                PolygonMapObject polygonObject = (PolygonMapObject) objecto;
                bd.type = BodyDef.BodyType.StaticBody;

                PolygonShape polygonShape = new PolygonShape();
                float[] vertices = polygonObject.getPolygon().getTransformedVertices();
                float[] worldVertices = new float[vertices.length];
                for (int i = 0; i < vertices.length; ++i) {
                    worldVertices[i] = vertices[i] / Config.PPM;
                }
                polygonShape.set(worldVertices);

                bd.position.set(polygonObject.getPolygon().getX() / 2 / Config.PPM, 
                                polygonObject.getPolygon().getY() / 2 / Config.PPM);
                cuerpo = mundo.createBody(bd);

                fd.shape = polygonShape;
                cuerpo.createFixture(fd);
            }
        }*/

        // ITERACION PARA LA CREACION DE CADA OBJETO DE UNA CAPA
       
        // CAPA OBJETOS SUELO
        for (MapObject objeto : mapa.getLayers().get("suelo").getObjects().getByType(RectangleMapObject.class)) { //Obtiene los obj. principales de tipo de rectangulo
        	new Suelo(pantalla, objeto);
        }
        
        
        // CAPA OBJETOS AGUA
        for (MapObject objeto : mapa.getLayers().get("agua").getObjects().getByType(RectangleMapObject.class)) { 
            new Agua(pantalla, objeto);
        }
        
        // CAPA OBJETOS PINCHES
        for (MapObject objeto : mapa.getLayers().get("pinches").getObjects().getByType(RectangleMapObject.class)) {
            new Pinches(pantalla, objeto);
        }
        
        // CAPA OBJETOS CHECKPOINT
        for (MapObject objeto : mapa.getLayers().get("checkpoint").getObjects().getByType(RectangleMapObject.class)) {
        	new Checkpoint(pantalla, objeto);
        }
        
        // CAPA OBJETOS TRAMPOLIN
        for (MapObject objeto : mapa.getLayers().get("trampolin").getObjects().getByType(RectangleMapObject.class)) {
            new Trampolin(pantalla, objeto);
        }
        
     // CAPA OBJETOS TRAMPOLIN
        for (MapObject objeto : mapa.getLayers().get("llave").getObjects().getByType(RectangleMapObject.class)) {
            new Llave(pantalla, objeto);
        }
         

    }
}
