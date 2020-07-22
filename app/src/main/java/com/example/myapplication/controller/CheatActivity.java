package com.example.myapplication.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

public class CheatActivity extends AppCompatActivity {

    public static final String CHEAT_ACT_RESPONSE = "saveResultResponse";
    public static final String TIME_REMINDING_RESPONSE = "timeRemindingResponse";
    public static final String ON_TEXT_VIEW_CHEAT_ANS = "onTextViewCheatAns";

    private TextView mTextViewCheat;
    private Button mButtonOrderCheat;
    private CountDownTimer mCountDownTimer;

    private long mTime;
    private Intent mIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        getTime();
        findViews();
        resetAfterChangedConfiguration(savedInstanceState);
        setListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelCountDownTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCountDownTimer();
    }

    private void resetAfterChangedConfiguration(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mTextViewCheat.setText(savedInstanceState.getString(ON_TEXT_VIEW_CHEAT_ANS));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ON_TEXT_VIEW_CHEAT_ANS, mTextViewCheat.getText().toString());
    }

    private void getTime() {
        mTime = getIntent().getLongExtra(QuizActivity.REMINDING_TIME, -1);
    }

    private void startCountDownTimer() {
        mCountDownTimer = new CountDownTimer(mTime, 1000) {

            public void onTick(long millisUntilFinished) {
                mTime = millisUntilFinished;
            }

            public void onFinish() {
            }
        }.start();
    }

    private void cancelCountDownTimer() {
        mCountDownTimer.cancel();
    }

    private void findViews() {
        mTextViewCheat = findViewById(R.id.text_view_cheat);
        mButtonOrderCheat = findViewById(R.id.button_order_cheat);
    }

    private void setListeners() {
        mButtonOrderCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printCheat();
                saveResult();
            }
        });
    }

    private void printCheat() {
        boolean isAnswerTrue = getIntent()
                .getBooleanExtra(QuizActivity.QUIZ_ACT_REQUEST, false);
        if (isAnswerTrue)
            mTextViewCheat.setText(R.string.button_true);
        else
            mTextViewCheat.setText(R.string.button_false);
    }

    private void saveResult() {
        mIntent.putExtra(CHEAT_ACT_RESPONSE, true);
        setResult(RESULT_OK, mIntent);
    }

    @Override
    public void onBackPressed() {
        mIntent.putExtra(TIME_REMINDING_RESPONSE, mTime);
        setResult(RESULT_OK, mIntent);
        finish();
    }
}