package com.example.chatgpt;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    CountryCodePicker ccp;
    private EditText phoneedittext;
    private PinView firstpinview;
    private ConstraintLayout phonelayout;

    private String selected_country_code="+91";
    private static final int CREDENTIAL_PICKER_REQUEST =120 ;
    private ProgressBar progressBar;
    /////firebase phone auth////
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResentToken;

    private FirebaseAuth mAtuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        ccp = findViewById(R.id.ccp);
        phoneedittext = (EditText) findViewById(R.id.editTextTextPersonName);
        firstpinview = (PinView) findViewById(R.id.firstPinView);
        phonelayout = (ConstraintLayout) findViewById(R.id.Phonelayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAtuth=FirebaseAuth.getInstance();


        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {

            @Override

            public void onCountrySelected() {

//Alert.showMessage (RegistrationActivity.this, ccp.getselectedCountryCodeWithPlus());
                selected_country_code = ccp.getSelectedCountryCodeWithPlus();
            }
        });

        phoneedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.toString().length() == 10) {
                    sentOtp();
                   // phonelayout.setVisibility(View.GONE);
                    //firstpinview.setVisibility(View.VISIBLE);
                }

            }

            private void sentOtp() {
                progressBar.setVisibility(View.VISIBLE);
                String phoneNumber= selected_country_code+phoneedittext.getText().toString();
                PhoneAuthOptions options= PhoneAuthOptions.newBuilder(mAtuth).setTimeout(60L,TimeUnit.SECONDS).setPhoneNumber(phoneNumber)
                        .setActivity(PhoneLoginActivity.this)
                        .setCallbacks(mCallbacks).build();
                PhoneAuthProvider.verifyPhoneNumber(options);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        firstpinview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.toString().length() == 6) {
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,firstpinview.getText().toString().trim());
                    signInWithAuthCredential(credential);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        /////auto phone select api////
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();


        PendingIntent intent = Credentials.getClient(PhoneLoginActivity.this).getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0, new Bundle());
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    ////otp callbacks/////

   private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            // The SMS message has been sent to the user's phone.
            // Save the verification ID and force resending token for later use.
           super.onCodeSent(s,forceResendingToken);
           mVerificationId = s;
           mResentToken = forceResendingToken;
            Toast.makeText(PhoneLoginActivity.this,"6 digit otp sent", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            phonelayout.setVisibility(View.GONE);
            firstpinview.setVisibility(View.VISIBLE);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // The verification has been completed successfully.
            // Use the credential to sign in the user.
                String code = credential.getSmsCode();
                if(code!=null)
                {
                    firstpinview.setText(code);
                    signInWithAuthCredential(credential);

                }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // The verification has failed.
            // Handle the error appropriately.
            Toast.makeText(PhoneLoginActivity.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            phonelayout.setVisibility(View.VISIBLE);
            firstpinview.setVisibility(View.GONE);
        }

    };




    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK)
        {
            // Obtain the phone number from the result
            Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);
            /* EditText.setText(credentials.getId().substring(3));*/ //get the selected phone number
//Do what ever you want to do with your selected phone number here

           phoneedittext.setText(credentials.getId().substring(3));


        }
        else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE)
        {
            // *** No phone numbers available ***
            Toast.makeText(PhoneLoginActivity.this, "No phone numbers found", Toast.LENGTH_LONG).show();
        }


    }
    private void signInWithAuthCredential(PhoneAuthCredential credential) {
        mAtuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PhoneLoginActivity.this,"Logged In",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PhoneLoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(PhoneLoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PhoneLoginActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }






}


