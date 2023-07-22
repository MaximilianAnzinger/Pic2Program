package de.p2l.service.parser.parser;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.p2l.R;
import de.p2l.service.parser.commands.Branch;

import static android.content.Context.MODE_PRIVATE;
import static de.p2l.ui.menu.mainmenu.MainActivity.SHARED_PREFS;
import static de.p2l.ui.menu.mainmenu.MainActivity.getContext;

public class InputToImg {
    private static SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);;
    private static final String TAG = "InputToImg";

    public static int parse(Input input){
        switch (input){
            case FORWARD:
                return R.drawable.forward;
            case BACKWARD:
                return R.drawable.backward;
            case LEFTTURN:
                return R.drawable.leftturn;
            case RIGHTTURN:
                return R.drawable.rightturn;
            case INTERACT:
                if (sharedPreferences.getInt("usingGoldenHammer",0)==1) return R.drawable.interactgold;
                return R.drawable.interact;
            case BRANCHKEY:
                //ContextCompat.getDrawable(getContext(),R.drawable.branchkey).setColorFilter(colorList.get(a), PorterDuff.Mode.MULTIPLY );
                return R.drawable.branchkey;
            case LOOPKEY:
                //ContextCompat.getDrawable(getContext(),R.drawable.loopkey).setColorFilter(colorList.get(a), PorterDuff.Mode.MULTIPLY );
                return R.drawable.loopkey;
            case FREEIN:
                return R.drawable.freein;
            case BLOCKEDIN:
                return R.drawable.blockedin;
            case BLOCKEND:
                return R.drawable.blockend;
            case GOALIN:
                return R.drawable.goalin;
            case NOGOALIN:
                return R.drawable.nogoalin;
                default:
                    Log.i(TAG, "invalid input - no image available");
                    return R.drawable.blank;
        }
    }
    public static ArrayList<Integer> parseList(ArrayList<Input> inputList){
        ArrayList<Integer> result = new ArrayList<>();
        for (Input input : inputList){
            result.add(parse(input));
        }
        return result;
    }
}
