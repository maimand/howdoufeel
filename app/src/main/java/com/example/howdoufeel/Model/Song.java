package com.example.howdoufeel.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Song {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ColumnInfo(name = "songId")
    private String songId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "imageUri")
    private String imageUri;

    @ColumnInfo(name="artists")
    private String artists;

    @ColumnInfo(name="playlist")
    private String playlist;

    @ColumnInfo(name="isFavorite")
    private boolean isFavorite;

    public Song(){

    }

    public Song(String name,@NonNull String id, String imageUri, String artists, String playlist) {
        this.name = name;
        this.songId = id;
        this.imageUri = imageUri;
        this.artists = artists;
        this.playlist = playlist;
        this.isFavorite = false;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getPlaylist() {
        return playlist;
    }

    public void setPlaylist(String playlist) {
        this.playlist = playlist;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
