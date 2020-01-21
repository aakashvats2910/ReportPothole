package com.myidea.sih.reportpothole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.user.UserDetails;
import com.myidea.sih.reportpothole.user_persistance.UserPersistance;
import com.myidea.sih.reportpothole.util.CodeSent;
import com.myidea.sih.reportpothole.util.MyRandom;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText mobile_edit_text;
    private Button send_otp_button;
    private Button login_govt_button;
    private Button login_civil_button;

    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Sign In");

        // Instantizing the firebase authentication
        Fire.firebaseAuth = FirebaseAuth.getInstance();
        Fire.mandatoryToRun();

        mobile_edit_text = findViewById(R.id.mobile_edit_text);
        send_otp_button = findViewById(R.id.send_otp_button);
        login_govt_button = findViewById(R.id.login_govt_button);
        login_civil_button = findViewById(R.id.login_civil_button);

        // For sending otp button.
        send_otp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO check the input of the mobile number and its validtion.
                if (mobile_edit_text.getText().length() < 10) {
                    mobile_edit_text.setError("Number less than 10 digits");
                } else {
                    //To sed OTP to the entered mobile number.
                    senOTP("+91" + mobile_edit_text.getText().toString(), MainActivity.this);
                }
            }
        });

        // When logging in as govt.
        login_govt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginGovtActivity.class));
            }
        });

        // When logging in as civil agency.
        login_civil_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginCivilActivity.class));
            }
        });

        // To log in user if already logged in ..
        if (!UserPersistance.getDefaults("userid", getApplicationContext()).trim().isEmpty() &&
        UserPersistance.getDefaults("numberforpassing", getApplicationContext()).equals("" + 1234)) {
            startActivity(new Intent(MainActivity.this, MarkPotholeActivity.class));
        }

    }

    // Method to send OTP
    public void senOTP(String number, Activity activity) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                activity,
                mCallback()
        );
    }

    // Callback for the the OTP sending method. It is an helper method.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback() {
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                System.out.println("()()()() SUCCESS");
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                System.out.println("()()()() FAILEDFAILED");
                System.out.println("()()()()" + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                System.out.println("()()()() CODESENT : " + s);
                Intent intent = new Intent(MainActivity.this, OTPVerificationActivity.class);
                // Saving the mobile number of the user in UserDetails.
                UserDetails.mobileNumber = mobile_edit_text.getText().toString();
                startActivity(intent);
                CodeSent.codeSent = s;
            }
        };
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
