/*
 * GlowNotifier Application for Android
 * Copyright (C) 2013 Youngbin Han<sukso96100@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hybdms.glownotifier;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.os.Build;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load Preference Value
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        Spinner posentry = (Spinner) findViewById(R.id.posentry);  //posentry spinner
        Spinner widthentry = (Spinner) findViewById(R.id.widthentry); //widthentry spinner
        Spinner heightentry = (Spinner) findViewById(R.id.heightentry); //heightentry spinner
        EditText glowdleay = (EditText) findViewById(R.id.delaytime); //delaytime Edittext
        int posentry_int = pref.getInt("posentry",0);
        int widthentry_int = pref.getInt("widthentry", 5);
        int heightentry_int = pref.getInt("heightentry", 5);

        String delaytime_String = pref.getString("delaytime", "5000");
        posentry.setSelection(posentry_int);
        widthentry.setSelection(widthentry_int);
        heightentry.setSelection(heightentry_int);
        glowdleay.setText(delaytime_String);

        //Launch Tutorial Activity If user new to this app
        Boolean firstrun = pref.getBoolean("firstrun", true);
        if (firstrun) {
            Intent guide = new Intent(MainActivity.this, Tutorial.class);
            startActivity(guide);
            SharedPreferences.Editor editor = pref.edit(); // Load Editor
            editor.putBoolean("firstrun", false); //put value
            editor.commit(); // Save value
        }
        else{
            //Do Nothing
        }

        TextView accessibility = (TextView)findViewById(R.id.accessibility);
        accessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
                    Intent accessibility = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(accessibility);
                }
                else{
                    Intent notiintent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    startActivity(notiintent);
                }
            }
        });

        TextView appinfo = (TextView)findViewById(R.id.appinfo);
        appinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appinfo = new Intent(MainActivity.this, Appinfo.class);
                startActivity(appinfo);
            }
        });

        TextView blacklist = (TextView)findViewById(R.id.blacklist);
        blacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blacklist = new Intent(MainActivity.this, Blacklist.class);
                startActivity(blacklist);
            }
        });

        TextView colorpicker = (TextView)findViewById(R.id.colorpicker);
        colorpicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });

    }

    public void onStop(){
        super.onStop();

        //Save Preference Value
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); // Save UI State
        SharedPreferences.Editor editor = pref.edit(); // Load Editor
        Spinner posentry = (Spinner) findViewById(R.id.posentry);
        Spinner widthentry = (Spinner) findViewById(R.id.widthentry);
        Spinner heightentry = (Spinner) findViewById(R.id.heightentry);
        EditText glowdelay = (EditText) findViewById(R.id.delaytime);
        // Input values
        int posentry_selected_value = posentry.getSelectedItemPosition();
        int widthentry_selected_value = widthentry.getSelectedItemPosition();
        int hedightentry_selected_value = heightentry.getSelectedItemPosition();
        String delaytime_edited_value = glowdelay.getText().toString();

        editor.putInt("posentry", posentry_selected_value);
        editor.putInt("widthentry", widthentry_selected_value);
        editor.putInt("heightentry", hedightentry_selected_value);
        editor.putString("delaytime", delaytime_edited_value);
        editor.commit(); // Save values
    }

    //ActionBar Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_preview:
                startService(new Intent(this, GlowOverlay.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showColorPickerDialog() {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);  //Load Preference
        int initialColor = pref.getInt("colorvalue", Color.WHITE);  //Load Color Value from Preference

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, initialColor, new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit(); //Load Editor
                editor.putInt("colorvalue", color); //Save Selected Color
                editor.commit();
            }

        });
        colorPickerDialog.show();

    }
}