
package de.p2l.ui.menu.mainmenu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFiles;

import de.p2l.R;
import de.p2l.ui.menu.selectors.DiffLevelSelector;
import de.p2l.ui.menu.selectors.EasyLevelSelector;
import de.p2l.ui.menu.selectors.MiddleLevelSelector;
import de.p2l.ui.menu.selectors.TutorialLevelSelector;
import de.p2l.ui.shop.Shop;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Dialog myDialog;
    public static final String SHARED_PREFS = "sharedPrefs";
    String level;
    boolean bonus;
    private static Context context;
    private ResultCalculator rc;
    private int numberOfCommands;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDialog = new Dialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        int binCheck1 = sharedPreferences.getInt("diffBtn", 0);
        Button diffBtn = findViewById(R.id.diffBtn);
        if(binCheck1==1){
            diffBtn.setEnabled(true);
            diffBtn.invalidate();
        }

        int binCheck2 = sharedPreferences.getInt("middBtn", 0);
        Button middBtn = findViewById(R.id.middleBtn);
        if(binCheck2==1){
            middBtn.setEnabled(true);
            middBtn.invalidate();
        }

        int binCheck3 = sharedPreferences.getInt("easyBtn", 0);
        Button easyBtn = findViewById(R.id.easyBtn);
        if(binCheck3==1){
            easyBtn.setEnabled(true);
            easyBtn.invalidate();
        }

        numberOfCommands = getIntent().getIntExtra("numberOfCommands", -1);
        bonus= false;
        level = getIntent().getStringExtra("level");
        if (level != null) {
            setResults();
        }

        //stars at start screen
        int x = sharedPreferences.getInt("tut1",0)+
                sharedPreferences.getInt("tut2",0)+
                sharedPreferences.getInt("tut3",0)+
                sharedPreferences.getInt("tut4",0)+
                sharedPreferences.getInt("tut5",0)+

                sharedPreferences.getInt("easy1",0)+
                sharedPreferences.getInt("easy2",0)+
                sharedPreferences.getInt("easy3",0)+
                sharedPreferences.getInt("easy4",0)+

                sharedPreferences.getInt("midd1",0)+
                sharedPreferences.getInt("midd2",0)+
                sharedPreferences.getInt("midd3",0)+
                sharedPreferences.getInt("midd4",0)+

                sharedPreferences.getInt("diff1",0)+
                sharedPreferences.getInt("diff2",0)+
                sharedPreferences.getInt("diff3",0)+
                sharedPreferences.getInt("diff4",0);


        TextView stars = (TextView) findViewById(R.id.stars);
        stars.setText(x + "/51");

        context = this;

    }

    public static Context getContext(){
        return context;
    }


    public void setResults(){

            myDialog.setContentView(R.layout.results);
            Button button = (Button) myDialog.findViewById(R.id.backBtn);
            button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.hide();
            }
        });
            TextView headerTextView = (TextView) myDialog.findViewById(R.id.headerTextView);
            TextView maybeTextView = (TextView) myDialog.findViewById(R.id.maybeTextView);
            TextView zuegeTextView = (TextView) myDialog.findViewById(R.id.zuegeTextView);
            TextView pic2talerTextView = (TextView) myDialog.findViewById(R.id.pic2talerNumber);

            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            int currentMoney = sharedPreferences.getInt("money", 0);

            if(sharedPreferences.getInt(level, 0)==0){
                maybeTextView.setText("Neues Level freigeschalten!");
                bonus = true;
            }
            else{
                maybeTextView.setText("");
            }

            String numberOfCommandsString = Integer.toString(numberOfCommands);
            String numberOfCommandsStringDisplayed = "";
            switch (numberOfCommandsString.length()){
                case 1: numberOfCommandsStringDisplayed = "00" + numberOfCommandsString;
                    break;
                case 2: numberOfCommandsStringDisplayed = "0" + numberOfCommandsString;
                    break;
                case 3: numberOfCommandsStringDisplayed = numberOfCommandsString;
                    break;
                default: numberOfCommandsStringDisplayed = "999";
                    break;
            }

            zuegeTextView.setText("Anzahl der Züge: " + numberOfCommandsStringDisplayed);

            rc = new ResultCalculator(level, bonus, numberOfCommands);

            int leistung = rc.checkPerformance();
            if(leistung==2){
                ImageView star3 = (ImageView) myDialog.findViewById(R.id.starImage3);
                star3.setImageResource(android.R.drawable.btn_star_big_off);
            }
            else if(leistung==1){
                ImageView star3 = (ImageView) myDialog.findViewById(R.id.starImage3);
                star3.setImageResource(android.R.drawable.btn_star_big_off);
                ImageView star2 = (ImageView) myDialog.findViewById(R.id.starImage2);
                star2.setImageResource(android.R.drawable.btn_star_big_off);
            }

            headerTextView.setText(rc.getName());
            pic2talerTextView.setText(rc.getMoney());

            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
    }

    public String textCalculation(int newMoney){
        String newMoneyString = Integer.toString(newMoney);
        String newMoneyDisplayed = "";
        switch (newMoneyString.length()){
            case 1: newMoneyDisplayed = "00" + newMoneyString;
                break;
            case 2: newMoneyDisplayed = "0" + newMoneyString;
                break;
            case 3: newMoneyDisplayed = newMoneyString;
                break;
            default: newMoneyDisplayed = "999";
                break;
        }
        return newMoneyDisplayed;
    }


    //create OptionsMenu on MainActivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.MainOptionsMenuItem:

                myDialog.setContentView(R.layout.options);
                Button button = (Button) myDialog.findViewById(R.id.backBtn);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.hide();
                    }
                });

                Button cheatBtn = (Button) myDialog.findViewById(R.id.cheatBtn);
                cheatBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        myDialog.setContentView(R.layout.cheats);
                        Button submitBtn = (Button) myDialog.findViewById(R.id.submitBtn);
                        final EditText editText = (EditText) myDialog.findViewById(R.id.editText);
                        submitBtn.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                String text = editText.getText().toString();
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                switch (text){
                                    case "4201":
                                        editor.putInt("tut1", 1);
                                        editor.putInt("tut2", 1);
                                        editor.putInt("tut3", 1);
                                        editor.putInt("tut4", 1);
                                        editor.putInt("tut5" ,1);
                                        editor.putInt("easyBtn",1);
                                        editor.apply();
                                        Toast.makeText(getContext(), "Alle Tutorials geschafft!", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                        break;
                                    case "4202":
                                        editor.putInt("easy1",1);
                                        editor.putInt("easy2",1);
                                        editor.putInt("easy3",1);
                                        editor.putInt("easy4",1);
                                        editor.putInt("easyBtn",1);
                                        editor.putInt("middBtn", 1);
                                        editor.apply();
                                        Toast.makeText(getContext(), "Alle leichten Level geschafft!", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                        break;
                                    case "4203":
                                        editor.putInt("midd1",1);
                                        editor.putInt("midd2",1);
                                        editor.putInt("midd3",1);
                                        editor.putInt("midd4",1);
                                        editor.putInt("middBtn",1);
                                        editor.putInt("diffBtn", 1);
                                        editor.apply();
                                        Toast.makeText(getContext(), "Alle mittleren Level geschafft!", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                        break;
                                    case "4204":
                                        editor.putInt("diff1",1);
                                        editor.putInt("diff2",1);
                                        editor.putInt("diff3",1);
                                        editor.putInt("diff4",1);
                                        editor.putInt("diffBtn",1);
                                        editor.apply();
                                        Toast.makeText(getContext(), "Alle schweren Level geschafft!", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                        break;
                                    case "4205":
                                        editor.putInt("money",10000);
                                        editor.apply();
                                        Toast.makeText(getContext(), "10000 Taler verdient!", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                        break;
                                    default:
                                        Toast.makeText(getContext(), "Ungültiger Code!", Toast.LENGTH_LONG).show();
                                        break;
                                }

                            }
                        });

                        Button backBtn = (Button) myDialog.findViewById(R.id.backBtn);
                        backBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialog.hide();

                            }
                        });
                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        myDialog.show();
                       }
                                            });

                Button resetBtn = (Button) myDialog.findViewById(R.id.resetBtn);
                resetBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.setContentView(R.layout.reset);

                        Button yesBtn = (Button) myDialog.findViewById(R.id.yesBtn);
                        yesBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("tut1", 0);
                                editor.putInt("tut2", 0);
                                editor.putInt("tut3", 0);
                                editor.putInt("tut4", 0);
                                editor.putInt("tut5" ,0);

                                editor.putInt("easy1",0);
                                editor.putInt("easy2",0);
                                editor.putInt("easy3",0);
                                editor.putInt("easy4",0);

                                editor.putInt("midd1",0);
                                editor.putInt("midd2",0);
                                editor.putInt("midd3",0);
                                editor.putInt("midd4",0);

                                editor.putInt("diff1",0);
                                editor.putInt("diff2",0);
                                editor.putInt("diff3",0);
                                editor.putInt("diff4",0);

                                editor.putInt("easyBtn",0);
                                editor.putInt("diffBtn", 0);
                                editor.putInt("middBtn",0);

                                editor.putInt("money",0);
                                editor.putInt("currentBtn",0);
                                editor.putInt("vikingBtn",0);
                                editor.putInt("grillBtn",0);
                                editor.putInt("goblinBtn",0);
                                editor.putInt("boughtTipp1Btn",0);
                                editor.putInt("boughtTipp2Btn",0);
                                editor.putInt("boughtTipp3Btn",0);

                                editor.putInt("boughtGoldenHammer",0);
                                editor.putInt("boughtGoldenBoots",0);
                                editor.putInt("boughtGoldenVictory",0);
                                editor.putInt("usingGoldenHammer",0);
                                editor.putInt("usingGoldenBoots",0);
                                editor.putInt("usingGoldenVictory",0);

                                editor.apply();
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        });

                        Button noBtn = (Button) myDialog.findViewById(R.id.noBtn);
                        noBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialog.hide();

                            }
                        });

                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        myDialog.show();

                    }
                });



                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
                return true;

            case R.id.Credits:

                myDialog.setContentView(R.layout.credits);

                Button backBtn = (Button) myDialog.findViewById(R.id.backBtnCredits);
                backBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialog.hide();
                                }
                        });

                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        myDialog.show();
                        return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void toTutorialLevelSelector (View view){
        Intent intent = new Intent(this, TutorialLevelSelector.class);
        startActivity(intent);
    }

    public void toShop (View view){
        Intent intent = new Intent(this, Shop.class);
        startActivity(intent);
    }

    public void toEasyLevelsSelector(View view){
        Intent intent = new Intent(this,EasyLevelSelector.class);
        startActivity(intent);
    }

    public void toMiddleLevelsSelector(View view){
        Intent intent = new Intent(this,MiddleLevelSelector.class);
        startActivity(intent);
    }

    public void toDiffLevelsSelector(View view){
        Intent intent = new Intent(this,DiffLevelSelector.class);
        startActivity(intent);
    }

}