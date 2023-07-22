package de.p2l.ui.shop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import de.p2l.R;

import static de.p2l.ui.menu.mainmenu.MainActivity.SHARED_PREFS;

public class Shop extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    TextView textViewMoney;
    Button btnSkin0;
    Button btnSkin1;
    Switch switchSkin0;
    Switch switchSkin1;

    int moneyInt;
    String money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        moneyInt = sharedPreferences.getInt("money", 0);
        money = Integer.toString(moneyInt);
        String displayedMoney = displayedMoney();

        textViewMoney = (TextView) findViewById(R.id.textViewMoney);
        textViewMoney.setText("Pic2Taler: " + displayedMoney);

        switchSkin0 = (Switch) findViewById(R.id.switchSkin0);
        switchSkin0.setChecked(true);
        switchSkin0.setClickable(false);
        switchSkin0.invalidate();

        switchSkin0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchSkin1.setOnCheckedChangeListener(null);
                switchSkin1.setChecked(false);
                switchSkin1.setOnCheckedChangeListener(this);
            }
        });


        switchSkin1 = (Switch) findViewById(R.id.switchSkin1);
        switchSkin1.setClickable(false);
        switchSkin1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchSkin0.setOnCheckedChangeListener (null);
                switchSkin0.setChecked(true);
                switchSkin0.setOnCheckedChangeListener (this);
            }
        });
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

    public void toSkins (View view){
        Intent intent = new Intent(this, Skins.class);
        startActivityForResult(intent,1);
    }

    public void toAcc (View view){
        Intent intent = new Intent(this, Accessoires.class);
        startActivityForResult(intent,1);
    }

    public void toTips (View view){
        Intent intent = new Intent(this, Tips.class);
        startActivityForResult(intent,1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String myStr=data.getStringExtra("MyData");
                textViewMoney.setText("Pic2Taler: " + myStr);
            }
        }
    }
}
