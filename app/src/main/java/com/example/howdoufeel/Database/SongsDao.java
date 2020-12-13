package com.example.howdoufeel.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.howdoufeel.Model.Song;

import java.util.List;

@Dao
public interface SongsDao
{
    @Query("Select * from Song")
    public List <Song> getAllSong ();
    @Query("Delete from Song where songId = :songId ")
    public void deleteSongById(String songId);
    @Query("Select * from Song where songId= :songId")
    public List <Song> getSongById (String songId);
    @Insert
    public void addSong(Song... song);
}
