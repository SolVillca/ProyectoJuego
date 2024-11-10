/*package com.abyss.explorer.elementos;

import java.util.HashMap;
import java.util.Map;

import com.abyss.explorer.pantallas.PantallaNivel;
import com.abyss.explorer.sprites.Agua;
import com.abyss.explorer.sprites.Checkpoint;
import com.abyss.explorer.sprites.Llave;
import com.abyss.explorer.sprites.ObjetoInteractivo;
import com.abyss.explorer.sprites.Pinches;
import com.abyss.explorer.sprites.Suelo;
import com.abyss.explorer.sprites.Trampolin;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

public class MundoBox2D { 
    private World mundo;
    private TiledMap mapa;
    private Map<String, ObjetoInteractivo> objetosInteractivos;

    public MundoBox2D(PantallaNivel pantalla) {
        this.mundo = pantalla.getMundo();
        this.mapa = pantalla.getMapa();
        this.objetosInteractivos = new HashMap<>();

        crearObjetos();
    }

    private void crearObjetos() {
        crearObjetosDeCapa("suelo", (objeto) -> new Suelo(mundo, mapa, objeto));
        crearObjetosDeCapa("agua", (objeto) -> new Agua(mundo, mapa, objeto));
        crearObjetosDeCapa("pinches", (objeto) -> new Pinches(mundo, mapa, objeto));
        crearObjetosDeCapa("checkpoint", (objeto) -> new Checkpoint(mundo, mapa, objeto));
        crearObjetosDeCapa("trampolin", (objeto) -> new Trampolin(mundo, mapa, objeto));
        crearObjetosDeCapa("llave", (objeto) -> new Llave(mundo, mapa, objeto));
    }

    private void crearObjetosDeCapa(String nombreCapa, ObjetoCreador creador) {
        if (mapa.getLayers().get(nombreCapa) != null) {
            for (MapObject objeto : mapa.getLayers().get(nombreCapa).getObjects().getByType(RectangleMapObject.class)) {
                ObjetoInteractivo nuevoObjeto = creador.crear(objeto);
                objetosInteractivos.put(objeto.getName(), nuevoObjeto);
            }
        } else {
            System.out.println("Advertencia: No se encontró la capa " + nombreCapa);
        }
    }

    @FunctionalInterface
    private interface ObjetoCreador {
        ObjetoInteractivo crear(MapObject objeto);
    }

    public void actualizarEstadoObjeto(String nombreObjeto, boolean activo) {
        ObjetoInteractivo objeto = objetosInteractivos.get(nombreObjeto);
        if (objeto != null) {
            objeto.setActivo(activo);
        }
    }

    public void dispose() {
        for (ObjetoInteractivo objeto : objetosInteractivos.values()) {
            objeto.dispose();
        }
        objetosInteractivos.clear();
    }
}*/