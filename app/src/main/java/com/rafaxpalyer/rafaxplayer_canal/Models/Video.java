package com.rafaxpalyer.rafaxplayer_canal.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {
    public String videoid;
    public String playlistId;
    public String thumbnail;
    public String title;
    public String description;
    public String date;
    public String duration;
    public String likes;
    public String viewcount;



    public Video() {

    }
    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getViewcount() {
        return viewcount;
    }

    public void setViewcount(String viewcount) {
        this.viewcount = viewcount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String data) {
        this.date = date;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static final Parcelable.Creator<Video> CREATOR =
            new Parcelable.Creator<Video>()
            {
                @Override
                public Video createFromParcel(Parcel parcel)
                {
                    return new Video(parcel);
                }

                @Override
                public Video[] newArray(int size)
                {
                    return new Video[size];
                }
            };

    public Video(Parcel parcel)
    {

        videoid = parcel.readString();
        playlistId = parcel.readString();
        thumbnail = parcel.readString();
        title = parcel.readString();;
        description = parcel.readString();
        date=parcel.readString();
        duration=parcel.readString();
        likes=parcel.readString();
        viewcount=parcel.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeString(videoid);
        parcel.writeString(playlistId);
        parcel.writeString(thumbnail);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(date);
        parcel.writeString(duration);
        parcel.writeString(likes);
        parcel.writeString(viewcount);

    }
}
