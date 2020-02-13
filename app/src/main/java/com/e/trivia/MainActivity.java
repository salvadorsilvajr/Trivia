package com.e.trivia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.e.trivia.data.AnswerListAsyncResponse;
import com.e.trivia.data.QuestionBank;
import com.e.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;

//https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questiontextview;
    private TextView questioncountertextview;
    private Button trueButton;
    private Button falseButoon;
    private ImageButton nextButoon;
    private ImageButton prevButton;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextButoon = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        questiontextview = findViewById(R.id.question_textview);
        questioncountertextview = findViewById(R.id.counter_textview);
        trueButton = findViewById(R.id.true_button);
        falseButoon = findViewById(R.id.false_button);

        nextButoon.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButoon.setOnClickListener(this);

            questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questiontextview.setText(questionArrayList.get(currentQuestionIndex).getAnswer());

                //Log.d("Inside", "processFinished: "+ questionArrayList);

            }
        });
        //Log.d("Main", "onCreate: "+ questionList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prev_button:
                if (currentQuestionIndex == 0){
                    currentQuestionIndex = questionList.size() -1;
                } else {
                    currentQuestionIndex = (currentQuestionIndex -1);
                }
                updateQuestion();
                break;
            case R.id.next_button:
                currentQuestionIndex = (currentQuestionIndex + 1) %questionList.size();
                updateQuestion();
                break;
            case R.id.true_button:
                checkAnswer(true);
                //updateQuestion();
                break;
            case R.id.false_button:
                checkAnswer(false);
                //updateQuestion();
                break;

        }
    }

    private void checkAnswer(boolean userChooseCorrect) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMessageid = 0;
        if (userChooseCorrect == answerIsTrue) {
            fadeView();
            toastMessageid = R.string.correct_answer;
        } else  {
            shakeAnimation();
            toastMessageid = R.string.wrong_answer;
        }
        Toast.makeText(MainActivity.this,toastMessageid,Toast.LENGTH_SHORT)
                .show();
    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questiontextview.setText(question);

        questioncountertextview.setText((currentQuestionIndex+1) +"/"+ questionList.size());
        Log.d("count", "updateQuestion: "+ questioncountertextview.getText());
    }

    private void fadeView (){
        final CardView cardView=findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.startAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(android.R.color.holo_green_light));


            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation (){
        Animation shake = AnimationUtils.loadAnimation
                (MainActivity.this,R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.startAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                questiontextview.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                questiontextview.setTextSize(28);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));
                questiontextview.setTextColor(getResources().getColor(android.R.color.black));
                questiontextview.setTextSize(18);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}
