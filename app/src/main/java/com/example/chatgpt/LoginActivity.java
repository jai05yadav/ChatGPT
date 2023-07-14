package com.example.chatgpt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private Button LoginButton,PhoneButton;




    Animation phoneAnimate,loginAnimate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PhoneButton = (Button) findViewById(R.id.button2);
        LoginButton = (Button) findViewById(R.id.button);


        phoneAnimate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_animation);
        loginAnimate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_animation);
        PhoneButton.setAnimation(phoneAnimate);
        LoginButton.setAnimation(loginAnimate);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, GmailLoginActivity.class);
                startActivity(intent);
            }
        });
    }




    public void phoneLoginClick(View view) {
        Intent intent = new Intent(LoginActivity.this,PhoneLoginActivity.class);
        startActivity(intent);


    }
   /* public void EmailLogin(View view)
    {
        Intent intent = new Intent(LoginActivity.this,GmailLoginActivity.class);
        startActivity(intent);
    }*/

}