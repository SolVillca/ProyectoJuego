package com.abyss.explorerserver.elementos;

import java.util.HashMap;
import java.util.Map;

// Importaciones necesarias para la clase MundoBox2D
import com.abyss.explorerserver.pantallas.PantallaNivel;
import com.abyss.explorerserver.sprites.Agua;
import com.abyss.explorerserver.sprites.Checkpoint;
import com.abyss.explorerserver.sprites.Llave;
import com.abyss.explorerserver.sprites.ObjetoInteractivo;
import com.abyss.explorerserver.sprites.Pinches;
import com.abyss.explorerserver.sprites.Suelo;
import com.abyss.explorerserver.sprites.Trampolin;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

// Clase que representa el mundo de Box2D y gestiona los objetos interactivos
public class MundoBox2D {
    private World mundo; // Mundo de Box2D
    private TiledMap mapa; // Mapa del juego
    private Map<String, ObjetoInteractivo> objetosInteractivos; // Mapa de objetos interactivos

    // Constructor de la clase MundoBox2D
    public MundoBox2D(PantallaNivel pantalla) {
        this.mundo = pantalla.getMundo(); // Obtiene el mundo de Box2D de la pantalla
        this.mapa = pantalla.getMapa(); // Obtiene el mapa de la pantalla
        this.objetosInteractivos = new HashMap<>(); // Inicializa el mapa de objetos interactivos

        crearObjetos(); // Crea los objetos interactivos en el mundo
    }

    // Método para crear los objetos interactivos a partir de las capas del mapa
    private void crearObjetos() {
        // Crea objetos de diferentes tipos a partir de las capas del mapa
        crearObjetosDeCapa("suelo", (objeto) -> new Suelo(mundo, mapa, objeto));
        crearObjetosDeCapa("agua", (objeto) -> new Agua(mundo, mapa, objeto));
        crearObjetosDeCapa("pinches", (objeto) -> new Pinches(mundo, mapa, objeto));
        crearObjetosDeCapa("checkpoint", (objeto) -> new Checkpoint(mundo, mapa, objeto));
        crearObjetosDeCapa("trampolin", (objeto) -> new Trampolin(mundo, mapa, objeto));
        crearObjetosDeCapa("llave", (objeto) -> new Llave(mundo, mapa, objeto));
    }

    // Método para crear objetos de una capa específica del mapa
    private void crearObjetosDeCapa(String nombreCapa, ObjetoCreador creador) {
        // Verifica si la capa existe en el mapa
        if (mapa.getLayers().get(nombreCapa) != null) {
            // Itera sobre los objetos de la capa y crea nuevos objetos interactivos
            for (MapObject objeto : mapa.getLayers().get(nombreCapa).getObjects().getByType(RectangleMapObject.class)) {
                ObjetoInteractivo nuevoObjeto = creador.crear(objeto); // Crea el objeto interactivo
                objetosInteractivos.put(objeto.getName(), nuevoObjeto); // Lo agrega al mapa de objetos
            }
        } else {
            System.out.println("Advertencia: No se encontró la capa " + nombreCapa); // Mensaje de advertencia si la capa no existe
        }
    }

    // Interfaz funcional para crear objetos interactivos
    @FunctionalInterface
    private interface ObjetoCreador {
        ObjetoInteractivo crear(MapObject objeto); // Método para crear un objeto interactivo
    }

    // Método para actualizar el estado de un objeto interactivo
    public void actualizarEstadoObjeto(String nombreObjeto, boolean activo) {
        ObjetoInteractivo objeto = objetosInteractivos.get(nombreObjeto); // Obtiene el objeto interactivo por su nombre
        if (objeto != null) {
            // objeto.setActivo(activo); // Descomentar para activar/desactivar el objeto
        }
    }

    // Método para liberar los recursos de los objetos interactivos
    public void dispose() {
        for (ObjetoInteractivo objeto : objetosInteractivos.values()) {
            objeto.dispose(); // Libera los recursos de cada objeto interactivo
        }
        objetosInteractivos.clear(); // Limpia el mapa de objetos interactivos
    }
}