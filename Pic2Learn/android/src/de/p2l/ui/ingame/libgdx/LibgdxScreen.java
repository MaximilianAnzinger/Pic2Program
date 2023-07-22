package de.p2l.ui.ingame.libgdx;

import android.content.SharedPreferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import de.p2l.service.parser.commands.Branch;
import de.p2l.service.parser.commands.Command;
import de.p2l.service.parser.commands.Condition;
import de.p2l.service.parser.commands.Falling;
import de.p2l.service.parser.commands.Idle;
import de.p2l.service.parser.commands.Interaction;
import de.p2l.service.parser.commands.Loop;
import de.p2l.service.parser.commands.StepBackward;
import de.p2l.service.parser.commands.StepForward;
import de.p2l.service.parser.commands.TurnLeft;
import de.p2l.service.parser.commands.TurnRight;
import de.p2l.service.parser.commands.Win;
import de.p2l.service.tmxreader.TMXReader;
import de.p2l.ui.ingame.libgdx.objects.ObjectCreation;
import de.p2l.ui.ingame.libgdx.player.Knolch;
import de.p2l.ui.ingame.libgdx.player.Lea;
import de.p2l.ui.ingame.libgdx.player.Olaf;
import de.p2l.ui.ingame.libgdx.player.Pic;
import de.p2l.ui.ingame.libgdx.player.Player;

import static android.content.Context.MODE_PRIVATE;
import static de.p2l.ui.menu.mainmenu.MainActivity.SHARED_PREFS;
import static de.p2l.ui.menu.mainmenu.MainActivity.getContext;

/*
LibGDXScreen loads the current map and renders it.
It instantiates the player and constantly uses its update() function.
The class also checks for collision and the case of winning
 */

public class LibgdxScreen implements Screen{

    private Pic2Learn game;

    private String level;
    private ArrayList<Command> commands;
    private int commandsIndex;
    private int numberOfCommands;

    private OrthographicCamera cam;
    private Viewport port;

    private TmxMapLoader loader;
    private TiledMap map;
    private int zielxCoordinate;
    private int zielyCoordinate;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private int skin;
    private Player player;

    private ArrayList<String> passableHoles;
    private ArrayList<String> passableStones;

    private Texture hammer;
    private Texture hammer_horizontal;

    private Texture goldenHammer;
    private Texture goldenHammer_horizontal;

    private int animationCounterHammerAvailable;
    private int animationCounterHammerNotAvailable;

    private Sound hammerSound;
    private Sound noHammerAvailableSound;

    private boolean fogOfWarLevel;

    private TMXReader tmxReader;
    private boolean[][] blockedMap;

    private boolean won;

    private SharedPreferences sharedPreferences;

