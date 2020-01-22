package com.myidea.sih.reportpothole.data_helper;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.myidea.sih.reportpothole.CivilInsideActivity;
import com.myidea.sih.reportpothole.LoginCivilActivity;
import com.myidea.sih.reportpothole.array_adapters.CivilComplaintAdapter;
import com.myidea.sih.reportpothole.complaint.UserComplaint;
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.util.AgencyName;

import java.util.ArrayList;
import java.util.List;

public class DataFramework {

    public static List<UserComplaint> userComplaintList;
    public static List<String> uniqueReferenceIDList;

    public static void mainForCivilInside(String agencyName, final Context context) {
        Fire.civilAgencyDatabaseReference.child(agencyName).child("COMPLAINTS_ASSIGNED").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                uniqueReferenceIDList = new ArrayList<>();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    uniqueReferenceIDList.add(snapshot.getKey());

                }

                // Run this after all the unique ID's are gathered.
                afterUniqueIDGatheredBeta(context);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // For getting the unique ID's
    private static void afterUniqueIDGatheredBeta(final Context context) {

        userComplaintList = new ArrayList<>();

        Fire.complaintListDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    for (int i = 0; i <= uniqueReferenceIDList.size()-1; i++) {
                        if (snapshot.getKey().equals(uniqueReferenceIDList.get(i))) {
                            userComplaintList.add(snapshot.getValue(UserComplaint.class));
                            uniqueReferenceIDList.remove(i);
                            break;
                        }
                    }
                }

                System.out.println("()()()() UL : " + userComplaintList.size());

                // Log in to the civil Inside activity.
                sendToCivilInside(context);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private static void sendToCivilInside(Context context) {
        context.startActivity(new Intent(context, CivilInsideActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

}
