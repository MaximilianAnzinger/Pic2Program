package de.p2l.ui.ingame.libgdx.player;

import android.content.SharedPreferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import de.p2l.service.parser.commands.Command;
import de.p2l.service.parser.commands.Falling;
import de.p2l.service.parser.commands.Idle;
import de.p2l.service.parser.commands.StepBackward;
import de.p2l.service.parser.commands.StepForward;
import de.p2l.service.parser.commands.TurnLeft;
import de.p2l.service.parser.commands.TurnRight;
import de.p2l.service.parser.commands.Win;
import de.p2l.ui.ingame.libgdx.LibgdxScreen;

import static android.content.Context.MODE_PRIVATE;
import static com.badlogic.gdx.math.MathUtils.floor;
import static de.p2l.ui.menu.mainmenu.MainActivity.SHARED_PREFS;
import static de.p2l.ui.menu.mainmenu.MainActivity.getContext;

/*
The Player class is constantly used within the LibGDXScreen by calling update()
Only when the rendering process of an action is finished, update() can call another
action function
 */

public abstract class Player extends Sprite {

    protected World world;
    protected LibgdxScreen screen;

    protected TextureRegion lookDown, lookLeft, lookRight, lookAway;
    protected TextureRegion finishRegion1, finishRegion2, finishRegion3;

    protected Array<TextureRegion> framesWalking;
    protected Array<TextureRegion> framesWinning;

    protected Animation<TextureRegion> goingUp;
    protected Animation<TextureRegion> goingLeft;
    protected Animation<TextureRegion> goingRight;
    protected Animation<TextureRegion> goingDown;
    protected Animation<TextureRegion> winning;

    protected float width,height;
    public Body b2body;
    protected BodyDef bdef;
    protected Fixture fix;
    protected FixtureDef fdef;
    protected PolygonShape shape;

    protected Command currentAction;
    public enum directions {
        lookingAway, lookingLeft, lookingRight, lookingDown};
    protected directions direction;
    protected Vector2 oldPos;
    protected float stateTime;
    protected int xFeld,yFeld;
    protected int xCoordinate, yCoordinate;

    protected int way2go;
    protected boolean falling;

    protected int finCounter;
    protected int animationCounter;

    protected Sound endSound;

    protected SharedPreferences sharedPreferences;

    protected boolean lastlyWentForwards;

    public Player(World world, LibgdxScreen screen, String level){
        this.world = world;
        this.screen=screen;

        width= 16f;
        height = 16f;
        bdef = new BodyDef();
        //hier
        //easy1: 4,1
        //easy2: 1,2
        //easy3: 1,2
        //easy4: 1,2
        //diff1: 1,2
        //diff2: ??
        //diff3: 1,2
        //diff4: 1,2

        initiatePos(level);

        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        fdef = new FixtureDef();
        shape = new PolygonShape();
        shape.setAsBox(2.5f,5f);
        fdef.filter.categoryBits = 2;
        fdef.filter.maskBits = 4 | 8 | 16;
        fdef.shape = shape;
        fix = b2body.createFixture(fdef);
        fix.setUserData("player");

        setBounds(56f,26.5f,width,height);
        //setRegion(lookAway);
        setPosition(b2body.getPosition().x-getWidth()/2, b2body.getPosition().y - getHeight()/2);

        currentAction = new Idle();
        direction = directions.lookingAway;

        stateTime=0;

        way2go=16;
        falling=false;

        finCounter=0;
        animationCounter=0;

        endSound=Gdx.audio.newSound(Gdx.files.internal("music/battle_viking_horn_call_close_01.wav"));

        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        lastlyWentForwards = true;

    }

    public void computeXY(){
        xCoordinate = xFeld * 16 + 8 ;
        yCoordinate = yFeld * 16 + 8 + 1;
    }

    public void update(float delta){
        setPosition(b2body.getPosition().x-getWidth()/2, b2body.getPosition().y - getHeight()/2);
        int velocity = 50;
        if(sharedPreferences.getInt("usingGoldenBoots", 0)==1) velocity = 70;
        if(currentAction instanceof TurnRight){
            turnRight(delta);
        }

        else if(currentAction instanceof TurnLeft){                              //same
            turnLeft(delta);
        }

        else if(currentAction instanceof StepForward){
            goForward(delta, velocity);
        }

        else if(currentAction instanceof StepBackward){
            goBack(delta, velocity);
        }

        else if(currentAction instanceof Falling){
            playerFalling();

        }

        else if(currentAction instanceof Win){
            playerWinning(delta);
        }
    }

    public void turnRight(float delta){
        switch(direction){
            case lookingDown: direction = directions.lookingLeft;
                setRegion(lookLeft);
                break;
            case lookingRight: direction = directions.lookingDown;
                setRegion(lookDown);
                break;
            case lookingLeft: direction = directions.lookingAway;
                setRegion(lookAway);
                break;
            case lookingAway: direction = directions.lookingRight;
                setRegion(lookRight);
                break;
            default:System.out.println("invalid direction");
                break;
        }
        stateTime = stateTime + delta;
        currentAction = new Idle();
        screen.deleteCommand();
    }

