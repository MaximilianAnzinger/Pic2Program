package de.p2l.ui.shop;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import de.p2l.R;

import static de.p2l.ui.menu.mainmenu.MainActivity.SHARED_PREFS;

public class Skins extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    int moneyInt;
    String money;

    Dialog myDialog;
    ImageButton currentBtn;

    TextView textViewMoney;

    String displayedMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDialog = new Dialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skins);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        moneyInt = sharedPreferences.getInt("money", 0);
        System.out.println(moneyInt);
        money = Integer.toString(moneyInt);
        displayedMoney = displayedMoney();

        textViewMoney = (TextView) findViewById(R.id.textViewMoney);
        textViewMoney.setText("Pic2Taler: " + displayedMoney);

        //current

        currentBtn = (ImageButton) findViewById(R.id.currentBtn);
        switch(sharedPreferences.getInt("currentBtn",0)){
            case 0: currentBtn.setImageResource(R.drawable.mpic);
                break;
            case 1: currentBtn.setImageResource(R.drawable.vpic);
                break;
            case 2: currentBtn.setImageResource(R.drawable.wpic);
                break;
            case 3: currentBtn.setImageResource(R.drawable.gpic);
                break;
            default: System.out.println("error");
                break;

        }

        //viking
        Button vikingBtn = (Button) findViewById(R.id.vikingBtn);
        if(sharedPreferences.getInt("vikingBtn",0)==1){
            vikingBtn.setEnabled(false);
            vikingBtn.invalidate();
        }
        //lea
        Button leaBtn = (Button) findViewById(R.id.leaBtn);
        if(sharedPreferences.getInt("grillBtn",0)==1){
            leaBtn.setEnabled(false);
            leaBtn.invalidate();
        }

        //goblin
        Button goblinBtn = (Button) findViewById(R.id.goblinBtn);
        if(sharedPreferences.getInt("goblinBtn",0)==1){
            goblinBtn.setEnabled(false);
            goblinBtn.invalidate();
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
                moneyInt = 99999;
                displayedMoney = "99999";
                break;
        }
        return displayedMoney;
    }

    public void vikingBtn(View view){
        if(moneyInt<100){
            Toast.makeText(Skins.this, "Nicht genügend Taler", Toast.LENGTH_SHORT).show();
        }
        else{
            moneySub(100);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("vikingBtn",1);
            editor.apply();
            Button vikingBtn = (Button) findViewById(R.id.vikingBtn);
            vikingBtn.setEnabled(false);
            vikingBtn.invalidate();
        }
    }

    public void leaBtn(View view){
        if(moneyInt<200){
            Toast.makeText(Skins.this, "Nicht genügend Taler", Toast.LENGTH_SHORT).show();
        }
        else{
            moneySub(200);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("grillBtn",1);
            editor.apply();
            Button leaBtn = (Button) findViewById(R.id.leaBtn);
            leaBtn.setEnabled(false);
            leaBtn.invalidate();
        }
    }

    public void goblinBtn(View view){
        if(moneyInt<500){
            Toast.makeText(Skins.this, "Nicht genügend Taler", Toast.LENGTH_SHORT).show();
        }
        else{
            moneySub(500);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("goblinBtn",1);
            editor.apply();
            Button goblinBtn = (Button) findViewById(R.id.goblinBtn);
            goblinBtn.setEnabled(false);
            goblinBtn.invalidate();
        }
    }

    public void currentBtn(View view){
        myDialog.setContentView(R.layout.skinchooser);


        ImageButton mainCharBtn = (ImageButton) myDialog.findViewById(R.id.imageButtonPic);
        ImageButton vikingBtn = (ImageButton) myDialog.findViewById(R.id.imageButtonOlaf);
        if(sharedPreferences.getInt("vikingBtn",0)==0){
            vikingBtn.setEnabled(false);
            vikingBtn.invalidate();
        }
        ImageButton leaBtn = (ImageButton) myDialog.findViewById(R.id.imageButtonLea);
        if(sharedPreferences.getInt("grillBtn",0)==0){
            leaBtn.setEnabled(false);
            leaBtn.invalidate();
        }
        ImageButton goblinBtn = (ImageButton) myDialog.findViewById(R.id.imageButtonKnolch);
        if(sharedPreferences.getInt("goblinBtn",0)==0){
            goblinBtn.setEnabled(false);
            goblinBtn.invalidate();
        }

        mainCharBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("currentBtn",0);
                editor.apply();
                currentBtn.setImageResource(R.drawable.mpic);
                myDialog.hide();
            }
        });

        vikingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("currentBtn",1);
                editor.apply();
                currentBtn.setImageResource(R.drawable.vpic);
                myDialog.hide();
            }
        });

        leaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("currentBtn",2);
                editor.apply();
                currentBtn.setImageResource(R.drawable.wpic);
                myDialog.hide();
            }
        });

        goblinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("currentBtn",3);
                editor.apply();
                currentBtn.setImageResource(R.drawable.gpic);
                myDialog.hide();
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

    public void moneySub(int i){
        System.out.println("subbing " + i);
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
