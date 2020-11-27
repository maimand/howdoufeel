package com.example.howdoufeel.Model;

import android.graphics.Bitmap;

public class Song {
    private String name;
    private String id;
    private String imageuri;
    private String artists;
    private String playlist;
    private boolean isFavorite;

    public Song(String name, String id, String imageuri, String artists, String playlist) {
        this.name = name;
        this.id = id;
        this.imageuri = imageuri;
        this.artists = artists;
        this.playlist = playlist;
        this.isFavorite = false;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return imageuri;
    }

    public String getArtists() {
        return artists;
    }

    public String getPlaylist() {
        return playlist;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String imageuri) {
        this.imageuri = imageuri;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public void setPlaylist(String playlist) {
        this.playlist = playlist;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
