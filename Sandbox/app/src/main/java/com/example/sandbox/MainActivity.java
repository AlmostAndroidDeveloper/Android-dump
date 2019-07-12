package com.example.sandbox;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends Activity {


    int[] levels = {1, 2, 4, 3, 1, 5, 6, 7, 3, 3, 1, 2, 4, 3, 1, 5, 6, 7, 3, 3, 1, 2, 4, 3, 1, 5, 6, 7, 3, 3};
    String[] questions = {"first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth", "first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth", "first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth"};
    String[] answers = {"первый", "второй", "третий", "четвертый", "пятый", "шестой", "седьмой", "восьмой", "девятый", "десятый", "первый", "второй", "третий", "четвертый", "пятый", "шестой", "седьмой", "восьмой", "девятый", "десятый", "первый", "второй", "третий", "четвертый", "пятый", "шестой", "седьмой", "восьмой", "девятый", "десятый"};
    GridView mainGrid;
    ArrayList<Card> cards = new ArrayList<>();
    CardAdapter cardAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fillData();
        cardAdapter = new CardAdapter(this, cards);

        mainGrid = findViewById(R.id.main_grid);
        mainGrid.setAdapter(cardAdapter);
    }

    private void fillData() {
        for (int i = 0; i < levels.length; i++)
            cards.add(new Card(levels[i], questions[i], answers[i]));
    }

    private void setMainGrid() {

    }
}