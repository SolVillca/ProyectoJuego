package com.abyss.explorer.pantallas;

import com.abyss.explorer.elementos.Imagen;
import com.abyss.explorer.utiles.Recursos;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaCarga implements Screen {

	SpriteBatch b;
	Imagen fondo;
	
	float transparencia = 0; 										// Equivale a 100% transparente
	boolean fadeEntradaTerminado = false, fadeTerminado = false; 	// Controlar el desvanecimiento del fondo
	float contTiempo = 0, tiempoDeEspera = 5;
	float contTiempoAlTerminar = 0, tiempoAlTerminar = 2;
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
		fondo = new Imagen(Recursos.FONDOCARGA);
		
		fondo.setTransparencia(0);
		b = Render.sb;
		
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		procesarFade();
		Render.LimpiarPantalla();
		b.begin();
			fondo.dibujar();
		b.end();
	}
	
	
	private void procesarFade() { 
		fondo.setTransparencia(transparencia);	//Establece la transparencia del fondo
		
		// (!fadeEntradaTerminado) COMO fade = false, !fade = true, ENTONCES EL CÓDIGO SE EJECUTA PORQUE SE CUMPLE LA CONDICIÓN 
		if (!fadeEntradaTerminado) {			//SI EL DESVANECIMIENTO DE ENTRADA NO A TERMINADO
			transparencia += 0.01f;
			
			if (transparencia > 1) {
				transparencia = 1;
				fadeEntradaTerminado = true;
			}	
		} else {								//SI EL DESVANECIMIENTO DE ENTRADA A TERMINADO
			contTiempo += 0.1f;
			if (contTiempo > tiempoDeEspera) {
				transparencia -= 0.01f;
				if (transparencia < 0) {
					transparencia = 0;
					fadeTerminado = true;
				}
			}
		}
	
		if (fadeTerminado) {					//TIEMPO DE ESPERA HASTA PASAR A LA SIGUIENTE PANTALLA
			contTiempoAlTerminar += 0.08f;
			if(contTiempoAlTerminar > tiempoAlTerminar) Render.app.setScreen(new PantallaMenu());
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
		fondo.dispose();
	}

}
