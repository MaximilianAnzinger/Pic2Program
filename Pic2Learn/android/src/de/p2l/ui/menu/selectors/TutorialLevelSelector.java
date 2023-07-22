package de.p2l.ui.menu.selectors;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.p2l.ui.ingame.fragments.PlayScreen;
import de.p2l.R;

import static de.p2l.ui.menu.mainmenu.MainActivity.SHARED_PREFS;


public class TutorialLevelSelector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_level_selector);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int binCheck1 = sharedPreferences.getInt("tut1", 0);
        int binCheck2 = sharedPreferences.getInt("tut2", 0);
        int binCheck3 = sharedPreferences.getInt("tut3", 0);
        int binCheck4 = sharedPreferences.getInt("tut4", 0);
        int binCheck5 = sharedPreferences.getInt("tut5", 0);
        Button button2 = findViewById(R.id.TLSLevel2Btn);
        if(binCheck1>0){
            button2.setEnabled(true);
            button2.invalidate();
        }
        Button button3 = findViewById(R.id.TLSLevel3Btn);
        if(binCheck2>0){
            button3.setEnabled(true);
            button3.invalidate();
        }
        Button button4 = findViewById(R.id.TLSLevel4Btn);
        if(binCheck3>0){
            button4.setEnabled(true);
            button4.invalidate();
        }
        Button button5 = findViewById(R.id.TLSLevel5Btn);
        if(binCheck4>0){
            button5.setEnabled(true);
            button5.invalidate();
        }
        if(binCheck5>0){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("easyBtn", 1);
            editor.apply();
        }
    }

    public void toLevelOne (View view){
        Intent intent = new Intent(this, PlayScreen.class);
        intent.putExtra("level_id", "tut1");
        startActivity(intent);
    }

    public void toLevelTwo (View view){
        Intent intent = new Intent(this, PlayScreen.class);
        intent.putExtra("level_id", "tut2");
        startActivity(intent);
    }

    public void toLevelThree (View view){
        Intent intent = new Intent(this, PlayScreen.class);
        intent.putExtra("level_id", "tut3");
        startActivity(intent);
    }

    public void toLevelFour (View view){
        Intent intent = new Intent(this, PlayScreen.class);
        intent.putExtra("level_id", "tut4");
        startActivity(intent);
    }

    public void toLevelFive (View view){
        Intent intent = new Intent(this, PlayScreen.class);
        intent.putExtra("level_id", "tut5");
        startActivity(intent);
    }
}
