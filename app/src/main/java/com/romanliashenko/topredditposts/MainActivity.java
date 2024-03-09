package com.romanliashenko.topredditposts;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;

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
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //ArrayList with all the Reddit Publications
    ArrayList<Publication> publications = new ArrayList<>();

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
    }

    public void readPublications() {
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
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO change this method to set given URL to given View element
    private void setImage(View view) {
        try {
            InputStream is = (InputStream) new URL("https://b.thumbs.redditmedia.com/v_82-Thy85ThGWSqPHUxETX1yWCL11P3hofnXaMakzU.jpg").getContent();
            Drawable d = Drawable.createFromStream(is, "thumbnail");
//            ImageView imageView = (ImageView) findViewById(R.id.imageView);
//            imageView.setImageDrawable(d);
        } catch (Exception e) {

        }
    }

    //TODO change this method to open URL in web
    public void goToImageUrl(View view) {
        String url = "https://b.thumbs.redditmedia.com/v_82-Thy85ThGWSqPHUxETX1yWCL11P3hofnXaMakzU.jpg";
        Uri uriUrl = Uri.parse(url);
        Intent openImageUrl = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(openImageUrl);
    }
}