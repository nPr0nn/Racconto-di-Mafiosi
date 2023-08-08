package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;


import com.mygdx.game.Screens.PlayScreen;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.crashinvaders.vfx.effects.util.MixEffect;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.*;


public class Jojo extends Game {

     public static final int V_WIDTH  = 800;
     public static final int V_HEIGHT = 408;
     public static final float PPM = 100;
     public SpriteBatch batch;

     public String vertexShader;
     public String fragmentShader;
     public static ShaderProgram shaderProgram;
     public static VfxManager vfxManager;

     public static BloomEffect vfxEffect;
     public static RadialDistortionEffect vfxEffect2;
     public static OldTvEffect vfxEffect3;
     public static VignettingEffect vfxEffect4;
     public static MotionBlurEffect vfxEffect5;

     public static final short NOTHING_BIT      = 0;
     public static final short GROUND_BIT       = 1 << 0;
     public static final short PLAYER_BIT       = 1 << 1;
     public static final short BRICK_BIT        = 1 << 2;
     public static final short COIN_BIT         = 1 << 3;
     public static final short DESTROYED_BIT    = 1 << 4;
     public static final short OBJECT_BIT       = 1 << 5;
     public static final short ENEMY_BIT        = 1 << 6;
     public static final short ENEMY_HEAD_BIT   = 1 << 7;
     public static final short ITEM_BIT         = 1 << 8;
     public static final short PLAYER_HEAD_BIT  = 1 << 9;
     public static final short BULLET_BIT       = 1 << 10;
     public static final short LIFE_BIT         = 1 << 11;
     public static final short DOOR_BIT         = 1 << 12;
     public static final short BULLET_ENEMY_BIT = 1 << 13;
     public static final short BULLET_BOSS_BIT  = 1 << 13;

     @Override
     public void create () {
          Gdx.app.setLogLevel(Application.LOG_DEBUG);

          batch = new SpriteBatch();
          setScreen(new PlayScreen(this, "act1.tmx", "Sdx/Music/act1.mp3", 0));
          vertexShader = Gdx.files.internal("Shaders/vertex.glsl").readString();
          fragmentShader = Gdx.files.internal("Shaders/fragment.glsl").readString();

          shaderProgram = new ShaderProgram(vertexShader,fragmentShader);

          vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
          vfxEffect = new BloomEffect();
          vfxEffect2 = new RadialDistortionEffect();
          vfxEffect3 = new OldTvEffect();
          vfxEffect4 = new VignettingEffect(false);
          vfxEffect5 = new MotionBlurEffect(Pixmap.Format.RGBA8888, MixEffect.Method.MAX, 0.35f);

          vfxManager.addEffect(vfxEffect);
          vfxManager.addEffect(vfxEffect2);
          vfxManager.addEffect(vfxEffect3);
          vfxManager.addEffect(vfxEffect4);
          vfxManager.addEffect(vfxEffect5);
     }

     @Override
     public void render () {
          super.render();
     }

}
