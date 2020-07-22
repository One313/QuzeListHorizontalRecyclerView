package com.example.myapplication.model;

import java.io.Serializable;

public class Question implements Serializable {
    private String mQuestionTitle;
    private boolean mAnswerTrue;
    private boolean mCheatTrue;
    private Color mQuestionColor;

    private boolean mIsCheated = false;

    public String getQuestionTitle() {
        return mQuestionTitle;
    }

    public void setQuestionTitle(String mQuestionTitle) {
        this.mQuestionTitle = mQuestionTitle;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean mAnswerTrue) {
        this.mAnswerTrue = mAnswerTrue;
    }

    public boolean isCheatTrue() {
        return mCheatTrue;
    }

    public void setCheatTrue(boolean mCheatTrue) {
        this.mCheatTrue = mCheatTrue;
    }

    public Color getQuestionColor() {
        return mQuestionColor;
    }

    public void setQuestionColor(Color mQuestionColor) {
        this.mQuestionColor = mQuestionColor;
    }

    public boolean isIsCheated() {
        return mIsCheated;
    }

    public void setIsCheated(boolean mIsCheated) {
        this.mIsCheated = mIsCheated;
    }

    public Question(String mQuestionTitle, boolean mAnswerTrue, boolean mCheatTrue, Color mQuestionColor) {
        this.mQuestionTitle = mQuestionTitle;
        this.mAnswerTrue = mAnswerTrue;
        this.mCheatTrue = mCheatTrue;
        this.mQuestionColor = mQuestionColor;
    }
}
