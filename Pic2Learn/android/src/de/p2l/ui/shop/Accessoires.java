package de.p2l.ui.shop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.p2l.R;

import static de.p2l.ui.menu.mainmenu.MainActivity.SHARED_PREFS;

public class Accessoires extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    int moneyInt;
    String money;

    TextView textViewMoney;

    String displayedMoney;

    private boolean goldenHammerUsed, goldenBootsUsed, goldenVictoryUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        moneyInt = sharedPreferences.getInt("money", 0);
        money = Integer.toString(moneyInt);
        displayedMoney = displayedMoney();

        goldenHammerUsed = false;
        goldenBootsUsed = false;

        textViewMoney = (TextView) findViewById(R.id.textViewMoney);
        textViewMoney.setText("Pic2Taler: " + displayedMoney);

        Button useGoldenHammerBtn = (Button) findViewById(R.id.useGoldenHammer);
        if(sharedPreferences.getInt("usingGoldenHammer",0)==1)
            useGoldenHammerBtn.setText("Nicht mehr nutzen");
        Button useGoldenBoots = (Button) findViewById(R.id.useGoldenBoots);
        if(sharedPreferences.getInt("usingGoldenBoots",0)==1)
            useGoldenBoots.setText("Nicht mehr nutzen");

        useGoldenHammerBtn.setEnabled(false);
        useGoldenHammerBtn.invalidate();
        useGoldenBoots.setEnabled(false);
        useGoldenBoots.invalidate();

        //gold hammer bought?
        Button buyGoldenHammer = (Button) findViewById(R.id.buyGoldenHammer);
        if(sharedPreferences.getInt("boughtGoldenHammer",0)==1){
            buyGoldenHammer.setEnabled(false);
            buyGoldenHammer.invalidate();
            useGoldenHammerBtn.setEnabled(true);
            useGoldenHammerBtn.invalidate();
        }
        //golden boots bought?
        Button buyGoldenBoots = (Button) findViewById(R.id.buyGoldenBoots);
        if(sharedPreferences.getInt("boughtGoldenBoots",0)==1){
            buyGoldenBoots.setEnabled(false);
            buyGoldenBoots.invalidate();
            useGoldenBoots.setEnabled(true);
            useGoldenBoots.invalidate();
        }
    }

    public void buyGoldenHammer(View view){
        if(moneyInt<150){
            Toast.makeText(Accessoires.this, "Nicht genügend Taler", Toast.LENGTH_SHORT).show();
        }
        else{
            moneySub(150);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("boughtGoldenHammer",1);
            editor.apply();
            Button buyGoldenHammer = (Button) findViewById(R.id.buyGoldenHammer);
            buyGoldenHammer.setEnabled(false);
            buyGoldenHammer.invalidate();
            Button useGoldenHammer = (Button) findViewById(R.id.useGoldenHammer);
            useGoldenHammer.setEnabled(true);
            useGoldenHammer.invalidate();
        }
    }

    public void buyGoldenBoots(View view){
        if(moneyInt<200){
            Toast.makeText(Accessoires.this, "Nicht genügend Taler", Toast.LENGTH_SHORT).show();
        }
        else{
            moneySub(200);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("boughtGoldenBoots",1);
            editor.apply();
            Button buyGoldenBoots = (Button) findViewById(R.id.buyGoldenBoots);
            buyGoldenBoots.setEnabled(false);
            buyGoldenBoots.invalidate();
            Button useGoldenBoots = (Button) findViewById(R.id.useGoldenBoots);
            useGoldenBoots.setEnabled(true);
            useGoldenBoots.invalidate();
        }
    }

    public void useGoldenHammer(View view){
        if(sharedPreferences.getInt("usingGoldenHammer",0)==1){
            goldenHammerUsed = false;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("usingGoldenHammer",0);
            editor.apply();
            Button button = (Button) findViewById(R.id.useGoldenHammer);
            button.setText("Nutzen");
        }
        else{
            goldenHammerUsed = true;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("usingGoldenHammer",1);
            editor.apply();
            Button button = (Button) findViewById(R.id.useGoldenHammer);
            button.setText("Nicht mehr nutzen");
        }
    }

    public void useGoldenBoots(View view){
        if(sharedPreferences.getInt("usingGoldenBoots",0)==1){
            goldenBootsUsed = false;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("usingGoldenBoots",0);
            editor.apply();
            Button button = (Button) findViewById(R.id.useGoldenBoots);
            button.setText("Nutzen");
        }
        else{
            goldenBootsUsed = true;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("usingGoldenBoots",1);
            editor.apply();
            Button button = (Button) findViewById(R.id.useGoldenBoots);
            button.setText("Nicht mehr nutzen");
        }
    }

    public String displayedMoney(){
        String displayedMoney;
        switch (money.length()){
            case 1: displayedMoney = "0000" + money;
                break;
            case 2: displayedMoney = "000" + money;
                break;
            case 3: displayedMoney = "00" + money;
                break;
            case 4: displayedMoney = "0" + money;
                break;
            case 5: displayedMoney = money;
                break;
            default: SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("money",99999);
                editor.apply();
                displayedMoney = "99999";
                break;
        }
        return displayedMoney;
    }

    public void moneySub(int i){
        moneyInt = moneyInt -i;
        money = Integer.toString(moneyInt);
        displayedMoney = displayedMoney();
        textViewMoney = (TextView) findViewById(R.id.textViewMoney);
        textViewMoney.setText("Pic2Taler: " + displayedMoney);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("money",moneyInt);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        String data = displayedMoney;
        Intent intent = new Intent();
        intent.putExtra("MyData", data);
        setResult(RESULT_OK, intent);
        finish();
    }
}
