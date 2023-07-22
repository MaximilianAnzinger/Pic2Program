package de.p2l.ui.ingame.libgdx.objects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Object {
    protected World world;
    protected TiledMap map;
    protected Rectangle rect;

    //properties of an object, e.g. mass, location, velocity..
    protected Body body;
    //bodies are made by setting first up a definition, then creating it
    protected BodyDef bdef;

    //fixtures for size, shape, ...
    protected Fixture fix;
    protected FixtureDef fdef;
    //shape checks for collision
    protected PolygonShape shape;

    protected Filter filter;

    public Object(World world, TiledMap map, Rectangle rect){
        this.world = world;
        this.map = map;
        this.rect = rect;

        bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((rect.getX() + rect.getWidth()/2),(rect.getY()+rect.getHeight()/2));
        body = world.createBody(bdef);

        fdef = new FixtureDef();
        shape = new PolygonShape();
        shape.setAsBox((rect.getWidth()/2),(rect.getHeight()/2));
        fdef.shape = shape;
        fix = body.createFixture(fdef);

        //category bits in each of the objects for collision
        filter = new Filter();
    }
}
