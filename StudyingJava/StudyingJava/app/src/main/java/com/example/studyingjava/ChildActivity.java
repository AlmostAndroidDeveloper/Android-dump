package com.example.studyingjava;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChildActivity extends AppCompatActivity implements View.OnClickListener {
    Button saveBtn, readBtn;
    EditText firstEditText;
    EditText secondEditText;
    EditText thirdEditText;
    DBHelper dbHelper;
    String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
        readBtn = findViewById(R.id.read_btn);
        readBtn.setOnClickListener(this);
        firstEditText = findViewById(R.id.first_edit_text);
        secondEditText = findViewById(R.id.second_edit_text);
        thirdEditText = findViewById(R.id.third_edit_text);
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        ContentValues cv = new ContentValues();

        String name = firstEditText.getText().toString();
        String lastName = secondEditText.getText().toString();
        String login = thirdEditText.getText().toString();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (v.getId()) {
            case R.id.save_btn:
                cv.put("name", name);
                cv.put("email", lastName);
                cv.put("login", login);
                long rowID = db.insert("mytable", null, cv);
                Toast.makeText(this, "data added to DB", Toast.LENGTH_SHORT).show();
                break;
            case R.id.read_btn:
                final LinearLayout mainLayout = new LinearLayout(getApplicationContext());
                mainLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                mainLayout.setOrientation(LinearLayout.VERTICAL);
                Cursor c = db.query("mytable", null, null, null, null, null, null);
                if (c.moveToNext()) {
                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("name");
                    int lastNameColIndex = c.getColumnIndex("email");
                    int loginColIndex = c.getColumnIndex("login");
                    do {
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", name = " + c.getString(nameColIndex) +
                                        ", email = " + c.getString(lastNameColIndex) +
                                        ", login = " + c.getString(loginColIndex));
                        final LinearLayout layoutToAdd = new LinearLayout(getApplicationContext());
                        TextView idText = new TextView(getApplicationContext());
                        TextView nameText = new TextView(getApplicationContext());
                        TextView lastNameText = new TextView(getApplicationContext());
                        TextView loginText = new TextView(getApplicationContext());
                        idText.setText(String.valueOf(c.getInt(idColIndex)));
                        nameText.setText(c.getString(nameColIndex));
                        lastNameText.setText(c.getString(lastNameColIndex));
                        loginText.setText(c.getString(loginColIndex));
                        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        textParams.weight = 1f;
                        idText.setLayoutParams(textParams);
                        nameText.setLayoutParams(textParams);
                        lastNameText.setLayoutParams(textParams);
                        loginText.setLayoutParams(textParams);
                        layoutToAdd.setId(c.getInt(idColIndex));
                        layoutToAdd.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
                        layoutToAdd.setBackgroundColor(Color.parseColor("#00ffaa"));
                        layoutToAdd.setOrientation(LinearLayout.HORIZONTAL);
                        layoutToAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(String.valueOf(layoutToAdd.getId()).equalsIgnoreCase("")){
                                    return;
                                }
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                db.delete("mytable", "id = " + layoutToAdd.getId(), null);
                                mainLayout.removeView(layoutToAdd);
                                db.close();
                            }
                        });
                        mainLayout.addView(layoutToAdd);
                        layoutToAdd.addView(idText);
                        layoutToAdd.addView(nameText);
                        layoutToAdd.addView(lastNameText);
                        layoutToAdd.addView(loginText);
                    } while (c.moveToNext());
                }

                c.close();
                setContentView(mainLayout);
                break;
        }
        dbHelper.close();
    }
}
