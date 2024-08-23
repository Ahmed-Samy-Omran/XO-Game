package com.example.myxogame;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HighScoreActivityActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHighScores;
    private HighScoreAdapter highScoreAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score_activity);

        recyclerViewHighScores = findViewById(R.id.recyclerViewHighScores);
        recyclerViewHighScores.setLayoutManager(new LinearLayoutManager(this));

        // Initialize database helper and fetch the high scores
        databaseHelper = new DatabaseHelper(this);

        // Use the correct method to retrieve scores
        List<Score> highScores = databaseHelper.getAllScores();

        // Set up the adapter with the list of high scores
        highScoreAdapter = new HighScoreAdapter(highScores);
        recyclerViewHighScores.setAdapter(highScoreAdapter);
    }
}