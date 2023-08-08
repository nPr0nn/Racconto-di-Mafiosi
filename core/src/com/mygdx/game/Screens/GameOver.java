package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

import com.mygdx.game.Jojo;

public class GameOver implements Screen {
     private Viewport viewport;
     private Stage stage;
     BitmapFont font;

     private Game game;
     private String act;
     private String act_music;

     public GameOver(Game game, String act, String act_music){
          this.game = game;
          this.act = act;
          this.act_music = act_music;

          viewport = new FitViewport(Jojo.V_WIDTH, Jojo.V_HEIGHT, new OrthographicCamera());
          stage = new Stage(viewport, ((Jojo) game).batch);

          Texture texture = new Texture(Gdx.files.internal("fonteTop.png"));
          texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

          font = new BitmapFont(Gdx.files.internal("fonteTop.fnt"), new TextureRegion(texture), false);


          Table table = new Table();
          table.center();
          table.setFillParent(true);

          Color hudColor = new Color(243, 136, 247, 1);

          Label gameOverLabel = new Label("GAME OVER", new Label.LabelStyle(font, hudColor));
          Label playAgainLabel = new Label("Click to Play Again", new Label.LabelStyle(font, hudColor));

          table.add(gameOverLabel).expandX();
          table.row();
          table.add(playAgainLabel).expandX().padTop(10f);

          stage.addActor(table);
     }

     @Override
     public void show() {

     }

     @Override
     public void render(float delta) {
          if(Gdx.input.justTouched()) {
               game.setScreen(new PlayScreen((Jojo) game, act, act_music, 0));
               dispose();
          }
          Gdx.gl.glClearColor(0, 0, 0, 1);
          Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
          stage.draw();
     }

     @Override
     public void resize(int width, int height) {

     }

     @Override
     public void pause() {

     }

     @Override
     public void resume() {

     }

     @Override
     public void hide() {

     }

     @Override
     public void dispose() {
          stage.dispose();
     }
}
