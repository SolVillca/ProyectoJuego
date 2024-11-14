package com.abyss.explorerserver.sprites;

import com.abyss.explorerserver.utiles.Config;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

//Clase abstracta que representa un objeto interactivo en el juego
public abstract class ObjetoInteractivo {
	protected World mundo; // Mundo de Box2D donde se encuentra el objeto
	protected TiledMap mapa; // Mapa de tipo TiledMap asociado al objeto
	protected Rectangle rectangulo; // Rectángulo que define la forma del objeto
	protected Body cuerpo; // Cuerpo físico del objeto en el mundo
	protected Fixture fixture; // Fixture que define la colisión del objeto

	// Constructor que inicializa el objeto interactivo con el mundo, mapa y objeto del mapa
	public ObjetoInteractivo(World mundo, TiledMap mapa, MapObject objeto) {
		this.mundo = mundo; // Asignación del mundo
		this.mapa = mapa; // Asignación del mapa
		this.rectangulo = ((RectangleMapObject) objeto).getRectangle(); // Obtención del rectángulo del objeto

		crearObjeto(); // Llamada al método para crear el objeto en el mundo
	}

	// Método que crea la caja de colisión del objeto y establece la configuración del cuerpo
    protected void crearObjeto() {
        BodyDef bd = new BodyDef(); // Definición del cuerpo
        FixtureDef fd = new FixtureDef(); // Definición del fixture
        PolygonShape forma = new PolygonShape(); // Forma poligonal para el fixture
        
        bd.type = BodyDef.BodyType.StaticBody; // Establece el tipo de cuerpo como estático
        // Establece la posición del cuerpo en el mundo, ajustando por el PPM (píxeles por metro)
        bd.position.set((rectangulo.getX() + rectangulo.getWidth() / 2) / Config.PPM,
                       (rectangulo.getY() + rectangulo.getHeight() / 2) / Config.PPM);
        
        cuerpo = mundo.createBody(bd); // Crea el cuerpo en el mundo
        
        // Define la forma del fixture como un rectángulo basado en el tamaño del objeto
        forma.setAsBox(rectangulo.getWidth() / 2 / Config.PPM,
                      rectangulo.getHeight() / 2 / Config.PPM);
        fd.shape = forma; // Asigna la forma al fixture
        fixture = cuerpo.createFixture(fd); // Crea el fixture en el cuerpo
        
        forma.dispose(); // Libera la forma para evitar fugas de memoria
    }

    // Método abstracto que debe ser implementado por las subclases para manejar la colisión con el 'Marciano'
    public abstract void colisionPies(Marciano marciano);

    // Método que establece el filtro de categoría para el fixture
    public void setFiltroDeCategoria(short filBit) {
        Filter filter = new Filter(); // Crea un nuevo filtro
        filter.categoryBits = filBit; // Establece los bits de categoría del filtro
        fixture.setFilterData(filter); // Aplica el filtro al fixture
    }

    // Método para eliminar el cuerpo del mundo y liberar recursos
    public void dispose() {
        if (cuerpo != null && mundo != null) { // Verifica que el cuerpo y el mundo no sean nulos
            mundo.destroyBody(cuerpo); // Destruye el cuerpo en el mundo
        }
    }
}