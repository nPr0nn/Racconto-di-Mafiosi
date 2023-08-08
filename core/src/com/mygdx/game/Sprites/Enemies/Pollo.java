package com.mygdx.game.Sprites.Enemies;


import com.mygdx.game.Jojo;
import com.mygdx.game.Sprites.Shot;
import com.mygdx.game.Scenes.HUD;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mafiosi;
import com.mygdx.game.Sprites.ShotEnemy;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.math.RandomXS128;

import java.lang.Math;

public class Pollo extends com.mygdx.game.Sprites.Enemies.Enemy{
     private float stateTime;
     private float actionTime;
     private float shotTime;

     private Texture img;
     private TextureRegion currentSprite;
     private TextureRegion[][] sprite;

     private Animation <TextureRegion> polloIdle;
     private Animation <TextureRegion> polloRun;

     private boolean setToDestroy;
     private boolean destroyed;
     private RandomXS128 random;
     private int life;

     float angle;
     private Array<ShotEnemy> shots;

     public Pollo(PlayScreen screen, float x, float y) {
          super(screen, x, y);
          this.screen = screen;
          this.world = screen.getWorld();
          this.life = 8;
          this.name = "Pollo";

          random = new RandomXS128();

          int p = 1 + random.nextInt(4);
          String pic = "pollo" + String.valueOf(p) +".png";
          img = new Texture(pic);
          sprite = TextureRegion.split(img, img.getWidth()/4, img.getHeight()/2);
          Array<TextureRegion> frames = new Array<TextureRegion>();

          for(int i = 0; i < 4; i++) frames.add(sprite[0][i]);
          polloRun = new Animation(0.1f, frames);
          frames.clear();

          for(int i = 0; i < 4; i++) frames.add(sprite[1][i]);
          polloIdle = new Animation(0.1f, frames);
          frames.clear();

          stateTime = 0;
          setBounds(getX(), getY(), 16 /Jojo.PPM, 16 /Jojo.PPM);
          setToDestroy = false;
          destroyed = false;
          angle = 0;

          shots = new Array<ShotEnemy>();
     }

     public void update(float dt){
          shotTime += dt;
          stateTime += dt;
          actionTime += dt;

          //if(shotTime > 3){         
               //shot();
               //shotTime = 0;
          //}


          if(setToDestroy && !destroyed){
               world.destroyBody(b2body);
               destroyed = true;
               HUD.addScore(30);
               screen.global_score += 30;
               stateTime = 0;
          }
          else if(!destroyed) {
               
               //for(ShotEnemy bullet : shots){
                    //bullet.update(dt);
                    //if(bullet.isDestroyed()) shots.removeValue(bullet, true);
               //}

               int r = random.nextInt(10);

               if(b2body.getLinearVelocity().y == 0 && r % 4 == 0 && actionTime > 2.5){
                    b2body.applyLinearImpulse(new Vector2(0, 3f), b2body.getWorldCenter(), true);
                    actionTime = 0;
               }
               else{
                    float amplitude = ((r/2)+7)/Jojo.PPM;
                    velocity = new Vector2((float)amplitude*(float)Math.sin(stateTime*Math.PI), 0);
                    b2body.applyLinearImpulse(velocity, b2body.getWorldCenter(), true);
               }

               setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
               currentSprite = polloRun.getKeyFrame(stateTime);
          }
     }
     protected void defineEnemy() {
          BodyDef bdef = new BodyDef();
          bdef.position.set(getX(), getY());
          bdef.type = BodyDef.BodyType.DynamicBody;
          b2body = world.createBody(bdef);

          FixtureDef fdef = new FixtureDef();
          CircleShape shape = new CircleShape();
          shape.setRadius((float)5.5 /Jojo.PPM);
          fdef.filter.categoryBits = Jojo.ENEMY_BIT;
          fdef.filter.maskBits = Jojo.GROUND_BIT | Jojo.PLAYER_BIT | Jojo.BULLET_BIT | Jojo.ENEMY_BIT;

          fdef.shape = shape;
          fdef.restitution = 0.6f;
          b2body.createFixture(fdef).setUserData(this);

          PolygonShape head = new PolygonShape();
          Vector2[] vertice = new Vector2[4];
          vertice[0] = new Vector2(-5, 8).scl(1 / Jojo.PPM);
          vertice[1] = new Vector2(5, 8).scl(1 / Jojo.PPM);
          vertice[2] = new Vector2(-3, 3).scl(1 / Jojo.PPM);
          vertice[3] = new Vector2(3, 3).scl(1 / Jojo.PPM);
          head.set(vertice);

          fdef.shape = head;
          fdef.restitution = 0.6f;
          fdef.filter.categoryBits = Jojo.ENEMY_HEAD_BIT;
          b2body.createFixture(fdef).setUserData(this);
     }

     public void shot(){
          shots.add(new ShotEnemy(screen, b2body.getPosition().x, b2body.getPosition().y + 3/Jojo.PPM, false));
     }

     public void draw(Batch batch){
          if(!destroyed || stateTime < 1){
               batch.draw(
                         currentSprite, b2body.getPosition().x - 12/Jojo.PPM, b2body.getPosition().y - 8/Jojo.PPM, 
                         ((float)1.55*currentSprite.getRegionWidth())/Jojo.PPM, 
                         ((float)1.55*currentSprite.getRegionHeight())/Jojo.PPM            
                         );
               //for(ShotEnemy bullet : shots)  if(bullet.invisible == false) bullet.draw(batch);
          }
     }

     @Override
     public void hitOnHead(Mafiosi player) {
          //setToDestroy = true;
     }

     @Override
     public void hitByEnemy(Enemy enemy) {
          return;
     }

     @Override
     public void hitByBullet(Shot shot) {
          life -= 1;
          b2body.applyLinearImpulse(new Vector2(0, 1.25f), b2body.getWorldCenter(), true);
          shot.invisible = true;
          if(life < 0) setToDestroy = true;
          return;
     }
}
