package de.p2l.ui.ingame.libgdx.objects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Gorge extends de.p2l.ui.ingame.libgdx.objects.Object {

    public Gorge(World world, TiledMap map, Rectangle rect){
        super(world, map, rect);
        fix.setUserData("hole");
        filter.categoryBits = 8;
        fix.setFilterData(filter);
    }
}
