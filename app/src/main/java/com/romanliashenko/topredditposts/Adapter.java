package com.romanliashenko.topredditposts;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
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

        try {
            InputStream inputStream = (InputStream) new URL(publications.get(position).getThumbnail()).getContent();
            Drawable drawable = Drawable.createFromStream(inputStream, "thumbnail");
            holder.thumbnail.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return publications.size();
    }
}
