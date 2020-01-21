package com.myidea.sih.reportpothole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.myidea.sih.reportpothole.complaint.UserComplaint;
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.user.UserDetails;
import com.myidea.sih.reportpothole.util.MyRandom;
import com.myidea.sih.reportpothole.util.SavedLatLng;

import java.io.ByteArrayOutputStream;

public class SendPotholeActivity extends AppCompatActivity {

    private ImageView pothole_image_view;
    private Button send_complaint_button;
    private EditText comment_edit_text;
    private EditText complainer_name_edit_text;

    private int REQUEST_IMAGE_CAPTURE = 2;
    private int CAMERA_ACCESS_REQUEST = 3;

    private boolean isPhotoClicked = false;

    private Bitmap capturedImageGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_pothole);

        setTitle("Send Pothole Data");

        pothole_image_view = findViewById(R.id.pothole_image_view);
        send_complaint_button = findViewById(R.id.send_complaint_button);
        comment_edit_text = findViewById(R.id.comment_edit_text);
        complainer_name_edit_text = findViewById(R.id.complainer_name_edit_text);

        // Opening camera when the user clicks on image_view.
        pothole_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("()()()() ONCLICK");
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SendPotholeActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_ACCESS_REQUEST);
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });

        // For finally sending the pothole complaint.
        send_complaint_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_complaint_button.setBackgroundResource(R.drawable.disabled_round);
                send_complaint_button.setEnabled(false);
                if (complainer_name_edit_text.getText().toString().isEmpty()) {
                    complainer_name_edit_text.setError("Name is empty!");
                }
                try {
                    if (isPhotoClicked) {
                        // Sending the complaint to the database.
                        UserComplaint userComplaint = new UserComplaint(
                                comment_edit_text.getText().toString().trim(),
                                SavedLatLng.lat,
                                SavedLatLng.lng,
                                complainer_name_edit_text.getText().toString().trim(),
                                UserDetails.userID,
                                UserDetails.mobileNumber,
                                uploadImage(capturedImageGlobal),
                                "SENT"
                        );
                        Fire.complaintListDatabaseReference.push().setValue(userComplaint);
                    } else {
                        Toast.makeText(getApplicationContext(), "Click atleast one picture", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    System.out.println("()()()()" + e.toString());
                }
            }
        });

    }

    // To open camera for taking pictures.
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Setting image captured on the image_view.
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            isPhotoClicked = true;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            capturedImageGlobal = imageBitmap;
            pothole_image_view.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Check if the permission for camera is granted or not.
        // If not granted the camera will not open.
        if (requestCode == CAMERA_ACCESS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getApplicationContext(), "Grant Camera access", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String uploadImage(Bitmap bitmap) {
        System.out.println("()()()() ENTERED!!");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        String uniqueID = MyRandom.makeRandomForStorage();

        UploadTask uploadTask = Fire.mainStorageReference.child(uniqueID).putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                startActivity(new Intent(SendPotholeActivity.this, SuccessActivity.class));
                Toast.makeText(SendPotholeActivity.this, "SUCCESSFULLY UPLOADED", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SendPotholeActivity.this, "FAILED FAILED" + e.toString(), Toast.LENGTH_SHORT).show();
                System.out.println("()()()() FAILED");
                System.out.println("()()()() REASON : " + e.toString());
            }
        });
        return uniqueID;
    }
}
