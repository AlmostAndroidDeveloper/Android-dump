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
                finishWithGoodResult();
            }
        });
    }

    private void setEditFields() {
        setQuestionField();
        setAnswerField();
    }

    private void setAnswerField() {
        editAnswer = findViewById(R.id.edit_answer);
        editAnswer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (isEnterKeyPressed(keyCode, event)) {
                    finishWithGoodResult();
                    return true;
                }
                return false;
            }
        });
    }

    private void setQuestionField() {
        editQuestion = findViewById(R.id.edit_question);
        editQuestion.performClick();
        editQuestion.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (isEnterKeyPressed(keyCode, event)) {
                    editAnswer.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }

    private boolean isEnterKeyPressed(int keyCode, KeyEvent event) {
        return event.getAction() == KeyEvent.ACTION_DOWN &&
                (keyCode == KeyEvent.KEYCODE_ENTER);
    }

    private void finishWithGoodResult() {
        setResult(RESULT_OK, new Intent()
                .putExtra("question", editQuestion.getText().toString())
                .putExtra("answer", editAnswer.getText().toString()));
        finish();
    }
}