package com.myidea.sih.reportpothole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.user_persistance.UserPersistance;

public class LoginGovtActivity extends AppCompatActivity {

    private EditText govt_username_edit_text;
    private EditText govt_password_edit_text;
    private Button govt_login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_govt);

        setTitle("Govt. Login");

        govt_username_edit_text = findViewById(R.id.govt_username_edit_text);
        govt_password_edit_text = findViewById(R.id.govt_password_edit_text);
        govt_login_button = findViewById(R.id.govt_login_button);

        govt_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (govt_password_edit_text.getText().toString().trim().isEmpty())
                    govt_password_edit_text.setError("Password is empty!");
                if (govt_username_edit_text.getText().toString().trim().isEmpty())
                    govt_username_edit_text.setError("Username is empty!");
                if (govt_username_edit_text.getText().toString().trim().length() > 0 &&
                govt_password_edit_text.getText().toString().trim().length() > 0) {
                    loginInDatabaseAsGovt(govt_username_edit_text.getText().toString().trim(),
                            govt_password_edit_text.getText().toString().trim());
                }
            }
        });
    }

    // Authenticating the log in credentials for the civil agency.
    // For the time being they are only one.
    public void loginInDatabaseAsGovt(final String username, final String password) {
        Fire.govtBodyDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("USERNAME").getValue(String.class).equals(username) &&
                        dataSnapshot.child("PASSWORD").getValue(String.class).equals(password)) {
                    // TODO start the after logged in activity.
                    Toast.makeText(getApplicationContext(), "HURRAY! LOGGED IN", Toast.LENGTH_SHORT).show();
                    // Sending to the Govt activity after logged in.
                    startActivity(new Intent(LoginGovtActivity.this, GovtInsideActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