    public void turnLeft(float delta){
        switch(direction){
            case lookingDown: direction = directions.lookingRight;
                setRegion(lookRight);
                break;
            case lookingRight: direction = directions.lookingAway;
                setRegion(lookAway);
                break;
            case lookingLeft: direction = directions.lookingDown;
                setRegion(lookDown);
                break;
            case lookingAway: direction = directions.lookingLeft;
                setRegion(lookLeft);
                break;
            default:System.out.println("invalid direction");
                break;
        }
        stateTime = stateTime + delta;
        currentAction = new Idle();
        screen.deleteCommand();
    }

    public void goForward(float delta, int velocity){
        way2go = 16;
        computeXY();
        lastlyWentForwards = true;
        switch(direction){
            case lookingDown:

                if(falling==true){
                    way2go = 4;
                }
                if(yCoordinate-way2go<b2body.getPosition().y){
                    b2body.setLinearVelocity(0,-velocity);
                    TextureRegion region = goingDown.getKeyFrame(stateTime,true);
                    setRegion(region);
                    stateTime = stateTime + delta;
                }
                else{
                    setRegion(lookDown);
                    yFeld--;
                    endOfAction();
                }
                break;
            case lookingRight:
                if(falling==true){
                    way2go = 10;
                }
                if(xCoordinate+way2go>b2body.getPosition().x){
                    b2body.setLinearVelocity(velocity,0);
                    TextureRegion region = goingRight.getKeyFrame(stateTime,true);
                    setRegion(region);
                    stateTime = stateTime + delta;
                }
                else{
                    setRegion(lookRight);
                    xFeld++;
                    endOfAction();
                }
                break;
            case lookingLeft:
                if(falling==true){
                    way2go = 10;
                }
                if(xCoordinate-way2go<b2body.getPosition().x){
                    b2body.setLinearVelocity(-velocity,0);
                    TextureRegion region = goingLeft.getKeyFrame(stateTime,true);
                    setRegion(region);
                    stateTime = stateTime + delta;
                }
                else{
                    setRegion(lookLeft);
                    xFeld--;
                    endOfAction();
                }
                break;
            case lookingAway:
                if(yCoordinate+16>b2body.getPosition().y) {
                    b2body.setLinearVelocity(0, velocity);
                    TextureRegion region = goingUp.getKeyFrame(stateTime, true);
                    setRegion(region);
                    stateTime = stateTime + delta;
                }

                else{
                    setRegion(lookAway);
                    yFeld++;
                    endOfAction();
                }
                break;
            default: System.out.println("invalid direction");
                break;
        }
    }

    public void goBack(float delta, int velocity){
        way2go = 16;
        computeXY();
        lastlyWentForwards = false;
        switch(direction){
            case lookingDown:

                if(falling==true){
                    way2go = 4;
                }
                if(yCoordinate+16>b2body.getPosition().y) {
                    b2body.setLinearVelocity(0, velocity);
                    TextureRegion region = goingDown.getKeyFrame(stateTime, true);
                    setRegion(region);
                    stateTime = stateTime + delta;
                }

                else{
                    setRegion(lookDown);
                    yFeld++;
                    endOfAction();
                }
                break;
            case lookingRight:
                if(falling==true){
                    way2go = 10;
                }
                if(xCoordinate-way2go<b2body.getPosition().x){
                    b2body.setLinearVelocity(-velocity,0);
                    TextureRegion region = goingRight.getKeyFrame(stateTime,true);
                    setRegion(region);
                    stateTime = stateTime + delta;
                }
                else{
                    setRegion(lookRight);
                    xFeld--;
                    endOfAction();
                }
                break;
            case lookingLeft:
                if(falling==true){
                    way2go = 10;
                }
                if(xCoordinate+way2go>b2body.getPosition().x){
                    b2body.setLinearVelocity(velocity,0);
                    TextureRegion region = goingLeft.getKeyFrame(stateTime,true);
                    setRegion(region);
                    stateTime = stateTime + delta;
                }
                else{
                    setRegion(lookLeft);
                    xFeld++;
                    endOfAction();
                }
                break;
            case lookingAway:
                if(yCoordinate-way2go<b2body.getPosition().y){
                    b2body.setLinearVelocity(0,-velocity);
                    TextureRegion region = goingUp.getKeyFrame(stateTime,true);
                    setRegion(region);
                    stateTime = stateTime + delta;
                }
                else{
                    setRegion(lookAway);
                    yFeld--;
                    endOfAction();
                }
                break;
            default: System.out.println("invalid direction");
                break;
        }
    }

