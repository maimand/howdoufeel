package com.example.howdoufeel.UI;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.howdoufeel.Model.Song;
import com.example.howdoufeel.R;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class HomeFragment extends Fragment{
    private static final String CLIENT_ID = "9358ba5d9d204c348e4f4e96f6e873ab";
    private static final String REDIRECT_URI = "com.example.howdoufeel://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    private boolean isPause = false;
    private String songURI = "", songImageURI, songName, songArtists;
    private long songLength;

    private ImageView play_pause, next, pre, shuffle, repeat;
    private TextView name, artist, songProgress, songDuration;
    private ImageView image;

    private SeekBar seekBar;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindingView(view);
        setSeekBar(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(getContext(), connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("errorConnect", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected() {
        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:artist:5dfZ5uSmzR7VQK0udbAVpf");
        UpdateSongTime.run();

        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(new Subscription.EventCallback<PlayerState>() {
                    @Override
                    public void onEvent(PlayerState playerState) {
                        Log.d("position", "" + playerState.playbackPosition);
                        final Track track = playerState.track;
                        Log.d("trackinfo", track.uri);
                        if (track != null) {
                            if (isChangeSong(track.uri)) {
                                seekBar.setProgress(0);
                                updateTrackInfo(track);
                            }
                            if (playerState.isPaused) isPause = true;
                            else isPause = false;
                        }
                        Log.d("trackinfo1", String.valueOf(isPause) + " def");

                    }
                });
    }

    public void updateTrackInfo(Track track){
        songLength = track.duration;
        songName = track.name;
        songArtists = track.artist.name;
        songURI = track.uri;
        songImageURI = "https://i.scdn.co/image/" + track.imageUri.toString().substring(22, track.imageUri.toString().length() - 2);

        name.setText(track.name);
        artist.setText(track.artist.name);
        songDuration.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) track.duration),
                TimeUnit.MILLISECONDS.toSeconds((long) track.duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                track.duration)))
        );
        Picasso.get()
                .load(songImageURI)
                .resize(150, 150)
                .centerCrop()
                .into(image);
        Log.d("trackinfo2", songName + songArtists + songURI);

    }

    public boolean isChangeSong(String uri) {
        if (songURI.equals(uri)) return false;
        else return true;
    }

    public void bindingView(View view){
        play_pause = view.findViewById(R.id.play_pause);
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPause) {
                    mSpotifyAppRemote.getPlayerApi().resume();
                    play_pause.setImageResource(R.drawable.pause);
                }
                else {
                    mSpotifyAppRemote.getPlayerApi().pause();
                    play_pause.setImageResource(R.drawable.play);
                }
            }
        });
        next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpotifyAppRemote.getPlayerApi().skipNext();
            }
        });

        pre = view.findViewById(R.id.pre);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpotifyAppRemote.getPlayerApi().skipPrevious();
            }
        });
        shuffle = view.findViewById(R.id.shuffle);
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mSpotifyAppRemote.getPlayerApi().set
            }
        });
        repeat = view.findViewById(R.id.repeat);
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getContext(), "clicked", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        name = view.findViewById(R.id.name);
        artist = view.findViewById(R.id.artist);
        image = view.findViewById(R.id.song_image);
        songProgress = view.findViewById(R.id.songProgess);
        songDuration = view.findViewById(R.id.songDuration);
    }

    public void setSeekBar(View view) {
        seekBar = view.findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(UpdateSongTime);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seek = (int) (seekBar.getProgress() * songLength / 100);
                mSpotifyAppRemote.getPlayerApi().seekTo(seek);
                mHandler.postDelayed(UpdateSongTime, 300);
            }
        });
    }


    Handler mHandler = new Handler();
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {

            mSpotifyAppRemote.getPlayerApi().getPlayerState()
                    .setResultCallback(playerState -> {
                        songProgress.setText(String.format("%d:%d",
                                TimeUnit.MILLISECONDS.toMinutes((long) playerState.playbackPosition),
                                TimeUnit.MILLISECONDS.toSeconds((long) playerState.playbackPosition) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                toMinutes((long) playerState.playbackPosition)))
                        );
                        seekBar.setProgress((int) ((playerState.playbackPosition * 100 / playerState.track.duration)));
                        mHandler.postDelayed(this, 300);
                    })
                    .setErrorCallback(throwable -> {
                        // error handle here
                    });
        }
    };
}