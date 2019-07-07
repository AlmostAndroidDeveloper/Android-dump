package com.example.cardstudying;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class AddNewCardActivity extends AppCompatActivity {
    EditText editQuestion, editAnswer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_card);
        setEditFields();
        setAddButton();
    }

    private void setAddButton() {
        findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent()
                        .putExtra("question", editQuestion.getText().toString())
                        .putExtra("answer", editAnswer.getText().toString()));
                finish();
            }
        });
    }

    private void setEditFields() {
        editQuestion = findViewById(R.id.edit_question);
        editQuestion.performClick();
        editQuestion.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    editAnswer.requestFocus();
                    return true;
                }
                return false;
            }
        });
        editAnswer = findViewById(R.id.edit_answer);
        editAnswer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    setResult(RESULT_OK, new Intent()
                            .putExtra("question", editQuestion.getText().toString())
                            .putExtra("answer", editAnswer.getText().toString()));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }


}
