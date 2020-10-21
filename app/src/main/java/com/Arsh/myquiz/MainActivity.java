package com.Arsh.myquiz;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

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
    private TextView triviaQuestion;
    private CountDownTimer countDownTimer;

    private String Question[] = new String[10];
    private String OptionA[] = new String[10];
    private String OptionB[] = new String[10];
    private String OptionC[] = new String[10];
    private String OptionD[] = new String[10];
    private String CorrectOption[] = new String[10] ;
    private  ArrayList <String> Shuffle = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        set_timer(60);
        AndroidNetworking.initialize(getApplicationContext());
        callAPI();
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


                            }

                            Log.i("Test Question", String.valueOf(Question[0]));
                            Log.i("Test OptionA", String.valueOf(OptionA[0]));
                            Log.i("Test OptionB", String.valueOf(OptionB[0]));
                            Log.i("Test OptionC", String.valueOf(OptionC[0]));
                            Log.i("Test OptionD", String.valueOf(OptionD[0]));
                            Log.i("Test Correct Option", String.valueOf(CorrectOption[0]));





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
        triviaQuestion = (TextView) findViewById(R.id.triviaQuestion);
    }

    private void set_timer(int time_limit) {


        final int[] counter = new int[1];
        counter[0] =60;
        countDownTimer=       new CountDownTimer(60000, 1000){
            public void onTick(long millisUntilFinished){
                timer.setText("00:"+String.valueOf(counter[0]));
                counter[0]--;
                timer.setTextColor(Color.WHITE);

                if(counter[0] <10 && counter[0] >=0)
                {

                    timer.setText("00:0"+String.valueOf(counter[0]));
                    Animation animation1 =
                            AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.blink);
                    timer.startAnimation(animation1);
                    timer.setTextColor(Color.parseColor(String.valueOf(R.color.red)));
                }
                if(counter[0] <0)
                {
                    counter[0] =00;
                }
            }
            public  void onFinish(){

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