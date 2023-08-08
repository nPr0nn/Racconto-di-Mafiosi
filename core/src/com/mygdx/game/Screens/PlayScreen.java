package com.mygdx.game.Screens;

import com.mygdx.game.Jojo;
import com.mygdx.game.Screens.Rumble;

import com.mygdx.game.Scenes.HUD;
import com.mygdx.game.Screens.GameOver;
import com.mygdx.game.Sprites.Mafiosi;
import com.mygdx.game.Sprites.Enemies.*;
import com.mygdx.game.Tools.b2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen{

     private Jojo game;
     private TextureAtlas playerAtlas;
     private OrthographicCamera gamecam;
     private Viewport gamePort;
     public int global_score;
     private HUD hud;

     private TmxMapLoader maploader;
     private TiledMap map;
     private OrthogonalTiledMapRenderer renderer;

     public World world;
     private Box2DDebugRenderer b2dr;

     private Mafiosi player;
     private Boss leone;

     private Music music;
     private Sound soundPause;
     public boolean cameraRumble;
     public boolean activateEffects;

     private boolean pause;
     private boolean change_act;

     private String act;
     private String act_music;
     private b2WorldCreator creator;

     public PlayScreen(Jojo game, String act, String act_music, int score){
          this.global_score = score;
          this.game = game;
          this.act = act;
          this.act_music = act_music;
          this.cameraRumble = false;
          this.global_score = score;
          this.activateEffects = false;

          soundPause = Gdx.audio.newSound(Gdx.files.internal("Sdx/Sounds/pause.wav"));

          pause = false;
          change_act = false;
          gamecam = new OrthographicCamera();


          gamePort = new FitViewport( (Jojo.V_WIDTH)/Jojo.PPM, (Jojo.V_HEIGHT)/Jojo.PPM, gamecam);
          hud = new HUD(game.batch, act, global_score);

          maploader = new TmxMapLoader(new InternalFileHandleResolver() );
          map = maploader.load(act);

          renderer = new OrthogonalTiledMapRenderer(map, 1/Jojo.PPM, game.batch);
          gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

          world = new World(new Vector2(0, -8), true);
          b2dr = new Box2DDebugRenderer();

          creator = new b2WorldCreator(this);
          player = new Mafiosi(this);

          world.setContactListener(new WorldContactListener());
          music = Gdx.audio.newMusic(Gdx.files.internal(act_music));
          music.setLooping(true);
          music.setVolume(0.1f);
          music.play();

          if(act == "act3-boss.tmx") leone = new Boss(this, 15*32/Jojo.PPM, 20*32/Jojo.PPM);
     }

     public TiledMap getMap(){
          return map;
     }
     public World getWorld(){
          return world;
     }
     public HUD getHud(){ 
          return hud; 
     }
     public Color toRGB(int r, int g, int b) {
          float RED = r / 255.0f;
          float GREEN = g / 255.0f;
          float BLUE = b / 255.0f;
          return new Color(RED, GREEN, BLUE, 1);
     }

     public void handleInput(float dt){

          if(player.currentState != Mafiosi.State.DEAD){
               //Player        
               if (Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x <= 2 && pause == false){
                    player.b2body.applyLinearImpulse(new Vector2(0.15f, 0), player.b2body.getWorldCenter(), true);    
               }
               if (Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x >= -2 && pause == false){
                    player.b2body.applyLinearImpulse(new Vector2(-0.15f, 0), player.b2body.getWorldCenter(), true);
               }
               if (Gdx.input.isKeyJustPressed(Input.Keys.W) && pause == false){
                    player.jump();
               }
               else if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
                    pause = !pause;
                    soundPause.play();
               }
               if (Gdx.input.isKeyJustPressed(Input.Keys.J) && pause == false){
                    player.shot();
               }

               if (Gdx.input.isKeyJustPressed(Input.Keys.E) && pause == false){
                    activateEffects = !activateEffects;
               }


               //if (Gdx.input.isKeyJustPressed(Input.Keys.K)){
               //cameraRumble = true;
               //player.getHit(1);
               //}
               //if (Gdx.input.isKeyJustPressed(Input.Keys.H)){
               //cameraRumble = true;
               //player.getHeal(1);
               //}
               //if (Gdx.input.isKeyJustPressed(Input.Keys.M)){
               //player.toggleAdvance();
               //}
          }

     }

     public void update(float dt){
          handleInput(dt);

          if(player.getAdvance() == true){
               change_act = true;
               player.toggleAdvance();
          }

          if( pause == false){
               music.play();
               world.step(1/60f, 6, 2);
               player.update(dt);
               hud.update(dt);

               if(act == "act3-boss.tmx"){
                    leone.update(dt);
                    if((leone.b2body.getPosition().x < player.b2body.getPosition().x  || !leone.runningRight) && !leone.currentSprite.isFlipX()){
                         leone.currentSprite.flip(true, false);
                         leone.runningRight = false;
                    }
                    else if((leone.b2body.getPosition().x >  player.b2body.getPosition().x || leone.runningRight) && leone.currentSprite.isFlipX()){
                         leone.currentSprite.flip(true, false);
                         leone.runningRight = true;
                    }
               }

               for(Pollo enemy : creator.getPollos()){
                    enemy.update(dt);
               }

               if(cameraRumble == true){
                    Rumble.rumble(5/Jojo.PPM, .8f);
                    Rumble.tick(Gdx.graphics.getDeltaTime());
                    gamecam.translate(Rumble.getPos());
                    cameraRumble = false;
               }
               else{
                    if(player.currentState != Mafiosi.State.DEAD){
                         gamecam.position.x = player.b2body.getPosition().x;
                         gamecam.position.y = player.b2body.getPosition().y;
                    }
               }

               gamecam.update();
               renderer.setView(gamecam);
          }
          else{
               music.stop();
          }
     }

     float renderTime = 0; 
     @Override
     public void render(float delta){

          if(renderTime < 0.001){
               renderTime += delta;
               update(delta);
          }

          renderTime = 0;

          Gdx.gl.glClearColor(0, 0, 0, 1);
          Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

          Jojo.vfxManager.cleanUpBuffers();

          Jojo.vfxManager.beginInputCapture();
          if(pause == true){
               b2dr.render(world, gamecam.combined);
          }

          renderer.getBatch().setShader(Jojo.shaderProgram);
          renderer.render();
          game.batch.setProjectionMatrix(gamecam.combined);

          player.shapeRenderer.begin(ShapeType.Filled);

          game.batch.begin();

          //Life Bar
          float percentHp = player.getLifeBar()/player.getMaxLifeBar(); 
          float green = (255*percentHp);
          float red = (255 - 255*percentHp);
          
          player.shapeRenderer.setColor(toRGB((int)red, (int)green, 0));
          player.shapeRenderer.rect(50, 50, 4*player.getLifeBar(), 30);


          //Things
          game.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
          game.batch.setShader(Jojo.shaderProgram);
          player.draw(game.batch);
          if(act == "act3-boss.tmx") leone.draw(game.batch);
          for(Enemy enemy : creator.getPollos()) enemy.draw(game.batch);

          game.batch.setShader(null);
          game.batch.end();

          player.shapeRenderer.end();

          game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
          hud.stage.draw();
          Jojo.vfxManager.endInputCapture();

          if(activateEffects == true){
               Jojo.vfxManager.applyEffects();
          }
          Jojo.vfxManager.renderToScreen();

          if(change_act){
               String nAct = nextAct(act);
               String nMusic = nextMusic(act_music);

               if(nAct == "undefined" || nMusic == "undefined"){
                    change_act = false;
               }
               else{
                    game.setScreen(new PlayScreen(game, nAct, nMusic, global_score));
                    dispose();
               }
          }

          if(act == "act3-boss.tmx"){
               if(leone.setToDestroy == true) change_act = true;
          }

          if(gameOver()){
               game.setScreen(new GameOver(game, act, act_music));
               dispose();
          }
     }

     public boolean gameOver(){
          if(player.currentState == Mafiosi.State.DEAD && player.getStateTimer() > 3){
               return true;
          }
          return false;
     }

     public String nextAct(String lastAct){
          if(lastAct=="act1.tmx") return "act2.tmx";
          else if(lastAct=="act2.tmx") return "act3-boss.tmx";
          else if(lastAct=="act3-boss.tmx") return "congratulations.tmx";
          else return "undefined";
     }

     public String nextMusic(String lastMusic){
          if(lastMusic=="Sdx/Music/act1.mp3") return "Sdx/Music/act2.mp3";
          else if(lastMusic=="Sdx/Music/act2.mp3") return "Sdx/Music/act3-boss.mp3";
          else if(lastMusic == "Sdx/Music/act3-boss.mp3") return "Sdx/Music/act1.mp3";
          else return "undefined";
     }

     @Override
     public void dispose(){
          map.dispose();
          renderer.dispose();
          world.dispose();
          b2dr.dispose();
          hud.dispose();
          music.dispose();
     }

     @Override
     public void hide(){

     }

     @Override
     public void resume(){

     }

     @Override
     public void show(){

     }

     @Override
     public void resize(int width, int height){
          gamePort.update(width, height);
     }

     @Override
     public void pause(){

     }
}

