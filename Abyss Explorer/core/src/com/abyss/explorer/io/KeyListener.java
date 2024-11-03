package com.abyss.explorer.io;

import com.abyss.explorer.pantallas.PantallaJuegoTerminado;
import com.abyss.explorer.pantallas.PantallaJugador;
import com.abyss.explorer.pantallas.PantallaMenu;
import com.abyss.explorer.pantallas.PantallaNivel;
import com.abyss.explorer.utiles.Config;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class KeyListener implements InputProcessor{
	
	private boolean  arriba = false, abajo = false, derecha = false, izquierda = false, teclaW = false, teclaA = false, teclaD = false, enter = false, click = false;

	
	
	private int mouseX = 0, mouseY = 0;
	
	private PantallaMenu app;
	private PantallaNivel app_nivel;
	private PantallaJugador app_jugador;
	private PantallaJuegoTerminado app_volver;
	
	public KeyListener(PantallaMenu app) {
		this.app = app;
	}
	
	public KeyListener(PantallaJugador app) {
		this.app_jugador = app;
	}
	
	public KeyListener(PantallaNivel app) {
		this.app_nivel = app;
	}

	public KeyListener(PantallaJuegoTerminado app) {
		this.app_volver = app;
	}

	public boolean isAbajo() {
	   return abajo;
	}

	public boolean isArriba() {
	   return arriba;
	}
	
	public boolean isDerecha() {
		return derecha;
	}
	
	public boolean isIzquierda() {
		return izquierda;
	}
	
	public boolean isTeclaW() {
		   return teclaW;
		}

		public boolean isTeclaA() {
		   return teclaA;
		}
		
		public boolean isTeclaD() {
			return teclaD;
		}
	
	public boolean isEnter() {
		return enter;
	}

	public boolean isClick() {
		return click;
	}
	
	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}
	
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		if (keycode == Keys.DOWN) {
            abajo = true;
        } 
        if (keycode == Keys.UP) {
            arriba = true;
        }
        if (keycode == Keys.RIGHT) {
            derecha = true;
        } 
        if (keycode == Keys.LEFT) {
            izquierda = true;
        }
        if (keycode == Keys.ENTER) {
            enter = true;
        }
        if (keycode == Keys.W) {
            teclaW = true;
        } 
        if (keycode == Keys.A) {
        	teclaA = true;
        }
        if (keycode == Keys.D) {
        	teclaD = true;
        }
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		if (keycode == Keys.DOWN) {
            abajo = false;
        }
        if (keycode == Keys.UP) {
            arriba = false;
        }
        if (keycode == Keys.RIGHT) {
            derecha = false;
        }
        if (keycode == Keys.LEFT) {
            izquierda = false;
        }
        if (keycode == Keys.ENTER) {
            enter = false;
        }
        if (keycode == Keys.W) {
            teclaW = false;
        } 
        if (keycode == Keys.A) {
        	teclaA = false;
        }
        if (keycode == Keys.D) {
        	teclaD = false;
        }
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		click = true;
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		click = false;
		return false;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		mouseX = screenX;
		mouseY = Config.ALTO - screenY;
		
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}

}
