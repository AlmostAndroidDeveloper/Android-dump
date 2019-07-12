package com.example.cardstudying;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AddCardsActivity extends AppCompatActivity {

    DBHelper dbhelper;
    GridView mainGrid;
    ArrayList<Card> cards = new ArrayList<>();
    CardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetCursorTask mTask = new GetCursorTask();
        mTask.execute();
        setContentView(R.layout.activity_add_cards);
    }

    private void makeAddButton() {
        findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), AddNewCardActivity.class), 1);
            }
        });
    }

    private void makeCardsList(Cursor c) {
        fillCardsData(c);
        mainGrid = findViewById(R.id.main_grid);
        cardAdapter = new CardAdapter(this, cards);
        mainGrid.setAdapter(cardAdapter);
        mainGrid.setVerticalSpacing(5);
        mainGrid.setHorizontalSpacing(5);
    }

    private void fillCardsData(Cursor c) {
        do {
            cards.add(new Card(
                    c.getInt(c.getColumnIndex("level")),
                    c.getString(c.getColumnIndex("question")),
                    c.getString(c.getColumnIndex("answer")),
                    c.getInt(c.getColumnIndex("id"))));
        } while (c.moveToNext());
        c.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) return;

        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("question", data.getStringExtra("question"));
        cv.put("answer", data.getStringExtra("answer"));
        cv.put("level", 0);
        long rowID = db.insert("cardsTable", null, cv);

        Cursor c = db.rawQuery("select * from cardsTable ORDER BY level, question", new String[]{});
        if (c.moveToNext())
            makeCardsList(c);
    }

    class GetCursorTask extends AsyncTask<Void, Void, Void> {

        private Cursor c;
        ProgressDialog pd;
        SQLiteDatabase db;

        @Override
        protected Void doInBackground(Void... voids) {
            dbhelper = new DBHelper(getApplicationContext());
            db = dbhelper.getReadableDatabase();
            c = db.rawQuery("select * from cardsTable ORDER BY level DESC, question", new String[]{});
            return null;
        }

        @Override
        protected void onPreExecute() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd = new ProgressDialog(AddCardsActivity.this);
                    pd.setMessage("Загрузка");
                    pd.show();
                }
            });

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (c.moveToNext())
                        makeCardsList(c);
                    makeAddButton();
                    pd.cancel();
                }
            });
            c.close();
            db.close();
        }
    }

}
