package com.example.sandbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CardAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater layoutInflater;
    ArrayList<Card> cards;

    CardAdapter(Context context, ArrayList<Card> cards) {
        this.ctx = context;
        this.cards = cards;
        this.layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = layoutInflater.inflate(R.layout.item, parent, false);

        Card card = cards.get(position);

        ((TextView) view.findViewById(R.id.level_text)).setText(String.valueOf(card.level));
        ((TextView) view.findViewById(R.id.question_text)).setText(card.question);
        ((TextView) view.findViewById(R.id.answer_text)).setText(card.answer);
        view.findViewById(R.id.del_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "clicked", Toast.LENGTH_SHORT).show();
                cards.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
