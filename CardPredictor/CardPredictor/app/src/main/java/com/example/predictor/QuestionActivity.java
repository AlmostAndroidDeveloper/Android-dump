package com.example.predictor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_question);
        setYesNoButtons();
        setShowAgainButton();
    }

    private void setShowAgainButton() {
        findViewById(R.id.show_again_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void setYesNoButtons() {
        setYesButton();
        setNoButton();

    }

    private void setNoButton() {
        findViewById(R.id.no_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent().putExtra("num", 0));
                finish();
            }
        });
    }

    private void setYesButton() {
        findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int power = getIntent().getIntExtra("num", 0);
                setResult(RESULT_OK, new Intent().putExtra("num", (int) Math.pow(2, power)));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}