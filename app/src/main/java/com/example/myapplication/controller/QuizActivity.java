package com.example.myapplication.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.Parser;
import com.example.myapplication.model.Question;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    public static final String ON_SAVE_INSTANCE_TIME = "onSaveInmTime";
    public static final String ON_SAVE_INSTANCE_INDEX = "onSaveInmCurrentIndex";
    public static final String ON_SAVE_INSTANCE_SCORE = "onSaveInmScore";
    public static final String ON_SAVE_INSTANCE_COUNTER = "onSaveInmCounter";
    public static final String ON_SAVE_INSTANCE_HISTORY = "onSaveInmHistory";
    public static final String QUIZ_ACT_REQUEST = "SHOW_CHEAT";
    public static final String SEND_RESULT = "mScore";
    public static final String REMINDING_TIME = "remindingTime";

    private LinearLayout mLinearLayoutMain, mLinearLayoutAns;

    private TextView mTextViewTimer, mTextViewScore, mTextViewQuestion;
    private Button mButtonTrue, mButtonFalse;
    private ImageButton mButtonNext, mButtonPrevious, mButtonFirst, mButtonLast;
    private Button mButtonCheat;
    private CountDownTimer mCountDownTimer;

    private String mData;
    private Question[] mQuestions;
    private long mTime;
    private int mCurrentIndex = 0;
    private int mScore = 0;
    private List<Integer> mHistory = new ArrayList<>();
    private int mCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        getData();
        getQuestions();
        getTime();
        findViews();
        resetAfterChangedConfiguration(savedInstanceState);
        updateStatesOfButtons();
        updateQuestion();
        setListeners();
    }

    private void resetAfterChangedConfiguration(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mTime = savedInstanceState.getLong(ON_SAVE_INSTANCE_TIME);
            mCurrentIndex = savedInstanceState.getInt(ON_SAVE_INSTANCE_INDEX);
            mScore = savedInstanceState.getInt(ON_SAVE_INSTANCE_SCORE);
            mCounter = savedInstanceState.getInt(ON_SAVE_INSTANCE_COUNTER);
            mHistory = savedInstanceState.getIntegerArrayList(ON_SAVE_INSTANCE_HISTORY);
            mTextViewScore.setText(String.valueOf(mScore));
        }
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

    private void startCountDownTimer() {
        mCountDownTimer = new CountDownTimer(mTime, 1000) {

            public void onTick(long millisUntilFinished) {
                mTime = millisUntilFinished;
                mTextViewTimer.setText("seconds remaining: " + mTime / 1000);
            }

            public void onFinish() {
                mTextViewTimer.setText("finished!");
                finishProcess();
            }
        }.start();
    }

    private void cancelCountDownTimer() {
        mCountDownTimer.cancel();
    }

    private void getData() {
        FileInputStream fileInput;
        StringBuilder stringBuilder = new StringBuilder();
        try {

            fileInput = openFileInput(QuizBuilderActivity.QUESTION_DATA);
            int i;
            while ((i = fileInput.read()) != -1) {
                stringBuilder.append((char) i);
            }
            fileInput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        mData = stringBuilder.toString();
    }

    private void getQuestions() {
        try {
            mQuestions = new Parser(mData).getQuestions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTime() {
        try {
            mTime = new Parser(mData).getTimeOut() * 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ON_SAVE_INSTANCE_TIME, mTime);
        outState.putInt(ON_SAVE_INSTANCE_INDEX, mCurrentIndex);
        outState.putInt(ON_SAVE_INSTANCE_SCORE, mScore);
        outState.putInt(ON_SAVE_INSTANCE_COUNTER, mCounter);
        outState.putIntegerArrayList(ON_SAVE_INSTANCE_HISTORY, (ArrayList<Integer>) mHistory);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null)
            return;

        if (requestCode == 0) {
            mQuestions[mCurrentIndex].setIsCheated(
                    data.getBooleanExtra(CheatActivity.CHEAT_ACT_RESPONSE, false));
            mTime = data.getLongExtra(CheatActivity.TIME_REMINDING_RESPONSE, -1);
        }
    }


    private void findViews() {
        mLinearLayoutMain = findViewById(R.id.linear_layout_main);
        mLinearLayoutAns = findViewById(R.id.linear_layout_ans);
        mTextViewTimer = findViewById(R.id.text_view_timer);
        mTextViewScore = findViewById(R.id.text_view_score);
        mTextViewQuestion = findViewById(R.id.text_view_question);
        mButtonTrue = findViewById(R.id.button_true);
        mButtonFalse = findViewById(R.id.button_false);
        mButtonNext = findViewById(R.id.button_next);
        mButtonPrevious = findViewById(R.id.button_previous);
        mButtonFirst = findViewById(R.id.button_first);
        mButtonLast = findViewById(R.id.button_last);
        mButtonCheat = findViewById(R.id.button_cheat);
    }

    private void updateStatesOfButtons() {
        if (mCurrentIndex == 0) {
            mButtonPrevious.setEnabled(false);
            mButtonNext.setEnabled(true);
            mButtonFirst.setEnabled(false);
            mButtonLast.setEnabled(true);
        } else if (mCurrentIndex == mQuestions.length - 1) {
            mButtonPrevious.setEnabled(true);
            mButtonNext.setEnabled(false);
            mButtonFirst.setEnabled(true);
            mButtonLast.setEnabled(false);
        } else {
            mButtonPrevious.setEnabled(true);
            mButtonNext.setEnabled(true);
            mButtonFirst.setEnabled(true);
            mButtonLast.setEnabled(true);
        }

        if (mQuestions[mCurrentIndex].isCheatTrue())
            mButtonCheat.setEnabled(true);
        else mButtonCheat.setEnabled(false);

        if (mHistory.contains(mCurrentIndex))
            setEnabled(mLinearLayoutAns, false);
        else
            setEnabled(mLinearLayoutAns, true);
    }

    private void updateTextScore() {
        mTextViewScore.setText(String.valueOf(mScore));
    }

    private void waitingForFinish() {
        mCounter++;
        if (isQuizFinished()) {
            finishProcess();
        }
    }


    private void setEnabled(LinearLayout layout, boolean enabled) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            layout.getChildAt(i).setEnabled(enabled);
        }
    }

    private void finishProcess() {
        setEnabled(mLinearLayoutMain, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(QuizActivity.this, ShowResultActivity.class);
                intent.putExtra(SEND_RESULT, mScore);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }


    private boolean isQuizFinished() {
        return mCounter == mQuestions.length;
    }

    private void updateQuestion() {
        Question currentQuestion = mQuestions[mCurrentIndex];
        mTextViewQuestion.setText(currentQuestion.getQuestionTitle());
        int color;
        switch (currentQuestion.getQuestionColor()) {
            case RED:
                color = Color.RED;
                break;
            case BLUE:
                color = Color.BLUE;
                break;
            case GREEN:
                color = Color.GREEN;
                break;
            default:
                color = Color.BLACK;
                break;
        }
        mTextViewQuestion.setTextColor(color);
    }

    private void messages(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private void checkAnswer(boolean userPressed) {
        Question currentQuestion = mQuestions[mCurrentIndex];
        if (currentQuestion.isIsCheated())
            messages(this, getString(R.string.toast_cheated_true));
        else {
            if (currentQuestion.isAnswerTrue() == userPressed)
                messages(this, getString(R.string.toast_correct));
            else
                messages(this, getString(R.string.toast_incorrect));
        }
    }

    private void calculateScore(boolean userPressed) {
        if (mQuestions[mCurrentIndex].isAnswerTrue() == userPressed)
            mScore++;
        else mScore--;
    }

    private void setListeners() {
        mButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHistory.add(mCurrentIndex);
                if (!mQuestions[mCurrentIndex].isIsCheated())
                    calculateScore(true);
                updateTextScore();
                updateStatesOfButtons();
                checkAnswer(true);
                waitingForFinish();
            }
        });

        mButtonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHistory.add(mCurrentIndex);
                if (!mQuestions[mCurrentIndex].isIsCheated())
                    calculateScore(false);
                updateTextScore();
                updateStatesOfButtons();
                checkAnswer(false);
                waitingForFinish();
            }
        });

        mButtonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = 0;
                updateStatesOfButtons();
                updateQuestion();
            }
        });

        mButtonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = mQuestions.length - 1;
                updateStatesOfButtons();
                updateQuestion();
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex < mQuestions.length - 1)
                    mCurrentIndex++;
                updateStatesOfButtons();
                updateQuestion();
            }
        });

        mButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex > 0)
                    mCurrentIndex--;
                updateStatesOfButtons();
                updateQuestion();
            }
        });

        mButtonCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuestions[mCurrentIndex].isCheatTrue()) {
                    Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                    intent.putExtra(QUIZ_ACT_REQUEST, mQuestions[mCurrentIndex].isAnswerTrue());
                    intent.putExtra(REMINDING_TIME, mTime);
                    startActivityForResult(intent, 0);
                }
            }
        });
    }
}

/*

{[{"Is Iran a country on the Asian continent?"},{true},{true},{BLACK}],
[{"Is Russia the largest country in the world?"},{true},{false},{RED}],
[{"Is India the most populous country in the world?"},{false},{true},{BLUE}],
[{"Is the Pacific Ocean the world's largest ocean?"},{true},{false},{GREEN}],
[{"Is the land flat?"},{false},{false},{BLACK}],
[{"Is Egypt on the Asian continent?"},{false},{true},{RED}]},{60}

 */