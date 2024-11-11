package com.abyss.explorer.pantallas;

import java.util.ArrayList;
import java.util.List;

// Importaciones necesarias para la clase PantallaJuegoTerminado
import com.abyss.explorer.AbyssExplorer;
import com.abyss.explorer.elementos.Menu;
import com.abyss.explorer.elementos.Texto;
import com.abyss.explorer.io.KeyListener;
import com.abyss.explorer.utiles.Config;
import com.abyss.explorer.utiles.Global;
import com.abyss.explorer.utiles.Recursos;
import com.abyss.explorer.utiles.Render;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// Clase que representa la pantalla de juego terminado
public class PantallaJuegoTerminado implements Screen {
    private AbyssExplorer game; // Referencia al juego principal
    private SpriteBatch b; // Batch para dibujar sprites
    private Texto titulo; // Título de la pantalla
    private Texto opciones[]; // Opciones del menú
    private String textos[] = {"Volver al menu", "Salir"}; // Texto de las opciones
    private Menu menu; // Menú de opciones
    private Viewport ventanaJuego; // Ventana del juego
    private OrthographicCamera camara; // Cámara del juego
    private KeyListener entradas; // Listener para las entradas del teclado
    private String marcianoRegion; // Región del marciano ganador

    // Constructor de la clase PantallaJuegoTerminado
    public PantallaJuegoTerminado(String marciano) {
        this.marcianoRegion = marciano; // Asigna la región del marciano
    }

    // Método que se llama al mostrar la pantalla
    @Override
    public void show() {
        camara = new OrthographicCamera(); // Inicializa la cámara
        ventanaJuego = new FitViewport(Config.ANCHO / Config.PPM, Config.ALTO / Config.PPM, camara); // Configura la vista

        // Ajustar la cámara
        camara.setToOrtho(false, ventanaJuego.getWorldWidth(), ventanaJuego.getWorldHeight());
        camara.update(); // Actualiza la cámara

        // Inicializa el título de la pantalla
        titulo = new Texto(Recursos.FUENTEMENU, 40, Color.valueOf("#D2704A"), false);
        if (Global.ganador.equals(marcianoRegion)) {
            System.out.println(marcianoRegion); // Mensaje de depuración
            titulo.setTexto("Juego Terminado : Ganador"); // Establece el texto de ganador
        } else {
            titulo.setTexto("Juego Terminado : Perdedor"); // Establece el texto de perdedor
        }
        titulo.setPosition((Config.ANCHO - titulo.getAncho()) / 2, 600); // Centra el título en la pantalla

        // Inicializa las opciones del menú
        opciones = new Texto[textos.length];

        // Lista de acciones para las opciones del menú
        List<Runnable> acciones = new ArrayList<>();
        acciones.add(() -> {
            // Acción para volver al menú
            Render.app.setScreen(new PantallaMenu());
        });
        acciones.add(() -> {
            // Acción para salir del juego
            Gdx.app.exit();
        });

        // Inicializa el listener de entradas
        entradas = new KeyListener(this);
        Gdx.input.setInputProcessor(entradas); // Establece el procesador de entrada

        // Inicializa el menú con las opciones y acciones
        menu = new Menu(opciones, textos, false, 40, entradas, acciones);
        b = Render.sb; // Asigna el SpriteBatch para el renderizado
    }

    // Método que se llama para renderizar la pantalla
    @Override
    public void render(float delta) {
        Render.LimpiarPantalla(); // Limpia la pantalla

        camara.update(); // Actualiza la cámara
        b.setProjectionMatrix(camara.combined); // Establece la matriz de proyección

        b.begin(); // Comienza el renderizado
        titulo.dibujar(); // Dibuja el título
        menu.dibujar(delta); // Dibuja el menú
        b.end(); // Finaliza el renderizado
    }

    @Override
    public void resize(int width, int height) {
       
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
       
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void dispose() {
        titulo.dispose(); // Libera los recursos del título
    }
}