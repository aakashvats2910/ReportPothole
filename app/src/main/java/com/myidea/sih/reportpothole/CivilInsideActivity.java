package com.myidea.sih.reportpothole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.myidea.sih.reportpothole.array_adapters.CivilComplaintAdapter;
import com.myidea.sih.reportpothole.array_adapters.ComplaintAdapter;
import com.myidea.sih.reportpothole.complaint.UserComplaint;
import com.myidea.sih.reportpothole.data_helper.DataFramework;
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.user_persistance.UserPersistance;
import com.myidea.sih.reportpothole.util.AgencyName;
import com.myidea.sih.reportpothole.util.UserComplaintToShow;

import java.util.ArrayList;
import java.util.List;

public class CivilInsideActivity extends AppCompatActivity {

    ListView just_civil_list_view;
    CivilComplaintAdapter civilComplaintAdapter;
    List<UserComplaint> userComplaintList;

    List<UserComplaint> unseenUserComplaint;
    List<UserComplaint> verifiedUserComplaint;
    List<UserComplaint> completeUserComplaint;

    List<UserComplaint> currentComplaintList;

    private Button unseen_button_civil;
    private Button verified_button_civil;
    private Button completed_button_civil;

    private ImageButton refresh_civil_inside;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civil_inside);

        setTitle("Complaint List");

        unseenUserComplaint = new ArrayList<>();
        verifiedUserComplaint = new ArrayList<>();
        completeUserComplaint = new ArrayList<>();

        just_civil_list_view = findViewById(R.id.just_civil_list_view);

        refresh_civil_inside = findViewById(R.id.refresh_civil_inside);

        unseen_button_civil = findViewById(R.id.unseen_button_civil);
        verified_button_civil = findViewById(R.id.verified_button_civil);
        completed_button_civil = findViewById(R.id.completed_button_civil);

        // Importing the value from data framework to the global variable in this class.
        userComplaintList = DataFramework.userComplaintList;

        currentComplaintList = userComplaintList;

        for (int i = 0; i <= userComplaintList.size()-1; i++) {
            if (userComplaintList.get(i).status.equals("VERIFIED")) {
                verifiedUserComplaint.add(userComplaintList.get(i));
            } else if (userComplaintList.get(i).status.equals("PROBLEM FORWARDED TO CIVIL AGENCY")) {
                unseenUserComplaint.add(userComplaintList.get(i));
            } else {
                completeUserComplaint.add(userComplaintList.get(i));
            }
        }

        civilComplaintAdapter = new CivilComplaintAdapter(CivilInsideActivity.this, R.layout.civil_list_layout, unseenUserComplaint);
        just_civil_list_view.setAdapter(civilComplaintAdapter);

        currentComplaintList = unseenUserComplaint;

        just_civil_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserComplaintToShow.toShow = currentComplaintList.get(position);
                startActivity(new Intent(CivilInsideActivity.this, CivilSeeFullComplaint.class));
            }
        });

        unseen_button_civil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unseen_button_civil.setBackgroundResource(R.drawable.disabled_round);
                verified_button_civil.setBackgroundResource(R.drawable.round_button);
                currentComplaintList = unseenUserComplaint;
                completed_button_civil.setBackgroundResource(R.drawable.round_button);
                civilComplaintAdapter = new CivilComplaintAdapter(CivilInsideActivity.this, R.layout.civil_list_layout, unseenUserComplaint);
                just_civil_list_view.setAdapter(civilComplaintAdapter);
            }
        });

        verified_button_civil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unseen_button_civil.setBackgroundResource(R.drawable.round_button);
                verified_button_civil.setBackgroundResource(R.drawable.disabled_round);
                completed_button_civil.setBackgroundResource(R.drawable.round_button);
                currentComplaintList = verifiedUserComplaint;
                civilComplaintAdapter = new CivilComplaintAdapter(CivilInsideActivity.this, R.layout.civil_list_layout, verifiedUserComplaint);
                just_civil_list_view.setAdapter(civilComplaintAdapter);
            }
        });

        completed_button_civil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unseen_button_civil.setBackgroundResource(R.drawable.round_button);
                verified_button_civil.setBackgroundResource(R.drawable.round_button);
                completed_button_civil.setBackgroundResource(R.drawable.disabled_round);
                currentComplaintList = completeUserComplaint;
                civilComplaintAdapter = new CivilComplaintAdapter(CivilInsideActivity.this, R.layout.civil_list_layout, completeUserComplaint);
                just_civil_list_view.setAdapter(civilComplaintAdapter);
            }
        });

        refresh_civil_inside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CivilInsideActivity.this, "CLICKED", Toast.LENGTH_SHORT).show();
                finish();
                DataFramework.mainForCivilInside(AgencyName.agencyName, getApplicationContext());
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CivilInsideActivity.this, LoginCivilActivity.class));
    }
}
