package com.abyss.explorer.elementos;

import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Imagen {
    private Texture t;
	private Sprite s;
    
    float imagenWidth;
    float imagenHeight;
    
    float escalaWidth;
    float escalaHeight;
    
    
    float x;
    float y;
    
    public Imagen(String ruta) {
        t = new Texture(ruta);
        s = new Sprite(t);
        
        imagenWidth = s.getWidth();
        imagenHeight = s.getHeight();
    }

    public void dispose() {
        t.dispose();
    }
    
    public void dibujar() {
        s.setPosition(x, y);
        s.draw(Render.sb);
    }
    
    public void drawPosition(float x, float y) {
        s.setPosition(x, y);
        s.draw(Render.sb);
    }
    
    public void setTransparencia(float a) {
    	s.setAlpha(a);
    }
    
    public void setSize(float ancho, float alto) {
    	s.setSize(ancho, alto);
    }
    
    public void drawResize() {
        s.setSize(escalaWidth, escalaHeight);
        s.setPosition(x, y);
        s.draw(Render.sb);
    }
    

    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public Sprite getSprite() {
		return s;
	}
    
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return s.getWidth();
    }

    public float getHeight() {
        return s.getHeight();
    }
}
