package com.example.todoapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DBHelper dbhelper;
    ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDataBaseHelper();
        setListenerForAddBtn();
        setTasksGrid();
    }

    private void setDataBaseHelper() {
        dbhelper = new DBHelper(getApplicationContext());
    }

    private void setTasksGrid() {
        fillNotesArray();
        NotesAdapter notesAdapter = new NotesAdapter(this, notes);
        GridView mainGrid = findViewById(R.id.main_grid);
        mainGrid.setAdapter(notesAdapter);
        mainGrid.setVerticalSpacing(5);
    }

    private void fillNotesArray() {
        notes = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from tasks", new String[]{});
        if (c.moveToNext())
            do
                notes.add(new Note(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("task"))));
            while (c.moveToNext());
            c.close();
        db.close();
    }

    private void setListenerForAddBtn() {
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), TaskAddActivity.class),
                        1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) return;

        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("task", data.getStringExtra("task"));
        db.insert("tasks", null, cv);

        Cursor c = db.rawQuery("select * from tasks", new String[]{});
        if (c.moveToNext())
            setTasksGrid();
        c.close();
    }
}
