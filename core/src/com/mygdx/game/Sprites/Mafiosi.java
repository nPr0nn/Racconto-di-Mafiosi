package com.mygdx.game.Sprites;

import com.mygdx.game.Jojo;
import com.mygdx.game.Scenes.HUD;
import com.mygdx.game.Sprites.Shot;
import com.mygdx.game.Sprites.Enemies.Enemy;
import com.mygdx.game.Screens.PlayScreen;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;

public class Mafiosi extends Sprite{

     public enum State { FALLING, JUMPING, IDLE, RUNNING, HURTED, DEAD};
     public State currentState;
     public State previousState;

     public World world;
     public Body b2body;
     public boolean isDead;

     private PlayScreen screen;
     public boolean advance;

     private int cFrame;
     private int cAction;

     private Texture img;
     private TextureRegion[][] sprite;
     public TextureRegion currentSprite;

     private Animation <TextureRegion> mafiosiIdle;
     private Animation <TextureRegion> mafiosiRun;
     private Animation <TextureRegion> mafiosiJump;
     private Animation <TextureRegion> mafiosiDie;
     private Animation <TextureRegion> mafiosiHit;

     private float stateTimer;

     private float hangTime;
     private float hangCounter;
     public boolean runningRight;
     private boolean hurted;

     public ShapeRenderer shapeRenderer;
     private float lifeBar;
     private float maxLifeBar;
     private float epsilon;

     Sound soundJump;
     Sound soundHit;
     Sound soundShot;
     Sound soundGameOver;

     private Array<Shot> shots;

     public Mafiosi(PlayScreen screen){
          this.advance = false;
          this.screen = screen;
          this.world = screen.getWorld();
          this.epsilon = (float) 0.0001;

          defineMafiosi();
          img = new Texture("mafiosi.png");
          lifeBar    = 40;
          maxLifeBar = 50;

          soundJump = Gdx.audio.newSound(Gdx.files.internal("Sdx/Sounds/jump2.wav"));
          soundHit = Gdx.audio.newSound(Gdx.files.internal("Sdx/Sounds/hit.wav"));
          soundShot = Gdx.audio.newSound(Gdx.files.internal("Sdx/Sounds/shot.wav"));
          soundGameOver = Gdx.audio.newSound(Gdx.files.internal("Sdx/Sounds/game_over.wav"));

          shapeRenderer = new ShapeRenderer();
          currentState = State.IDLE;
          previousState = State.IDLE;

          cFrame = 0;
          cAction = 0;

          stateTimer = 0;
          runningRight = true;
          hurted = false;
          sprite = TextureRegion.split(img, img.getWidth()/11, img.getHeight()/6);

          Array<TextureRegion> frames = new Array<TextureRegion>();

          for(int i = 0; i < 11; i++) frames.add(sprite[0][i]);
          mafiosiRun = new Animation(0.1f, frames);
          frames.clear();

          for(int i = 0; i < 11; i++) frames.add(sprite[1][i]);
          mafiosiIdle = new Animation(0.1f, frames);
          frames.clear();

          for(int i = 0; i < 8; i++) frames.add(sprite[2][i]);
          mafiosiJump = new Animation(0.1f, frames);
          frames.clear();

          for(int i = 0; i < 11; i++) frames.add(sprite[4][i]);
          mafiosiHit = new Animation(0.1f, frames);
          frames.clear();

          for(int i = 0; i < 11; i++) frames.add(sprite[5][i]);
          mafiosiDie = new Animation(0.1f, frames);
          frames.clear();

          shots = new Array<Shot>();
     }

     public float getStateTimer(){
          return stateTimer;
     }

     public void defineMafiosi(){
          BodyDef bdef = new BodyDef();
          bdef.position.set(64/Jojo.PPM, 64/Jojo.PPM);
          bdef.type = BodyDef.BodyType.DynamicBody;
          b2body = world.createBody(bdef);

          FixtureDef fdef = new FixtureDef();
          CircleShape shape = new CircleShape();
          shape.setRadius(6 / Jojo.PPM);
          fdef.filter.categoryBits = Jojo.PLAYER_BIT;
          fdef.filter.maskBits = Jojo.GROUND_BIT |
               Jojo.COIN_BIT |
               Jojo.BRICK_BIT |
               Jojo.ENEMY_BIT |
               Jojo.OBJECT_BIT |
               Jojo.ENEMY_HEAD_BIT |
               Jojo.LIFE_BIT |
               Jojo.DOOR_BIT |
               Jojo.BULLET_ENEMY_BIT;

          fdef.shape = shape;
          b2body.createFixture(fdef).setUserData(this);

          EdgeShape head = new EdgeShape();
          head.set(new Vector2(-2 / Jojo.PPM, 6 / Jojo.PPM), new Vector2(2 / Jojo.PPM, 6 / Jojo.PPM));
          fdef.filter.categoryBits = Jojo.PLAYER_HEAD_BIT;
          fdef.shape = head;
          fdef.isSensor = true;

          b2body.createFixture(fdef).setUserData(this);
     }

