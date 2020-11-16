package com.example.yu.videochat;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vidyo.VidyoClient.Connector.ConnectorPkg;
import com.vidyo.VidyoClient.Connector.Connector;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;


public class MainActivity extends AppCompatActivity implements Connector.IConnect /*, YoutubeFragment.FragmentActivityCommunication*/ {
    private Connector vc;
    private FrameLayout videoFrame;
    public int seektotime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ConnectorPkg.setApplicationUIContext(this);
        ConnectorPkg.initialize();
        videoFrame=(FrameLayout)findViewById(R.id.videoFrame);
    }





    /*@Override
    public void ActivityFragmentCommunication(String text) {
        YoutubeFragment youtubeFragment = (YoutubeFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        if (seektotime != 100000) {
            youtubeFragment.videoSetTo(seektotime);
            seektotime = 100000;
        }
    }
    */

    public void Start (View v){
        vc=new Connector(videoFrame,Connector.ConnectorViewStyle.VIDYO_CONNECTORVIEWSTYLE_Default,3,"","",0);
        vc.showViewAt(videoFrame,0,0,videoFrame.getWidth(),videoFrame.getHeight());
    }

    public void Connect(View v){
        String token="cHJvdmlzaW9uAHZpc3VhbGNpbmVtYUBkYzQzYTQudmlkeW8uaW8ANjM2OTA1MDkwNzMAADg4YWQxODVhNGMzNGFiZGIzNmQ5MzBlMmUxN2Q2N2VkNjExODVlODA4NTdjN2JlMDZjNjRmMzdlNmE2Nzg3YmJmZmQxYTAyOWQyZDYwZTE5Y2ZjMTNlOTdiZmIzYzNmNQ==";
        vc.connect("prod.vidyo.io",token,"visualcinema","group10", this);
    }
    public void Disconnect (View v){
        vc.disconnect();

    }

    @Override
    public void onSuccess() {
    }

    @Override
    public void onFailure(Connector.ConnectorFailReason connectorFailReason) {

    }

    @Override
    public void onDisconnected(Connector.ConnectorDisconnectReason connectorDisconnectReason) {

    }
}
