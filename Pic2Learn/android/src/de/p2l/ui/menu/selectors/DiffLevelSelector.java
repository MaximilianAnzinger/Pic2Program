package de.p2l.ui.menu.selectors;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.p2l.R;
import de.p2l.ui.ingame.fragments.PlayScreen;

import static de.p2l.ui.menu.mainmenu.MainActivity.SHARED_PREFS;

public class DiffLevelSelector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_level_selector);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int binCheck1 = sharedPreferences.getInt("diff1", 0);
        int binCheck2 = sharedPreferences.getInt("diff2", 0);
        int binCheck3 = sharedPreferences.getInt("diff3", 0);
        int binCheck4 = sharedPreferences.getInt("diff4", 0);
        Button button2 = findViewById(R.id.diffLevel2Btn);
        if(binCheck1>0){
            button2.setEnabled(true);
            button2.invalidate();
        }
        Button button3 = findViewById(R.id.diffLevel3Btn);
        if(binCheck2>0){
            button3.setEnabled(true);
            button3.invalidate();
        }
        Button button4 = findViewById(R.id.diffLevel4Btn);
        if(binCheck3>0){
            button4.setEnabled(true);
            button4.invalidate();
        }
        if(binCheck4>0){
            //was the last level until now
        }
    }

    public void toLevelOne (View view){
        Intent intent = new Intent(this, PlayScreen.class);
        intent.putExtra("level_id", "diff1");
        startActivity(intent);
    }

    public void toLevelTwo (View view){
        Intent intent = new Intent(this, PlayScreen.class);
        intent.putExtra("level_id", "diff2");
        startActivity(intent);
    }

    public void toLevelThree (View view){
        Intent intent = new Intent(this, PlayScreen.class);
        intent.putExtra("level_id", "diff3");
        startActivity(intent);
    }

    public void toLevelFour (View view){
        Intent intent = new Intent(this, PlayScreen.class);
        intent.putExtra("level_id", "diff4");
        startActivity(intent);
    }
}
