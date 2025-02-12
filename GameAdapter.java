package com.eyantra.mind_cure_ai;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;
import java.util.ArrayList;

public class GameAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GameModel> gamesList;

    public GameAdapter(Context context, ArrayList<GameModel> gamesList) {
        this.context = context;
        this.gamesList = gamesList;
    }

    @Override
    public int getCount() {
        return gamesList.size();
    }

    @Override
    public Object getItem(int position) {
        return gamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_game, parent, false);
        }

        GameModel game = gamesList.get(position);

        ImageView gameImage = convertView.findViewById(R.id.gameImage);
        TextView gameName = convertView.findViewById(R.id.gameName);

        gameImage.setImageResource(game.getGameImage());
        gameName.setText(game.getGameName());

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, game.getGameActivity());
            context.startActivity(intent);
        });

        return convertView;
    }
}
