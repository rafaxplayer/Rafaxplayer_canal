package com.rafaxpalyer.rafaxplayer_canal.Models;

/**
 * Created by rafax on 08/06/2016.
 */
public class Comment {
    private String nick;
    private String commnet;
    private String plublishedAt;
    private String imageuser;

    public Comment(String nick, String commnet, String plublishedAt, String imageuser) {
        this.nick = nick;
        this.commnet = commnet;
        this.imageuser = imageuser;
        this.plublishedAt=plublishedAt;
    }

    public String getPlublishedAt() {
        return plublishedAt;
    }

    public void setPlublishedAt(String plublishedAt) {
        this.plublishedAt = plublishedAt;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getCommnet() {
        return commnet;
    }

    public void setCommnet(String commnet) {
        this.commnet = commnet;
    }

    public String getImageuser() {
        return imageuser;
    }

    public void setImageuser(String imageuser) {
        this.imageuser = imageuser;
    }
}
