package com.example.predictor;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStartBtn();
    }

    private void setStartBtn() {
        startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), PredictActivity.class), 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) return;
        int card = data.getIntExtra("card", 0);
        printResultCard(card);
    }

    private void printResultCard(int card) {
        String msg;
        if (card < 6 || card > 14) msg = getString(R.string.wrong_answers);
        else msg = "Ваша карта: " + showCard(card);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private String showCard(int num) {
        switch (num) {
            case 11:
                return "Валет";
            case 12:
                return "Дама";
            case 13:
                return "Король";
            case 14:
                return "Туз";
            default:
                return String.valueOf(num);
        }
    }
}
