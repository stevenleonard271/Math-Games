package com.steven.mathgames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Hide the bar, make the game fullscreen
        getSupportActionBar().hide();
    }

    public void startGame(View view) {
        Intent intent = new Intent(MainActivity.this, StartGame.class);
        startActivity(intent);
    }
}