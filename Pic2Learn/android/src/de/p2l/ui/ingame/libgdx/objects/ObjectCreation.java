package de.p2l.ui.ingame.libgdx.objects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/*
ObjectCreation works as a delegate and instantiates the different Objects.
(Not the illustrations!)
 */

public class ObjectCreation {

    public ObjectCreation(World world, TiledMap map){

        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            //typecast object to rectangle
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            //exactly this rectangle now becomes a stone
            new de.p2l.ui.ingame.libgdx.objects.Stone(world, map, rect);
        }

        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Gorge(world, map, rect);
        }

        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new de.p2l.ui.ingame.libgdx.objects.Other(world, map, rect);
        }
    }
}
