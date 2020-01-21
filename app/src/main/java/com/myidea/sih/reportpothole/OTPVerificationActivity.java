package com.myidea.sih.reportpothole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.user.UserDetails;
import com.myidea.sih.reportpothole.user_persistance.UserPersistance;
import com.myidea.sih.reportpothole.util.CodeSent;

public class OTPVerificationActivity extends AppCompatActivity {

    private EditText otp_edit_text;
    private Button verify_button;

    private final int LOCATION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        setTitle("Verify OTP");

        otp_edit_text = findViewById(R.id.otp_edit_text);
        verify_button = findViewById(R.id.verify_button);


        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(CodeSent.codeSent, otp_edit_text.getText().toString());
                signInWithPhoneAuthCredential(phoneAuthCredential, OTPVerificationActivity.this);
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final Activity activity) {
        Fire.firebaseAuth.signInWithCredential(credential).addOnCompleteListener(activity,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("()()()() SUCCESSFULL OTP VERIFY");
                            //Add userID to the UserDetails class.
                            addUserDetails();
                            // Asking for the location access.
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                            } else {
                                // If permission already granted then transfer to the maps activity automatically.
                                startActivity(new Intent(OTPVerificationActivity.this, MarkPotholeActivity.class));
                            }
                        } else {
                            System.out.println("()()()() OTP FAILED");
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Checking for the result of location access.
        if (requestCode == LOCATION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(new Intent(OTPVerificationActivity.this, MarkPotholeActivity.class), 0);
            } else {
                Toast.makeText(getApplicationContext(), "Please provide location access", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    // To add user ID to the UderDetails class to retrieve it later.
    public void addUserDetails() {
        UserDetails.userID = Fire.firebaseAuth.getUid();
        Fire.initializeUserToDatabase(UserDetails.userID);

        UserPersistance.setDefaults("userid", UserDetails.userID, getApplicationContext());
        UserPersistance.setDefaults("numberforpassing", "" + 1234, getApplicationContext());


    }

}
