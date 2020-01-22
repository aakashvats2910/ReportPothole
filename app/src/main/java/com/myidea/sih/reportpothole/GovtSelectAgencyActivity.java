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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.myidea.sih.reportpothole.array_adapters.ComplaintAdapter;
import com.myidea.sih.reportpothole.array_adapters.SelectAgencyAdapter;
import com.myidea.sih.reportpothole.complaint.UserComplaint;
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.status.StatusUpdater;
import com.myidea.sih.reportpothole.util.UserComplaintToShow;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class GovtSelectAgencyActivity extends AppCompatActivity {

    ListView govt_see_agency_list_view;
    List<String> agencyList;
    SelectAgencyAdapter selectAgencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_select_agency);

        setTitle("Select Agency");

        govt_see_agency_list_view = findViewById(R.id.govt_see_agency_list_view);

        //Getting the list of all the agencies.
        Fire.civilAgencyDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                agencyList = new ArrayList<>();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    agencyList.add(snapshot.getKey());
                }

                selectAgencyAdapter = new SelectAgencyAdapter(GovtSelectAgencyActivity.this, R.layout.agency_list_layout, agencyList);
                govt_see_agency_list_view.setAdapter(selectAgencyAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Send the request to agency on click.
        // After clicking this the request will go to the agency.
        govt_see_agency_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                govt_see_agency_list_view.setBackgroundResource(R.drawable.disabled_round);
                govt_see_agency_list_view.setEnabled(false);

                final String agencyName = agencyList.get(position);
                System.out.println("()()()()" + agencyName);

                StatusUpdater.updateStatus("PROBLEM FORWARDED TO CIVIL AGENCY");

                // Getting the required unique complaint key!
                Query query = Fire.complaintListDatabaseReference.orderByChild("imageUniqueID").equalTo(UserComplaintToShow.toShow.imageUniqueID);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                            Fire.civilAgencyDatabaseReference.child(agencyName).child("COMPLAINTS_ASSIGNED").child(dataSnapshot1.getKey()).setValue(dataSnapshot1.getKey());
                        }

                        startActivity(new Intent(GovtSelectAgencyActivity.this, SuccessActivityGovt.class));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GovtSelectAgencyActivity.this, GovtInsideActivity.class));
    }
}
