package com.example.todoapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fabAdd;
    DBHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbhelper = new DBHelper(getApplicationContext());
        setListenerForAddBtn();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from tasks", new String[]{});
        if (c.moveToNext())
            makeTasksList(c);
        db.close();
    }

    private void makeTasksList(Cursor c) {
        final LinearLayout mainLayout = findViewById(R.id.main_layout);
        mainLayout.removeAllViews();
        do {
            final View layout = getLayoutInflater().inflate(R.layout.one_task_layout, null, false);
            TextView id = layout.findViewById(R.id.id_text);
            TextView text = layout.findViewById(R.id.task_text);
            Button delBtn = layout.findViewById(R.id.del_btn);
            layout.setId(c.getInt(c.getColumnIndex("id")));
            id.setText(String.valueOf(c.getInt(c.getColumnIndex("id"))));
            text.setText(c.getString(c.getColumnIndex("task")));
            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (String.valueOf(layout.getId()).equalsIgnoreCase("")) {
                        return;
                    }
                    SQLiteDatabase db = dbhelper.getWritableDatabase();
                    db.delete("tasks", "id = " + layout.getId(), new String[]{});
                    mainLayout.removeView(layout);
                    db.close();
                }
            });
            mainLayout.addView(layout);

        } while (c.moveToNext());
        c.close();
    }

    private void setListenerForAddBtn() {
        fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), TaskAddActivity.class),
                        1);
            }
        });
    }

    private void makeTaskAddDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final EditText enterTask = new EditText(getApplicationContext());
        enterTask.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(enterTask, InputMethodManager.SHOW_FORCED);
            }
        });
        alertDialog.setTitle("Что нужно сделать?");
        alertDialog.setView(enterTask);
        alertDialog.setPositiveButton("добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) return;

        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("task", data.getStringExtra("task"));
        long rowID = db.insert("tasks", null, cv);

        Cursor c = db.rawQuery("select * from tasks", new String[]{});
        if (c.moveToNext())
            makeTasksList(c);
    }
}
