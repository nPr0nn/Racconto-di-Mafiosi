package com.mygdx.game.Sprites.Enemies;


import com.mygdx.game.Jojo;
import com.mygdx.game.Scenes.HUD;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mafiosi;
import com.mygdx.game.Sprites.Shot;
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

import java.lang.Math;
import java.util.Random;

public class Boss extends com.mygdx.game.Sprites.Enemies.Enemy{
     private float stateTime;
     private float actionTime;
     private float shotTime;

     public Body b2body;
     private Texture img;
     private TextureRegion[][] sprite;
     public TextureRegion currentSprite;
     public boolean runningRight;

     private Animation <TextureRegion> polloIdle;
     private Animation <TextureRegion> polloRun;

     public boolean setToDestroy;
     private boolean destroyed;
     private Random random;
     private int life;

     float angle;
     private Array<ShotEnemy> shots;

     public Boss(PlayScreen screen, float x, float y) {
          super(screen, x, y);
          this.screen = screen;
          this.world = screen.getWorld();
          this.life = 160;
          this.name = "Boss";

          random = new Random();

          int p = 1 + random.nextInt(4);
          String pic = "Boss.png";
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
          actionTime = 0;
          setBounds(getX(), getY(), 32/Jojo.PPM, 32/Jojo.PPM);
          setToDestroy = false;
          destroyed = false;
          angle = 0;

          shots = new Array<ShotEnemy>();
          runningRight = false;
     }

     public void update(float dt){
          shotTime += dt;
          stateTime += dt;
          actionTime += dt;

          if(shotTime > 3){
               shot();
               shotTime = 0;
          }

          if(setToDestroy && !destroyed){
               world.destroyBody(b2body);
               destroyed = true;
               HUD.addScore(1000);
               screen.global_score += 1000;
               stateTime = 0;
          }

          else if(!destroyed) {

               int r = random.nextInt(10);

               if(b2body.getLinearVelocity().y == 0 && r % 4 == 0 && actionTime > 2.5){
                    b2body.applyLinearImpulse(new Vector2(0, 3f), b2body.getWorldCenter(), true);
                    actionTime = 0;
               }
               else{
                    float amplitude = ((r/2)+20)/Jojo.PPM;
                    velocity = new Vector2((float)amplitude*(float)Math.sin(stateTime*Math.PI), 0);
                    b2body.applyLinearImpulse(velocity, b2body.getWorldCenter(), true);
               }

               for(ShotEnemy bullet : shots){
                    bullet.update(dt);
                    if(bullet.isDestroyed()) shots.removeValue(bullet, true);
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
          shape.setRadius(50/Jojo.PPM);
          fdef.filter.categoryBits = Jojo.ENEMY_BIT;
          fdef.filter.maskBits = Jojo.GROUND_BIT | Jojo.PLAYER_BIT | Jojo.BULLET_BIT;

          fdef.shape = shape;
          fdef.restitution = 0.3f;
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
          //return;
          shots.add(new ShotEnemy(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
     }

     public void draw(Batch batch){
          if(!destroyed || stateTime < 1){
               batch.draw(
                         currentSprite, b2body.getPosition().x - 130/Jojo.PPM, b2body.getPosition().y - 55/Jojo.PPM, 
                         ((float)8*currentSprite.getRegionWidth())/Jojo.PPM, 
                         ((float)8*currentSprite.getRegionHeight())/Jojo.PPM            
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
          shot.invisible = true;
          if(life < 0) setToDestroy = true;
          return;
     }
}

