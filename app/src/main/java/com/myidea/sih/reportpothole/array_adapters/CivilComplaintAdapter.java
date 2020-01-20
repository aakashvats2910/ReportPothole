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

public class CivilComplaintAdapter extends ArrayAdapter<UserComplaint> {

    public CivilComplaintAdapter(@NonNull Context context, int resource, @NonNull List<UserComplaint> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.civil_list_layout, parent, false);
        }

        TextView civil_just_name_text_view = convertView.findViewById(R.id.civil_just_name_text_view);
        TextView civil_just_number_text_view = convertView.findViewById(R.id.civil_just_number_text_view);

        UserComplaint userComplaint = getItem(position);

        // TODO add image later.
        civil_just_name_text_view.setText(userComplaint.complainerName);
        civil_just_number_text_view.setText(userComplaint.mobileNumber);

        return convertView;

    }
}
