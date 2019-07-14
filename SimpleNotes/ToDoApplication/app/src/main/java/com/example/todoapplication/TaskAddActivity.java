package com.example.todoapplication;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class TaskAddActivity extends AppCompatActivity {
    EditText editTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);
        setEditField();
    }

    private void setEditField() {
        editTask = findViewById(R.id.edit_task);
        editTask.performClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.confirm:
                finishWithGoodResult();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void finishWithGoodResult() {
        setResult(RESULT_OK, new Intent().putExtra("task", editTask.getText().toString()));
        finish();
    }
}
