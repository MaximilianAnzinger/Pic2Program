package de.p2l.ui.ingame.libgdx.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import de.p2l.ui.ingame.libgdx.LibgdxScreen;

public class Olaf extends Player {

    Texture characterSheet;
    Texture winningSheet;

    public Olaf(World world, LibgdxScreen screen, String level){
        super(world, screen, level);
        characterSheet = new Texture("persons/chara5.png");
        winningSheet = new Texture("persons/animation2.png");

        lookDown = new TextureRegion(characterSheet, 26,0,26,36);
        lookLeft = new TextureRegion(characterSheet, 26,36,26,36);
        lookRight = new TextureRegion(characterSheet, 26,72,26,36);
        lookAway = new TextureRegion(characterSheet, 26,108,26,36);

        finishRegion1 = new TextureRegion(winningSheet,0,0,47,50);
        finishRegion2 = new TextureRegion(winningSheet,47,0,47,50);
        finishRegion3 = new TextureRegion(winningSheet,94,0,47,50);

        framesWalking = new Array<TextureRegion>();
        addframes(0);
        addframes(36);
        addframes(72);
        addframes(108);

        framesWinning = new Array<TextureRegion>();
        framesWinning.add(finishRegion1);
        framesWinning.add(finishRegion2);
        framesWinning.add(finishRegion3);
        winning = new Animation<TextureRegion>(0.1f,framesWinning);

        setRegion(lookAway);
    }

    private void addframes(int a){
        framesWalking.add(new TextureRegion(characterSheet,0,a,26,36));
        framesWalking.add(new TextureRegion(characterSheet,26,a,26,36));
        framesWalking.add(new TextureRegion(characterSheet,52,a,26,36));

        switch(a){                                                              //hier
            case 0:  goingDown = new Animation(0.1f,framesWalking);
                break;
            case 36: goingLeft = new Animation(0.1f,framesWalking);
                break;
            case 72: goingRight = new Animation(0.1f,framesWalking);
                break;
            case 108:goingUp =new Animation(0.1f,framesWalking);
                break;
            default:System.out.println("Invalid frameset");
        }
        framesWalking.clear();
    }
}