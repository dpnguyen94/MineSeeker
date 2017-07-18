package com.assignment.pdnguyen.mineseeker;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        player = MediaPlayer.create(this, R.raw.guile_theme);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setLooping(true);
        player.start();

        Button buttonSkip = (Button) findViewById(R.id.buttonSkip);
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, MenuActivity.class);
                startActivity(intent);
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
    protected void onPause() {
        if (player != null) {
            player.stop();
        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        if (player != null) {
            player.stop();
        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
            player.stop();
        }

        super.onDestroy();
    }
}
