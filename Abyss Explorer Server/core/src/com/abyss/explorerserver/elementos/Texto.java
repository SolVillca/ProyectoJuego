package com.abyss.explorerserver.elementos;

import com.abyss.explorerserver.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;

public class Texto {
	
	BitmapFont fuente;
	private String texto = "";
	GlyphLayout layout;

	private float x = 0;
	private float y = 0;
    
    
	public Texto(String rutaFuente, int size, Color color, Boolean sombra) {
		generarTexto(rutaFuente, size, color, sombra);

	}

	private void generarTexto(String rutaFuente, int size, Color color, Boolean sombra) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(rutaFuente));
        FreeTypeFontParameter parametros = new FreeTypeFontParameter();
        
        parametros.size = size;
        parametros.color = color;
        
        if(sombra) {
        	parametros.shadowColor = Color.BLACK;
        	parametros.shadowOffsetX = 1;
        	parametros.shadowOffsetY = 1;
        }
        
        fuente = generator.generateFont(parametros);
        layout = new GlyphLayout();
        generator.dispose();
	}
	
	public void dibujar() {
		fuente.draw(Render.sb, texto, x , y);
	}
	
	public void setColor(Color color) {
		fuente.setColor(color);
	}
	

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
		layout.setText(fuente, texto);
	}
	
	public float getAncho() {
		return layout.width;
	}
	
	public float getAlto() {
		return layout.height;
	}

	public Vector2 getDimensionTexto() {
		return new Vector2(layout.width, layout.height);
	}
	
	

	public void dispose() {
		if (fuente != null) {
            fuente.dispose();
        }
	}
	
}

