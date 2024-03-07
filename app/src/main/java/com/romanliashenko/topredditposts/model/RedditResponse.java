package com.romanliashenko.topredditposts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RedditResponse {
    private RedditData data;

    public RedditData getData() {
        return data;
    }

    public void setData(RedditData data) {
        this.data = data;
    }
}