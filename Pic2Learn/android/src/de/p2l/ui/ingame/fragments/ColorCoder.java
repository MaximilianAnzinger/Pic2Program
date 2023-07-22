package de.p2l.ui.ingame.fragments;

import android.graphics.Color;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class ColorCoder {

    private static final String TAG = "ColorCode";
    private int selectedPosition;
    private ArrayList<Integer> colorList;
    private HashMap<Integer, Integer> colorCode;

    public ColorCoder() {
        colorList = new ArrayList<>();
        colorCode = new HashMap<>();
    }

    public void generateColorCode(ArrayList<Set<Integer>> allAssociatedIndexes){
        colorCode.clear();

        //add colors if there exist more associated indexes than colors
        int sizeDifference = allAssociatedIndexes.size()-colorList.size();
        if (sizeDifference>0) addColors(sizeDifference);

        for (int i=0; i<allAssociatedIndexes.size(); i++){
            Set<Integer> associatedIndexes = allAssociatedIndexes.get(i);
            for (Integer index : associatedIndexes){
                colorCode.put(index, colorList.get(i));
            }
        }

    }

    private void addColors(int number){
        for (int i=0; i<number;i++){
            colorList.add(getRandomColor());
        }
    }

    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public HashMap<Integer, Integer> getColorCode() {
        return colorCode;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public void increaseSelectedPosition(){
        selectedPosition++;
    }

    public void decreaseSelectedPosition(){
        selectedPosition--;
    }
}
