package com.myidea.sih.reportpothole.status;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.util.UserComplaintToShow;

public class StatusUpdater {

    public static void updateStatus(final String status) {
        Query query = Fire.complaintListDatabaseReference.orderByChild("imageUniqueID").equalTo(UserComplaintToShow.toShow.imageUniqueID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    System.out.println("()()()() + +++ " + snapshot.getKey());
                    Fire.complaintListDatabaseReference.child(snapshot.getKey()).child("status").setValue(status);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
