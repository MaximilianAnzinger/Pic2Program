package de.p2l.ui.ingame.libgdx.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import de.p2l.ui.ingame.libgdx.LibgdxScreen;

public class Knolch extends Player{

    Texture characterSheet;
    Texture winningSheet;

    public Knolch(World world, LibgdxScreen screen, String level){
        super(world, screen, level);
        characterSheet = new Texture("persons/chara5.png");
        winningSheet = new Texture("persons/animation2.png");

        lookDown = new TextureRegion(characterSheet, 260,144,26,36);
        lookLeft = new TextureRegion(characterSheet, 260,180,26,36);
        lookRight = new TextureRegion(characterSheet, 260,216,26,36);
        lookAway = new TextureRegion(characterSheet, 260,252,26,36);

        finishRegion1 = new TextureRegion(winningSheet,141,150,47,50);
        finishRegion2 = new TextureRegion(winningSheet,188,150,47,50);
        finishRegion3 = new TextureRegion(winningSheet,235,150,47,50);

        framesWalking = new Array<TextureRegion>();
        addframes(144);
        addframes(180);
        addframes(216);
        addframes(252);

        framesWinning = new Array<TextureRegion>();
        framesWinning.add(finishRegion1);
        framesWinning.add(finishRegion2);
        framesWinning.add(finishRegion3);
        winning = new Animation<TextureRegion>(0.1f,framesWinning);

        setRegion(lookAway);
    }

    private void addframes(int a){
        framesWalking.add(new TextureRegion(characterSheet,234,a,26,36));
        framesWalking.add(new TextureRegion(characterSheet,260,a,26,36));
        framesWalking.add(new TextureRegion(characterSheet,288,a,26,36));

        switch(a){                                                              //hier
            case 144:  goingDown = new Animation(0.1f,framesWalking);
                break;
            case 180: goingLeft = new Animation(0.1f,framesWalking);
                break;
            case 216: goingRight = new Animation(0.1f,framesWalking);
                break;
            case 252:goingUp =new Animation(0.1f,framesWalking);
                break;
            default:System.out.println("Invalid frameset");
        }
        framesWalking.clear();
    }
}
