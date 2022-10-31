package com.example.videostream;

import androidx.appcompat.app.AppCompatActivity;

import android.drm.DrmStore;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class VideoActivity extends AppCompatActivity {
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

        player = new ExoPlayer.Builder(VideoActivity.this).build();
        video_view.setPlayer(player);

    }

    @Override
    protected void onResume() {
        super.onResume();
       if(url!=null)
       {
           MediaItem mediaItem1 = MediaItem.fromUri(url);
           player.setMediaItem(mediaItem1);
           player.prepare();
           player.play();
           video_view.hideController();
       }
       else
       {
           Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
       }
    }
}