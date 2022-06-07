package com.example.ala;

import android.content.Context;

public class Preferences {
    public static final String PREFERENCE_NAME = "MyPref";
    private final android.content.SharedPreferences sharedpreferences;

    public Preferences(Context context)
    {
        sharedpreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void setID(int id){
        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("sp_id", id);
        editor.commit();
    }

}
