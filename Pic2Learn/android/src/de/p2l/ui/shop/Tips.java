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
import android.widget.TextView;
import android.widget.Toast;

import de.p2l.R;

import static de.p2l.ui.menu.mainmenu.MainActivity.SHARED_PREFS;

public class Tips extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    int moneyInt;
    String money;

    Dialog myDialog;

    TextView textViewMoney;

    String displayedMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDialog = new Dialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        moneyInt = sharedPreferences.getInt("money", 0);
        money = Integer.toString(moneyInt);
        displayedMoney = displayedMoney();

        textViewMoney = (TextView) findViewById(R.id.textViewMoney);
        textViewMoney.setText("Pic2Taler: " + displayedMoney);

        //Allg Tipp bought?
        Button buyTipp1Btn = (Button) findViewById(R.id.buyTipp1Btn);
        if(sharedPreferences.getInt("boughtTipp1Btn",0)==1){
            buyTipp1Btn.setText("Ansehen");
        }
        //Tipp 2 bought?
        Button buyTipp2Btn = (Button) findViewById(R.id.buyTipp2Btn);
        if(sharedPreferences.getInt("boughtTipp2Btn",0)==1){
            buyTipp2Btn.setText("Ansehen");
        }

        //Tipp 3 bought?
        Button buyTipp3Btn = (Button) findViewById(R.id.buyTipp3Btn);
        if(sharedPreferences.getInt("boughtTipp3Btn",0)==1){
            buyTipp3Btn.setText("Ansehen");
        }
    }

    public void buyTipp1Btn(View view){
        if(sharedPreferences.getInt("boughtTipp1Btn",0)==1){
            showTipp1Btn();
        }
        else{
            if(moneyInt<100){
                Toast.makeText(Tips.this, "Nicht genügend Taler", Toast.LENGTH_SHORT).show();
            }
            else{
                moneySub(100);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("boughtTipp1Btn",1);
                editor.apply();
                Button buyTipp1Btn = (Button) findViewById(R.id.buyTipp1Btn);
                buyTipp1Btn.setText("Ansehen");
            }
        }
    }

    public void buyTipp2Btn(View view){
        if(sharedPreferences.getInt("boughtTipp2Btn",0)==1){
            showTipp2Btn();
        }
        else{
            if(moneyInt<100){
                Toast.makeText(Tips.this, "Nicht genügend Taler", Toast.LENGTH_SHORT).show();
            }
            else{
                moneySub(100);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("boughtTipp2Btn",1);
                editor.apply();
                Button buyTipp2Btn = (Button) findViewById(R.id.buyTipp2Btn);
                buyTipp2Btn.setText("Ansehen");
            }
        }

    }

    public void buyTipp3Btn(View view){
        if(sharedPreferences.getInt("boughtTipp3Btn",0)==1){
            showTipp3Btn();
        }
        else{
            if(moneyInt<150){
                Toast.makeText(Tips.this, "Nicht genügend Taler", Toast.LENGTH_SHORT).show();
            }
            else{
                moneySub(150);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("boughtTipp3Btn",1);
                editor.apply();
                Button buyTipp3Btn = (Button) findViewById(R.id.buyTipp3Btn);
                buyTipp3Btn.setText("Ansehen");
            }
        }

    }

    public void showTipp1Btn(){
        String header = "Tipp für 'Snake'";
        String text = "Innerhalb der Verzweigung wird noch eine Verzweigung benötigt!";
        showing(header,text);
    }

    public void showTipp2Btn(){
        String header = "Tipp für 'Kreis und Kluft'";
        String text = "Erst Hämmern und dann nochmal prüfen!";
        showing(header,text);
    }

    public void showTipp3Btn(){
        String header = "Tipp für 'Fog of War'";
        String text = "Benutze die Linke-Hand-Regel! Folge immer der Wand zu deiner Linken und die Figur wird ins Ziel finden!";
        showing(header,text);
    }

    public void showing(String header, String text){
        myDialog.setContentView(R.layout.tippshower);
        TextView tippTextView = (TextView) myDialog.findViewById(R.id.textView);
        tippTextView.setText(text);
        TextView headerTextView = (TextView) myDialog.findViewById(R.id.headerTextView);
        headerTextView.setText(header);
        Button button = (Button) myDialog.findViewById(R.id.backBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.hide();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
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
