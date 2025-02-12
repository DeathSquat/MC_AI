package com.eyantra.mind_cure_ai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
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

        gamesGrid = findViewById(R.id.gamesGrid);
        gamesList = new ArrayList<>();

        // Add existing games
        gamesList.add(new GameModel("Bubble Pop", R.drawable.bubble_pop_logo, BubbleGameActivity.class));

        // Future games can be added here

        gameAdapter = new GameAdapter(this, gamesList);
        gamesGrid.setAdapter(gameAdapter);
    }
}
