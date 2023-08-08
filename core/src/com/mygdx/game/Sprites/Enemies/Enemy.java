package com.mygdx.game.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import com.mygdx.game.Sprites.Shot;

import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mafiosi;


public abstract class Enemy extends Sprite {
     protected World world;
     protected PlayScreen screen;
     public Body b2body;
     public Vector2 velocity;
     public String name;

     public Enemy(PlayScreen screen, float x, float y){
          this.world = screen.getWorld();
          this.screen = screen;
          setPosition(x, y);
          velocity = new Vector2(0, -2);
          defineEnemy();
     }

     protected abstract void defineEnemy();
     public abstract void update(float dt);
     public abstract void hitOnHead(Mafiosi player);
     public abstract void hitByEnemy(Enemy enemy);
     public abstract void hitByBullet(Shot shot);

     public void reverseVelocity(boolean x, boolean y){
          if(x) velocity.x = -velocity.x;
          if(y) velocity.y = -velocity.y;
     }
}
