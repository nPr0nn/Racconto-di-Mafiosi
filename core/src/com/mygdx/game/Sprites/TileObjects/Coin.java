package com.mygdx.game.Sprites.TileObjects;

import com.mygdx.game.Jojo;
import com.mygdx.game.Scenes.HUD;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mafiosi;

import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Coin extends InteractiveTileObject {

     Sound sound;

     public Coin(PlayScreen screen, MapObject object){
          super(screen, object);
          fixture.setUserData(this);
          setCategoryFilter(Jojo.COIN_BIT);
          sound = Gdx.audio.newSound(Gdx.files.internal("Sdx/Sounds/coin.wav"));
     }

     @Override 
     public void onHeadHit(Mafiosi mafiosi){
          setCategoryFilter(Jojo.DESTROYED_BIT);
          getCell().setTile(null);
          sound.play();
          screen.global_score += 10;
          HUD.addScore(10);
     }

}
