package com.example.cardstudying;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    TextView card;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        card = findViewById(R.id.card);
        setNewCard();
        setNextButton();
    }

    private void setNewCard() {
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from cardsTable ORDER BY RANDOM() LIMIT 1", new String[]{});
        if (c.moveToNext())
            setMainTxt(c);
        c.close();
    }

    private void setMainTxt(Cursor c) {
        final String question = c.getString(c.getColumnIndex("question"));
        final String answer = c.getString(c.getColumnIndex("answer"));
        card.setText(question);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.card_flip));
                if (card.getText().equals(question)) card.setText(answer);
                else card.setText(question);
            }
        });
    }

    private void setNextButton() {
        findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewCard();
                card.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.card_flip));
            }
        });
    }
}
