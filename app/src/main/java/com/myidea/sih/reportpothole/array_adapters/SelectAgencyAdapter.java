package com.myidea.sih.reportpothole.array_adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myidea.sih.reportpothole.R;
import com.myidea.sih.reportpothole.complaint.UserComplaint;

import java.util.List;

public class SelectAgencyAdapter extends ArrayAdapter<String> {


    public SelectAgencyAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView =((Activity)getContext()).getLayoutInflater().inflate(R.layout.agency_list_layout, parent, false);
        }

        TextView agency_name_text_view = convertView.findViewById(R.id.agency_name_text_view);

        String agencyName = getItem(position);

        agency_name_text_view.setText(agencyName);

        return convertView;

    }
}
