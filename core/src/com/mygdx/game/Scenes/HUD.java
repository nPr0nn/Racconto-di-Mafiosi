package com.mygdx.game.Scenes;

import com.mygdx.game.Jojo;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.Screens.PlayScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HUD implements Disposable{

     //Scene2D.ui Stage and its own Viewport for HUD
     public Stage stage;
     private Viewport viewport;

     public static Integer worldTimer;
     private boolean timeUp; // true when the world timer reaches 0
     private float timeCount;
     private static Integer score;

     //Scene2D widgets
     private Label countdownLabel;
     private static Label scoreLabel;
     private Label timeLabel;
     private Label levelLabel;
     private Label worldLabel;
     private Label marioLabel;

     BitmapFont font;

     public HUD(SpriteBatch sb, String act, int score){
          //define our tracking variables
          this.score = score;

          Texture texture = new Texture(Gdx.files.internal("fonteTop.png"));
          texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

          font = new BitmapFont(Gdx.files.internal("fonteTop.fnt"), new TextureRegion(texture), false);
          font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
          worldTimer = 400;
          timeCount = 0;

          Color hudColor = new Color(243, 136, 247, 1);
          viewport = new FitViewport((float)Jojo.V_WIDTH, (float)Jojo.V_HEIGHT, new OrthographicCamera());
          stage = new Stage(viewport, sb);

          Table table = new Table();
          table.top();
          table.setFillParent(true);

          countdownLabel = new Label(   String.valueOf(worldTimer), new Label.LabelStyle(font, hudColor)   );
          scoreLabel =new Label( String.valueOf(score), new Label.LabelStyle(font, hudColor));
          timeLabel = new Label("Tempo", new Label.LabelStyle(font, hudColor));

          if(act == "act1.tmx") levelLabel = new Label("1-1", new Label.LabelStyle(font, hudColor));
          else if(act == "act2.tmx") levelLabel = new Label("2-1", new Label.LabelStyle(font, hudColor));
          else if(act == "act3-boss.tmx") levelLabel = new Label("3-1", new Label.LabelStyle(font, hudColor));
          else levelLabel = new Label("4-1", new Label.LabelStyle(font, hudColor));


          worldLabel = new Label("ACT", new Label.LabelStyle(font, hudColor));
          marioLabel = new Label("Monet", new Label.LabelStyle(font, hudColor));

          //add our labels to our table, padding the top, and giving them all equal width with expandX
          table.add(marioLabel).expandX().padTop(10);
          table.add(worldLabel).expandX().padTop(10);
          table.add(timeLabel).expandX().padTop(10);
          //add a second row to our table
          table.row();
          table.add(scoreLabel).expandX();
          table.add(levelLabel).expandX();
          table.add(countdownLabel).expandX();

          //add our table to the stage
          stage.addActor(table);
     }

     public void update(float dt){
          timeCount += dt;
          if(timeCount >= 1){
               if (worldTimer > 0) {
                    worldTimer--;
               } else {
                    timeUp = true;
               }
               countdownLabel.setText(worldTimer);
               timeCount = 0;
          }
     }

     public static void addScore(int value){
          score += value;
          scoreLabel.setText(score);
     }

     @Override
     public void dispose() { stage.dispose(); }

     public boolean isTimeUp() { return timeUp; }
}
