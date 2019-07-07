package com.example.cardstudying;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnAdd;
    SharedPreferences prefs;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        if (prefs.getBoolean("isFirst", true)) fillDB();
        setStartButton();
        setAddButton();
        setMainCard();
    }

    private void setMainCard() {
        final TextView card = findViewById(R.id.card);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.card_flip));
            }
        });
    }

    private void fillDB() {
        new FillDataBaseTask().execute();
        prefs.edit().putBoolean("isFirst", false).apply();
    }

    private void setAddButton() {
        btnAdd = findViewById(R.id.add_btn);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddCardsActivity.class));
            }
        });
    }

    private void setStartButton() {
        btnStart = findViewById(R.id.start_btn);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
            }
        });
    }

    class FillDataBaseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            dbHelper = new DBHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] allCards = getResources().getString(R.string.values).split(";");
            for (String card : allCards) {
                String[] splitted = card.split("=");
                ContentValues cv = new ContentValues();
                cv.put("question", splitted[0]);
                cv.put("answer", splitted[1]);
                db.insert("cardsTable", null, cv);
            }
            db.close();
            return null;
        }
    }
}


