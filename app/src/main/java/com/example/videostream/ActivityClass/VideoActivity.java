package com.example.videostream.ActivityClass;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.videostream.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;

import java.net.URL;

public class VideoActivity extends AppCompatActivity  {
    ExoPlayer exoPlayer;
    PlayerView video_view;
    String url;
    ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        //video_view = findViewById(R.id.video_view);
        video_view = findViewById(R.id.video_view);

        Bundle getUrl = getIntent().getExtras();
        url = getUrl.getString("url");

       try{
           new URL(url).toURI();
       }catch (Exception e)
       {
           Toast.makeText(this, "url Invalid", Toast.LENGTH_SHORT).show();
       }

        player = new ExoPlayer.Builder(VideoActivity.this).build();
        video_view.setPlayer(player);
    }


    @Override
    protected void onResume() {
        super.onResume();
           try {
               MediaItem mediaItem1 = MediaItem.fromUri(url);
               player.setMediaItem(mediaItem1);
               player.prepare();
               player.play();
               video_view.hideController();

           }catch (Exception e)
           {
               Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
           }

    }

    @Override
    public void onBackPressed() {
        player.stop();
        super.onBackPressed();
        this.finish();
    }


}