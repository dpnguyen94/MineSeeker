package com.assignment.pdnguyen.mineseeker;

import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        int [] num_rows = getResources().getIntArray(R.array.num_rows);
        int [] num_cols = getResources().getIntArray(R.array.num_cols);
        int [] num_mines = getResources().getIntArray(R.array.num_mines);
        String [] filenames = getResources().getStringArray(R.array.filename);
        String [] showtexts = getResources().getStringArray(R.array.showtext);

        SharedPreferences prefs = getSharedPreferences("Game Settings", MODE_PRIVATE);
        int prev_id = prefs.getInt("ID number", -1);
        int prev_mines = prefs.getInt("Number of mines", 0);
        String prev_music = prefs.getString("Sound Effect", "no string found");

        RadioGroup tableSize = (RadioGroup) findViewById(R.id.tableSize);
        for (int i = 0; i < num_rows.length; i ++) {
            RadioButton button = new RadioButton(this);
            button.setTextSize(18);
            button.setText(num_rows[i] + " by " + num_cols[i]);

            final int id = i;
            final int nRows = num_rows[i];
            final int nCols = num_cols[i];
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences prefs = getSharedPreferences("Game Settings", MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putInt("ID number", id);
                    edit.putInt("Number of rows", nRows);
                    edit.putInt("Number of cols", nCols);
                    edit.apply();
                }
            });

            tableSize.addView(button);

            if (i == prev_id) {
                button.setChecked(true);
            }
        }

        RadioGroup numOfMines = (RadioGroup) findViewById(R.id.numberOfMines);
        for (int i = 0; i < num_mines.length; i ++) {
            RadioButton button = new RadioButton(this);
            button.setTextSize(18);
            button.setText(num_mines[i] + " mines");

            final int nMines = num_mines[i];
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences prefs = getSharedPreferences("Game Settings", MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putInt("Number of mines", nMines);
                    edit.apply();
                }
            });

            numOfMines.addView(button);

            if (num_mines[i] == prev_mines) {
                button.setChecked(true);
            }
        }

        RadioGroup soundEffects = (RadioGroup) findViewById(R.id.soundEffects);
        for (int i = 0; i < filenames.length; i ++) {
            RadioButton button = new RadioButton(this);
            button.setTextSize(18);
            button.setText("" + showtexts[i]);

            final String fileName = filenames[i];
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences prefs = getSharedPreferences("Game Settings", MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("Sound Effect", fileName);
                    edit.apply();
                }
            });

            soundEffects.addView(button);

            if (filenames[i].equals(prev_music)) {
                button.setChecked(true);
            }
        }

        setUpOKButton();
    }

    private void setUpOKButton() {
        Button button = (Button) findViewById(R.id.buttonOK);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
