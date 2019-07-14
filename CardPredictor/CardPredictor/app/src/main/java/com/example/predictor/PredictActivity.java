package com.example.predictor;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PredictActivity extends AppCompatActivity {
    int[][] cardsStacks = new int[][]{
            {6, 8, 10, 12, 14},
            {6, 9, 10, 13, 14},
            {7, 8, 9, 10},
            {11, 12, 13, 14}
    };
    int[] cardNumbers = {6, 7, 8, 9, 10, 11, 12, 13, 14};
    int[] cardIds = {
            R.drawable.card_6,
            R.drawable.card_7,
            R.drawable.card_8,
            R.drawable.card_9,
            R.drawable.card_10,
            R.drawable.card_jack,
            R.drawable.card_queen,
            R.drawable.card_king,
            R.drawable.card_ace
    };
    HashMap<Integer, Integer> cardMap;
    boolean isStackShowFinished = false;
    boolean needToShowStackAgain = false;
    Button confirmGuessBtn;
    ImageView cardImage;
    int cardNum = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);
        setConfirmButton();
        setCardMap();
    }

    private void setCardMap() {
        cardMap = new HashMap<>();
        for (int i = 0; i < cardNumbers.length; i++)
            cardMap.put(cardNumbers[i], cardIds[i]);
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
        cardImage = findViewById(R.id.card_img);
        new MyTask().execute();
    }

    private int showCard(int num) {
        if (cardMap.containsKey(num))
            return cardMap.get(num);
        return 0;
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (int currentStack = 0; currentStack < 4; currentStack++) {
                for (int currentCard = 0; currentCard < cardsStacks[currentStack].length; currentCard++) {
                    changeCardImage(currentStack, currentCard);
                    animateCardImageAndPause();
                }
                startQuestionActivity(currentStack);
                makeEmptyImage();
                isStackShowFinished = false;
                while (!isStackShowFinished) {
                    pauseTask(1000);
                }
                if (needToShowStackAgain) {
                    --currentStack;
                    needToShowStackAgain = false;
                }
            }
            return null;
        }

        private void startQuestionActivity(int currentStack) {
            startActivityForResult(new Intent(getApplicationContext(),
                    QuestionActivity.class)
                    .putExtra("num", currentStack), 1);
        }

        private void makeEmptyImage() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cardImage.setBackgroundResource(0);
                }
            });
        }

        private void animateCardImageAndPause() {
            cardImage.startAnimation(
                    AnimationUtils.loadAnimation(getApplicationContext(), R.anim.card_appear));
            pauseTask(1250);
        }

        private void changeCardImage(final int currentStack, final int currentCard) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cardImage.setBackgroundResource(showCard(cardsStacks[currentStack][currentCard]));
                }
            });
        }

        private void pauseTask(int millis) {
            try {
                TimeUnit.MILLISECONDS.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            showResultCard();
            cardNum = 3;
        }

        private void showResultCard() {
            startActivityForResult(new Intent(getApplicationContext(), ResultActivity.class)
                    .putExtra("id", showCard(cardNum)), 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                if (data == null) return;
                cardNum += data.getIntExtra("num", 0);
                break;
            case RESULT_CANCELED:
                needToShowStackAgain = true;
                break;
            case 2:
                setContentView(R.layout.activity_predict);
                setConfirmButton();
                break;
        }
        isStackShowFinished = true;
    }
}