package com.example.videostream.ActivityClass;

import static com.google.android.exoplayer2.PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND;
import static com.google.android.exoplayer2.PlaybackException.ERROR_CODE_IO_UNSPECIFIED;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.videostream.NetworkConnectivity;
import com.example.videostream.NetworkListner;
import com.example.videostream.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;


public class VideoActivity extends AppCompatActivity implements NetworkListner {

    PlayerView video_view;
    String url;
    ExoPlayer player;
    BroadcastReceiver broadcastReceiver = null;
    int getNetwork = -1;
    AlertDialog alertDialog;
    int errorCode = -1;
    AlertDialog UrlErrorDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        video_view = findViewById(R.id.video_view);


        //GETTING URL FROM INTENT
        Bundle getUrl = getIntent().getExtras();
        url = getUrl.getString("url");

        //DASH URL FOR CHECKING
        //url="https://dash.akamaized.net/dashif/ad-insertion-testcase1/batch5/real/b/ad-insertion-testcase1.mpd";

        //EXOPLAYER BUILDER
        player = new ExoPlayer.Builder(VideoActivity.this).build();
        video_view.setPlayer(player);


        //BROADCAST RECIEVER FOR INTERNET CONNECTIVITY
        broadcastReceiver = new NetworkConnectivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //FIRST WE CHECK NETWORK IS AVAILABLE OR NOT
        if (!isOnline(getApplicationContext())) {
            retry();
        } else {
            loaddata();
        }


    }

    //WE GET URL IN onceate() FUNCTION SO THIS FUNCTION GETS URL FROM oncreate AND LOAD DATA ON PLAYERVIEW WHENEVER IT'S NEEDED :
    private void loaddata() {
        try {

            MediaItem mediaItem1 = MediaItem.fromUri(url);
            player.setMediaItem(mediaItem1);
            player.prepare();
            player.play();
            video_view.hideController();

        } catch (Exception e) {
            Toast.makeText(this, "Something error occured", Toast.LENGTH_SHORT).show();
        }

        //THIS ADDLISTNER USED FOR A PLAYBACK ERROR
        //IF ANY ERROR OCCURED DURING PLAYBACK IT WILL THROWS ERROR AND FOR THAT WE USED A CHECKERRORS FUNCTION
        //IF USER GETS INTERNET CONNECTIVITY ERROR : FOR THAT WE USED A BROADCAST RECEIVER SO ACTIVITY GET DIALOG NO INTERNET CONNECTION
        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                int code[] = {PlaybackException.ERROR_CODE_PARSING_CONTAINER_UNSUPPORTED,
                        ERROR_CODE_IO_FILE_NOT_FOUND,
                        ERROR_CODE_IO_UNSPECIFIED};

                errorCode = error.errorCode;


                for (int i = 0; i < code.length; i++) {
                    if (errorCode == code[i]) {
                        urlDialog();
                    } else {
                        urlDialog();
                    }
                }

            }
        });
    }

    //TO POPUP ALERT DIALOG NETWORK ERROR USES THIS FUNCTION
    private void retry() {
        alertDialog = new AlertDialog.Builder(VideoActivity.this)
                .setMessage("No Internet Connection")
                .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (getNetwork == 0) {
                            if (alertDialog != null) {
                                alertDialog.dismiss();
                            }
                            loaddata();
                        } else {
                            retry();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();
                    }
                })
                .setCancelable(false)
                .show();
    }

    //IF ANY ERROR OCCURED IT WILL SHOWS A URL ERROR
    private void urlDialog() {
        UrlErrorDialog = new AlertDialog.Builder(VideoActivity.this)
                .setMessage("Entered Url are Wrong !")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();
                    }
                })
                .show();
    }

    //REGISTER A RECEIVER
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, filter);
    }

    //UNREGISTER A RECEIVER
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }


    //CHECKING INTERNET CONNECTION WHEN ENTERING IN ACTIVITY
    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(context, "Netwekwlfmer", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //RECEIVE NETWORK STATE FROM BROADCAST RECEIVER
    @Override
    public void getState(int network_state) {
        getNetwork = network_state;
        if (getNetwork == 0) {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            onResume();
        }
        if (getNetwork == 1) {
            if (player != null) {
                player.stop();
            }
            if (alertDialog != null) {
                alertDialog.dismiss();
                retry();
            } else {
                retry();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.stop();
    }

    @Override
    public void onBackPressed() {
        if (UrlErrorDialog != null) {
            UrlErrorDialog.dismiss();
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        try {
            player.stop();
        } catch (Exception e) {

        }
        super.onBackPressed();
        this.finish();
    }
}