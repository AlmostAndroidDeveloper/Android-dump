package com.example.scalecalc;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
NumberPicker from, to;
EditText input;
TextView resultText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
        setListenersForChange();
        copyTextByClick(this);
    }

    private void copyTextByClick(final Context context) {
        resultText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", resultText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Text copied",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setListenersForChange() {
        from.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                resultText.setText(Converter.calculate(input.getText().toString(), from.getValue(), to.getValue()));
            }
        });
        to.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                resultText.setText(Converter.calculate(input.getText().toString(), from.getValue(), to.getValue()));
            }
        });
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resultText.setText(Converter.calculate(input.getText().toString(), from.getValue(), to.getValue()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initializeComponents() {
        from = findViewById(R.id.from_pick);
        from.setMinValue(2);
        from.setMaxValue(36);
        to = findViewById(R.id.to_pick);
        to.setMinValue(2);
        to.setMaxValue(36);
        input = findViewById(R.id.edit_number);
        resultText = findViewById(R.id.result_txt);
    }

}
