package com.mygdx.game.Tools;

import com.mygdx.game.Jojo;
import com.mygdx.game.Sprites.Enemies.*;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.TileObjects.*;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


public class b2WorldCreator{

     private Array<Pollo> pollos;

     public b2WorldCreator(PlayScreen screen){
          World world = screen.getWorld();
          TiledMap map = screen.getMap();

          BodyDef bdef = new BodyDef();
          PolygonShape shape = new PolygonShape();
          FixtureDef fdef = new FixtureDef();
          Body body;

          for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
               Rectangle rect = ((RectangleMapObject) object).getRectangle();
               bdef.type = BodyDef.BodyType.StaticBody;
               bdef.position.set(  (rect.getX() + rect.getWidth()/2)/Jojo.PPM, (rect.getY() + rect.getHeight()/2)/Jojo.PPM);
               body = world.createBody(bdef);
               shape.setAsBox((rect.getWidth()/2)/Jojo.PPM, (rect.getHeight()/2)/Jojo.PPM);
               fdef.shape = shape;
               body.createFixture(fdef);
          }

          for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
               new Bricks(screen, object);
          }
          for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
               new Coin(screen, object);
          }
          for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
               new Life(screen, object);
          }
          for(MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
               new Door(screen, object);
          }

          pollos = new Array<Pollo>();
          for( int i = 6; i < 11; i++){
               for(MapObject object : map.getLayers().get(i).getObjects().getByType(RectangleMapObject.class)){
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                         pollos.add(new Pollo(screen, rect.getX() / Jojo.PPM, rect.getY() / Jojo.PPM));
                    }
          }

     }

     public Array<Pollo> getPollos() {
        return pollos;
    }
}
