package com.example.eitaaprivateapi;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    public static SharedPreferences sharedPreferences;

    public  SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }


    public void setToken(String str){
        sharedPreferences.edit().putString("token",str).apply();
    }
    public String getToken(){
        return sharedPreferences.getString("token","");
}


}
