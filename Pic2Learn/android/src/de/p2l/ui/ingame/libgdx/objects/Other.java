package de.p2l.ui.ingame.libgdx.objects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Other extends Object {

    public Other (World world, TiledMap map, Rectangle rect){
        super(world, map, rect);
        fix.setUserData("other");
        filter.categoryBits = 16;
        fix.setFilterData(filter);
    }
}