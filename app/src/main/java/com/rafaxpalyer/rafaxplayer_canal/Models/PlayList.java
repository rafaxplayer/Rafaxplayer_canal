package com.rafaxpalyer.rafaxplayer_canal.Models;

import java.util.ArrayList;

public class PlayList {

    private String title;
    private String playlistid;
    private String description;



    private String thumbnail;
    private ArrayList<String> videoIds;

    public PlayList(String title, String playlistid, String description, String thumbnail) {
        this.title = title;
        this.playlistid = playlistid;
        this.description = description;
        this.thumbnail = thumbnail;
        this.videoIds= new ArrayList<String>();
    }
    public ArrayList<String> getVideoIds() {
        return videoIds;
    }

    public void setVideoIds(ArrayList<String> videoIds) {
        this.videoIds = videoIds;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlaylistid() {
        return playlistid;
    }

    public void setPlaylistid(String playlistid) {
        this.playlistid = playlistid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
