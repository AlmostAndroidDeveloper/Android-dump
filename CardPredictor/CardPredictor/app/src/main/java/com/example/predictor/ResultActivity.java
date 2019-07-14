package com.example.predictor;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class ResultActivity extends AppCompatActivity {

    ImageView guessedCardImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setResetButton();
        setGuessedCardImage();
    }

    private void setGuessedCardImage() {
        guessedCardImg = findViewById(R.id.guessed_card_img);
        int imgId = getIntent().getIntExtra("id", 0);
        if (imgId == 0) setUnknownResult();
        else guessedCardImg.setImageResource(imgId);
        guessedCardImg.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_appear));
    }

    private void setUnknownResult() {
        setUnknownImage();
        showAlertDialog();
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.wrong_answers))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(2);
                        finish();
                    }
                }).create().show();
    }
    private void setUnknownImage() {
        guessedCardImg.setImageResource(R.drawable.card_unknown);
    }

    private void setResetButton() {
        Button resetBtn = findViewById(R.id.restart_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
