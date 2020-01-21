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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.myidea.sih.reportpothole.data_helper.DataFramework;
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.util.AgencyName;

public class LoginCivilActivity extends AppCompatActivity {

    private Button civil_login_button;
    private EditText civil_username_edit_text;
    private EditText civil_password_edit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_civil);

        setTitle("Civil Agency Login");

        civil_login_button = findViewById(R.id.civil_login_button);
        civil_password_edit_text = findViewById(R.id.civil_password_edit_text);
        civil_username_edit_text = findViewById(R.id.civil_username_edit_text);

        civil_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (civil_password_edit_text.getText().toString().trim().isEmpty())
                    civil_password_edit_text.setError("Password is empty!");
                if (civil_username_edit_text.getText().toString().trim().isEmpty())
                    civil_username_edit_text.setError("Username is empty!");
                if (civil_username_edit_text.getText().toString().trim().length() > 0 &&
                civil_password_edit_text.getText().toString().trim().length() > 0) {
                    loginInDatabaseAsCivil(civil_username_edit_text.getText().toString().trim(),
                            civil_password_edit_text.getText().toString().trim());
                }
            }
        });

    }

    // Authenticating the log in credentials for the civil agency.
    // This is to get check the username and password within the many agency listed in our database.
    public void loginInDatabaseAsCivil(final String username, final String password) {
        Fire.civilAgencyDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("USERNAME").getValue(String.class).equals(username) &&
                    snapshot.child("PASSWORD").getValue(String.class).equals(password)) {
                        Toast.makeText(getApplicationContext(), "HURRAY! FOUND AND LOGGED IN", Toast.LENGTH_SHORT).show();
                        // Setting the current agency selected in the static variable to access later.
                        AgencyName.agencyName = snapshot.getKey();
                        // Start collecting the data here only.
                        // This will take care of sending it to the next required activity.
                        DataFramework.mainForCivilInside(AgencyName.agencyName, LoginCivilActivity.this);
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(), "Wrong username/password", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginCivilActivity.this, MainActivity.class));
    }
}
