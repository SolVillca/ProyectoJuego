package com.abyss.explorer.io;

import com.abyss.explorer.pantallas.PantallaMenu;
import com.abyss.explorer.pantallas.PantallaNivel;
import com.abyss.explorer.utiles.Config;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class KeyListener implements InputProcessor{
	
	private boolean  arriba = false, abajo = false, derecha = false, izquierda = false, enter = false, click = false;
	
	private int mouseX = 0, mouseY = 0;
	
	private PantallaMenu app;
	private PantallaNivel app_nivel;
	
	public KeyListener(PantallaMenu app) {
		this.app = app;
	}
	
	public KeyListener(PantallaNivel app) {
		this.app_nivel = app;
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
