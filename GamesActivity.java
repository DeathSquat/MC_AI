package com.eyantra.mind_cure_ai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class GamesActivity extends AppCompatActivity {

    private GridView gamesGrid;
    private ArrayList<GameModel> gamesList;
    private GameAdapter gameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        // Initialize views
        gamesGrid = findViewById(R.id.gamesGrid);
        ImageButton backButton = findViewById(R.id.backButton);

        // Back button functionality
        backButton.setOnClickListener(v -> finish());

        // Initialize game list
        gamesList = new ArrayList<>();

        // Add available games
        gamesList.add(new GameModel("Bubble Pop", R.drawable.bubble_pop_logo, BubbleGameActivity.class));
        gamesList.add(new GameModel("Memory Match", R.drawable.memory_match_logo, MemoryMatchActivity.class));

        // Set adapter
        gameAdapter = new GameAdapter(this, gamesList);
        gamesGrid.setAdapter(gameAdapter);

        // Handle game selection
        gamesGrid.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent intent = new Intent(GamesActivity.this, gamesList.get(position).getGameActivity());
            startActivity(intent);
        });
    }
}
