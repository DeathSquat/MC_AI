package com.eyantra.mind_cure_ai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MemoryMatchAdapter extends BaseAdapter {
    private Context context;
    private Integer[] cards;
    private OnCardClickListener listener;

    public MemoryMatchAdapter(Context context, Integer[] cards, OnCardClickListener listener) {
        this.context = context;
        this.cards = cards;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return cards.length;
    }

    @Override
    public Object getItem(int position) {
        return cards[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.memory_card_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.cardImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageResource(R.drawable.card_back);
        convertView.setOnClickListener(view -> listener.onCardClick(view, position));

        return convertView;
    }

    public static class ViewHolder {
        ImageView imageView;
    }

    public interface OnCardClickListener {
        void onCardClick(View view, int position);
    }
}
