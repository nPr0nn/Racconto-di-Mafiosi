package com.mygdx.game.Tools;

import com.mygdx.game.Sprites.TileObjects.InteractiveTileObject;
import com.mygdx.game.Jojo;
import com.mygdx.game.Sprites.Mafiosi;
import com.mygdx.game.Sprites.Enemies.Enemy;

import com.mygdx.game.Sprites.Shot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldContactListener implements ContactListener {
     @Override
     public void beginContact(Contact contact) {
          Fixture fixA = contact.getFixtureA();
          Fixture fixB = contact.getFixtureB();

          int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

          switch (cDef){
               case Jojo.PLAYER_BIT | Jojo.DOOR_BIT:
               case Jojo.PLAYER_BIT | Jojo.LIFE_BIT:
               case Jojo.PLAYER_BIT | Jojo.BRICK_BIT:
               case Jojo.PLAYER_BIT | Jojo.COIN_BIT:
                    if(fixA.getFilterData().categoryBits == Jojo.PLAYER_BIT)
                         ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mafiosi) fixA.getUserData());
                    else
                         ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mafiosi) fixB.getUserData());
                    break;
               case Jojo.ENEMY_BIT | Jojo.OBJECT_BIT:
                    if(fixA.getFilterData().categoryBits == Jojo.ENEMY_BIT)
                         ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                    else
                         ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                    break;
               case Jojo.PLAYER_BIT | Jojo.ENEMY_BIT:
                    if(fixA.getFilterData().categoryBits == Jojo.PLAYER_BIT)
                         ((Mafiosi) fixA.getUserData()).hit((Enemy)fixB.getUserData());
                    else
                         ((Mafiosi) fixB.getUserData()).hit((Enemy)fixA.getUserData());
                    break;
               case Jojo.ENEMY_BIT | Jojo.ENEMY_BIT:
                    ((Enemy)fixA.getUserData()).hitByEnemy((Enemy)fixB.getUserData());
                    ((Enemy)fixB.getUserData()).hitByEnemy((Enemy)fixA.getUserData());
                    break;
               case Jojo.BULLET_BIT | Jojo.ENEMY_BIT:
                    if(fixA.getFilterData().categoryBits == Jojo.ENEMY_BIT)
                         ((Enemy) fixA.getUserData()).hitByBullet((Shot)fixB.getUserData());
                    else
                         ((Enemy) fixB.getUserData()).hitByBullet((Shot)fixA.getUserData());
                    break;
          }
     }

     @Override
     public void endContact(Contact contact) {
     }

     @Override
     public void preSolve(Contact contact, Manifold oldManifold) {
     }

     @Override
     public void postSolve(Contact contact, ContactImpulse impulse) {

     }

}
