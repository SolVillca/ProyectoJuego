package com.abyss.explorer.elementos;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.abyss.explorer.utiles.Config;
import com.abyss.explorer.utiles.Recursos;
import com.abyss.explorer.utiles.Render;
import com.abyss.explorer.io.KeyListener;
import java.util.List;

public class Menu {
	boolean mouseArriba = false;
	

    private SpriteBatch b;
    private Texto[] opciones;
    private String[] textos;
    private int opcion, avance;
    private float tiempo = 0;
    private KeyListener entrada;
    private List<Runnable> acciones; // Lista de acciones para cada opción

    public Menu(Texto[] opciones, String[] textos, int avance, KeyListener entrada, List<Runnable> acciones) {

        this.opciones = opciones;
        this.textos = textos;
        this.avance = avance;
        this.entrada = entrada;
        this.acciones = acciones;
        this.opcion = 1;
        this.tiempo = 0;
        
        for (int i = 0; i < opciones.length; i++) {
        	opciones[i] = new Texto(Recursos.FUENTEMENU, 40, Color.valueOf("#D2704A"), false);
        	opciones[i].setTexto(textos[i]);
        	opciones[i].setPosition((Config.ANCHO / 2f) - (opciones[i]. getAncho() / 2f), ((Config.ALTO/ 3.2f) + (opciones[0].getAlto()/ 3.2f)) - ((opciones[i].getAlto()*i) + (avance * i)));

        }
        
        b = Render.sb;
    }

    public void dibujar(float delta) {

        for (int i = 0; i < opciones.length; i++) {
        	opciones[i].dibujar();
        }
        	

        tiempo += delta;

        manejarNavegacion();
        actualizarColores();
        manejarSeleccion();
    }

    private void manejarNavegacion() {
        // DESPLAZAMIENTO POR EL MENU CON LAS FLECHAS
        if (entrada.isAbajo() && tiempo > 0.2f) {
            tiempo = 0;
            opcion++;
            if (opcion > opciones.length) {
                opcion = 1;
            }
        }

        if (entrada.isArriba() && tiempo > 0.2f) {
            tiempo = 0;
            opcion--;
            if (opcion < 1) {
                opcion = opciones.length;
            }
        }

        // DESPLAZAMIENTO POR EL MENU CON EL MOUSE
        for (int i = 0; i < opciones.length; i++) {
            if (entrada.getMouseX() >= opciones[i].getX() && entrada.getMouseX() <= (opciones[i].getX() + opciones[i].getAncho())) {
                if (entrada.getMouseY() >= opciones[i].getY() - opciones[i].getAlto() && entrada.getMouseY() <= opciones[i].getY()) {
                    opcion = i + 1;
                }
            }
        }
    }

    private void actualizarColores() {
    	// CAMBIA DE COLOR DEPENDIENDO SI ESTA SIENDO SELECCIONADO
    	for (int i = 0; i < opciones.length; i++) {
    		if(i == (opcion -1)){
    			opciones[i].setColor(Color.YELLOW);
    		} else {
    			opciones[i].setColor(Color.valueOf("#D2704A"));
    		}
    	}
    }

    private void manejarSeleccion() {
        // ACCION DEPENDIENDO DE LA OPCION OPRIMIDA
        if (entrada.isEnter() || entrada.isClick()) {
            if (opcion >= 1 && opcion <= acciones.size()) {
                acciones.get(opcion - 1).run(); // EJECUTA LA OPC CORRESPONDIENTE
            }
        }
    }
}