     public void update(float dt){
          currentSprite = getFrame(dt);
          if(b2body.getPosition().y < -15 && isDead == false){
               isDead = true;
               soundGameOver.play();
          }
          if(HUD.worldTimer <= 0){
               isDead = true;
               soundGameOver.play();
               Filter filter = new Filter();
               filter.maskBits = Jojo.NOTHING_BIT;

               for (Fixture fixture : b2body.getFixtureList()) {
                    fixture.setFilterData(filter);
               }
               b2body.applyLinearImpulse(new Vector2(0, 2f), b2body.getWorldCenter(), true);
          }
          for(Shot bullet : shots){
               bullet.update(dt);
               if(bullet.isDestroyed()) shots.removeValue(bullet, true);
          }
     }

     public TextureRegion getFrame(float dt){
          currentState = getState();
          TextureRegion region;

          switch(currentState){
               case RUNNING:
                    region = mafiosiRun.getKeyFrame(stateTimer, true);
                    break;
               case JUMPING:
                    region = mafiosiJump.getKeyFrame(stateTimer);
                    break;
               case FALLING:
                    region = sprite[2][7];
                    break;
               case HURTED:
                    region = mafiosiHit.getKeyFrame(stateTimer);
                    break;
               default:
                    region = mafiosiIdle.getKeyFrame(stateTimer, true);
                    break;
          }

               if((b2body.getLinearVelocity().x + epsilon < 0 || !runningRight) && !region.isFlipX()){
                    region.flip(true, false);
                    runningRight = false;
               }

               else if((b2body.getLinearVelocity().x - epsilon > 0 || runningRight) && region.isFlipX()){
                    region.flip(true, false);
                    runningRight = true;
               }


          stateTimer = currentState == previousState ? stateTimer + dt : 0;

          previousState = currentState;
          return region;
     }

     public State getState(){
          if(isDead == true) return State.DEAD;
          if((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
               return State.JUMPING;
          }
          else if(b2body.getLinearVelocity().y < 0){
               return State.FALLING;
          }
          else if(b2body.getLinearVelocity().x != 0){
               return State.RUNNING;
          }
          else if(hurted == true){
               hurted = false;
               return State.HURTED;
          }
          else{
               return State.IDLE;
          }
     }

     public void getHit(int damage){
          lifeBar -= damage;
          hurted = true;
          if(lifeBar < 0){
               isDead = true;
               soundGameOver.play();
               Filter filter = new Filter();
               filter.maskBits = Jojo.NOTHING_BIT;

               for (Fixture fixture : b2body.getFixtureList()) {
                    fixture.setFilterData(filter);
               }
               b2body.applyLinearImpulse(new Vector2(0, 3f), b2body.getWorldCenter(), true);
          }
     }
     public void getHeal(int heal){
          lifeBar += heal;
     }

     public void hit(Enemy enemy){
          soundHit.play();
          screen.cameraRumble = true;

          float dir = -1;
          if(b2body.getLinearVelocity().x < 0) dir = 1;
          b2body.applyLinearImpulse(new Vector2(-2*b2body.getLinearVelocity().x + dir*1.2f, 4f), b2body.getWorldCenter(), true);

          if(enemy.name == "Boss"){
               getHit(10);
          }
          else{
               getHit(5);
          }
     }

     public void toggleAdvance(){
          advance = !advance;
     }
     public boolean getAdvance(){
          return advance;
     }

     public float getLifeBar(){
          return lifeBar;
     }
     public float getMaxLifeBar(){
          return maxLifeBar;
     }

     public void jump(){
          if ( currentState != State.JUMPING ) {
               soundJump.play();
               b2body.applyLinearImpulse(new Vector2(0, 6f), b2body.getWorldCenter(), true);
               currentState = State.JUMPING;
          }
     }

     public void shot(){
          shots.add(new Shot(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
          soundShot.play();
     }

     public void draw(SpriteBatch batch){
          batch.draw(
                    currentSprite, b2body.getPosition().x - 8/Jojo.PPM, b2body.getPosition().y - 5/Jojo.PPM, 
                    ((float)1.25*currentSprite.getRegionWidth())/Jojo.PPM, 
                    ((float)1.25*currentSprite.getRegionHeight())/Jojo.PPM            
                    );
          for(Shot bullet : shots)  if(bullet.invisible == false) bullet.draw(batch);
     }
}
