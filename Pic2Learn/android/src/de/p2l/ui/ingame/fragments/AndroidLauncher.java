package de.p2l.ui.ingame.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.ArrayList;

import de.p2l.service.parser.commands.Command;
import de.p2l.ui.ingame.libgdx.Pic2Learn;
import de.p2l.ui.menu.mainmenu.MainActivity;

import static de.p2l.ui.menu.mainmenu.MainActivity.SHARED_PREFS;

/*
AndroidLauncher takes all the user input from the Android XML files and
then starts the LibGDX components.
 */

public class AndroidLauncher extends AndroidApplication implements Pic2Learn.MyGameCallback {

    ArrayList<Command> commands;
    Pic2Learn p2l;
    String level;
    int numberOfCommands;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        Intent intent = getIntent();
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int skin = sharedPreferences.getInt("currentBtn",0);
        intent.setExtrasClassLoader(Command.class.getClassLoader());

        commands = intent.getParcelableArrayListExtra("commands");
        level = intent.getStringExtra("level");
        numberOfCommands = intent.getIntExtra("numberOfCommands",-1);
        p2l = new Pic2Learn();
        System.out.println("numberofCommands in AndroidL" + numberOfCommands);
        p2l.setCommands(commands);
        p2l.setLevel(level);
        p2l.setNumberOfCommands(numberOfCommands);
        p2l.setMyGameCallback(this);
        p2l.setSkin(skin);
        initialize(p2l, config);
    }

    @Override
    public void startActivityWin(String level, int numberOfCommands) {

        if(level.equals("tut5")){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("easyBtn", 1);
            editor.apply();
        }
        if(level.equals("easy4")){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("middBtn", 1);
            editor.apply();
        }
        if(level.equals("midd4")){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("diffBtn", 1);
            editor.apply();
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("level",level);
        intent.putExtra("numberOfCommands", numberOfCommands);
        startActivity(intent);
    }

}