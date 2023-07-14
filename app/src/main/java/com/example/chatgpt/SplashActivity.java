package com.example.chatgpt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private TextView animationText;
    Animation animateNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

///hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //redirect page on selected pages
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this , LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);
        init();

    }

       private void init()
    {

        animationText = (TextView)findViewById(R.id.textview2);
        animateNow = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.up_animation);
        animationText.setAnimation(animateNow);
    }
}