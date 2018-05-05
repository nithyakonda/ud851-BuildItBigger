package com.udacity.nkonda.jokesandroidlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayJokeActivity extends AppCompatActivity {
    public static final String ARG_JOKE = "ARG_JOKE";

    private TextView tvJoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joke);
        tvJoke = (TextView) findViewById(R.id.tv_joke);

        if (getIntent().hasExtra(ARG_JOKE)) {
            String jokeStr = getIntent().getStringExtra(ARG_JOKE);
            tvJoke.setText(jokeStr);
        }
    }
}
