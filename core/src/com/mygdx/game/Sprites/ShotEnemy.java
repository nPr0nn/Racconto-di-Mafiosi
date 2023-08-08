package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import com.mygdx.game.Jojo;
import com.mygdx.game.Screens.PlayScreen;


public class ShotEnemy extends Sprite {

     PlayScreen screen;
     World world;

     private Animation <TextureRegion> shotAnim;

     float stateTime;

     public boolean invisible;
     public boolean destroyed;
     public boolean setToDestroy;
     boolean fireRight;

     private Texture img;
     private TextureRegion[][] sprite;
     private TextureRegion currentSprite;

     public Body b2body;
     public ShotEnemy (PlayScreen screen, float x, float y, boolean fireRight){
          this.fireRight = fireRight;
          this.screen = screen;
          this.world = screen.getWorld();

          invisible = false;
          Array<TextureRegion> frames = new Array<TextureRegion>();

          img = new Texture("shots.png");
          sprite = TextureRegion.split(img, img.getWidth()/2, img.getHeight()/5);

          for(int i = 0; i < 2; i++) frames.add(sprite[1][i]);
          shotAnim = new Animation(0.05f, frames);
          frames.clear();
          setBounds(x, y, 6 / Jojo.PPM, 6 / Jojo.PPM);
          defineFireBall();
     }

     public void defineFireBall(){
          BodyDef bdef = new BodyDef();
          bdef.position.set(fireRight ? getX() + 12 /Jojo.PPM : getX() - 12 /Jojo.PPM, getY());
          bdef.type = BodyDef.BodyType.DynamicBody;
          if(!world.isLocked())
               b2body = world.createBody(bdef);

          FixtureDef fdef = new FixtureDef();
          CircleShape shape = new CircleShape();
          shape.setRadius(3 / Jojo.PPM);
          fdef.filter.categoryBits = Jojo.BULLET_ENEMY_BIT;
          fdef.filter.maskBits = Jojo.GROUND_BIT | Jojo.BRICK_BIT;

          fdef.shape = shape;
          fdef.restitution = 1;
          fdef.friction = 0;
          b2body.createFixture(fdef).setUserData(this);
     }

     public void update(float dt){
          stateTime += dt;
          currentSprite = sprite[0][0];
          b2body.setLinearVelocity(new Vector2(fireRight ? 1 : -1, 0));
          setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

          if((stateTime > 3 || setToDestroy) && !destroyed) {
               world.destroyBody(b2body);
               destroyed = true;
          }
      
     }

     public void setToDestroy(){
          setToDestroy = true;
     }

     public boolean isDestroyed(){
          return destroyed;
     }

     public void draw(SpriteBatch batch){
          batch.draw(
                    currentSprite, b2body.getPosition().x - 8/Jojo.PPM, b2body.getPosition().y - 5/Jojo.PPM, 
                    ((float)1.25*currentSprite.getRegionWidth())/Jojo.PPM, 
                    ((float)1.25*currentSprite.getRegionHeight())/Jojo.PPM            
                    );
     }


}

