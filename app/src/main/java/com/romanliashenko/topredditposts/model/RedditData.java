package com.romanliashenko.topredditposts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RedditData {
    private List<RedditPost> children;

    public List<RedditPost> getChildren() {
        return children;
    }

    public void setChildren(List<RedditPost> children) {
        this.children = children;
    }
}