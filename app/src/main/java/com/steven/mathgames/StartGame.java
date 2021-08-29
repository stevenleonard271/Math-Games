package com.steven.mathgames;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class StartGame extends AppCompatActivity {

    int op1, op2, correctAnswer, incorrectAnswer;
    TextView tvTimer, tvResults, tvPoints, tvSum;
    Button btn0, btn1, btn2, btn3;
    CountDownTimer countDownTimer;
    long millisUntilFinished;
    int points;
    int numberOfQuestions;
    Random random;
    int[] btnIds;
    ArrayList<Integer> incorrectAnswers;
    String[] operatorArray;

    int correctAnswerPosition;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startgame);
        //Hide the bar, make the game fullscreen
        getSupportActionBar().hide();

        op1 = 0;
        op2 = 0;
        correctAnswer = 0;
        tvTimer = findViewById(R.id.tvTimer);
        tvResults = findViewById(R.id.tvResults);
        tvSum = findViewById(R.id.tvSum);
        tvPoints = findViewById(R.id.tvPoints);
        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        millisUntilFinished = 30100;
        points = 0;
        numberOfQuestions = 0;
        random = new Random();
        btnIds = new int[]{R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3};
        incorrectAnswers = new ArrayList<>();
        operatorArray = new String[]{"+", "-", "x", "÷"};
        startGame();
    }

    private void startGame() {
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.soundtrack);
        final MediaPlayer fail = MediaPlayer.create(this, R.raw.fail);

        mediaPlayer.start();
        tvTimer.setText("" + (millisUntilFinished / 1000) + 's');
        tvPoints.setText("" + points + "/" + numberOfQuestions);
        generateQuestion();
//        finish();


        countDownTimer = new CountDownTimer(millisUntilFinished, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("" + (millisUntilFinished / 1000) + "s");
//                tvPoints.setText("" + points + "/" + numberOfQuestions);

            }

            @Override
            public void onFinish() {
                btn0.setClickable(false);
                btn1.setClickable(false);
                btn2.setClickable(false);
                btn3.setClickable(false);
                mediaPlayer.stop();
                Intent intent = new Intent(StartGame.this, GameOver.class);
                intent.putExtra("points", points);
                startActivity(intent);
                fail.start();
                finish();


            }
        }.start();
    }

    private void generateQuestion() {
        numberOfQuestions++;
        op1 = random.nextInt(10);
        op2 = 1 + random.nextInt(9);
        String selectedOperator = operatorArray[random.nextInt(4)];
        correctAnswer = getAnswer(selectedOperator);
        tvSum.setText(op1 + " " + selectedOperator + " " + op2 + " = ");
        correctAnswerPosition = random.nextInt(4);
        ((Button) findViewById(btnIds[correctAnswerPosition])).setText("" + correctAnswer);
        while (true) {
            if (incorrectAnswers.size() > 3)
                break;
            op1 = random.nextInt(10);
            op2 = 1 + random.nextInt(9);
            selectedOperator = operatorArray[random.nextInt(4)];
            incorrectAnswer = getAnswer(selectedOperator);
            if (incorrectAnswer == correctAnswer) continue;
            incorrectAnswers.add(incorrectAnswer);
        }

        for (int i = 0; i < 3; i++) {
            if (i == correctAnswerPosition)
                continue;
            ((Button) findViewById(btnIds[i])).setText("" + incorrectAnswers.get(i));
        }
        incorrectAnswers.clear();
    }

    private int getAnswer(String selectedOperator) {
        int answer = 0;
        switch (selectedOperator) {
            case "+":
                answer = op1 + op2;
                break;
            case "-":
                answer = op1 - op2;
                break;
            case "x":
                answer = op1 * op2;
                break;
            case "÷":
                answer = op1 / op2;
                break;
        }
        return answer;
    }

    public void chooseAnswer(View view) {
        int answer = Integer.parseInt(((Button) view).getText().toString());
        final MediaPlayer correct = MediaPlayer.create(this, R.raw.correct);
        final MediaPlayer wrong = MediaPlayer.create(this, R.raw.wrong);
        if (answer == correctAnswer) {
            points++;
            tvResults.setText("Correct!");
            if(wrong.isPlaying()){
                wrong.stop();
            }
            correct.start();
        } else {
            tvResults.setText("Wrong!");
            if(correct.isPlaying()){
                correct.stop();
            }
            wrong.start();
        }
        tvPoints.setText(points + "/" + numberOfQuestions);
        generateQuestion();

    }

    @Override
    public void onBackPressed() {

    }
}
