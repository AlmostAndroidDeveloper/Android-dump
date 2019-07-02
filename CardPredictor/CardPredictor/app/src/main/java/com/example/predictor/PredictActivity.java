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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PredictActivity extends AppCompatActivity {
    int[][] cardsStack = new int[][]{                     // 4 стопки для угадывания карты
            {6, 8, 10, 12, 14},
            {6, 9, 10, 13, 14},
            {7, 8, 9, 10},
            {11, 12, 13, 14}
    };
    String[] cardNames = {"6", "7", "8", "9", "10", "Валет", "Дама", "Король", "Туз"}; // наименования карт
    int[] cardNumbers = {6, 7, 8, 9, 10, 11, 12, 13, 14}; // номиналы карт
    int[] cardIds = {                                     // id картинок для карт
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
    HashMap<Integer, Integer> cardMap;                  // соответствие номинал-id изображеиня карты
    boolean isStackFinished = false;                    // закончился ли показ стопки
    boolean needToShowAgain = false;                    // нужно ли показывать стопку снова
    Button confirmGuessBtn;                             // кнопка для начала показа стопок
    ImageView cardImage;                                // элемент, в котором устанавливаем изображения карт из стопок
    int cardNum = 3;                                    // номинал угадываемой карты

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);
        setConfirmButton();
        setCardMap();
    }

    private void setCardMap() {                         // заполнить словарь с соответствием номинал карты-изображение карты
        cardMap = new HashMap<>();
        for (int i = 0; i < cardNumbers.length; i++)
            cardMap.put(cardNumbers[i], cardIds[i]);
    }

    private void setConfirmButton() {                   // найти кнопку и установить обработчик нажатия
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
        new MyTask().execute();                         //запуск задачи угадывания карты
    }

    private int showCard(int num) {
        if (cardMap.containsKey(num))                   // достать id карты, соответствующий ее номиналу
            return cardMap.get(num);
        return 0;
    }

    class MyTask extends AsyncTask<Void, Void, Void> {  // задача показа карт и вопросов

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < 4; i++) {               // для 4 стопок карт
                for (int j = 0; j < cardsStack[i].length; j++) { // для каждой карты в стопке
                    final int finalI = i;
                    final int finalJ = j;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cardImage.setBackgroundResource(showCard(cardsStack[finalI][finalJ])); // установка изображения карты
                        }
                    });
                    cardImage.startAnimation(
                            AnimationUtils.loadAnimation(getApplicationContext(), R.anim.card_appear)); //запуск анимации
                    try {
                        TimeUnit.MILLISECONDS.sleep(1250); // тормозим фоновую задачу на время появления карты
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                startActivityForResult(new Intent(getApplicationContext(),
                        QuestionActivity.class)
                        .putExtra("num", i), 1); // запускаем окно с вопросом
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cardImage.setBackgroundResource(0);

                    }
                });
                isStackFinished = false;
                while (!isStackFinished) {              // пока показ стопки не завершен, усыпляем фоновую задачу
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (needToShowAgain) {                 // если нужно показать колоду заново, минусуем счетчик
                    --i;
                    needToShowAgain = false;           // колоду пока не нужно показывать еще раз
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            // перед выполнением фоновой задачи
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startActivityForResult(new Intent(getApplicationContext(), ResultActivity.class) // выводим угаданную карту в новом окне
                    .putExtra("id", showCard(cardNum)), 1);
        }
    }

    private void printResultCard(int card) {
        String msg;
        if (card < 6 || card > 14) msg = getString(R.string.wrong_answers);
        else msg = "Ваша карта: " + printCard(card);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private String printCard(int card) {
        return cardNames[card - 6];
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // вызывается после ответа на вопрос, была ли карта в стопке
        switch (resultCode) {
            case RESULT_OK:
                if (data == null) return;
                cardNum += data.getIntExtra("num", 0); // если карта была в стопке, прибавить числовое значение этой стопки
                break;
            case RESULT_CANCELED:
                needToShowAgain = true; // показать текущую стопку еще раз, если нажата кнопка показа заново
                break;
            case 2:
                setContentView(R.layout.activity_predict);
                setConfirmButton();
                break;
        }
        isStackFinished = true; // показ текущей стопки завершен
    }
}