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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class AddCardsActivity extends AppCompatActivity {

    DBHelper dbhelper;

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
        final LinearLayout mainLayout = findViewById(R.id.main_layout);
        mainLayout.removeAllViews();

        do {
            final View layout = getLayoutInflater().inflate(R.layout.one_task_layout, null, false);
            layout.setId(c.getInt(c.getColumnIndex("id")));

            TextView level = makeTextView(layout, R.id.level_text, String.valueOf(c.getInt(c.getColumnIndex("level"))));
            TextView question = makeTextView(layout, R.id.question_text, c.getString(c.getColumnIndex("question")));
            TextView answer = makeTextView(layout, R.id.answer_text, c.getString(c.getColumnIndex("answer")));
            Button delBtn = makeDeleteButton(mainLayout, layout);

            mainLayout.addView(layout);
        }
        while (c.moveToNext());
        c.close();
    }

    private Button makeDeleteButton(final LinearLayout mainLayout, final View layout) {
        Button button = layout.findViewById(R.id.del_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(layout.getId()).equalsIgnoreCase("")) {
                    return;
                }
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                db.delete("cardsTable", "id = " + layout.getId(), new String[]{});
                mainLayout.removeView(layout);
                db.close();
            }
        });
        return button;
    }

    private TextView makeTextView(View layout, int viewId, String textToSet) {
        TextView textView = layout.findViewById(viewId);
        textView.setText(textToSet);
        return textView;
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
