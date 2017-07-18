package com.assignment.pdnguyen.mineseeker;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        player = MediaPlayer.create(this, R.raw.menu_music);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setLooping(true);
        player.start();

        Button buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, PlayActivity.class);
                startActivity(intent);

                player.stop();
            }
        });

        Button buttonOptions = (Button) findViewById(R.id.buttonOptions);
        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, OptionsActivity.class);
                startActivity(intent);
            }
        });

        Button buttonHelp = (Button) findViewById(R.id.buttonHelp);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        Button buttonQuit = (Button) findViewById(R.id.buttonQuit);
        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.stop();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (player != null) {
            player.stop();
        }

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        if (!player.isPlaying()) {

            player = MediaPlayer.create(this, R.raw.menu_music);
            player.setLooping(true);
            player.start();
        }

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
            player.stop();
        }

        super.onDestroy();
    }

}
