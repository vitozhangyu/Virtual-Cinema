package com.example.yu.videochat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class YoutubeFragment extends Fragment implements YouTubePlayer.OnInitializedListener{

    FragmentActivity mContext;
    public YouTubePlayer player;
    private AutoCompleteTextView urlATV;
    private AutoCompleteTextView controlATV;
    private String url = "a4NT5iBFuZs";
    private MyPlaybackEventListener playbackEventListener;
    public String timeElapsed;
    public String actionMessage = "none";

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference myChildRef = myRef.child("data");

    /*public interface FragmentActivityCommunication {
        void ActivityFragmentCommunication(String text);

    }
    */


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        /*
        try{
            mCallback = (FragmentActivityCommunication) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + "must implement FragmentActicityCommunication");
        }
        */

        if (activity instanceof FragmentActivity) {
            mContext = (FragmentActivity) activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.youtube_fragment, container, false);
        playbackEventListener = new MyPlaybackEventListener();

        myChildRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String actionMessage = dataSnapshot.getValue(String.class);
                if(actionMessage.contains("play")){
                    player.play();
                    actionMessage = "";
                }
                if(actionMessage.contains("pause")){
                    player.pause();
                    actionMessage = "";
                }
                if(actionMessage.contains("seekto")){
                    int skipToSecs = Integer.valueOf(actionMessage.replace("seekto",""));
                    player.seekToMillis(skipToSecs);
                    actionMessage = "";
                }
                if(actionMessage.contains("url")){
                    url = actionMessage.replace("url","");
                    player.cueVideo(url);
                    actionMessage = "";
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        urlATV = view.findViewById(R.id.urlATV);
        Button playButton = (Button) view.findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = (String) urlATV.getText().toString();
                if (url.contains("https://youtu.be/")){
                    url=url.replace("https://youtu.be/","");
                }
                myChildRef.setValue("url"+url);
                player.cueVideo(url);
            }
        });

        /*
        final EditText seekToText = (EditText) view.findViewById(R.id.seek_to_text);
        Button seekToButton = (Button) view.findViewById(R.id.seek_to_button);
        seekToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int skipToSecs = Integer.valueOf(seekToText.getText().toString());
                player.seekToMillis(skipToSecs * 1000);
            }
        });
        */

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, this);

        return view;
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            player = youTubePlayer;

            player.setShowFullscreenButton(false);





            player.setPlaybackEventListener(playbackEventListener);


            player.cueVideo("2zNSgSzhBfM");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    private void showMessage(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            showMessage("Playing");
            myChildRef.setValue("play");
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
            showMessage("Paused");
            myChildRef.setValue("pause");
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
            showMessage("Stopped");
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            timeElapsed = Integer.toString(player.getCurrentTimeMillis());
            showMessage(timeElapsed);
            myChildRef.setValue("seekto"+timeElapsed);
        }


    }

    //ferhat's code



}
