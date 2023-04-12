package com.example.ala.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AppCompatActivity;


public class InternetService {

    Context context;
    public InternetService(Context context)
    {
        this.context = context;
    }

    public boolean checkConnection(){
         ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if(info != null){
            if(info.isConnected())
                return true;
            else
                return false;
        }else
            return false;
    }
}
