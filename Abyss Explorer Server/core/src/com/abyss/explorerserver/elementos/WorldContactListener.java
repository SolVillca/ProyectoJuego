package com.abyss.explorerserver.elementos;

import com.abyss.explorerserver.AbyssExplorerServer;
import com.abyss.explorerserver.sprites.Marciano;
import com.abyss.explorerserver.sprites.ObjetoInteractivo;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

// Clase que maneja las colisiones en el mundo de Box2D
public class WorldContactListener implements ContactListener {

    // Método llamado al inicio de una colisión
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA(); // Obtiene el primer fixture involucrado en la colisión
        Fixture fixtureB = contact.getFixtureB(); // Obtiene el segundo fixture involucrado en la colisión
        
        // Combina los bits de categoría de los dos fixtures para identificar la colisión
        int idColision = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        // Maneja las colisiones específicas
        switch (idColision) {
            case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.DEFAULT_BIT:
            case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.AGUA_BIT:
            case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.PINCHES_BIT:
            case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.TRAMPOLIN_BIT:
            case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.CHECKPOINT_BIT: // Colisión con checkpoint
            case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.CHECKPOINT_ACTIVADO_BIT: // Colisión con checkpoint activado
            case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.ENEMIGO_BIT:
            case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.FIN_BIT:
                // Si el fixture A es el pie del marciano, llama al método de colisión en el objeto interactivo B
                if (fixtureA.getFilterData().categoryBits == AbyssExplorerServer.MARCIANO_PIES_BIT) {
                    ((ObjetoInteractivo) fixtureB.getUserData()).colisionPies((Marciano) fixtureA.getUserData());
                } else {
                    // Si el fixture B es el pie del marciano, llama al método de colisión en el objeto interactivo A
                    ((ObjetoInteractivo) fixtureA.getUserData()).colisionPies((Marciano) fixtureB.getUserData());
                }
                break;

            default:
                break; // No hace nada para otras colisiones
        }
    }

    // Método llamado al final de una colisión
    @Override
    public void endContact(Contact contact) {
        // No se implementa lógica para el final de la colisión
    }

    // Método llamado antes de resolver la colisión
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // No se implementa lógica antes de resolver la colisión
    }

    // Método llamado después de resolver la colisión
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // No se implementa lógica después de resolver la colisión
    }
}