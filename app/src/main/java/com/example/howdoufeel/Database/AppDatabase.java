package com.example.howdoufeel.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.howdoufeel.Model.Song;

@Database(entities = {Song.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SongsDao getSongsDao();
}