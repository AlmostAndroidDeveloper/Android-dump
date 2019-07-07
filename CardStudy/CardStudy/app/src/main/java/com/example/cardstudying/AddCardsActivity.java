package com.example.cardstudying;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddCardsActivity extends AppCompatActivity {

    DBHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cards);
        dbhelper = new DBHelper(this);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from cardsTable", new String[]{});
        if (c.moveToNext())
            makeCardsList(c);
        db.close();
        makeAddButton();
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

            //TextView id = makeTextView(layout, R.id.id_text, String.valueOf(c.getInt(c.getColumnIndex("id"))));
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
        long rowID = db.insert("cardsTable", null, cv);

        Cursor c = db.rawQuery("select * from cardsTable", new String[]{});
        if (c.moveToNext())
            makeCardsList(c);
    }
}
