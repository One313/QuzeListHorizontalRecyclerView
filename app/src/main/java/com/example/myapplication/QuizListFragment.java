package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.myapplication.controller.QuizBuilderActivity;
import com.example.myapplication.model.Parser;
import com.example.myapplication.model.Question;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizListFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public QuizListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private String getData() {
        FileInputStream fileInput;
        StringBuilder stringBuilder = new StringBuilder();
        try {

            fileInput = getActivity().openFileInput(QuizBuilderActivity.QUESTION_DATA);
            int i;
            while ((i = fileInput.read()) != -1) {
                stringBuilder.append((char) i);
            }
            fileInput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    private List<Question> getQuestions() {
        List<Question> mQuestions = new ArrayList<>();
        try {
            mQuestions = Arrays.asList(new Parser(getData()).getQuestions());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mQuestions;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz_list, container, false);
        findViews(view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));


        List<Question> questions = getQuestions();
        QuestionAdapter adapter = new QuestionAdapter(questions);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerViewListQuestion);
    }

    private class QuizHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewQuestion;
        private CheckBox mCheckBoxAnswer;

        public QuizHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewQuestion = itemView.findViewById(R.id.text_view_question_row);
            mCheckBoxAnswer = itemView.findViewById(R.id.checkbox_answer_row);
        }

        public void bindQuestion(Question question) {
            mTextViewQuestion.setText(question.getQuestionTitle());
            mCheckBoxAnswer.setChecked(question.isAnswerTrue());
        }
    }

    private class QuestionAdapter extends RecyclerView.Adapter<QuizHolder> {

        private List<Question> mQuestions;

        public QuestionAdapter(List<Question> questions) {
            mQuestions = questions;
        }

        @NonNull
        @Override
        public QuizHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_row_question, parent, false);

            QuizHolder quizHolder = new QuizHolder(view);
            return quizHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull QuizHolder holder, int position) {
            Question question = mQuestions.get(position);
            holder.bindQuestion(question);
        }

        @Override
        public int getItemCount() {
            return mQuestions.size();
        }
    }
}