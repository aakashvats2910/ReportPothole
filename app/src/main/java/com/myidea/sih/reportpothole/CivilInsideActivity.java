package com.myidea.sih.reportpothole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.myidea.sih.reportpothole.array_adapters.CivilComplaintAdapter;
import com.myidea.sih.reportpothole.array_adapters.ComplaintAdapter;
import com.myidea.sih.reportpothole.complaint.UserComplaint;
import com.myidea.sih.reportpothole.data_helper.DataFramework;
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.util.AgencyName;

import java.util.ArrayList;
import java.util.List;

public class CivilInsideActivity extends AppCompatActivity {

    ListView just_civil_list_view;
    CivilComplaintAdapter civilComplaintAdapter;
    List<UserComplaint> userComplaintList;
    List<String> uniqueReferenceIDList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civil_inside);

        just_civil_list_view = findViewById(R.id.just_civil_list_view);

        civilComplaintAdapter = new CivilComplaintAdapter(CivilInsideActivity.this, R.layout.civil_list_layout, DataFramework.userComplaintList);
        just_civil_list_view.setAdapter(civilComplaintAdapter);

        just_civil_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }


}
