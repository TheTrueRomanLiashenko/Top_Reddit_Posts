package com.romanliashenko.topredditposts;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

    private TextView textView;
    private Button button;

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

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
    }

    public void readPublications(View v) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            //Here we read data from https://www.reddit.com/top.json and extract needed one
            //using Jackson library to Publication objects.
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

            //Just some testing. Will be deleted soon.
            Date date;
            StringBuilder result = new StringBuilder();
            for (Publication publication : publications) {
                date = new Date();
                Date postDate = new Date(publication.getCreated_utc());
                result.append(publication.toString()).append('\n');
                result.append("Publication was made ")
                        .append((date.getTime() / 1000f - postDate.getTime()) / 3600f)
                        .append(" hours ago. \n\n");
            }
            textView.setText(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}