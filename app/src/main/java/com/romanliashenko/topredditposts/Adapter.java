package com.romanliashenko.topredditposts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

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
                Drawable drawable = Drawable.createFromStream(inputStream, "thumbnail");
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
                    public void onClick(View v) {}
                });
            }
        }
        else {
            System.out.println("ERROR: Publication #" + position + " contains \"default\" or https://external-preview.redd.it");
        }

        holder.buttonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.buttonMore);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.download_thumbnail) {
                            if (downloadThumbnailToGallery(publications.get(position).getThumbnail())){
                                Toast.makeText(context, "Thumbnail downloaded successfully!", Toast.LENGTH_SHORT).show();
                                return true;
                            } else {
                                Toast.makeText(context, "Failed downloading thumbnail", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private boolean downloadThumbnailToGallery(String thumbnailUrl) {
        try {
            InputStream inputStream = (InputStream) new URL(thumbnailUrl).getContent();
            Drawable drawable = Drawable.createFromStream(inputStream, "thumbnail");
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "RedditThumbnail", "");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
