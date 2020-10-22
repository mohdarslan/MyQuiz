package com.Arsh.myquiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    private TextView qns_tv;
    private RadioGroup radioGroup;
    private RadioButton radio_button_1;
    private RadioButton radio_button_2;
    private RadioButton radio_button_3;
    private RadioButton radio_button_4;
    private Button NEXT;
    private Button PREV;
    private Button timer;
    private Button EVALUATE;
    private CountDownTimer countDownTimer;
    private RadioButton selectedRadioButton;
    private Animation animation1;

    private String Question[] = new String[10];
    private String OptionA[] = new String[10];
    private String OptionB[] = new String[10];
    private String OptionC[] = new String[10];
    private String OptionD[] = new String[10];
    private String CorrectOption[] = new String[10] ;
    private  ArrayList <String> Shuffle = new ArrayList<String>();
    private int Scores[] = new int[10];
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        AndroidNetworking.initialize(getApplicationContext());
        callAPI();

        NEXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index <10){
                    view.clearAnimation();
                    click_NEXT_button(index);
                }else{
                    return;
                }

            }
        });

        PREV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index >= 0){
                    click_PREV_button(index);
                }else{
                    return;
                }
            }
        });

        EVALUATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_EVALUATE_button();
            }
        });

    }

    private void click_EVALUATE_button() {
        int correct_answers = 0;
        int wrong_answers = 0;
        int unattempted_questions = 0;

        for(int i=0;i<10;i++){
            if(Scores[i]==0){
                unattempted_questions++;
            }
            if(Scores[i]==1){
                correct_answers++;
            }
            if(Scores[i]==-1){
                wrong_answers++;
            }
        }

        Log.i("correct ", String.valueOf(correct_answers));
        Log.i("wrong ", String.valueOf(wrong_answers));
        Log.i("unattempted ", String.valueOf(unattempted_questions));

        Intent intent = new Intent(this, Result.class);
        intent.putExtra("correct_answers",correct_answers);
        intent.putExtra("wrong_answers",wrong_answers);
        intent.putExtra("unattempted_questions",unattempted_questions);
        startActivity(intent);
    }

    private Future<Long> setValues(int i) {
        if(i<10 && i >=0){
            NEXT.setClickable(true);
            PREV.setClickable(true);
            qns_tv.setText(Question[i]);
            radio_button_1.setText(OptionA[i]);
            radio_button_2.setText(OptionB[i]);
            radio_button_3.setText(OptionC[i]);
            radio_button_4.setText(OptionD[i]);
        }
        if (i>=10){
            NEXT.setClickable(false);
        }
        if(i==0){
            PREV.setClickable(false);
        }


        return null;
    }


    private void callAPI() {
        AndroidNetworking.post("https://opentdb.com/api.php?amount=10&category=9&difficulty=easy&type=multiple")
                .addQueryParameter("amount", "10")
                .addQueryParameter("category", "9")
                .addQueryParameter("difficulty", "easy")
                .addQueryParameter("type", "multiple")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for(int i = 0; i< jsonArray.length(); i++){
                                JSONObject obj = jsonArray.getJSONObject(i);

                                CorrectOption[i] = obj.getString("correct_answer");
                                Question[i] = obj.getString("question");

                                JSONArray opt= obj.getJSONArray("incorrect_answers");
                                //  JSONObject g =opt.getJSONObject(j);

                                    Shuffle.add(0, opt.getString(0));
                                    Shuffle.add(1, opt.getString(1));
                                    Shuffle.add(2, opt.getString(2));
                                    Shuffle.add(3, obj.getString("correct_answer"));

                                Collections.shuffle(Shuffle);

                                Log.i("Test Shuffle", String.valueOf(Shuffle.get(0)));
                                Log.i("Test Shuffle", String.valueOf(Shuffle.get(1)));
                                Log.i("Test Shuffle", String.valueOf(Shuffle.get(2)));
                                Log.i("Test Shuffle", String.valueOf(Shuffle.get(3)));

                             //   break;
                                OptionA[i] = Shuffle.get(0);
                                OptionB[i] = Shuffle.get(1);
                                OptionC[i] = Shuffle.get(2);
                                OptionD[i] = Shuffle.get(3);
                                Shuffle.clear();

                            }
                            Log.i("Test Question", String.valueOf(Question[0]));
                            Log.i("Test OptionA", String.valueOf(OptionA[0]));
                            Log.i("Test OptionB", String.valueOf(OptionB[0]));
                            Log.i("Test OptionC", String.valueOf(OptionC[0]));
                            Log.i("Test OptionD", String.valueOf(OptionD[0]));
                            Log.i("Test Correct Option", String.valueOf(CorrectOption[0]));
                          setValues(index);
                        } catch (JSONException e) {
                            Log.i("Test Error", String.valueOf(e));
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    private void click_NEXT_button(int i){
        int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();
        if(countDownTimer != null){
            countDownTimer.cancel();
//            animation1.cancel();
            animation1 = null;
        }


        radio_button_1.setClickable(true);
        radio_button_2.setClickable(true);
        radio_button_3.setClickable(true);
        radio_button_4.setClickable(true);
        set_timer(60);

        if(selectedRadioButtonID != -1){
            selectedRadioButton = findViewById(selectedRadioButtonID);
            String Selected_RadioButton_Text = selectedRadioButton.getText().toString();
            Log.i("Selected Option is ", Selected_RadioButton_Text);
            Log.i("CorrectOption[i] ", CorrectOption[i]);

            if(Selected_RadioButton_Text.equalsIgnoreCase(CorrectOption[i])){
                Scores[i] = 1;
            }else{
                Scores[i] = -1;
            }
        }
        if(index<9){
            index++;
            setValues(index);
        }
        if(index == 9){
            setValues(index);
        }

    }

    private void click_PREV_button(int i){
        int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();
        if(countDownTimer != null){
            countDownTimer.cancel();
//            animation1.cancel();
            animation1 = null;
        }


        radio_button_1.setClickable(true);
        radio_button_2.setClickable(true);
        radio_button_3.setClickable(true);
        radio_button_4.setClickable(true);
        set_timer(60);

        if(selectedRadioButtonID != -1){
            selectedRadioButton = findViewById(selectedRadioButtonID);
            String Selected_RadioButton_Text = selectedRadioButton.getText().toString();

            if(Selected_RadioButton_Text.equalsIgnoreCase(CorrectOption[i])){
                Scores[i] = 1;
            }else{
                Scores[i] = -1;
            }
        }
        if(index > 0){
            index--;
            setValues(index);
        }

    }

    private void init() {

        qns_tv= (TextView) findViewById(R.id.qns_tv);
        radioGroup= (RadioGroup) findViewById(R.id.radioGroup);
        radio_button_1= (RadioButton) findViewById(R.id.radio_button_1);
        radio_button_2= (RadioButton) findViewById(R.id.radio_button_2);
        radio_button_3= (RadioButton) findViewById(R.id.radio_button_3);
        radio_button_4= (RadioButton) findViewById(R.id.radio_button_4);
        NEXT= (Button) findViewById(R.id.NEXT);
        PREV= (Button) findViewById(R.id.PREV);
        timer=(Button)findViewById(R.id.timer);
        EVALUATE=(Button)findViewById(R.id.EVALUATE);
        Future<Long> futuretask = setValues(index);
        set_timer(60);
    }

    private void set_timer(int time_limit) {
        final int[] counter = new int[1];
        counter[0] =time_limit;
        countDownTimer = new CountDownTimer(60000, 1000){
            public void onTick(long millisUntilFinished){

                    timer.setText("00:"+String.valueOf(counter[0]));
                    counter[0]--;
                    timer.setTextColor(Color.WHITE);

                    if(counter[0] <10 && counter[0] >=0)
                    {
                        timer.setText("00:0"+String.valueOf(counter[0]));
                        animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                        timer.startAnimation(animation1);
                        timer.setTextColor(Color.RED);
                    }
                    if(counter[0]<0)
                    {
                        counter[0]=00;
                    }

            }
            public void onFinish(){

                timer.setText("FINISH!!");
                timer.setTextSize(30);
                timer.setTextColor(Color.RED);

                radio_button_1.setClickable(false);
                radio_button_2.setClickable(false);
                radio_button_3.setClickable(false);
                radio_button_4.setClickable(false);
                //unattempted question
               // SCORE_LIST.set(question_rank, 0);
            }
        }.start();
    }

}