package com.example.movieapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.movieapp.DBHelper;
import com.example.movieapp.Helper;
import com.example.movieapp.R;
import com.example.movieapp.data.model.HistoryDTO;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class PlayerActivity extends AppCompatActivity {
    private PlayerView playerView;
    private ExoPlayer simpleExoPlayer;
    private String VIDEO_URl;
    private String VIDEO_TITLE;
    private int Episode_ID;
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(VIDEO_TITLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            userId = Helper.TokenManager.INSTANCE.getId(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        Intent intent = getIntent();
        Episode_ID = intent.getBundleExtra("videoUrl").getInt("ID");
        VIDEO_URl = intent.getBundleExtra("videoUrl").getString("URL");
        VIDEO_TITLE = intent.getBundleExtra("videoUrl").getString("TITLE");
        playerView = findViewById(R.id.video_player);
        simpleExoPlayer = new ExoPlayer.Builder(PlayerActivity.this).build();
        playerView.setPlayer(simpleExoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(VIDEO_URl);
        simpleExoPlayer.setMediaItem(mediaItem);
        simpleExoPlayer.prepare();
        simpleExoPlayer.setPlayWhenReady(true);

        Long saveTime = GetSaveTime(userId, Episode_ID);
        if(saveTime != 0){
            simpleExoPlayer.seekTo(saveTime);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SaveCurrentPlayTime();
    }

    private void SaveCurrentPlayTime(){
        try {
            if(simpleExoPlayer != null){
                Long currenttime = simpleExoPlayer.getCurrentPosition();
                Log.e("Current Time",currenttime.toString());
                DBHelper dbHelper = new DBHelper(this);
                Long resultUpdate = dbHelper.addTime(userId, Episode_ID, currenttime);
                if(resultUpdate > 0){
                    Log.e("UpdateTime","Successfully");
                }else{
                    Log.e("UpdateTime","Fail");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private Long GetSaveTime(int userId, int epId){
        DBHelper dbHelper = new DBHelper(this);
        HistoryDTO result = dbHelper.getHistoryByUserIdandItemId(userId,epId);
        Log.e("GetSaveTime",result.toString());
        return result.getTime();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}