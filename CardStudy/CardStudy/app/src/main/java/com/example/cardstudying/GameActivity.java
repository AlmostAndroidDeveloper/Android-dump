package com.example.cardstudying;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    TextView card;
    DBHelper dbHelper;
    String question, answer;
    SharedPreferences prefs;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        card = findViewById(R.id.card);
        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        setNewCard();
        setHelpText();
        setMarkButtons();
    }

    @Override
    public void onClick(View v) {
        int level = 0;
        switch (v.getId()) {
            case R.id.btn1:
                level = 1;
                break;
            case R.id.btn2:
                level = 2;
                break;
            case R.id.btn3:
                level = 3;
                break;
            case R.id.btn4:
                level = 4;
                break;
            case R.id.btn5:
                level = 5;
                break;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("question", question);
        cv.put("answer", answer);
        cv.put("level", level);
        db.update("cardsTable", cv, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        setNewCard();
    }

    private void setMarkButtons() {
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
    }

    private void setNewCard() {
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from cardsTable ORDER BY level, RANDOM() LIMIT 1", new String[]{});
        if (c.moveToNext())
            setMainTxt(c);
        card.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.card_flip));
        c.close();
    }

    private void setHelpText() {
        findViewById(R.id.help_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                builder.setTitle("Справка")
                        .setMessage(getResources().getString(R.string.help))
                        .setCancelable(false)
                        .setNegativeButton("Понятно",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void setMainTxt(Cursor c) {
        id = c.getInt(c.getColumnIndex("id"));
        question = c.getString(c.getColumnIndex("question"));
        answer = c.getString(c.getColumnIndex("answer"));
        boolean invert = prefs.getBoolean("invert",false);
        card.setText(invert ? answer : question);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.card_flip));
                if (card.getText().equals(question)) card.setText(answer);
                else card.setText(question);
            }
        });
    }

}
