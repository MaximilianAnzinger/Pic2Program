package de.p2l.ui.menu.mainmenu;

import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static de.p2l.ui.menu.mainmenu.MainActivity.SHARED_PREFS;
import static de.p2l.ui.menu.mainmenu.MainActivity.getContext;

/*
ResultCalculator checks how well the level was mastered.
It also defines how much money the player gets.
 */

public class ResultCalculator {

    boolean bonus;
    private String level;
    private int az;

    private int moneyBonus;
    private int performance;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public ResultCalculator(String level, boolean bonus, int anzahlZuege){
        this. level = level;
        this.bonus = bonus;
        this.az = anzahlZuege;

        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        performance = checkPerformance();

        //level is mastered. depending on the performance with the respective number of stars
        if(sharedPreferences.getInt(level, 0)<performance){
            editor.putInt(level, performance);
            editor.apply();
        }

        moneyBonus = 0;
    }

    public int checkPerformance(){
        int performance = -1;
        switch(level){
            case "tut1": if(az<=3) return 3;
                         else if(az<=4) return 2;
                         else return 1;
            case "tut2": if(az<=4) return 3;
                         else if(az<=5) return 2;
                         else return 1;
            case "tut3": if(az<=5) return 3;
                         else if(az<=6) return 2;
                         else return 1;
            case "tut4": if(az<=5) return 3;
                         else if(az<=7) return 2;
                         else return 1;
            case "tut5": if(az<=10) return 3;
                         else if(az<=11) return 2;
                         else return 1;

            case "easy1": if(az<=6) return 3;
                          else if(az<=10) return 2;
                          else return 1;
            case "easy2": if(az<=10) return 3;
                          if(az<=15) return 2;
                          else return 1;
            case "easy3": if(az<=9) return 3;
                          else if(az<=14) return 2;
                          else return 1;
            case "easy4": if(az<=11) return 3;
                          else if(az<=15) return 2;
                          else return 1;

            case "midd1": if(az<=12) return 3;
                          else if(az<=16) return 2;
                          else return 1;
            case "midd2": if(az<=12) return 3;
                          else if(az<=16) return 2;
                          else return 1;
            case "midd3": if(az<=11) return 3;
                          else if(az<=14) return 2;
                          else return 1;
            case "midd4": if(az<=15) return 3;
                          else if(az<=20) return 2;
                          else return 1;

            case "diff1": if(az<=14) return 3;
                          else if(az<=19) return 2;
                          else return 1;
            case "diff2": if(az<=17) return 3;
                          else if(az<=22) return 2;
                          else return 1;
            case "diff3": if(az<=10) return 3;
                          else if(az<=17) return 2;
                          else return 1;
            case "diff4": if(az<=25) return 3;
                          else if(az<=32) return 2;
                          else return 1;

            default: performance = -1;
        }
        return performance;
    }

    public String getName() {
        switch (level) {
            case "tut1":
                return "Tutorial 1";
            case "tut2":
                return "Tutorial 2";
            case "tut3":
                return "Tutorial 3";
            case "tut4":
                return "Tutorial 4";
            case "tut5":
                return "Tutorial 5";

            case "easy1":
                return "Große Schlucht";
            case "easy2":
                return "Großer Kreis";
            case "easy3":
                return "Rechts-Links";
            case "easy4":
                return "Umgekehrter Kreis";

            case "midd1":
                return "Zurücktreten 1";
            case "midd2":
                return "Ständiges Prüfen";
            case "midd3":
                return "Von Stein zu Stein";
            case "midd4":
                return "Snake";

            case "diff1":
                return "Kreis und Kluft";
            case "diff2":
                return "Zurücktreten 2";
            case "diff3":
                return "Fog of War";
            case "diff4":
                return "Der Wanderer";

            default:
                return null;
        }
    }

    public String getMoney(){
        int money = 0;

        switch(level){
            case "tut1": if(performance==3) money = 10;
                         else if(performance==2) money = 5;
                         else money = 1;
                break;
            case "tut2": if(performance==3) money = 10;
                         else if(performance==2) money = 5;
                         else money = 1;
                break;
            case "tut3": if(performance==3) money = 10;
                         else if(performance==2) money = 5;
                         else money = 1;
                break;
            case "tut4": if(performance==3) money = 15;
                         else if(performance==2) money = 8;
                         else money = 3;
                break;
            case "tut5": if(performance==3) money = 20;
                         else if(performance==2) money = 10;
                         else money = 7;
                break;

            case "easy1": if(performance==3) money = 30;
                          else if(performance==2) money = 20;
                          else money = 8;
                break;

            case "easy2": if(performance==3) money = 30;
                          else if(performance==2) money = 20;
                          else money = 8;
                break;
            case "easy3": if(performance==3) money = 40;
                          else if(performance==2) money = 30;
                          else money = 15;
                break;
            case "easy4": if(performance==3) money = 50;
                          else if(performance==2) money = 35;
                          else money = 15;
                break;

            case "midd1": if(performance==3) money = 60;
                          else if(performance==2) money = 40;
                          else money = 15;
                break;
            case "midd2": if(performance==3) money = 80;
                          else if(performance==2) money = 60;
                          else money = 20;
                break;
            case "midd3": if(performance==3) money = 100;
                          else if(performance==2) money = 75;
                          else money = 25;
                break;
            case "midd4": if(performance==3) money = 120;
                          else if(performance==2) money = 90;
                          else money = 30;
                break;

            case "diff1": if(performance==3) money = 130;
                          else if(performance==2) money = 100;
                          else money = 50;
                break;
            case "diff2": if(performance==3) money = 180;
                          else if(performance==2) money = 130;
                          else money = 50;
                break;
            case "diff3": if(performance==3) money = 250;
                          else if(performance==2) money = 160;
                          else money = 70;
                break;
            case "diff4": if(performance==3) money = 280;
                          else if(performance==2) money = 200;
                          else money = 80;
                break;

            default: money = -1;
        }

        if(bonus==true) money = money+moneyBonus;
        int oldMoney = sharedPreferences.getInt("money",0);
        editor.putInt("money", money+oldMoney);
        editor.apply();
        return "Erhaltene Pic2Taler: " + textCalculation(money);
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

}
