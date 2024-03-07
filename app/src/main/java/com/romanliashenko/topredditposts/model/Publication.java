package com.romanliashenko.topredditposts.model;

public class Publication {
    private String thumbnail;
    private String author;
    private long created_utc;
    private int num_comments;

    public Publication(String thumbnail, String author, long created_utc, int num_comments) {
        this.thumbnail = thumbnail;
        this.author = author;
        this.created_utc = created_utc;
        this.num_comments = num_comments;
    }

    @Override
    public String toString() {
        return "model.Publication{" +
                "thumbnail='" + thumbnail + '\'' +
                ", author='" + author + '\'' +
                ", created_utc=" + created_utc +
                ", num_comments=" + num_comments +
                '}';
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getCreated_utc() {
        return created_utc;
    }

    public void setCreated_utc(long created_utc) {
        this.created_utc = created_utc;
    }

    public int getNum_comments() {
        return num_comments;
    }

    public void setNum_comments(int num_comments) {
        this.num_comments = num_comments;
    }
}