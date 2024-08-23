package com.example.myxogame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.HighScoreViewHolder> {

    private final List<Score> highScoreList;

    public HighScoreAdapter(List<Score> highScoreList) {
        this.highScoreList = highScoreList;
    }

    @NonNull
    @Override
    public HighScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.high_score_item, parent, false);
        return new HighScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HighScoreViewHolder holder, int position) {
        Score score = highScoreList.get(position);
        holder.playerName.setText(score.getPlayerName());
        holder.playerScore.setText(String.valueOf(score.getScore()));
    }

    @Override
    public int getItemCount() {
        return highScoreList.size();
    }

    public static class HighScoreViewHolder extends RecyclerView.ViewHolder {

        TextView playerName;
        TextView playerScore;

        public HighScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.playerName);
            playerScore = itemView.findViewById(R.id.playerScore);
        }
    }
}
