package com.abyss.explorerserver.elementos;


import com.abyss.explorerserver.AbyssExplorerServer;
import com.abyss.explorerserver.sprites.Marciano;
import com.abyss.explorerserver.sprites.ObjetoInteractivo;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

//SE LLAMA CUANDO DOS OBJETOS DE BOX2D COLISIONAN
public class WorldContactListener implements ContactListener{


	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		int idColision = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;		//COMBINACION UNICA DE LOS BITS DE LOS DOS FIXTURES
		//Gdx.app.log("Colision", "FixtureA: " + fixtureA.getUserData() + " FixtureB: " + fixtureB.getUserData());

		switch (idColision) {
		case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.DEFAULT_BIT:
		case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.AGUA_BIT:
		case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.PINCHES_BIT:
		case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.TRAMPOLIN_BIT:
		case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.CHECKPOINT_BIT: //!!!
		case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.CHECKPOINT_ACTIVADO_BIT: //!!!
		case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.ENEMIGO_BIT:
		case AbyssExplorerServer.MARCIANO_PIES_BIT | AbyssExplorerServer.FIN_BIT :
			
			
			 if(fixtureA.getFilterData().categoryBits == AbyssExplorerServer.MARCIANO_PIES_BIT)
                 ((ObjetoInteractivo) fixtureB.getUserData()).colisionPies((Marciano) fixtureA.getUserData());
			 		
             else
                 ((ObjetoInteractivo) fixtureA.getUserData()).colisionPies((Marciano) fixtureB.getUserData());
		 		
			break;
		
		//case AbyssExplorer.MARCIANO_PIES_BIT | AbyssExplorer.CHECKPOINT_BIT:
			//break;
		
		default:
			break;
		}
		
	}
		
		
	@Override
	public void endContact(Contact contact) {
	
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
