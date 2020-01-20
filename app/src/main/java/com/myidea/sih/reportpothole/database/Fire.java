package com.myidea.sih.reportpothole.database;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.ChildrenNode;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Fire {

    public static FirebaseAuth firebaseAuth;
    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference mainDatabaseReference;
    public static FirebaseStorage firebaseStorage;
    public static StorageReference mainStorageReference;

    // users list:
    public static DatabaseReference usersListDatabaseReference;

    // Complaint list, it is for every user.
    public static DatabaseReference complaintListDatabaseReference;

    // For civil agency.
    public static DatabaseReference civilAgencyDatabaseReference;

    // For govt body
    public static DatabaseReference govtBodyDatabaseReference;



    public static void mandatoryToRun() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        mainDatabaseReference = firebaseDatabase.getReference();

        firebaseStorage = FirebaseStorage.getInstance();
        mainStorageReference = firebaseStorage.getReference();

        usersListDatabaseReference = mainDatabaseReference.child("USERS");
        complaintListDatabaseReference = mainDatabaseReference.child("COMPLAINTS");

        civilAgencyDatabaseReference = mainDatabaseReference.child("CIVIL_AGENCY");

        govtBodyDatabaseReference = mainDatabaseReference.child("GOVT_BODY");

    }

    // To set the user in the database and initialize it with initial value.
    public static void initializeUserToDatabase(final String user) {
        usersListDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(user)) {
                    usersListDatabaseReference.child(user).child("INITIAL").setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
