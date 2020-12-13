package com.example.howdoufeel.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.howdoufeel.Database.AppDatabase;
import com.example.howdoufeel.Database.FavoriteAdapter;
import com.example.howdoufeel.Database.SongsDao;
import com.example.howdoufeel.Model.Song;
import com.example.howdoufeel.R;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment {

    ArrayList<Song> songs;
    FavoriteAdapter favoriteAdapter;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_favorite, container, false);

        AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, "Song")
                .allowMainThreadQueries()
                .build();
        SongsDao songsdao = database.getSongsDao();
        List<Song> data = songsdao.getAllSong();
        songs=new ArrayList<>();
        songs.addAll(data);
        favoriteAdapter=new FavoriteAdapter(getContext(),songs);
        recyclerView = root.findViewById(R.id.recyclerSong);
        recyclerView.setAdapter(favoriteAdapter);
        Log.d("TAG", "onCreate: " +songs);
        System.out.println(songs);


        return root;
    }
}