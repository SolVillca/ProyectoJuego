package com.abyss.explorer.pantallas;

import java.awt.Cursor;

import com.abyss.explorer.elementos.Imagen;
import com.abyss.explorer.elementos.Texto;
import com.abyss.explorer.io.KeyListener;
import com.abyss.explorer.utiles.Config;
import com.abyss.explorer.utiles.Recursos;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaMenu implements Screen{

	Imagen fondo;
	SpriteBatch b;
	
	KeyListener entrada ;
	
	Texto titulo;
	
	Texto opciones[] = new Texto[3];
	String textos[] = {"Inicio", "Opciones", "Salir"};
	
	public float tiempo = 0;
	int opcion = 1, cont = 0;
	boolean mouseArriba = false;
	
	@Override
	public void show() {
		
		fondo = new Imagen(Recursos.FONDOMENU);
		fondo.setSize(Config.ANCHO, Config.ALTO);
		
		titulo = new Texto(Recursos.FUENTEMENU, 80, Color.valueOf("#D2704A") ,false);
		titulo.setTexto(Config.NOMBRE);
		titulo.setPosition( (Config.ANCHO - titulo.getAncho() ) / 2f , 600); //(Config.ALTO- t.getAlto())/2)
		
		
		entrada = new KeyListener(this);
		Gdx.input.setInputProcessor(entrada);
		
		b = Render.sb;
		
		// OPCIONES DEL MENU
		int avance = 40;
		for (int i = 0; i < opciones.length; i++) {
			opciones[i] = new Texto(Recursos.FUENTEMENU, 40, Color.valueOf("#D2704A"), false);
			opciones[i].setTexto(textos[i]);
			opciones[i].setPosition((Config.ANCHO / 2f) - (opciones[i]. getAncho() / 2f), ((Config.ALTO/ 3.2f) + (opciones[0].getAlto()/ 3.2f)) - ((opciones[i].getAlto()*i) + (avance * i)));
		}
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		b.begin();
			fondo.dibujar();
			titulo.dibujar();
			
			for (int i = 0; i < opciones.length; i++) {
				opciones[i].dibujar();
			}
			
		b.end();
		
		tiempo += delta;
		
		//DESPLAZAMIENTO POR EL MENU CON LAS TECLAS ARRIBA Y ABAJO
		
		if(entrada.isAbajo()) {
			if(tiempo>0.2f) {
				tiempo = 0;
				opcion++;
				if(opcion > 3) {
					opcion = 1;
				}
			}
		}
		
		if(entrada.isArriba()) {
			if(tiempo>0.2f) {
				tiempo = 0;
				opcion--;
				if(opcion<1) {
					opcion = 3;
				}
			}
		}
		
		// DESPLAZAMIENTO POR EL MENU CON EL MOUSE
		
		for (int i = 0; i < opciones.length; i++) {
			
			//DEFINE SI EL MOUSE ESTA POSICIONADO SOBRE ALGUNA DE LAS OPCIONES DEL MENU
			if((entrada.getMouseX() >= opciones[i].getX()) && (entrada.getMouseX() <= (opciones[i].getX() + opciones[i].getAncho()))) {
				if((entrada.getMouseY() >= opciones[i].getY() - opciones[i].getAlto()) && (entrada.getMouseY()<= (opciones[i].getY()))) {
					
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
		for (int i = 0; i < opciones.length; i++) {
			if(i == (opcion -1)){
				opciones[i].setColor(Color.YELLOW);
			} else {
				opciones[i].setColor(Color.valueOf("#D2704A"));
			}
		}
		
		// ACCION DEPENDIENDO DE LA OPCION OPRIMIDA
		if((entrada.isEnter()) || (entrada.isClick())){
			if(opcion==1) {
				Render.app.setScreen(new PantallaNivel());
			} else if (opcion == 3) {
				Gdx.app.exit();
			}
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
		this.dispose();
		titulo.dispose();
		fondo.dispose();
		
	}

}
