package com.example.homework41;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences preferences;

    public Prefs(Context context) {
        preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public void saveBoardState(){
        preferences.edit().putBoolean("avatar", true).apply();
    }

    public boolean isBoardShown(){
        return preferences.getBoolean("avatar", false);
    }

    public void saveAvatar(String image){
        preferences.edit().putString("image", image).apply();
    }

    public String getAvatar (){
        return preferences.getString("image", null);
    }

    public void saveName(String name){
        preferences.edit().putString("name", name).apply();
    }

    public String getName (){
        return preferences.getString("name", "");
    }

    public void cleanPreferences(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("name").apply();
        editor.remove("image").apply();
    }
}
