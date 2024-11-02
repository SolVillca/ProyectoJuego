package com.abyss.explorer.pantallas;

import com.abyss.explorer.elementos.Imagen;
import com.abyss.explorer.elementos.Texto;
import com.abyss.explorer.io.KeyListener;
import com.abyss.explorer.utiles.Config;
import com.abyss.explorer.utiles.Recursos;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaJugador implements Screen{
	
	SpriteBatch b; 
	Imagen fondo;
	Texto menu[] = new Texto[2];
	String[] opciones = {"1 Jugador", "2 Jugadores"};
	KeyListener entradas;
	
	public float tiempo = 0;
	int opcion = 1, cont = 0;
	boolean mouseArriba = false;
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		fondo = new Imagen(Recursos.FONDOMENU);
		for (int i = 0; i < menu.length; i++) {
			menu[i] = new Texto(Recursos.FUENTEMENU, 40, Color.valueOf("#D2704A"), false);
			menu[i].setTexto(opciones[i]);
			menu[i].setPosition((Config.ANCHO / 2f) - (menu[i]. getAncho() / 2f), ((Config.ALTO/ 3.2f) + (menu[0].getAlto()/ 3.2f)) - ((menu[i].getAlto()*i) + (40 * i)));
		}
		
		entradas = new KeyListener(this);
		Gdx.input.setInputProcessor(entradas);
		
		b = Render.sb;
		
	}

	@Override
	public void render(float delta) {
		Render.LimpiarPantalla();
		b.begin();
			fondo.dibujar();
			for (int i = 0; i < menu.length; i++) {
				menu[i].dibujar();
			}
		b.end();
		// TODO Auto-generated method stub
		
		tiempo += delta;
		
		//DESPLAZAMIENTO POR EL MENU CON LAS TECLAS ARRIBA Y ABAJO
		
		if(entradas.isAbajo()) {
			if(tiempo>0.2f) {
				tiempo = 0;
				opcion++;
				if(opcion > 2) {
					opcion = 1;
				}
			}
		}
		
		if(entradas.isArriba()) {
			if(tiempo>0.2f) {
				tiempo = 0;
				opcion--;
				if(opcion<1) {
					opcion = 2;
				}
			}
		}
		
		// DESPLAZAMIENTO POR EL MENU CON EL MOUSE
		
		for (int i = 0; i < menu.length; i++) {
			
			//DEFINE SI EL MOUSE ESTA POSICIONADO SOBRE ALGUNA DE LAS OPCIONES DEL MENU
			if((entradas.getMouseX() >= menu[i].getX()) && (entradas.getMouseX() <= (menu[i].getX() + menu[i].getAncho()))) {
				if((entradas.getMouseY() >= menu[i].getY() - menu[i].getAlto()) && (entradas.getMouseY()<= (menu[i].getY()))) {
					
					//Gdx.graphics.setSystemCursor(SystemCursor.Hand);
					opcion = i+1;
					cont++;
				} 
			} else {
				//Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
			}
		}
		
		if (cont>0) {
			mouseArriba = true;
		} else {
			mouseArriba = false;
		}
		
		// CAMBIA DE COLOR DEPENDIENDO SI ESTA SIENDO SELECCIONADO
		for (int i = 0; i < menu.length; i++) {
			if(i == (opcion -1)){
				menu[i].setColor(Color.YELLOW);
			} else {
				menu[i].setColor(Color.valueOf("#D2704A"));
			}
		}
		
		// ACCION DEPENDIENDO DE LA OPCION OPRIMIDA
		if((entradas.isEnter()) || (entradas.isClick())){
			int cantJugador = (opcion == 1) ? 1 : 2; // Determina la cantidad de jugadores según la opción seleccionada
            Render.app.setScreen(new PantallaNivel(cantJugador)); // Cambia a la pantalla de nivel
		}
	
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
