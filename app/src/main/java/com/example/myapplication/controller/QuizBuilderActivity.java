package com.example.myapplication.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.QuizListActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Parser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class QuizBuilderActivity extends AppCompatActivity {

    public static final String VALUE_Q_BUILDER_EDIT_TEXT = "com.example.myapplication.val";
    public static final String ON_TEXT_CATCH_EXCEPTION_TAG = "com.example.onText";
    public static final String QUESTION_DATA = "questionData";
    private Button mStartButton;
    private EditText mQuestionsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_builder);

        findViews();

        mQuestionsText.setBackgroundColor(getResources().getColor(R.color.invalidBackgroundEditTest));
        mStartButton.setEnabled(false);

        setListeners();
    }

    private void findViews() {
        mStartButton = findViewById(R.id.startButton);
        mQuestionsText = findViewById(R.id.editTextTextMultiLine);
    }

    private void setListeners() {

        mQuestionsText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (new Parser(s.toString()).checkFormatAndValidation()) {
                        mQuestionsText.setBackgroundColor(
                                getResources().getColor(R.color.validBackgroundEditTest));
                        mStartButton.setEnabled(true);
                    } else {
                        mQuestionsText.setBackgroundColor(
                                getResources().getColor(R.color.invalidBackgroundEditTest));
                        mStartButton.setEnabled(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(ON_TEXT_CATCH_EXCEPTION_TAG, Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    saveDataInInternalStorage(mQuestionsText.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(QuizBuilderActivity.this, QuizListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void saveDataInInternalStorage(String data) throws IOException {
        FileOutputStream fileOutput = openFileOutput(QUESTION_DATA,Context.MODE_PRIVATE);
        fileOutput.write(data.getBytes());
        fileOutput.close();
    }

}