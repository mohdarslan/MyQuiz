package com.Arsh.myquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    TextView correct;
    TextView wrong;
    TextView unattempted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        correct = (TextView) findViewById(R.id.correct);
        wrong = (TextView) findViewById(R.id.wrong);
        unattempted = (TextView) findViewById(R.id.unattempted);

        Bundle bundle = getIntent().getExtras();
        int correct_answers = bundle.getInt("correct_answers");
        int wrong_answers = bundle.getInt("wrong_answers");
        int unattempted_questions = bundle.getInt("unattempted_questions");

        correct.setText(String.valueOf(correct_answers));
        wrong.setText(String.valueOf(wrong_answers));
        unattempted.setText(String.valueOf(unattempted_questions));
    }

}