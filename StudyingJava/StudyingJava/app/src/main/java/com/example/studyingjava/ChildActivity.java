package com.example.studyingjava;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Usual order");
        menu.add(0, 1, 0, "Sort by name");
        menu.add(0, 2, 0, "Sort by lastName");
        menu.add(0, 3, 0, "Sort by login");
        menu.add(0, 4, 0, "Descend");
        menu.add(0, 5, 0, "Descend by name");
        menu.add(0, 6, 0, "Descend by lastname");
        menu.add(0, 7, 0, "Descend by login");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(0, true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        String orderBy = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlExpression = null;
        setContentView(R.layout.database_show);
        switch (item.getItemId()) {
            case 0:
                sqlExpression = "SELECT * FROM mytable order by id ;";
                break;
            case 1:
                sqlExpression = "SELECT * FROM mytable order by name ;";
                break;
            case 2:
                sqlExpression = "SELECT * FROM mytable order by lastName ;";
                break;
            case 3:
                sqlExpression = "SELECT * FROM mytable order by login ;";
                break;
            case 4:
                sqlExpression = "SELECT * FROM mytable order by id desc ;";
                break;
            case 5:
                sqlExpression = "SELECT * FROM mytable order by name desc ;";
                break;
            case 6:
                sqlExpression = "SELECT * FROM mytable order by lastName desc ;";
                break;
            case 7:
                sqlExpression = "SELECT * FROM mytable order by login desc ;";
                break;
        }
        Cursor c = db.rawQuery(sqlExpression, new String[]{});
        if (c.moveToNext()) {
            makeDataBaseLayout(c);
        }
        c.close();
        db.close();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        ContentValues cv = new ContentValues();

        String name = firstEditText.getText().toString();
        String lastName = secondEditText.getText().toString();
        String login = thirdEditText.getText().toString();
        firstEditText.setText(null);
        secondEditText.setText(null);
        thirdEditText.setText(null);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.save_btn:
                cv.put("name", name);
                cv.put("lastName", lastName);
                cv.put("login", login);
                long rowID = db.insert("mytable", null, cv);
                Toast.makeText(this, "data added to DB", Toast.LENGTH_SHORT).show();
                break;
            case R.id.read_btn:
                setContentView(R.layout.database_show);
                Cursor c = db.query("mytable", null, null, null, null, null, null);
                if (c.moveToNext()) {
                    makeDataBaseLayout(c);
                }
                c.close();
                break;
        }
        dbHelper.close();
    }

    private void makeDataBaseLayout(Cursor c) {
        do {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int lastNameColIndex = c.getColumnIndex("lastName");
            int loginColIndex = c.getColumnIndex("login");
            final LinearLayout mainLayout = findViewById(R.id.main_layout);
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
                    if (String.valueOf(layoutToAdd.getId()).equalsIgnoreCase("")) {
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
}
