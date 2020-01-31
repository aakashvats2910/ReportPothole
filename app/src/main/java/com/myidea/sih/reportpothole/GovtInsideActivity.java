package com.myidea.sih.reportpothole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.myidea.sih.reportpothole.array_adapters.ComplaintAdapter;
import com.myidea.sih.reportpothole.complaint.UserComplaint;
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.user_persistance.UserPersistance;
import com.myidea.sih.reportpothole.util.UserComplaintToShow;

import java.util.ArrayList;
import java.util.List;

public class GovtInsideActivity extends AppCompatActivity {

    ListView just_govt_list_view;
    ComplaintAdapter complaintAdapter;
    List<UserComplaint> userComplaintList;
    List<UserComplaint> seenUserComplaints;
    List<UserComplaint> unseenUserComplaints;
    List<UserComplaint> forwardedToAgencyUserComplaints;

    private Button seen_button_govt;
    private Button unseen_button_govt;
    private Button forwarded_button_govt;

    private ImageButton refresh_govt_inside;

    private List<UserComplaint> currentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_inside);

        setTitle("Complaint List");

        just_govt_list_view = findViewById(R.id.just_govt_list_view);
        seen_button_govt = findViewById(R.id.seen_button_govt);
        unseen_button_govt = findViewById(R.id.unseen_button_govt);
        forwarded_button_govt = findViewById(R.id.forwarded_button_govt);

        refresh_govt_inside = findViewById(R.id.refresh_govt_inside);

        userComplaintList = new ArrayList<>();
        unseenUserComplaints = new ArrayList<>();
        seenUserComplaints = new ArrayList<>();
        forwardedToAgencyUserComplaints = new ArrayList<>();

        currentList = userComplaintList;


        // For setting up the complaint list when the govt log in inside this.
        Fire.complaintListDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    UserComplaint userComplaint = snapshot.getValue(UserComplaint.class);
                    userComplaintList.add(userComplaint);
//                    if (snapshot.child("status").getValue(String.class).equals("SEEN"))
//                        System.out.println("()()()() FIRST FIRST");
                }

                for (int i = 0; i <= userComplaintList.size()-1; i++) {
                    if (userComplaintList.get(i).status.equals("SEEN")) {
                        seenUserComplaints.add(userComplaintList.get(i));
                    } else if ((userComplaintList.get(i).status.equals("SENT"))) {
                        unseenUserComplaints.add(userComplaintList.get(i));
                    } else {
                        forwardedToAgencyUserComplaints.add(userComplaintList.get(i));
                    }
                }

                try {
                    complaintAdapter = new ComplaintAdapter(GovtInsideActivity.this, R.layout.govt_list_layout, unseenUserComplaints);
                    just_govt_list_view.setAdapter(complaintAdapter);

                    currentList = unseenUserComplaints;
                } catch (Exception e) {
                    System.out.println("()()()()" + e.toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seen_button_govt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle("Seen");
                seen_button_govt.setBackgroundResource(R.drawable.disabled_round);
                unseen_button_govt.setBackgroundResource(R.drawable.round_button);
                forwarded_button_govt.setBackgroundResource(R.drawable.round_button);
                currentList = seenUserComplaints;
                complaintAdapter = new ComplaintAdapter(GovtInsideActivity.this, R.layout.govt_list_layout, seenUserComplaints);
                just_govt_list_view.setAdapter(complaintAdapter);
            }
        });

        unseen_button_govt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle("Unseen");
                seen_button_govt.setBackgroundResource(R.drawable.round_button);
                unseen_button_govt.setBackgroundResource(R.drawable.disabled_round);
                forwarded_button_govt.setBackgroundResource(R.drawable.round_button);
                currentList = unseenUserComplaints;
                complaintAdapter = new ComplaintAdapter(GovtInsideActivity.this, R.layout.govt_list_layout, unseenUserComplaints);
                just_govt_list_view.setAdapter(complaintAdapter);
            }
        });

        forwarded_button_govt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle("Forwarded");
                seen_button_govt.setBackgroundResource(R.drawable.round_button);
                unseen_button_govt.setBackgroundResource(R.drawable.round_button);
                forwarded_button_govt.setBackgroundResource(R.drawable.disabled_round);
                currentList = forwardedToAgencyUserComplaints;
                complaintAdapter = new ComplaintAdapter(GovtInsideActivity.this, R.layout.govt_list_layout, forwardedToAgencyUserComplaints);
                just_govt_list_view.setAdapter(complaintAdapter);
            }
        });

        refresh_govt_inside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        // Setting up the on Item click listener.
        just_govt_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // For getting and showing up the full complaint in the next activity.
                UserComplaintToShow.toShow = currentList.get(position);
                startActivity(new Intent(GovtInsideActivity.this, GovtSeeFullComplaint.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GovtInsideActivity.this, LoginGovtActivity.class));
    }
}