    public void playerFalling(){
        if(direction!=directions.lookingDown){
            if(width>0) {
                b2body.setAngularVelocity(2*(MathUtils.PI-b2body.getAngle()));
                //Body is decreases slower at the beginning
                if (width > 8) {
                    width = width - 0.25f;
                    height = height - 0.25f;
                    setBounds(b2body.getPosition().x - width / 2, b2body.getPosition().y - height / 2, width, height);
                } else {
                    width = width - 0.35f;
                    height = height - 0.35f;
                    setBounds(b2body.getPosition().x - width / 2, b2body.getPosition().y - height / 2, width, height);
                }
            }
            else{
                screen.lost();
            }
        }
        else{
            //Exception for lookingDown: hero first off walks a little bit and then starts falling
            if(width>0) {
                if (width > 14) {
                    width = width - 0.14f;
                    height = height - 0.14f;
                    setBounds(b2body.getPosition().x - width / 2, b2body.getPosition().y - height / 2, width, height);
                    b2body.setLinearVelocity(0, -120);
                } else {
                    width = width - 0.3f;
                    height = height - 0.3f;
                    setBounds(b2body.getPosition().x - width / 2, b2body.getPosition().y - height / 2, width, height);
                    b2body.setLinearVelocity(0, 0);
                }
            }
            else{
                Gdx.app.exit();
            }
        }
    }

    public void playerWinning(float delta){
        if(finCounter==0){
            //place the body at the correct position, look right
            endSound.play(2f);
            direction = directions.lookingRight;
            setPosition(b2body.getPosition().x-getWidth()/2-8, b2body.getPosition().y - getHeight()/2+1);
            oldPos.x = floor(b2body.getPosition().x);
            oldPos.y = floor(b2body.getPosition().y);
            finCounter++;
        }
        else if(finCounter==1){
            //walk the stairs upwards
            if(oldPos.x+6.5>b2body.getPosition().x){
                b2body.setLinearVelocity(17.5f,22.5f);
                TextureRegion region = goingRight.getKeyFrame(stateTime,true);
                setRegion(region);
                stateTime = stateTime + delta;
            }
            else{
                b2body.setLinearVelocity(0,0);
                setRegion(lookDown);
                finCounter++;
            }
        }
        //for 60 Update rounds winning animation. Then levelSuccess()
        else if(finCounter==2&&animationCounter<60){
            setBounds(b2body.getPosition().x-width/2-3,b2body.getPosition().y-height/2,width+7,height+7);
            TextureRegion region=winning.getKeyFrame(stateTime,true);
            setRegion(region);
            stateTime = stateTime + delta;
            animationCounter++;
        }
        else{
            setBounds(b2body.getPosition().x-width/2-3,b2body.getPosition().y-height/2,width+7,height+7);
            screen.levelSuccess();
        }
    }

    public Command getAction(){
        return currentAction;
    }

    public boolean lookingLeft() {
        if(direction == directions.lookingLeft){
            return true;
        }
        else return false;
    }

    public boolean lookingDown() {
        if(direction == directions.lookingDown){
            return true;
        }
        else return false;
    }

    //Current action of the player. is Executed in update as long as not changed
    public void setAction(int x){
        if(x==1) currentAction = new StepForward();
        else if(x==2) currentAction = new TurnRight();
        else if(x==3) currentAction = new TurnLeft();
        else if(x==4) currentAction = new StepBackward();
        else if(x==5) currentAction = new Falling();
        else if(x==6) currentAction = new Win();
    }

    public void setFalling(){
        falling=true;
    }

    public void endOfAction(){
        b2body.setLinearVelocity(0,0);
        oldPos.x = floor(b2body.getPosition().x);
        oldPos.y = floor(b2body.getPosition().y);
        currentAction = new Idle();
        System.out.println("x: " + b2body.getPosition().x);
        System.out.println("y: " + b2body.getPosition().y);
        screen.deleteCommand();
    }

    //Returns X value, if withDir true then the line of sight is added as well
    public int returnX(boolean withDir){
        int dir =0;
        if(withDir==true){
            if(direction==directions.lookingRight) dir=1;
            if(direction==directions.lookingLeft) dir=-1;
        }
        return xFeld+dir;
    }

    //same for y
    public int returnY(boolean withDir){
        int dir =0;
        if(withDir==true){
            if(direction==directions.lookingDown) dir=-1;
            if(direction==directions.lookingAway) dir=1;
        }
        return yFeld+dir;
    }

    public void initiatePos(String level){
        if(level.equals("easy1")){
            bdef.position.set(72f,25f);
            oldPos = new Vector2(72f,25f);
            xFeld=4;
            yFeld=1;
        }
        else if(level.equals("diff4")||level.equals("easy4")){
            xFeld = 2;
            yFeld = 1;
            bdef.position.set(40f, 25f);
            oldPos = new Vector2(40f, 25f);
        }
        else if(level.equals("easy2")||level.equals("easy3")||level.equals("midd1")||
                level.equals("midd2")||level.equals("diff1")||level.equals("diff3")||level.equals("diff2")){
            bdef.position.set(24f,41f);
            oldPos = new Vector2(24f,41f);
            xFeld=1;
            yFeld=2;
        }
        else if(level.equals("midd4")||level.equals("midd3")){
            bdef.position.set(120f,41f);
            oldPos = new Vector2(120f,41f);
            xFeld=7;
            yFeld=2;
        }
        else{
            bdef.position.set(56f,25f);
            oldPos = new Vector2(56f,25f);
            xFeld=3;
            yFeld=1;
        }
    }

    public directions getDirection(){
        return this.direction;
    }
}
