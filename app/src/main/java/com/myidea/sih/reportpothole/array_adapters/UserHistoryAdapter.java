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

public class UserHistoryAdapter extends ArrayAdapter<String> {

    public UserHistoryAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView =((Activity)getContext()).getLayoutInflater().inflate(R.layout.user_history_layout, parent, false);
        }

        TextView user_unique_id_text_view = convertView.findViewById(R.id.user_unique_id_text_view);

        String uniqueId = getItem(position);

        user_unique_id_text_view.setText(uniqueId);

        return convertView;

    }

}
