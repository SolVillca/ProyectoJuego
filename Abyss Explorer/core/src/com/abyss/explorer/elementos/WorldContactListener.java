package com.abyss.explorer.elementos;


import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.abyss.explorer.AbyssExplorer;
//import com.abyss.explorer.sprites.ObjetoInteractivo;
import com.abyss.explorer.sprites.Marciano;
import com.abyss.explorer.sprites.ObjetoInteractivo;

//SE LLAMA CUANDO DOS OBJETOS DE BOX2D COLISIONAN
public class WorldContactListener implements ContactListener{


	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		int idColision = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;		//COMBINACION UNICA DE LOS BITS DE LOS DOS FIXTURES
		//Gdx.app.log("Colision", "FixtureA: " + fixtureA.getUserData() + " FixtureB: " + fixtureB.getUserData());

		switch (idColision) {
		case AbyssExplorer.MARCIANO_PIES_BIT | AbyssExplorer.DEFAULT_BIT:
		case AbyssExplorer.MARCIANO_PIES_BIT | AbyssExplorer.AGUA_BIT:
		case AbyssExplorer.MARCIANO_PIES_BIT | AbyssExplorer.PINCHES_BIT:
		case AbyssExplorer.MARCIANO_PIES_BIT | AbyssExplorer.TRAMPOLIN_BIT:
		case AbyssExplorer.MARCIANO_PIES_BIT | AbyssExplorer.CHECKPOINT_BIT: //!!!
		case AbyssExplorer.MARCIANO_PIES_BIT | AbyssExplorer.CHECKPOINT_ACTIVADO_BIT: //!!!
		case AbyssExplorer.MARCIANO_PIES_BIT | AbyssExplorer.ENEMIGO_BIT:
		case AbyssExplorer.MARCIANO_PIES_BIT | AbyssExplorer.FIN_BIT :
			
			 if(fixtureA.getFilterData().categoryBits == AbyssExplorer.MARCIANO_PIES_BIT)
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
