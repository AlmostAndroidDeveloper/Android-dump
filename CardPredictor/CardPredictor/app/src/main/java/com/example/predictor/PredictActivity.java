package com.example.predictor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class PredictActivity extends AppCompatActivity {
    int[][] cardsStack = new int[][]{
            {6, 8, 10, 12, 14},
            {6, 9, 10, 13, 14},
            {7, 8, 9, 10},
            {11, 12, 13, 14}
    };
    boolean isStackFinished = false;
    Button confirmGuessBtn;
    TextView cardTxt;
    int cardNum = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);
        setConfirmButton();
    }

    private void setConfirmButton() {
        confirmGuessBtn = findViewById(R.id.confirm_btn);
        confirmGuessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guessCards();
            }
        });
    }

    private void guessCards() {
        setContentView(R.layout.show_card);
        cardTxt = findViewById(R.id.card_txt);
        MyTask myTask = new MyTask();
        myTask.execute();
    }

    private String showCard(int num) {
        switch (num) {
            case 11:
                return "В";
            case 12:
                return "Д";
            case 13:
                return "К";
            case 14:
                return "Т";
            default:
                return String.valueOf(num);
        }
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < cardsStack[i].length; j++) {
                    final int finalI = i;
                    final int finalJ = j;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cardTxt.setText(showCard(cardsStack[finalI][finalJ]));
                        }
                    });
                    cardTxt.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.card_appear));
                    try {
                        TimeUnit.MILLISECONDS.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                startActivityForResult(new Intent(getApplicationContext(), QuestionActivity.class).putExtra("num", i), 1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cardTxt.setText(" ");
                    }
                });
                isStackFinished = false;
                while (!isStackFinished) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setResult(RESULT_OK, new Intent().putExtra("card", cardNum));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) return;
        if (resultCode == RESULT_OK)
            cardNum += data.getIntExtra("num", 0);
        isStackFinished = true;
    }
}




