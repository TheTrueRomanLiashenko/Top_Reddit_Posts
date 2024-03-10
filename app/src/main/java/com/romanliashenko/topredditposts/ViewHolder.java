package com.romanliashenko.topredditposts;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView thumbnail, authorAvatar, timer, commentCounter;
    TextView authorText, timerText, commentCounterText;
    ImageButton buttonMore;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        thumbnail = itemView.findViewById(R.id.thumbnail);
        authorAvatar = itemView.findViewById(R.id.author_avatar_ic_drawable);
        timer = itemView.findViewById(R.id.timer_ic_drawable);
        commentCounter = itemView.findViewById(R.id.comments_counter_ic_drawable);

        authorText = itemView.findViewById(R.id.author_name_text);
        timerText = itemView.findViewById(R.id.timer_text);
        commentCounterText = itemView.findViewById(R.id.comments_counter_text);

        buttonMore = itemView.findViewById(R.id.buttonMore);
    }
}
