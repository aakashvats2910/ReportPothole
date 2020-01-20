package com.myidea.sih.reportpothole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.myidea.sih.reportpothole.array_adapters.ComplaintAdapter;
import com.myidea.sih.reportpothole.complaint.UserComplaint;
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.util.UserComplaintToShow;

import java.util.ArrayList;
import java.util.List;

public class GovtInsideActivity extends AppCompatActivity {

    ListView just_govt_list_view;
    ComplaintAdapter complaintAdapter;
    List<UserComplaint> userComplaintList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_inside);

        just_govt_list_view = findViewById(R.id.just_govt_list_view);

        // For setting up the complaint list when the govt log in inside this.
        Fire.complaintListDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userComplaintList = new ArrayList<>();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    UserComplaint userComplaint = snapshot.getValue(UserComplaint.class);
                    userComplaintList.add(userComplaint);
                }

                try {
                    complaintAdapter = new ComplaintAdapter(GovtInsideActivity.this, R.layout.govt_list_layout, userComplaintList);
                    just_govt_list_view.setAdapter(complaintAdapter);
                } catch (Exception e) {
                    System.out.println("()()()()" + e.toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Setting up the on Item click listener.
        just_govt_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // For getting and showing up the full complaint in the next activity.
                UserComplaintToShow.toShow = userComplaintList.get(position);
                startActivity(new Intent(GovtInsideActivity.this, GovtSeeFullComplaint.class));
            }
        });

    }



}
