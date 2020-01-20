package com.myidea.sih.reportpothole.array_adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myidea.sih.reportpothole.R;
import com.myidea.sih.reportpothole.complaint.UserComplaint;

import java.util.List;

public class ComplaintAdapter extends ArrayAdapter<UserComplaint> {


    public ComplaintAdapter(@NonNull Context context, int resource, @NonNull List<UserComplaint> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView =((Activity)getContext()).getLayoutInflater().inflate(R.layout.govt_list_layout, parent, false);
        }

        TextView just_name_text_view = convertView.findViewById(R.id.just_name_text_view);
        TextView just_number_text_view = convertView.findViewById(R.id.just_number_text_view);

        UserComplaint userComplaint = getItem(position);

        // TODO add image later.
        just_name_text_view.setText(userComplaint.complainerName);
        just_number_text_view.setText(userComplaint.mobileNumber);

        return convertView;

    }
}
