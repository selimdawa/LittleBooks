package com.flatcode.littlebooks.Unit;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.flatcode.littlebooks.R;

public class THEME {

    public static void setThemeOfApp(@NonNull Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        if (sharedPreferences.getString("color_option", "ONE").equals("ONE")) {
            context.setTheme(R.style.OneTheme);
        } else if (sharedPreferences.getString("color_option", "NIGHT_ONE").equals("NIGHT_ONE")) {
            context.setTheme(R.style.OneNightTheme);
        }
    }
}