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

import com.example.howdoufeel.R;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.squareup.picasso.Picasso;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class HomeFragment extends Fragment {
    private static final String CLIENT_ID = "44c2497a375d418db9065456518bbd4e";
    private static final String REDIRECT_URI = "com.example.howdoufeel://callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private final String[] GOOD_MOOD = {"playlist:37i9dQZF1DX3rxVfibe1L0", // mood booster
                                        "playlist/37i9dQZF1DXdPec7aLTmlC", // happy songs
                                        "playlist/5s8Fc6myVaFFWkRH73k62g"};// happy hits

    private final String[] BAD_MOOD = {"playlist:37i9dQZF1DX3rxVfibe1L0", // mood booster
                                        "playlist:1nVWPImtwYUYdvCTPHTtpJ", // sad lofi
                                        "playlist:37i9dQZF1DX64Y3du11rR1"};// sad covers
    private String mood;
    private boolean isPause = false;
    private String songURI = "", songImageURI, songName, songArtists;
    private long songLength;
    private ImageView play_pause, next, pre, shuffle, repeat;
    private TextView name, artist, songProgress, songDuration;
    private ImageView image;
    private String playlist;
    private boolean isStreaming;

    private SeekBar seekBar;
    private PlayingModel model;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isStreaming", isStreaming);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingView(view);
        setSeekBar(view);
        Bundle bundle = getArguments();
        if(bundle != null){
            mood = bundle.getString("mood");
        }else{
            mood = "happy";
        }
        if(savedInstanceState!=null){
            isStreaming = savedInstanceState.getBoolean("isStreaming");
            Log.e("no","no");
        }else {
            isStreaming = false;
        }
    }
//    public void saveData(String mood, String playlist, boolean isStreaming) {
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("namesharedPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("mood", mood);
//        editor.putString("playlist", playlist);
//        editor.putBoolean("isStreaming", isStreaming);
//        editor.apply();
//    }
//    public void loadData() {
//        SharedPreferences namesharedPrefs = getContext().getSharedPreferences("namesharedPrefs", MODE_PRIVATE);
//        mood = namesharedPrefs.getString("mood", "happy");
//        playlist = namesharedPrefs.getString("playlist", "");
//        isStreaming = namesharedPrefs.getBoolean("isStreaming", false);
//    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("stream?", isStreaming?"yes":"0");
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(getContext(), connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;

                        Log.e("stream?", isStreaming?"yes":"0");

                        if(!isStreaming){
                            connected();
                        }
                        // Now you can start interacting with App Remote
                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
//        saveData("","", false);
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected() {
        isStreaming = true;
        // Play a playlist
        if(mood.equals("happy")){
            playlist = getRandom(GOOD_MOOD);
        }else{
            playlist = getRandom(BAD_MOOD);
        }
        mSpotifyAppRemote.getPlayerApi().play("spotify:" + playlist);
        UpdateSongTime.run();
//

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(new Subscription.EventCallback<PlayerState>() {
                    @Override
                    public void onEvent(PlayerState playerState) {
                        final Track track = playerState.track;
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
    public static String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
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