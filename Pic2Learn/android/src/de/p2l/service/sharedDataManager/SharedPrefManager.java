package de.p2l.service.sharedDataManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import de.p2l.service.parser.parser.Input;

public class SharedPrefManager {
    private static final String TAG = "SharedPrefManager";

    public static void write(Context context, ArrayList<Input> inputList, String key){
        SharedPreferences sharedPref = context.getSharedPreferences("pic2learn", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        try {
            editor.putString(key, Serializer.serizalize(inputList));
        } catch (IOException e) {
            Log.e(TAG,"Could not serialize inputList");
        }
        editor.apply();
    }

    public static void write(Context context, int input, String key){
        SharedPreferences sharedPref = context.getSharedPreferences("pic2learn", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, input);
        editor.apply();
    }

    public static ArrayList<Input> read(Context context, String key){
        SharedPreferences sharedPref = context.getSharedPreferences("pic2learn", Context.MODE_PRIVATE);
        try {
            return (ArrayList<Input>) Serializer.deserialize(sharedPref.getString(key, ""));
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG,"Could not deserialize string");
        }
        return null;
    }

    public static int readInt(Context context, String key){
        SharedPreferences sharedPref = context.getSharedPreferences("pic2learn", Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 0);

    }

}
