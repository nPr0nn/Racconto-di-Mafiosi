package com.mygdx.game.Sprites.TileObjects;

import com.mygdx.game.Jojo;
import com.mygdx.game.Scenes.HUD;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mafiosi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by brentaureli on 8/28/15.
 */
public class Bricks extends InteractiveTileObject {

     Sound sound;
     public Bricks(PlayScreen screen, MapObject object){
          super(screen, object);
          fixture.setUserData(this);
          setCategoryFilter(Jojo.BRICK_BIT);
          sound = Gdx.audio.newSound(Gdx.files.internal("Sdx/Sounds/breakBrick.wav"));
     }

     @Override 
     public void onHeadHit(Mafiosi mafiosi){
          setCategoryFilter(Jojo.DESTROYED_BIT);
          getCell().setTile(null);
          HUD.addScore(20);
     }
}
