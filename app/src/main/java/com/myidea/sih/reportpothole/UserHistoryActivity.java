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
import com.myidea.sih.reportpothole.array_adapters.UserHistoryAdapter;
import com.myidea.sih.reportpothole.complaint.UserComplaint;
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.user.UserDetails;
import com.myidea.sih.reportpothole.user.UserHistoryData;

import java.util.ArrayList;
import java.util.List;

public class UserHistoryActivity extends AppCompatActivity {

    UserHistoryAdapter userHistoryAdapter;
    List<String> uniqueIdList;
    ListView user_history_list_view;
    List<UserComplaint> userComplaintListForHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);

        setTitle("My History");

        user_history_list_view = findViewById(R.id.user_history_list_view);
        uniqueIdList = new ArrayList<>();
        userComplaintListForHistory = new ArrayList<>();

        System.out.println("()()()() REACHED HERE");

        Fire.complaintListDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    if (snapshot.child("userID").getValue(String.class).equals(UserDetails.userID)) {
                        uniqueIdList.add(snapshot.getKey());
                        userComplaintListForHistory.add(snapshot.getValue(UserComplaint.class));
                    }
                }

                userHistoryAdapter = new UserHistoryAdapter(UserHistoryActivity.this, R.layout.user_history_layout, uniqueIdList);
                user_history_list_view.setAdapter(userHistoryAdapter);
                UserHistoryData.userHistoryData = userComplaintListForHistory;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        user_history_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserHistoryData.selectedToSee = userComplaintListForHistory.get(position);
                startActivity(new Intent(UserHistoryActivity.this, UserSeeFullHistory.class));
            }
        });

    }
}
