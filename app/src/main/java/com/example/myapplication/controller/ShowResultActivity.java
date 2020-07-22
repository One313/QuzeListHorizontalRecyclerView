package com.example.myapplication.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

public class ShowResultActivity extends AppCompatActivity {

    private TextView mTextViewResult;
    private Button mButtonReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        findViews();
        showResult();
        setListeners();
    }

    private void findViews() {
        mTextViewResult = findViewById(R.id.text_view_result);
        mButtonReset = findViewById(R.id.button_reset);
    }

    private void setListeners() {
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowResultActivity.this, QuizActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showResult() {
        int score = getIntent().getIntExtra(QuizActivity.SEND_RESULT, -1000);
        mTextViewResult.setText(String.valueOf(score));
    }
}