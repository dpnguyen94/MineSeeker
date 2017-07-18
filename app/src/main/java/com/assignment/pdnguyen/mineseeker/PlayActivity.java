package com.assignment.pdnguyen.mineseeker;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

public class PlayActivity extends AppCompatActivity {
    private MediaPlayer player;
    private int nScans = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //Play background music
        SharedPreferences prefs = getSharedPreferences("Game Settings", MODE_PRIVATE);
        String filename = prefs.getString("Sound Effect", "adventure_music");
        int music_id = getResources().getIdentifier(filename, "raw", this.getPackageName());

        player = MediaPlayer.create(this, music_id);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setLooping(true);
        player.start();

        //Get number of rows and cols from SharedPrefs
        final int nRows = prefs.getInt("Number of rows", 4);
        final int nCols = prefs.getInt("Number of cols", 6);
        final int nMines = prefs.getInt("Number of mines", 10);

        final Button buttons[][] = new Button[nRows][nCols];
        final boolean mines[][] = new boolean[nRows][nCols];
        final boolean reveals[][] = new boolean[nRows][nCols];

        //Originate HUD
        final TextView textTrace = (TextView) findViewById(R.id.textTrace);
        textTrace.setText("Found 0 of " + nMines + " mines");
        final TextView textCount = (TextView) findViewById(R.id.textCount);
        textCount.setText("# of Scans used: " + 0);

        //Generate random mines positions
        int n = 0;
        do {
            int r = new Random().nextInt(nRows);
            int c = new Random().nextInt(nCols);

            if (mines[r][c]) continue;
            mines[r][c] = true;
            n ++;
        } while (n < nMines);

        //Build table of cells
        TableLayout table = (TableLayout) findViewById(R.id.TableforCells);
        for (int row = 0; row < nRows; row ++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            table.addView(tableRow);

            for (int col = 0; col < nCols; col ++) {
                final Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));

                button.setTextSize(24);
                button.setPadding(0,0,0,0);

                final int CURR_ROW = row;
                final int CURR_COL = col;

                //Set up on-click function
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mines[CURR_ROW][CURR_COL]) {
                            LockButtonSize();

                            //Scale image to button size
                            int newWidth = button.getWidth();
                            int newHeight = button.getHeight();
                            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cherrybomb);
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
                            Resources resource = getResources();
                            button.setBackgroundDrawable(new BitmapDrawable(resource, scaledBitmap));

                            PlaySound(R.raw.ding_sound);
                            UpdateTable(CURR_ROW, CURR_COL);

                            if (getCountAll(mines) == 0) {
                                PopUpMessage();
                                PlaySound(R.raw.applause_sound);
                            }

                        } else {
                            if (reveals[CURR_ROW][CURR_COL]) {
                                PlaySound(R.raw.press_sound);
                            }
                            else {
                                PlaySound(R.raw.camera_sound);

                                nScans ++;
                                textCount.setText("# of Scan used: " + nScans);

                                reveals[CURR_ROW][CURR_COL] = true;
                                button.setText("" + getCount(CURR_ROW, CURR_COL));
                            }
                        }
                    }

                    private void PlaySound(int resid) {
                        MediaPlayer player = MediaPlayer.create(PlayActivity.this, resid);
                        player.start();
                    }

                    private void PopUpMessage() {
                        FragmentManager manager = getSupportFragmentManager();
                        MessageFragment dialog = new MessageFragment();
                        dialog.show(manager, "Message Dialog");
                    }

                    private int getCountAll(boolean[][] mines) {
                        int count = 0;
                        for (int row = 0; row < nRows; row ++)
                            for (int col = 0; col < nCols; col ++)
                                if (mines[row][col]) count ++;

                        return count;
                    }

                    private int getCount(int curr_row, int curr_col) {
                        int count = 0;
                        for (int col = 0; col < nCols; col ++) {
                            if (mines[curr_row][col]) count ++;
                        }
                        for (int row = 0; row < nRows; row ++) {
                            if (mines[row][curr_col]) count ++;
                        }

                        return count;
                    }

                    private void LockButtonSize() {
                        for (int row = 0; row < nRows; row ++) {
                            for (int col = 0; col < nCols; col ++) {
                                Button button = buttons[row][col];

                                int width = button.getWidth();
                                button.setMinWidth(width);
                                button.setMaxWidth(width);

                                int height = button.getHeight();
                                button.setMinHeight(height);
                                button.setMaxHeight(height);
                            }
                        }
                    }

                    private void UpdateTable(int curr_row, int curr_col) {
                        mines[curr_row][curr_col] = false;

                        textTrace.setText("Found " + (nMines - getCountAll(mines)) + " of " + nMines + " mines");
                        for (int col = 0; col < nCols; col ++) {
                            if (reveals[curr_row][col]) {
                                Button button = buttons[curr_row][col];
                                button.setText("" + getCount(curr_row, col));
                            }
                        }
                        for (int row = 0; row < nRows; row ++) {
                            if (reveals[row][curr_col]) {
                                Button button = buttons[row][curr_col];
                                button.setText("" + getCount(row, curr_col));
                            }
                        }
                    }
                });

                tableRow.addView(button);
                buttons[row][col] = button;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null) {
            player.stop();
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
            player.stop();
        }

        super.onDestroy();
    }
}
