package com.romanliashenko.topredditposts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RedditPost {
    private PostData data;

    public PostData getData() {
        return data;
    }

    public void setData(PostData data) {
        this.data = data;
    }
}