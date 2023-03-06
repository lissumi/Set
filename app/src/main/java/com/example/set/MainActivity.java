package com.example.set;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNewGameClick(View view) {
        Intent intent = new Intent(MainActivity.this,GameActivity.class);
        intent.putExtra (GameActivity.NEW_GAME_PRESSED,true);
        startActivity(intent);
    }

    public void onResumeClick(View view) {
        Intent intent = new Intent(MainActivity.this,GameActivity.class);
        intent.putExtra (GameActivity.NEW_GAME_PRESSED,false);
        startActivity(intent);
    }

    public void onHowToPlayClick(View view) {
        Intent intent = new Intent(MainActivity.this,RulesActivity.class);
        startActivity(intent);
    }
}