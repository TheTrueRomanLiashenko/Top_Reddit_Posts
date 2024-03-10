package com.romanliashenko.topredditposts;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanliashenko.topredditposts.model.Publication;
import com.romanliashenko.topredditposts.model.RedditPost;
import com.romanliashenko.topredditposts.model.RedditResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //ArrayList with all the Reddit Top Publications
    List<Publication> publications = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        readPublications();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter(getApplicationContext(), publications));
    }

    public void readPublications() {
        //Changing ThreadPolicy is needed in order to have access to network and storage
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Creating ObjectMapper instance from Jackson library
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            /*
            * Here we read data from https://www.reddit.com/top.json and extract needed one
            * using Jackson library to Publication objects.
            */
            RedditResponse response = mapper.readValue(new URL("https://www.reddit.com/top.json"),
                    RedditResponse.class);
            List<RedditPost> redditPosts = response.getData().getChildren();

            for (int i = 0; i < 25; i++) {
                RedditPost redditPost = redditPosts.get(i);
                Publication publication = new Publication(
                        redditPost.getData().getThumbnail(),
                        redditPost.getData().getAuthor(),
                        redditPost.getData().getCreated_utc(),
                        redditPost.getData().getNum_comments()
                );
                publications.add(publication);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * We can save any information using this method in case if activity will be destroyed.
     * Currently we don't need it, but if this project will be modified with extra activities
     * it won't be hard to implement state saving
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putString("SomeKey", "SomeValue");
        super.onSaveInstanceState(outState, outPersistentState);
    }
}