    public LibgdxScreen(Pic2Learn game, String level, ArrayList<Command> commands, int numberOfCommands, int skin){

        this.game = game;
        this.level=level;
        this.commands = commands;
        this.commandsIndex = 0;
        this.numberOfCommands = numberOfCommands;

        cam = new OrthographicCamera();
        if(level.charAt(0)=='t'){
            cam.setToOrtho(false,112,192);
            port = new FitViewport(112,192,cam);
        }
        else{
            cam.setToOrtho(false,144,256);
            port = new FitViewport(144,256,cam);
        }

        fogOfWarLevel = false;

        loader = new TmxMapLoader();

        mapLoading();

        blockedMap = tmxReader.computeFieldStatus();

        renderer = new OrthogonalTiledMapRenderer(map,1f);
        renderer.setView(cam);


        //0,0 describes gravity
        world = new World(new Vector2(0,0),true);
        //graphical representation of our bodies and fixtures within the box2d world
        new ObjectCreation(world, map);
        setContactListening();
        b2dr = new Box2DDebugRenderer();
        this.skin = skin;
        switch (skin){
            case 0: player = new Pic(world,this, level);
                break;
            case 1: player = new Olaf(world, this, level);
                break;
            case 2: player = new Lea(world, this, level);
                break;
            case 3: player = new Knolch(world, this, level);
                break;
            default: System.out.print("ERROR WRONG SKIN");
                break;
        }

        passableHoles = new ArrayList<String>();

        hammer = new Texture("objects/Weapon_47.png");
        hammer_horizontal = new Texture("objects/Weapon_47_horizontal.png");

        goldenHammer = new Texture("objects/goldenHammer.png");
        goldenHammer_horizontal = new Texture("objects/goldenHammer_horizontal.png");

        animationCounterHammerAvailable = 0;
        animationCounterHammerNotAvailable = 0;

        hammerSound = Gdx.audio.newSound(Gdx.files.internal("music/punch_low_deep_impact_01.wav"));
        noHammerAvailableSound = Gdx.audio.newSound(Gdx.files.internal("music/retro_ui_menu_error_03.wav"));

        won = false;

        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    /**
     * Wertet eine Condition zu boolean aus
     * @param condition zu überprüfende Condition
     * @return Auswertung der Condition
     */
    private boolean checkCondition(Condition condition){
        switch (condition){
            case BLOCKED:
                /*DEBUG
                System.out.println(TMXReaderTest.boolArrayToString(blockedMap));
                System.out.println("x: "+player.returnX(true)+", y: "+player.returnY(true));
                System.out.println("pl x: "+player.returnX(false)+", pl y: "+player.returnY(false));    */
                return !blockedMap[player.returnX(true)][player.returnY(true)];
            case FREE:
                return blockedMap[player.returnX(true)][player.returnY(true)];
            case GOAL:
                return (zielxCoordinate==player.returnX(true))&&(zielyCoordinate==player.returnY(true));
            case NOGOAL:
                return !((zielxCoordinate==player.returnX(true))&&(zielyCoordinate==player.returnY(true)));
            default:
                throw new RuntimeException("Unknown Condition");
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(commands.size() == commandsIndex){
            if(zielxCoordinate==player.returnX(false)&&zielyCoordinate==player.returnY(false)){
                //zoomen + winning animation
                won = true;
                cam.position.set(player.getX()+player.getWidth()/2,player.getY()+player.getHeight()/2,0);
                cam.zoom = 0.3f;
                cam.update();
                renderer.setView(cam);
                commands.add(commandsIndex, new Win());
            }
        }

        //map is rendered
        renderer.render();

        player.update(delta);

        if(player.getAction() instanceof Idle && commands.size() != commandsIndex &&
                animationCounterHammerAvailable<=0&&animationCounterHammerNotAvailable<=0) {

            if (commands.get(commandsIndex) instanceof StepForward) {       //g
                if(fogOfWarLevel==true){
                    TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(2);
                    try{
                        TiledMapTileLayer.Cell cell = layer.getCell(player.returnX(true), player.returnY(true));
                        cell.setTile(null);
                    }
                    catch(Exception e){
                    }
                }
                player.setAction(1);
            } else if (commands.get(commandsIndex) instanceof TurnRight) {  //r
                player.setAction(2);
            } else if (commands.get(commandsIndex) instanceof TurnLeft) {   //l
                player.setAction(3);
            } else if (commands.get(commandsIndex) instanceof Falling) {     //f
                player.setAction(5);
            } else if (commands.get(commandsIndex) instanceof Interaction) {  //o
                TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
                try {
                    //frame in front of the player is set to null
                    //frame is added to passableHoles
                    TiledMapTileLayer.Cell cell = layer.getCell(player.returnX(true), player.returnY(true));
                    if(sharedPreferences.getInt("usingGoldenHammer",0)==1)
                        animationCounterHammerNotAvailable = 26;
                    else animationCounterHammerNotAvailable = 52;
                    cell.setTile(null);
                    animationCounterHammerNotAvailable = 0;
                    if(sharedPreferences.getInt("usingGoldenHammer",0)==1)
                        animationCounterHammerAvailable = 26;
                    else animationCounterHammerAvailable = 52;
                    passableHoles.add(player.returnX(true) + "." + player.returnY(true));
                    blockedMap[player.returnX(true)][player.returnY(true)] = true;  //damit die Überprüfung für die Schlucht dann auch stimmt
                    deleteCommand();
                } catch (Exception e) {
                    deleteCommand();
                }
            } else if(commands.get(commandsIndex) instanceof StepBackward) { //z
                player.setAction(4);
            } else if(commands.get(commandsIndex) instanceof Win) {         //w
                player.setAction(6);
            } else if(commands.get(commandsIndex) instanceof Branch) {
                if(checkCondition(((Branch) commands.get(commandsIndex)).getCondition())){
                    commands.addAll(commandsIndex+1, ((Branch) commands.get(commandsIndex)).getTrueStmts());
                    commandsIndex++;
                } else {
                    commands.addAll(commandsIndex+1, ((Branch) commands.get(commandsIndex)).getFalseStmts());
                    commandsIndex++;
                }
            } else if(commands.get(commandsIndex) instanceof Loop) {
                if(checkCondition(((Loop) commands.get(commandsIndex)).getCondition())){
                    commands.addAll(commandsIndex, ((Loop) commands.get(commandsIndex)).getLoopCommands());     //hier ohne +1, damit Loop mitgeschoben wir -> neue Auswertung
                } else {
                    deleteCommand();
                }
            } else {
                System.out.println("Command not available");
            }
        }

        //b2dr.render(world,cam.combined);

        //if timestep = 1/60f each step call will advance the game by 1/60 of a second
        //60 frames -> step() is called 60 times a second. realistic movement

        //changed to 1/120f because of accuracy
        world.step(1/120f,6,2);

        game.batch.setProjectionMatrix(cam.combined);

        game.batch.begin();

        if(animationCounterHammerAvailable>0){
            if(animationCounterHammerAvailable==25){
                hammerSound.play();
            }
            if(animationCounterHammerAvailable==10){
                hammerSound.play();
            }
            hammerAnimation();
            animationCounterHammerAvailable--;
        }

        if(animationCounterHammerNotAvailable>0){
            if(animationCounterHammerNotAvailable==15){
                noHammerAvailableSound.play();
            }
            hammerAnimation();
            animationCounterHammerNotAvailable--;
        }
        player.draw(game.batch);
        game.batch.end();

    }

    public void mapLoading(){
        if(level.equals("tut1")){
            map = loader.load("maps/newtut1.tmx");
            zielxCoordinate = 3;
            zielyCoordinate =4;
            tmxReader = new TMXReader("maps/newtut1.tmx");
        }
        else if(level.equals("tut2")){
            map = loader.load("maps/newtut2.tmx");
            zielxCoordinate = 4;
            zielyCoordinate = 3;
            tmxReader = new TMXReader("maps/newtut2.tmx");
        }
        else if(level.equals("tut3")){
            map = loader.load("maps/newtut3.tmx");
            zielxCoordinate = 3;
            zielyCoordinate = 4;
            tmxReader = new TMXReader("maps/newtut3.tmx");
        }
        else if(level.equals("tut4")) {
            map = loader.load("maps/newtutschleifen.tmx");
            zielxCoordinate = 3;
            zielyCoordinate = 9;
            tmxReader = new TMXReader("maps/newtutschleifen.tmx");

        }
        else if(level.equals("tut5")) {
            map = loader.load("maps/newtut4.tmx");
            zielxCoordinate = 3;
            zielyCoordinate = 9;
            tmxReader = new TMXReader("maps/newtut4.tmx");
        }

        //55, 40 px persons. map 200 200
        else if(level.equals("easy1")) {
            map = loader.load("maps/easy1.tmx");
            zielxCoordinate = 4;
            zielyCoordinate = 13;
            tmxReader = new TMXReader("maps/easy1.tmx");
        }
        else if(level.equals("easy2")) {
            map = loader.load("maps/easyKreis.tmx");
            zielxCoordinate = 5;
            zielyCoordinate = 3;
            tmxReader = new TMXReader("maps/easyKreis.tmx");
        }
        else if(level.equals("easy3")) {
            map = loader.load("maps/rechtslinks.tmx");
            zielxCoordinate = 6;
            zielyCoordinate = 13;
            tmxReader = new TMXReader("maps/rechtslinks.tmx");
        }
        else if(level.equals("easy4")) {
            map = loader.load("maps/UmgekehrterKreis.tmx");
            zielxCoordinate = 4;
            zielyCoordinate = 11;
            tmxReader = new TMXReader("maps/UmgekehrterKreis.tmx");
        }

        else if(level.equals("midd1")) {
            map = loader.load("maps/backKreisMiddle.tmx");
            zielxCoordinate = 4;
            zielyCoordinate = 5;
            tmxReader = new TMXReader("maps/backKreisMiddle.tmx");
        }
        else if(level.equals("midd2")) {
            map = loader.load("maps/rechtschecker.tmx");
            zielxCoordinate = 5;
            zielyCoordinate = 7;
            tmxReader = new TMXReader("maps/rechtschecker.tmx");
        }
        else if(level.equals("midd3")) {
            map = loader.load("maps/vszs.tmx");
            zielxCoordinate = 4;
            zielyCoordinate = 6;
            tmxReader = new TMXReader("maps/vszs.tmx");

        }
        else if(level.equals("midd4")) {
            map = loader.load("maps/onlyoneway.tmx");
            zielxCoordinate = 3;
            zielyCoordinate = 12;
            tmxReader = new TMXReader("maps/onlyoneway.tmx");
        }

        else if(level.equals("diff1")) {
            map = loader.load("maps/diffKreis.tmx");
            zielxCoordinate = 5;
            zielyCoordinate = 3;
            tmxReader = new TMXReader("maps/diffKreis.tmx");
        }
        else if(level.equals("diff2")) {
            map = loader.load("maps/backKreis.tmx");
            zielxCoordinate = 4;
            zielyCoordinate = 4;
            tmxReader = new TMXReader("maps/backKreis.tmx");
        }
        else if(level.equals("diff3")) {
            map = loader.load("maps/fogoftest.tmx");
            zielxCoordinate = 6;
            zielyCoordinate = 1;
            fogOfWarLevel = true;
            tmxReader = new TMXReader("maps/fogoftest.tmx");
        }
        else if(level.equals("diff4")) {
            map=loader.load("maps/wanderer3.tmx");
            zielxCoordinate= 6;
            zielyCoordinate = 12;
            tmxReader = new TMXReader("maps/wanderer3.tmx");
        }
    }

    public void hammerAnimation(){
        Texture usedTexture;
        Texture usedTexture_horizontal;
        if(sharedPreferences.getInt("usingGoldenHammer",0)==1){
            usedTexture = goldenHammer;
            usedTexture_horizontal = goldenHammer_horizontal;
        }
        else{
            usedTexture = hammer;
            usedTexture_horizontal=hammer_horizontal;
        }
        if(player.lookingLeft()==true){
            game.batch.draw(usedTexture_horizontal,player.b2body.getPosition().x-10, player.b2body.getPosition().y-5,10,10);
        }
        else if(player.lookingDown()==true) {
            if(skin==0){
                game.batch.draw(usedTexture_horizontal, player.b2body.getPosition().x - 13, player.b2body.getPosition().y - 7, 10, 10);
            }
            else{
                game.batch.draw(usedTexture_horizontal, player.b2body.getPosition().x - 12, player.b2body.getPosition().y - 5, 10, 10);
            }
        }
        else{
            game.batch.draw(usedTexture,player.b2body.getPosition().x+2,player.b2body.getPosition().y-5,10,10);
        }
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }

    public void deleteCommand(){
        this.commandsIndex++;
    }

    public void setContactListening(){
        world.setContactListener(new ContactListener() {
            //2 Objekte collide
            @Override
            public void beginContact(Contact contact) {
                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();

                //one of the colliding objets is the player
                if(a.getUserData()=="player" || b.getUserData()=="player"){
                    Fixture object;
                    if(a.getUserData()=="player"){
                        object = b;
                    }
                    else{
                        object = a;
                    }
                    //check which kind of object collides
                    if(object.getUserData() == "stone"){
                        if(won==true){
                            Filter filter = new Filter();
                            filter.categoryBits = 32;
                            object.setFilterData(filter);
                        }
                        else{
                            lost();
                        }

                    }
                    else if(object.getUserData() == "hole"){
                        //check if an interaction was already used on the gorge
                        if(passableHoles.contains(player.returnX(true)+"."+player.returnY(true))){
                            passableHoles.remove(player.returnX(true)+"."+player.returnY(true));
                            Filter filter = new Filter();
                            filter.categoryBits = 32;
                            object.setFilterData(filter);
                        }
                        else{
                            commands.add(commandsIndex+1, new StepForward());
                            commands.add(commandsIndex+2, new Falling());
                            commandsIndex++;
                            player.setFalling();
                            Filter filter = new Filter();
                            filter.categoryBits = 32;
                            object.setFilterData(filter);
                        }

                    }
                    else if(object.getUserData() == "other"){
                        lost();
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    public void levelSuccess(){
        game.callback.startActivityWin(level, numberOfCommands);
    }

    public void lost(){
        Gdx.app.exit();
    }

    @Override
    public void show() {

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


}
