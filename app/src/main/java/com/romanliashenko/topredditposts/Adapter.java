package com.romanliashenko.topredditposts;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.romanliashenko.topredditposts.model.Publication;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {
    Context context;
    List<Publication> publications;

    public Adapter(Context context, List<Publication> publications) {
        this.context = context;
        this.publications = publications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_publication, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.authorText.setText(publications.get(position).getAuthor());
        String timerTextTemp = Math.round((new Date().getTime()/1000f - publications.get(position).getCreated_utc())/3600f)+ " hors ago";
        holder.timerText.setText(timerTextTemp);
        holder.commentCounterText.setText(String.valueOf(publications.get(position).getNum_comments()));

        if (!publications.get(position).getThumbnail().contains("default") || !publications.get(position).getThumbnail().contains("https://external-preview.redd.it")) {
            try {
                InputStream inputStream = (InputStream) new URL(publications.get(position).getThumbnail()).getContent();
                Drawable drawable = Drawable.createFromStream(inputStream, "thumbnail1");
                holder.thumbnail.setImageDrawable(drawable);
                holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToImageURL(publications.get(position).getThumbnail());
                    }
                });
            } catch (IOException e) {
                System.out.println("ERROR: Publication #" + position);
                holder.thumbnail.setImageResource(R.drawable.ic_no_image);
                holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
        else {
            System.out.println("ERROR: Publication #" + position + " contains \"default\" or https://external-preview.redd.it");
        }
    }

    private void goToImageURL(String thumbnailUrl) {
        Intent openImageUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(thumbnailUrl));
        openImageUrl.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(openImageUrl);
    }

    @Override
    public int getItemCount() {
        return publications.size();
    }
}
